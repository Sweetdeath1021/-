package org.grade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.grade.common.ServiceException;
import org.grade.mapper.MenuMapper;
import org.grade.mapper.UserMapper;
import org.grade.model.LoginUser;
import org.grade.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl extends ServiceImpl<UserMapper, User>  implements UserDetailsService {
    @Resource
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userAccount);
        User user = getOne(wrapper);
        //若数据库中不存在用户
        if (Objects.isNull(user)) {
            throw new ServiceException("user.is.null");
        }
        // 根据用户查询权限信息 添加到LoginUser中
        List<String> list = menuMapper.selectPermsByUserId(user.getUserId());
        // 封装成UserDetails对象返回
        return new LoginUser(user, list);
    }
}
