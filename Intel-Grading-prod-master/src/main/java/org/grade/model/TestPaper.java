package org.grade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "grd_test_paper")
public class TestPaper {
    @TableId
    private String paperId;
    private String schoolName;
    private String paperName;
    private String paperSubject;
    private Integer sheetNum;
    private Integer completedNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Integer paperStatus;
    private Integer sheetStatus;
    private String paperPoint;
}
