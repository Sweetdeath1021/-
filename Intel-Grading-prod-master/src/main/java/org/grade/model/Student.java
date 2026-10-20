package org.grade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Student {
    @TableId
    private String userId;
    private String studentNumber;
}
