package org.grade.bean.response;

import lombok.Builder;
import lombok.Data;

/**
 * 对用户数据脱敏
 */
@Data
@Builder
public class UserInfoResponse {
    private String userName;
    private Integer userSex;
    private String userPhone;
    private String userEmail;
    private String userSchool;
    private String userImage;
}
