package org.grade.manage;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.grade.bean.dto.FileDTO;
import org.grade.bean.request.TestPaper.TestPaperDeleteRequest;
import org.grade.bean.request.TestPaper.TestPaperPageRequest;
import org.grade.bean.request.TestPaper.TestPaperPointGetRequest;
import org.grade.bean.request.TestPaper.TestPaperUpdateRequest;
import org.grade.common.Result;
import org.grade.common.ServiceException;
import org.grade.common.UploadPaperTask;
import org.grade.common.UploadSheetTask;
import org.grade.model.TestPaper;
import org.grade.service.ITestPaperService;
import org.grade.utils.SecurityUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.grade.constant.OSSConstant.*;

@Service
public class QuestionGroupManage {
    @Resource
    private ITestPaperService testPaperService;

    @Resource
    private ApplicationContext applicationContext;

    // 创建线程池
    static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            12,
            25,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(64),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Transactional(rollbackFor = Exception.class)
    public Result pushQuestionGroup(TestPaper testPaper, MultipartFile[] papers, MultipartFile[] sheets) {
        // 1.判断数据库中是否存在相同试卷
        boolean flag = testPaperService.lambdaQuery()
                .eq(TestPaper::getPaperName, testPaper.getPaperName())
                .eq(TestPaper::getPaperSubject, testPaper.getPaperSubject())
                .count() > 0;
        if (BooleanUtil.isTrue(flag)) {
            throw new ServiceException("已存在相同试卷，请勿重复导入");
        }
        // 2.将样卷数据导入数据库
        // 2.1.设置当前时间并存入数据库
        testPaper.setCreateTime(DateTime.now());
        testPaper.setCompletedNum(0);
        if (!testPaperService.save(testPaper)) {
            throw new ServiceException("数据保存异常");
        }

        // 3.异步调用方法，实现云端上传的异步操作
        // 3.1.获取学校名称、样卷学科、班级
        String schoolName = SecurityUtil.getUserFromHolder().getUserSchool();
        // 3.2.获取样卷Id
        String paperId = testPaper.getPaperId();
        // 3.3.拼接存储地址
        String paperUrl = URL_STORAGE_TESTPAPER +
                schoolName + FILE_SEPARATOR;
        String sheetUrl = URL_STORAGE_ANSWERSHEET +
                schoolName + FILE_SEPARATOR;
        // 4.提交线程池任务
        FileDTO paperDTO = new FileDTO(paperUrl, paperId, convertMap(papers));
        FileDTO sheetDTO = new FileDTO(sheetUrl, paperId, convertMap(sheets));

        UploadPaperTask uploadPaperTask = applicationContext.getBean(UploadPaperTask.class).setFileDTO(paperDTO);
        UploadSheetTask uploadSheetTask = applicationContext.getBean(UploadSheetTask.class).setFileDTO(sheetDTO);

        executorService.submit(uploadPaperTask);
        executorService.submit(uploadSheetTask);

        return Result.ok("文件正在上传至云端");
    }

    //为避免序列化，将MultipartFile数组转为Map
    public Map<String, InputStream> convertMap(MultipartFile[] files) {
        Map<String, InputStream> map;
        try {
            map = new HashMap<>();
            for (MultipartFile file: files) {
                map.put(file.getOriginalFilename(), file.getInputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("Map转换失败");
        }
        return map;
    }

    public Result queryQuestionGroup(TestPaperPageRequest request){
        // 1.构建查询条件，根据schoolName查询样卷（本校管理者只能查询本校试卷）
        // 1.1.获取schoolName
        String schoolName = request.getSchoolName();
        // 1.2.构建查询条件
        QueryWrapper<TestPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("school_name", schoolName)
                .orderByDesc("create_time");

        // 2.构建分页对象
        Page<TestPaper> page = new Page<>(request.getCurrent(), request.getSize());
        // 3.执行分页查询
        IPage<TestPaper> testPaperPage = testPaperService.page(page, queryWrapper);

        // 4.如果查询不到数据
        if (testPaperPage.getRecords().isEmpty()) {
            return Result.fail("您所在的学校尚未上传过样卷");
        }

        // 5.打印查询结果
        System.out.println("-------------------------------");
        System.out.println(testPaperPage.getRecords()); // 获取查询结果列表
        System.out.println("Total: " + testPaperPage.getTotal()); // 获取总记录数
        System.out.println("Pages: " + testPaperPage.getPages()); // 获取总页数
        System.out.println("Current Page: " + testPaperPage.getCurrent()); // 获取当前页码
        System.out.println("Page Size: " + testPaperPage.getSize()); // 获取每页大小
        System.out.println("-------------------------------");

        return Result.ok(testPaperPage);
    }

    public Result deleteQuestionGroup(TestPaperDeleteRequest testPaper){
        // 获取试卷ID
        String paperId = testPaper.getPaperId();
        // 构造删除条件
        QueryWrapper<TestPaper> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId);

        if (!testPaperService.remove(wrapper)) {
            return Result.fail("删除失败");
        }
        return Result.ok("删除成功");
    }

    public Result updateQuestionGroup(TestPaperUpdateRequest request){
        LambdaUpdateWrapper<TestPaper> wrapper = new LambdaUpdateWrapper<>();
        wrapper
                .eq(TestPaper::getPaperId, request.getPaperId())
                .eq(TestPaper::getPaperName, request.getPaperName())
                .eq(TestPaper::getPaperSubject , request.getPaperSubject());

        if (!testPaperService.update(wrapper)) {
            return Result.fail();
        }
        return Result.ok("修改成功");
    }

    public Result getPointById(TestPaperPointGetRequest request) {
        TestPaper one = testPaperService.lambdaQuery()
                .eq(TestPaper::getPaperId, request.getPaperId())
                .one();

        if (Objects.isNull(one)) {
            return Result.fail("不存在对应题组");
        }
        return Result.ok(one.getPaperPoint());
    }
}
