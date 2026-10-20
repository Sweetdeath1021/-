package org.grade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "grd_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private String userId;
    private String userName;
    private Integer userSex;
    private String userAccount;
    private String userPassword;
    private String userEmail;
    private String userSchool;
    private String userImage;
}
