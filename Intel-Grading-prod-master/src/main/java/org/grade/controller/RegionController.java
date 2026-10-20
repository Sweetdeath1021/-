package org.grade.controller;

import org.grade.bean.request.Region.*;
import org.grade.common.Result;
import org.grade.manage.ExamInfoManage;
import org.grade.manage.PaperMarkingManage;
import org.grade.manage.RegionDivideManage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Resource
    private ExamInfoManage examInfoManage;

    @Resource
    private PaperMarkingManage paperMarkingManage;

    @Resource
    private RegionDivideManage regionDivideManage;

    @PostMapping("/add")
    public Result divideRegion(@RequestPart("entity") RegionAddRequest request,
                            @RequestPart("template") MultipartFile[] files) {
        return regionDivideManage.divideRegion(request, files);
    }

    @PostMapping("/point/get")
    public Result template(@RequestBody TemplateGetRequest request) {
        return regionDivideManage.getTemplate(request);
    }

    @PostMapping("/imageList")
    public Result queryGradingRegion(@RequestBody RegionListRequest request) {
        return paperMarkingManage.queryGradingRegion(request);
    }

    @PostMapping("/getScore")
    public Result queryRegionScore(@RequestBody RegionGetScoreRequest request) {
        return paperMarkingManage.queryRegionScore(request);
    }

    @PostMapping("/putScore")
    public Result updateRegionScore(@RequestBody RegionPutScoreRequest request) {
        return paperMarkingManage.updateRegionScore(request);
    }

    @PostMapping("/getNumber")
    public Result queryEvaluatedCount(@RequestBody RegionGetNumberRequest request) {
        return paperMarkingManage.queryEvaluatedCount(request);
    }

    @PostMapping("/putIntelScore")
    public Result putIntelScore(@RequestPart("entity") RegionIntelScoreRequest request,
                                @RequestPart("images") MultipartFile images) {
        return paperMarkingManage.putIntelScore(request, images);
    }

    @PostMapping("/getContent")
    public Result queryRegionContent(@RequestBody RegionGetContentRequest request) {
        return examInfoManage.queryRegionContent(request);
    }

    @PostMapping("/regionList")
    public Result queryRegionList(@RequestBody RegionImageRequest request) {
        return examInfoManage.queryRegionList(request);
    }

    @PostMapping("/getContentAndScore")
    public Result queryContentAndScore(@RequestBody RegionContentRequest request) {
        return examInfoManage.queryContentAndScore(request);
    }
}
