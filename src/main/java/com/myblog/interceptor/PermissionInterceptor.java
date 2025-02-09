package com.myblog.interceptor;

import com.myblog.annotation.RequirePermission;
import com.myblog.utility.TokenUtil;
import com.myblog.utility.UserRole;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * 权限拦截器
 * 用于拦截请求并检查用户是否有足够的权限访问特定的端点
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 在请求处理之前进行调用
     * 检查请求的方法是否有 @RequirePermission 注解，并验证用户权限
     *
     * @param request  当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param handler  选择要执行的处理程序，通常是 HandlerMethod 类型
     * @return 如果用户有足够的权限则返回 true，否则返回 false
     * @throws Exception 如果发生异常
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequirePermission annotation = handlerMethod.getMethodAnnotation(RequirePermission.class);

        // 如果方法没有RequirePermission注解，直接通过
        if (annotation == null) {
            return true;
        }

        String requiredRole = annotation.value();

        // 验证JWT
        String token = request.getHeader("Authorization");

        if (token != null && !token.isEmpty()) {
            if (tokenUtil.validateToken(token)) {
                String userId = tokenUtil.getUserIdFromToken(token);
                String storedToken = redisTemplate.opsForValue().get("token:" + userId);

                if (token.equals(storedToken)) {
                    String userRole = tokenUtil.getRoleFromToken(token);
                    // 检查用户角色是否满足要求
                    if (UserRole.hasPermission(userRole, requiredRole)) {
                        request.setAttribute("userId", userId);
                        request.setAttribute("userRole", userRole);

                        // 检查是否需要刷新token
                        if (tokenUtil.shouldRefreshToken(token)) {
                            String newToken = tokenUtil.refreshToken(token);
                            // 更新Redis中的token
                            redisTemplate.opsForValue().set("token:" + userId, newToken, tokenUtil.getExpiration(), TimeUnit.SECONDS);
                            // 在响应头中返回新的token
                            response.setHeader("NewToken", newToken);
                        }
                        return true;
                    }
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    /**
     * 从请求中获取当前用户ID
     * 注意：这个方法需要根据实际的认证系统来实现
     *
     * @param request 当前 HTTP 请求
     * @return 当前用户ID，如果未登录则返回 null
     */
    private String getCurrentUserId(HttpServletRequest request) {
        // 从session或者token中获取当前用户ID
        // 这里需要根据您的认证系统来实现
        // 例如：return (String) request.getSession().getAttribute("userId");
        return null; // 临时返回null，需要替换为实际实现
    }
}
