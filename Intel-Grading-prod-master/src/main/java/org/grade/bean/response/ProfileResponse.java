package org.grade.bean.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private String userName;
    private Integer roleId;
}
