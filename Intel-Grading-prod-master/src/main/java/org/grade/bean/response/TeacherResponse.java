package org.grade.bean.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherResponse {
    private String userId;
    private String userImage;
    private String userName;

}
