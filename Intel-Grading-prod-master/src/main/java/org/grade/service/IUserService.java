package org.grade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.grade.bean.response.StudentResponse;
import org.grade.model.User;

import java.util.List;

public interface IUserService extends IService<User> {
    Integer getRoleId(String userId);

    String getStudentNumber(String userId);

    String selectClass(String userId);

    List<StudentResponse> studentList(String studentClass, String userSchool);

}
