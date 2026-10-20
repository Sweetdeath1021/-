package org.grade.bean.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDTO {
    private String taskId;
    private String userName;
    private String userPhone;
    private String taskName;
    private Integer regionNumber;
    private Integer taskNum;
    private Integer completedNum;
}
