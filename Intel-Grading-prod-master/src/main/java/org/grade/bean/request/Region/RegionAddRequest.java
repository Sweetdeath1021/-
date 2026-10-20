package org.grade.bean.request.Region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionAddRequest {
    private String paperId;
    private List<Point> pointList;
}
