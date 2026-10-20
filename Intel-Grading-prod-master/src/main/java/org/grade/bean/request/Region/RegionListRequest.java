package org.grade.bean.request.Region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionListRequest {
    private String userId;
    private String paperId;
    private Integer regionNumber;
}
