package org.grade.pattern;

import org.grade.bean.request.User.UserRegisterRequest;
import org.grade.mapper.UserMapper;
import org.grade.model.Student;
import org.grade.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StudentRegister extends UserRegisterTemplate{
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserRegisterRequest request, String avatarUrl) {
        // 1.获取request中的student和user对象
        Student student = request.getStudent();
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
        // 5.获取userId，和学号
        String userId = user.getUserId();
        String studentNumber = student.getStudentNumber();
        // 6.将相关信息存入数据库
        userMapper.register(user.getUserId(), request.getRoleId());
        userMapper.registerStudent(userId, studentNumber);
    }
}
