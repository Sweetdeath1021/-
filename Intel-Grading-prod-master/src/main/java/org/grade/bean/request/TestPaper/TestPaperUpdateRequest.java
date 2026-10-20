package org.grade.bean.request.TestPaper;

import lombok.Data;

@Data
public class TestPaperUpdateRequest {
    private String paperId;
    private String paperName;
    private String paperSubject;
}
