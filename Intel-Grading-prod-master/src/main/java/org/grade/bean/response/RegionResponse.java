package org.grade.bean.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionResponse {
    private String regionId;
    private Boolean isGraded;
    private String regionImage;
}