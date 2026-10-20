package org.grade.bean.request.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForgetRequest {
    private String userAccount;
    private String userEmail;
    private String userPassword;
}
