package org.grade.controller;

import org.grade.bean.request.Task.TaskAddRequest;
import org.grade.bean.request.Task.TaskListRequest;
import org.grade.bean.request.Task.TaskPageRequest;
import org.grade.bean.request.User.TeacherInquireRequest;
import org.grade.common.Result;
import org.grade.manage.PaperMarkingManage;
import org.grade.manage.TaskAssignManage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskAssignManage taskAssignManage;

    @Resource
    private PaperMarkingManage paperMarkingManage;

    @PostMapping("/teacherList")
    public Result queryTeacherList(@RequestBody TeacherInquireRequest request) {
        return taskAssignManage.queryTeacherList(request);
    }

    @PostMapping("/addList")
    public Result assignTaskForTeacher(@RequestBody TaskAddRequest request) {
        return taskAssignManage.assignTaskForTeacher(request);
    }

    @PostMapping("/taskList")
    public Result queryTaskProgress(@RequestBody TaskListRequest request){
        return taskAssignManage.queryTaskProgress(request);
    }

    @PostMapping("/taskPage")
    public Result taskPage(@RequestBody TaskPageRequest request){
        return paperMarkingManage.queryTaskPage(request);
    }
}
