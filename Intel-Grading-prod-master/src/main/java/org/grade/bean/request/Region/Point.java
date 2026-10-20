package org.grade.bean.request.Region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    private Double left;
    private Double top;
    private Double width;
    private Double height;
}
