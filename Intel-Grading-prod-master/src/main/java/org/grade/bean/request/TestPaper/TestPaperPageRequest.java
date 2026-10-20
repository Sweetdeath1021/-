package org.grade.bean.request.TestPaper;

import lombok.Data;

@Data
public class TestPaperPageRequest {
    private String schoolName;
    private long current; // 第几页
    private long size; // 每页多少数据
}
