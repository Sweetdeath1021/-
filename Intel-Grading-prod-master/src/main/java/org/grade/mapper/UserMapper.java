package org.grade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.grade.bean.response.StudentResponse;
import org.grade.model.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void register(@Param("userId") String userId, @Param("roleId") String roleId);

    void registerTeacher(@Param("userId") String userId,
                         @Param("teacherClass") String teacherClass,
                         @Param("subject") String subject);

    void registerStudent(@Param("userId") String userId,
                         @Param("studentNumber") String studentNumber);

    Integer getRoleId(@Param("userId") String userId);

    String selectClass(@Param("userId") String userId);

    List<StudentResponse> studentList(@Param("studentClass") String studentClass,
                                      @Param("userSchool") String userSchool);

    String getStudentNumber(@Param("userId") String userId);

}
