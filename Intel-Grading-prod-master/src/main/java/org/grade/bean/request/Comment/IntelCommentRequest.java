package org.grade.bean.request.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntelCommentRequest {
    private String userName;
    private List<String> tags;
}
