package org.grade.bean.request.Comment;

import cn.hutool.json.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grade.model.Question;
import org.grade.model.TestPaper;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QusetionCommentRequest {
    private String schoolName;
    private String paperId;
    private String paperSubject;
    private List<TestPaper> testPaperPage;
    private Question question;
    private JSONArray paperContent;
}
