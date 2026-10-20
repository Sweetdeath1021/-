package org.grade.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.grade.model.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(String userId);
}
