package org.grade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.mapper.TaskMapper;
import org.grade.model.Task;
import org.grade.service.ITaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {
}
