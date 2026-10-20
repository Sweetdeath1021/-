package org.grade.bean.request.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grade.bean.dto.TeacherDTO;
import org.grade.model.Student;
import org.grade.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private User user;
    private String roleId;
    private TeacherDTO teacher;
    private Student student;
}
