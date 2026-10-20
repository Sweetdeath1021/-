package org.grade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value ="grd_template")
public class Template {
    @TableId
    private String tmpId;
    private String paperId;
    private String tmpImage;
}