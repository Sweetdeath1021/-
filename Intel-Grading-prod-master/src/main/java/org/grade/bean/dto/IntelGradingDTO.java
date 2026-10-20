package org.grade.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntelGradingDTO {
    private String regionId;

    private HashMap<String, Object> paramMap = new HashMap<>();
}
