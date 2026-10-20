package org.grade.manage;

import org.grade.bean.request.Region.RegionContentRequest;
import org.grade.bean.request.Region.RegionGetContentRequest;
import org.grade.bean.request.Region.RegionImageRequest;
import org.grade.bean.response.RegionContentResponse;
import org.grade.bean.response.RegionResponse;
import org.grade.common.Result;
import org.grade.common.ServiceException;
import org.grade.model.AnswerSheet;
import org.grade.model.Region;
import org.grade.service.IAnswerSheetService;
import org.grade.service.IRegionService;
import org.grade.service.IUserService;
import org.grade.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Service
public class ExamInfoManage {
    @Resource
    private IAnswerSheetService answerSheetService;

    @Resource
    private IRegionService regionService;

    @Resource
    private IUserService userService;

    public Result queryRegionContent(RegionGetContentRequest request) {
        String regionId = request.getRegionId();
        // 获取评分细则
        String content = regionService.lambdaQuery()
                .eq(Region::getRegionId, regionId)
                .one().getRegionContent();
        return Result.ok(content);
    }

    public Result queryRegionList(RegionImageRequest request) {
        // 前置准备
        String paperId = request.getPaperId();
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();
        String studentNumber = userService.getStudentNumber(userId);
        // 查询数据库，获取sheetId
        AnswerSheet one = answerSheetService.lambdaQuery()
                .eq(AnswerSheet::getPaperId, paperId)
                .eq(AnswerSheet::getStudentNumber, studentNumber)
                .one();
        if (Objects.isNull(one)) {
            throw new ServiceException("该试卷尚未打分");
        }
        String sheetId = one.getSheetId();

        List<RegionResponse> collect = regionService.lambdaQuery()
                .eq(Region::getSheetId, sheetId).list()
                .stream()
                .map(region -> new RegionResponse(region.getRegionId(), region.getIsGraded(), region.getRegionImage()))
                .collect(Collectors.toList());

        return Result.ok(collect);
    }

    public Result queryContentAndScore(RegionContentRequest request) {
        String regionId = request.getRegionId();

        Region one = regionService.lambdaQuery()
                .eq(Region::getRegionId, regionId)
                .one();
        RegionContentResponse build = RegionContentResponse.builder()
                .score(one.getRegionScore())
                .content(one.getRegionContent())
                .build();
        return Result.ok(build);
    }
}
