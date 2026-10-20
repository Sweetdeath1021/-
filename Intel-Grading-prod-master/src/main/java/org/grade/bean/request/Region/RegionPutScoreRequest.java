package org.grade.bean.request.Region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionPutScoreRequest {
    private String regionId;
    private Integer regionScore;
}
