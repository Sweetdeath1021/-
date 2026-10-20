package org.grade.controller;

import org.grade.bean.request.Comment.IntelCommentRequest;
import org.grade.bean.request.Comment.KnowledgeExtendRequest;
import org.grade.bean.request.Comment.PutCommentRequest;
import org.grade.bean.request.Comment.QusetionCommentRequest;
import org.grade.bean.request.User.StudentInquireRequest;
import org.grade.common.Result;
import org.grade.manage.CommentWriteManage;
import org.grade.manage.KnowledgeGuideManage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentWriteManage commentWriteManage;

    @Resource
    private KnowledgeGuideManage knowledgeGuideManage;

    @GetMapping("/selectClass")
    public Result queryClassList() {
        return commentWriteManage.queryClassList();
    }

    @PostMapping("/studentList")
    public Result queryStudentList(@RequestBody StudentInquireRequest request) {
        return commentWriteManage.queryStudentList(request);
    }

    @PostMapping("/intelComment")
    public Result intelComment(@RequestBody IntelCommentRequest request) {
        return commentWriteManage.intelComment(request);
    }

    @PostMapping("/put")
    public Result updateComment(@RequestBody PutCommentRequest request) {
        return commentWriteManage.updateComment(request);
    }

    @PostMapping("/extend")
    public Result intelGuide(@RequestBody KnowledgeExtendRequest request) {
        return knowledgeGuideManage.intelGuide(request);
    }

//    获取该学生所有考试试卷和当前试卷学情扩展内容
    @PostMapping("/paperList")
    public Result queryPaperList(@RequestBody QusetionCommentRequest request) {
        return knowledgeGuideManage.queryPaperList(request);
    }

//    获取该试卷学情扩展的所有记录
    @PostMapping("/question")
    public Result question(@RequestBody QusetionCommentRequest request) {
        return knowledgeGuideManage.queryQuestionRecord(request);
    }

//    修改或者添加该试卷的知识拓展
    @PostMapping("/change")
    public Result change(@RequestBody QusetionCommentRequest request) {
        return knowledgeGuideManage.change(request);
    }
}
