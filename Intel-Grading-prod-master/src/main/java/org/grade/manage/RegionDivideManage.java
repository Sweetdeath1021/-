package org.grade.manage;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.grade.bean.request.Region.Point;
import org.grade.bean.request.Region.RegionAddRequest;
import org.grade.bean.request.Region.TemplateGetRequest;
import org.grade.bean.response.TemplateGetResponse;
import org.grade.common.Result;
import org.grade.model.AnswerSheet;
import org.grade.model.Region;
import org.grade.model.Template;
import org.grade.model.TestPaper;
import org.grade.service.IAnswerSheetService;
import org.grade.service.IRegionService;
import org.grade.service.ITemplateService;
import org.grade.service.ITestPaperService;
import org.grade.utils.OSSUtil;
import org.grade.utils.PyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.grade.constant.OSSConstant.*;
import static org.grade.constant.PythonConstant.SEG_SCRIPT_PATH;

@Service
public class RegionDivideManage {
    @Resource
    private IAnswerSheetService answerSheetService;
    @Resource
    private ITestPaperService testPaperService;
    @Resource
    private ITemplateService templateService;
    @Resource
    private IRegionService regionService;
    @Resource
    private OSSUtil ossUtil;

    @Transactional(rollbackFor = Exception.class)
    public Result divideRegion(RegionAddRequest request, MultipartFile[] files){
        // 1.将坐标点数组转为json字符串存入testPaper表中
        List<Point> pointList = request.getPointList();
        String jsonPoint = JSONUtil.toJsonStr(pointList);
        boolean update = testPaperService.lambdaUpdate()
                .eq(TestPaper::getPaperId, request.getPaperId())
                .set(TestPaper::getPaperPoint, jsonPoint).update();
        if (BooleanUtil.isFalse(update)) {
            return Result.fail("坐标点更新失败");
        }

        // 2.将空白模板上传至阿里云oss
        // todo 可能有多个模板
        String paperId = request.getPaperId();
        // 2.1.遍历所有模板文件
        for (MultipartFile file : files) {
            try {
                String filename = file.getOriginalFilename();
                InputStream inputStream = file.getInputStream();
                String storageUrl = URL_STORAGE_TEMPLATE + paperId + FILE_SEPARATOR;
                // 2.2.获取存储路径
                String imageUrl = ossUtil.upload(filename, inputStream, storageUrl);
                Template build = Template.builder()
                        .paperId(paperId)
                        .tmpImage(imageUrl).build();
                // 2.3.构建wrapper，如果存在就更新，不存在则创建
                LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Template::getPaperId, paperId);

                templateService.saveOrUpdate(build, wrapper);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        // 3.获取该题组所有答题卡
        List<AnswerSheet> list = answerSheetService.lambdaQuery()
                .eq(AnswerSheet::getPaperId, request.getPaperId()).list();
        // 3.1.如果没有学生答题卡，则返回错误
        if (list.isEmpty()) {
            return Result.fail("不存在学生答题卡");
        }
        if (pointList.isEmpty()) {
            return Result.fail("请先划分阅卷区域");
        }
        // 4.遍历所有学生答题卡
        // 4.1.获取学校名和学科
        TestPaper testPaper = testPaperService.lambdaQuery()
                .eq(TestPaper::getPaperId, request.getPaperId())
                .one();
        String schoolName = testPaper.getSchoolName();
        String paperSubject = testPaper.getPaperSubject();

        for (AnswerSheet sheet: list) {
            // 4.1.获取每个答题卡image、imageUrl和studentNumber
            String imageUrl = sheet.getImageUrl();
            // 4.2.拼接存储url
            String storageUrl = URL_STORAGE_REGION
                    + schoolName + FILE_SEPARATOR + paperSubject + FILE_SEPARATOR
                    + sheet.getStudentNumber() + FILE_SEPARATOR;
            // 4.3.获取文件名
            String fileName = sheet.getImageUrl()
                    .substring(sheet.getImageUrl().lastIndexOf('/'));
            int num = 0;
            // 4.4.迭代坐标集合，调用Python脚本，将每一张答题卡进行截取
            for (Point point : pointList) {
                num += 1;
                String jsonStr = JSONUtil.toJsonStr(point);
                byte[] bytes = PyUtil.SegScript(SEG_SCRIPT_PATH, jsonStr, imageUrl);
                // 获取python脚本传递的byte流
                String regionUrl = ossUtil.upload(fileName, new ByteArrayInputStream(bytes), storageUrl);

                // 判断是否存在过被分割的答题卡，若存在，进行更新
                boolean isUpdate = regionService.lambdaUpdate()
                        .eq(Region::getSheetId, sheet.getSheetId())
                        .eq(Region::getRegionNumber, num)
                        .set(Region::getRegionImage, regionUrl)
                        .update();
                if (BooleanUtil.isTrue(isUpdate)) {
                    continue;
                }

                // 将region传入数据库
                Region build = Region.builder()
                        .sheetId(sheet.getSheetId())
                        .regionImage(regionUrl)
                        .regionScore(0)
                        .regionNumber(num).build();
                regionService.save(build);
            }
        }

        return Result.ok("上传完毕");
    }

    public Result getTemplate(TemplateGetRequest request) {
        String paperId = request.getPaperId();

        Template template = templateService.lambdaQuery()
                .eq(Template::getPaperId, paperId).one();
        if (ObjectUtil.isEmpty(template)) {
            return Result.fail("请先划分阅卷区域！");
        }
        TestPaper testPaper = testPaperService.lambdaQuery()
                .eq(TestPaper::getPaperId, paperId)
                .one();

        TemplateGetResponse build = TemplateGetResponse.builder()
                .tmpImage(template.getTmpImage())
                .paperPoint(testPaper.getPaperPoint()).build();
        return Result.ok(build);
    }
}
