package org.grade.bean.request.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPageRequest {
    private String userId;
    private long current;
    private long size;
}
