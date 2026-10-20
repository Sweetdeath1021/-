package org.grade.bean.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {
    private String userId;
    private String userName;
}
