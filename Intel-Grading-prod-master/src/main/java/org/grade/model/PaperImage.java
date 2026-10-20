package org.grade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "grd_paper_image")
public class PaperImage {
    @TableId
    private String paperImageId;
    private String imageUrl;
    private String paperId;

    public PaperImage(String imageUrl, String paperId) {
        this.imageUrl = imageUrl;
        this.paperId = paperId;
    }
}
