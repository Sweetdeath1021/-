package org.grade.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grade.common.ServiceException;
import org.grade.model.LoginUser;
import org.grade.utils.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.grade.constant.RedisConstant.LOGIN_CODE_KEY;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 没有token，放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String userId;
        try {
            userId = JwtUtil.parseJwt(token);
        } catch (Exception e) {
            throw new ServiceException("非法的token：" + token);
        }
        // 从redis中获取用户信息
        String userJson = stringRedisTemplate
                .opsForValue().get(LOGIN_CODE_KEY + userId);
        ObjectMapper objectMapper = new ObjectMapper();
        LoginUser loginUser = objectMapper.readValue(userJson, LoginUser.class);
        if (Objects.isNull(loginUser)) {
            throw new ServiceException("用户未登录");
        }
        // 存入SecurityContextHolder，设置用户授权信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
