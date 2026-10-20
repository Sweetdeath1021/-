package org.grade.pattern;

import cn.hutool.json.JSONUtil;
import org.grade.bean.dto.TeacherDTO;
import org.grade.bean.request.User.UserRegisterRequest;
import org.grade.mapper.UserMapper;
import org.grade.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TeacherRegister extends UserRegisterTemplate {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    /**
     * 设置用户基本信息
     *
     * @param request
     * @param avatarUrl
     */
    public void saveUser(UserRegisterRequest request, String avatarUrl) {
        // 1.获取request中的teacher和user对象
        TeacherDTO teacher = request.getTeacher();
        User user = request.getUser();

        // 2.密码加密
        user.setUserPassword(passwordEncoder
                .encode(user.getUserPassword()));
        // 3.设置头像路径
        user.setUserImage(avatarUrl);
        // 4.user存入数据库
        if(userMapper.insert(user) < 0) {
            throw new RuntimeException("用户注册失败");
        }
        // 5.获取userId，将list集合转化为JSON串
        String userId = user.getUserId();
        String teacherClass = JSONUtil.toJsonStr(teacher.getTeacherClass());
        String subject = JSONUtil.toJsonStr(teacher.getSubject());
        // 6.将相关信息存入数据库
        userMapper.register(user.getUserId(), request.getRoleId());
        userMapper.registerTeacher(userId, teacherClass, subject);
    }
}
