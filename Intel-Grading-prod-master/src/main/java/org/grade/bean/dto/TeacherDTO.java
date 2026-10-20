package org.grade.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherDTO {
    private String userId;
    private List<String> teacherClass;
    private List<String> subject;
}
