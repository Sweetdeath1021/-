package org.grade.bean.request.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListRequest {
    private String paperId;
    private long current;
    private long size;
}
