package org.grade.model;

import cn.hutool.core.date.DateTime;
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
@TableName(value = "grd_answer_sheet")
public class AnswerSheet {
    @TableId
    private String sheetId;
    private String paperId;
    private String studentNumber;
    private String imageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Integer status;

    public AnswerSheet(String paperId, String studentNumber, String imageUrl, DateTime createTime, Integer status) {
        this.paperId = paperId;
        this.studentNumber = studentNumber;
        this.imageUrl = imageUrl;
        this.createTime = createTime;
        this.status = status;
    }
}
