package org.grade.utils;

import org.grade.bean.request.User.UserUpdateRequest;
import org.grade.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 映射工具类，进行对象属性复制
 */
@Mapper(componentModel = "spring")
public abstract class ConverterUtil {

    @Mappings({
            @Mapping(target = "userAccount", source = "userPhone"),
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "userImage", ignore = true),
            @Mapping(target = "userPassword", ignore = true)
    })
    public abstract User req2User(UserUpdateRequest request);
}
