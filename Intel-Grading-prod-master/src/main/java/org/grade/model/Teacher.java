package org.grade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("grd_teacher")
public class Teacher {
    @TableId
    private String userId;
    private String teacherClass;
    private String subject;
}
