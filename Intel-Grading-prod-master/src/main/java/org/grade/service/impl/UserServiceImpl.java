package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.grade.bean.response.StudentResponse;
import org.grade.mapper.UserMapper;
import org.grade.model.User;
import org.grade.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Integer getRoleId(String userId) {
        return baseMapper.getRoleId(userId);
    }

    @Override
    public String getStudentNumber(String userId) {
        return baseMapper.getStudentNumber(userId);
    }

    @Override
    public String selectClass(String userId) {
        return baseMapper.selectClass(userId);
    }

    @Override
    public List<StudentResponse> studentList(String studentClass, String userSchool) {
        return baseMapper.studentList(studentClass, userSchool);
    }
}


