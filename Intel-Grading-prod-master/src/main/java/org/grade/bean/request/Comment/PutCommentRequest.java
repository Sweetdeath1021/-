package org.grade.bean.request.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutCommentRequest {
    private String commentName;
    private String userId;
    private String commentContent;
}
