package org.grade.manage;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.grade.bean.dto.TaskDTO;
import org.grade.bean.request.Task.TaskAddRequest;
import org.grade.bean.request.Task.TaskListRequest;
import org.grade.bean.request.User.TeacherInquireRequest;
import org.grade.bean.response.TeacherResponse;
import org.grade.common.Result;
import org.grade.mapper.TeacherMapper;
import org.grade.model.*;
import org.grade.service.IAnswerSheetService;
import org.grade.service.IRegionService;
import org.grade.service.ITaskService;
import org.grade.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lixin
 * @date 2024/5/28
 */
@Service
public class TaskAssignManage {
    @Resource
    private IUserService userService;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private IAnswerSheetService answerSheetService;
    @Resource
    private IRegionService regionService;
    @Resource
    private ITaskService taskService;

    public Result queryTeacherList(TeacherInquireRequest request) {
        // 1.获取本校所有的user
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserSchool, request.getSchool());
        List<User> list = userService.list(wrapper);
        // 2.判断集合是否为空
        if (list.isEmpty()) {
            return Result.fail("该学校暂无教师用户");
        }
        // 3.筛出所有满足条件的teacher
        List<TeacherResponse> teacherList = new LinkedList<>();
        for (User user : list) {
            // 获取每个用户的roleId
            String userId = user.getUserId();
            Integer roleId = userService.getRoleId(userId);
            // roleId不是老师，跳过
            if (!roleId.equals(2)) {
                continue;
            }
            // 获取teacher对象，利用JSONUtil将字符串转为集合
            Teacher teacher = teacherMapper.selectById(user.getUserId());
            List<String> subjectList = JSONUtil.
                    toList(teacher.getSubject(), String.class);
            // 如果不是该科老师，跳过
            if (!subjectList.contains(request.getSubject())) {
                continue;
            }
            // 将对应学科的老师加入结果集中
            TeacherResponse teacherResponse = TeacherResponse.builder()
                    .userId(user.getUserId())
                    .userImage(user.getUserImage())
                    .userName(user.getUserName())
                    .build();
            teacherList.add(teacherResponse);
        }

        return Result.ok(teacherList);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result assignTaskForTeacher(TaskAddRequest request) {
        // 开启事务，如果出现异常，全部回滚
        // 1.前置准备
        // 1.1.获取paperId和regionNumber，定义map存储区域
        List<Region> list = new ArrayList<>();
        String paperId = request.getTaskList().get(0).getPaperId();
        Integer regionNumber = request.getTaskList().get(0).getRegionNumber();
        // 1.2.stream流获取答题卡Id集合
        List<String> sheetIds = answerSheetService.lambdaQuery()
                .eq(AnswerSheet::getPaperId, paperId).list()
                .stream().map(AnswerSheet::getSheetId)
                .collect(Collectors.toList());
        if (sheetIds.isEmpty()) {
            return Result.fail("该题组不存在答题卡");
        }
        // 1.3.根据sheetIds集合和regionNumber获取需要评阅的对应区域
        for (String sheetId : sheetIds) {
            Region region = regionService.lambdaQuery()
                    .eq(Region::getSheetId, sheetId)
                    .eq(Region::getRegionNumber, regionNumber).one();
            list.add(region);
        }
        // 2.迭代器迭代每个角色
        Iterator<Task> iterator = request.getTaskList().iterator();
        while (iterator.hasNext()) {
            // 2.1.获取list的每个元素，设置
            Task task = iterator.next();
            task.setCompletedNum(0);
            boolean save = taskService.save(task);
            if (BooleanUtil.isFalse(save)) {
                throw new RuntimeException("插入异常");
            }
            // 2.2.遍历map，为每个角色分配区域
            int num = 0;
            for (Region region: list) {
                // 如果已经分配过，跳过
                String userId = regionService.lambdaQuery()
                        .eq(Region::getRegionId, region.getRegionId()).one().getUserId();
                if (StrUtil.isNotBlank(userId)) {
                    continue;
                }
                // 如果分配完毕，结束循环
                if (Integer.compare(num, task.getTaskNum()) != -1) {
                    break;
                }
                // 为老师分配区域
                region.setUserId(task.getUserId());
                boolean update = regionService.lambdaUpdate()
                        .eq(Region::getRegionId, region.getRegionId())
                        .update(region);
                if (BooleanUtil.isFalse(update)) {
                    throw new RuntimeException("更新异常");
                }
                num++;
            }
        }
        return Result.ok("任务分配成功！");
    }

    public Result queryTaskProgress(TaskListRequest request) {
        // 1.构建查询条件，根据paperId查询任务
        // 1.1.获取paperId
        String paperId = request.getPaperId();
        // 1.2.构建查询条件
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("paper_id", paperId);

        // 2.构建分页对象
        Page<Task> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<Task> taskPage = taskService.page(page, queryWrapper);

        // 3.如果查询不到数据
        if (taskPage.getRecords().isEmpty()) {
            return Result.fail("您所在的学校尚未上传过样卷");
        }

        // 4.添加TaskVO的list集合
        List<TaskDTO> taskList = new LinkedList<>();
        for (Task task : taskPage.getRecords()) {
            User user = userService.getById(task.getUserId());
            TaskDTO build = TaskDTO.builder()
                    .taskId(task.getTaskId()).taskNum(task.getTaskNum())
                    .regionNumber(task.getRegionNumber())
                    .completedNum(task.getCompletedNum()).taskName(task.getTaskName())
                    .userName(user.getUserName()).userPhone(user.getUserAccount()).build();
            taskList.add(build);
        }

        // 5.设置IPage属性
        IPage<TaskDTO> taskVoPage = new Page<>();
        taskVoPage.setRecords(taskList);
        taskVoPage.setTotal(taskPage.getTotal());
        taskVoPage.setPages(taskPage.getPages());
        taskVoPage.setSize(taskPage.getSize());
        taskVoPage.setCurrent(taskPage.getCurrent());

        // 5.打印查询结果
        System.out.println("-------------------------------");
        System.out.println(taskVoPage.getRecords()); // 获取查询结果列表
        System.out.println("Total: " + taskVoPage.getTotal()); // 获取总记录数
        System.out.println("Pages: " + taskVoPage.getPages()); // 获取总页数
        System.out.println("Current Page: " + taskVoPage.getCurrent()); // 获取当前页码
        System.out.println("Page Size: " + taskVoPage.getSize()); // 获取每页大小
        System.out.println("-------------------------------");

        return Result.ok(taskVoPage);
    }
}
