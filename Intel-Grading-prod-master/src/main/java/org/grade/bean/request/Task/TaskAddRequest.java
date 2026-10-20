package org.grade.bean.request.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grade.model.Task;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAddRequest {
    private List<Task> taskList;
}
