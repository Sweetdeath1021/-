package org.grade.manage;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.grade.bean.request.Comment.IntelCommentRequest;
import org.grade.bean.request.Comment.PutCommentRequest;
import org.grade.bean.request.User.StudentInquireRequest;
import org.grade.bean.response.StudentResponse;
import org.grade.common.Result;
import org.grade.model.Comment;
import org.grade.service.ICommentService;
import org.grade.service.IUserService;
import org.grade.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Service
public class CommentWriteManage {
    @Resource
    private IUserService userService;
    @Resource
    private ICommentService commentService;

    public Result queryClassList() {
        String userId = SecurityUtil
                .getUserFromHolder().getUserId();
        String classStr = userService.selectClass(userId);
        List<String> classList = JSONUtil.toList(classStr, String.class);

        if (classList.isEmpty()) {
            return Result.fail("该老师未教授任何班级");
        }
        return Result.ok(classList);
    }

    public Result queryStudentList(StudentInquireRequest request) {
        // 获取班级和学校
        String studentClass = request.getStudentClass();
        String userSchool = SecurityUtil
                .getUserFromHolder().getUserSchool();

        List<StudentResponse> list = userService.studentList(studentClass, userSchool);
        if (list.isEmpty()) {
            return Result.fail("该班级学生尚未注册账号");
        }
        return Result.ok(list);
    }

    @Value("${api.comment.url}")
    private String COMMENT_URL;
    @Value("${api.comment.request.header}")
    private String COMMENT_REQUEST_HEADER;
    @Value("${api.comment.request.secret}")
    private String COMMENT_REQUEST_SECRET;

    public Result intelComment(IntelCommentRequest request) {
        // 拼接请求参数，获取request中的数据
        String name = request.getUserName();

        StringBuilder sb = new StringBuilder();
        sb.append("学生名字叫");
        sb.append(name);
        sb.append(", ");
        for (String tag : request.getTags()) {
            sb.append(tag);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("student", sb.toString());

        HttpResponse execute = HttpRequest.post(COMMENT_URL)
                .header(COMMENT_REQUEST_HEADER, COMMENT_REQUEST_SECRET)
                .form(paramMap)
                .execute();

        if (execute.getStatus() != 200) {
            return Result.fail("云端调用失败");
        }

        return Result.ok(execute.body());
    }

    public Result updateComment(PutCommentRequest request) {
        String userId = request.getUserId();

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        Comment build = Comment.builder()
                .commentContent(request.getCommentContent())
                .commentName(request.getCommentName())
                .userId(userId)
                .build();
        boolean flag = commentService.saveOrUpdate(build, wrapper);
        if (!flag) {
            return Result.fail("更新失败");
        }

        return Result.ok("评语保存成功");
    }
}
