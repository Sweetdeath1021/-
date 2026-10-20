package org.grade.bean.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateGetResponse {
    private String tmpImage;
    private String paperPoint;
}
