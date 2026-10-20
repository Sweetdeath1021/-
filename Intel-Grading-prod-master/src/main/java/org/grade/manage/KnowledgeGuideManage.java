package org.grade.manage;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.grade.bean.request.Comment.KnowledgeExtendRequest;
import org.grade.bean.request.Comment.QusetionCommentRequest;
import org.grade.common.Result;
import org.grade.common.ServiceException;
import org.grade.model.AnswerSheet;
import org.grade.model.Question;
import org.grade.model.TestPaper;
import org.grade.service.IQuestionService;
import org.grade.service.ITestPaperService;
import org.grade.service.IUserService;
import org.grade.service.impl.AnswerSheetServiceImpl;
import org.grade.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Service
public class KnowledgeGuideManage {
    @Resource
    private IUserService userService;
    @Resource
    private IQuestionService questionService;
    @Resource
    private AnswerSheetServiceImpl answerSheetService;
    @Resource
    private ITestPaperService testPaperService;

    @Value("${api.extend.url}")
    private String EXTEND_URL;
    @Value("${api.extend.request.header}")
    private String EXTEND_REQUEST_HEADER;
    @Value("${api.extend.request.secret}")
    private String EXTEND_REQUEST_SECRET;

    public Result intelGuide(KnowledgeExtendRequest request) {
        // 获取paperId和studentNumber
        String paperId = request.getPaperId();
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();
        String studentNumber = userService.getStudentNumber(userId);
        // 获取答题卡图片数据流
        AnswerSheet sheet = answerSheetService.lambdaQuery()
                .eq(AnswerSheet::getPaperId, paperId)
                .eq(AnswerSheet::getStudentNumber, studentNumber)
                .one();
        if (Objects.isNull(sheet)) {
            throw new ServiceException("您的试卷尚未评阅，请等待！");
        }
        String imageUrl = sheet.getImageUrl();

        byte[] imageBytes = HttpUtil.downloadBytes(imageUrl);
        String suffix = imageUrl.substring(imageUrl.lastIndexOf('.'));
        String body = null;
        try {
            // 存入临时文件
            Path tempImagePath = Files.createTempFile("imageTemp", suffix);
            Files.write(tempImagePath, imageBytes, StandardOpenOption.CREATE);
            // 从临时文件中获取file
            File student = tempImagePath.toFile();

            String url = EXTEND_URL;
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("student", student);
            HttpResponse execute = HttpRequest.post(url)
                    .header(EXTEND_REQUEST_HEADER, EXTEND_REQUEST_SECRET)
                    .form(paramMap)
                    .execute();

            int status = execute.getStatus();
            if (status != 200) {
                return Result.fail("执行失败，错误码：" + status);
            }

            body = execute.body();

            Files.delete(tempImagePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Result.ok(body);
    }

    public Result queryPaperList(QusetionCommentRequest request){
//      获取学生id
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();

        // 1.构建查询条件，根据schoolName查询样卷（本校管理者只能查询本校试卷）
        // 1.1.获取schoolName
        String schoolName = request.getSchoolName();
        // 1.2.构建查询条件
        QueryWrapper<TestPaper> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .eq(TestPaper::getSchoolName, schoolName)
                .eq(TestPaper::getPaperSubject, request.getPaperSubject())
                .orderByDesc(TestPaper::getCreateTime);

        List<TestPaper> testPaperPage = testPaperService.list(queryWrapper);

        // 5.打印查询结果
        System.out.println("-------------------------------");
        System.out.println(testPaperPage); // 获取查询结果列表
        System.out.println("-------------------------------");
        // 4.如果查询不到数据
        if (testPaperPage.isEmpty()) {
            return Result.fail("您所在的学校尚未上传过样卷");
        }

        // 5.打印查询结果
        System.out.println("-------------------------------");
        System.out.println(testPaperPage); // 获取查询结果列表
        System.out.println("-------------------------------");

//        // 6.新建返回对象
        QusetionCommentRequest qusetionComment = new QusetionCommentRequest();
        // 7.将试卷列表放入新建对象中
        qusetionComment.setTestPaperPage(testPaperPage);
        // 8.查询该试卷的考情分析记录
        LambdaQueryWrapper<Question> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Question::getPaperId,request.getPaperId())
                .eq(Question::getUserId,userId);
        // 9.新建question对象
        Question question = questionService.getOne(lqw);
        System.out.println(("==================================="));
        System.out.println(userId);
        System.out.println(request.getPaperId());
        System.out.println(("==================================="));
        // 10.将question对象加入返回对象
        qusetionComment.setQuestion(question);

        return Result.ok(qusetionComment);

    }

    //    获取该试卷学情扩展的所有记录
    public Result queryQuestionRecord(QusetionCommentRequest request){
        //      获取学生id
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();

        // 查询该试卷的考情分析记录
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Question::getPaperId, request.getPaperId())
                .eq(Question::getUserId, userId);
        // 新建question对象
        Question question = questionService.getOne(wrapper);
        //将字符串转化成json对象
        if(question!=null){
            JSONArray jsonArray = new JSONArray(question.getQuestionContent());
            return Result.ok(jsonArray);
        }
        return Result.ok(question);
    }

    //    修改或者添加该试卷的知识拓展
    public Result change(QusetionCommentRequest request){
        //      获取学生id
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println(request.getPaperContent());
        JSONArray jsonArray = new JSONArray(request.getPaperContent());
        String paperContent = jsonArray.toString();
        //新建一个question，并且放入所有数据
        Question question = new Question();
        question.setPaperId(request.getPaperId());
        question.setUserId(userId);
        question.setQuestionContent(paperContent);
        // 构建更新条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                        .eq(Question::getUserId, userId)
                        .eq(Question::getPaperId, request.getPaperId());

        // 检查记录是否存在
        int existingRecords = questionService.count(queryWrapper);
        if (existingRecords > 0) {
            // 如果记录存在，则更新
            boolean rowsAffected = questionService.update(question, queryWrapper);
            if (rowsAffected) {
                return Result.ok("修改成功"); // 更新成功
            }
        }
        if (existingRecords <= 0) {
            // 如果记录不存在，则插入
            boolean rowsInserted = questionService.save(question);
            if (rowsInserted) {
                return Result.ok("新建成功"); // 插入新数据成功
            }
        }
        return Result.fail("请求失败，请重试");
    }
}
