package com.myblog.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Data
@Component
public class TokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;//单位秒

    /**
     * 为给定的用户ID和角色生成JWT令牌
     *
     * @param userId 用户ID
     * @param role 用户角色
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String userId, String role) {
        return Jwts.builder()
                .setSubject(userId)  // 设置令牌的主题为用户ID
                .claim("role", role)  // 添加自定义声明，包含用户角色
                .setIssuedAt(new Date())  // 设置令牌的签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // 设置令牌的过期时间
                .signWith(SignatureAlgorithm.HS512, secret)  // 使用HS512算法和密钥签名令牌
                .compact();  // 生成最终的令牌字符串
    }

    /**
     * 从给定的令牌中解析出声明
     *
     * @param token JWT令牌字符串
     * @return 令牌中包含的声明
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)  // 设置用于验证签名的密钥
                .parseClaimsJws(token)  // 解析令牌
                .getBody();  // 获取令牌主体（即声明部分）
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌字符串
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从令牌中获取用户角色
     *
     * @param token JWT令牌字符串
     * @return 用户角色
     */
    public String getRoleFromToken(String token) {
        return (String) getClaimsFromToken(token).get("role");
    }

    /**
     * 验证令牌是否有效
     *
     * @param token JWT令牌字符串
     * @return 如果令牌有效返回true，否则返回false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;  // 如果解析成功，则令牌有效
        } catch (Exception e) {
            return false;  // 如果解析过程中抛出异常，则令牌无效
        }
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean shouldRefreshToken(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.getTime() - System.currentTimeMillis() < 600000; // 10 minutes in milliseconds
    }
}

