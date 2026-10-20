package org.grade.controller;

import org.grade.bean.request.TestPaper.TestPaperDeleteRequest;
import org.grade.bean.request.TestPaper.TestPaperPageRequest;
import org.grade.bean.request.TestPaper.TestPaperPointGetRequest;
import org.grade.bean.request.TestPaper.TestPaperUpdateRequest;
import org.grade.common.Result;
import org.grade.manage.QuestionGroupManage;
import org.grade.model.TestPaper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/grade")
public class PaperController {
    @Resource
    private QuestionGroupManage questionGroupManage;

    @PostMapping("/upload")
    public Result pushQuestionGroup(@RequestPart("entity") TestPaper testPaper,
                              @RequestPart("paper") MultipartFile[] papers,
                              @RequestPart("sheet") MultipartFile[] sheets) {
        return questionGroupManage.pushQuestionGroup(testPaper, papers, sheets);
    }

    @PostMapping("/list")
    public Result queryQuestionGroup(@RequestBody TestPaperPageRequest testPaperPageRequest){
        return questionGroupManage.queryQuestionGroup(testPaperPageRequest);
    }

    @PostMapping("/delete")
    public Result deleteQuestionGroup(@RequestBody TestPaperDeleteRequest testPaperDeleteRequest){
        return questionGroupManage.deleteQuestionGroup(testPaperDeleteRequest);
    }

    @PostMapping("/update")
    public Result updateQuestionGroup(@RequestBody TestPaperUpdateRequest testPaperUpdateRequest){
        return questionGroupManage.updateQuestionGroup(testPaperUpdateRequest);
    }

    @PostMapping("/point/get")
    public Result getPoint(@RequestBody TestPaperPointGetRequest request) {
        return questionGroupManage.getPointById(request);
    }
}
