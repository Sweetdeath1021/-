package org.grade.bean.request.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String userName;
    private Integer userSex;
    private String userPhone;
    private String userEmail;
    private String userSchool;
}
