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
@TableName(value ="grd_region")
public class Region{
    @TableId
    private String regionId;
    private String sheetId;
    private String userId;
    private Integer regionNumber;
    private Integer regionScore;
    private Boolean isGraded;
    private String regionImage;
    private String regionContent;
}