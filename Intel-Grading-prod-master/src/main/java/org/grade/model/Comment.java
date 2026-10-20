package org.grade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "grd_comment")
public class Comment {
    @TableId
    private String commentId;
    private String userId;
    private String commentName;
    private String commentContent;
    private String commentSubject;
}
