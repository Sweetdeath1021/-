package org.grade.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private String storageUrl;
    private String paperId;
    private Map<String, InputStream> map;
}
