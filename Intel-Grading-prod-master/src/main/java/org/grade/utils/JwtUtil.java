package org.grade.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.grade.common.ServiceException;
import org.springframework.util.StringUtils;

import java.util.Date;


public class JwtUtil {
    // token失效：24小时
    public static final String token = "token";
    public static final long EXPIPE = 1000 * 60 * 60 * 10;
    public static final String APP_SECRET = "modox@ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 根据传入的用户Id生成token
     * @param userId
     * @return JWT规则生成的token
     */
    public static String createToken(String userId) {
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("grd_user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIPE))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 验证token是否有效
     * @param jwtToken token字符串
     * @return 如果token有效返回true，否则false
     */
    public static boolean checkToken(String jwtToken) {
        try {
            if (!StringUtils.hasText(jwtToken))
                return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            throw new ServiceException();
        }
        return true;
    }

    /**
     * 根据token获取User信息
     * @param jwtToken token字符串
     * @return 解析token获得的user对象
     */
    public static String parseJwt(String jwtToken) {
        //验证token
        if (checkToken(jwtToken)) {
            Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken).getBody();
            return claims.get("userId").toString();
        }else {
            throw new RuntimeException("超时或不合法token");
        }
    }
}
