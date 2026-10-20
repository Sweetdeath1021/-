package org.grade.utils;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grade.common.ServiceException;
import org.grade.model.LoginUser;
import org.grade.model.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.grade.constant.RedisConstant.LOGIN_CODE_KEY;

public class SecurityUtil {
    /**
     * 将字符串渲染到客户端
     * @param response
     * @param string
     * @return
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserFromHolder() {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        return loginUser.getUser();
    }

    public static void UpdateCache(User user, StringRedisTemplate stringRedisTemplate) {
        // 获取userId
        String userId = getUserFromHolder().getUserId();
        // 获取缓存中的user对象
        String jsonCache = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + userId);
        ObjectMapper objectMapper = new ObjectMapper();

        LoginUser userCache = null;
        try {
            userCache = objectMapper.readValue(jsonCache, LoginUser.class);
        } catch (JsonProcessingException e) {
            throw new ServiceException("JSON转换异常", e);
        }
        User newUser = userCache.getUser();

        // 利用反射机制获取User类的所有字段
        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // 设置字段可访问
                field.setAccessible(true);
                // 获取user对象中对应字段的值
                Object value = field.get(user);
                // 如果字段值非空，并且不是userId字段，则将其设置到newUser对象中
                if (value != null && !field.getName().equals("userId")) {
                    field.set(newUser, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 更新缓存中的user信息
        userCache.setUser(newUser);
        stringRedisTemplate.opsForValue()
                .set(LOGIN_CODE_KEY + userId, JSONUtil.toJsonStr(userCache));
    }
}
