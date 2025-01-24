package com.myblog.interceptor;

import com.myblog.annotation.RequirePermission;
import com.myblog.utility.UserRole;
import com.myblog.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截器
 * 用于拦截请求并检查用户是否有足够的权限访问特定的端点
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionService permissionService;

    /**
     * 在请求处理之前进行调用
     * 检查请求的方法是否有 @RequirePermission 注解，并验证用户权限
     *
     * @param request 当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param handler 选择要执行的处理程序，通常是 HandlerMethod 类型
     * @return 如果用户有足够的权限则返回 true，否则返回 false
     * @throws Exception 如果发生异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查处理程序是否为 HandlerMethod 类型
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取方法上的 RequirePermission 注解
        RequirePermission annotation = handlerMethod.getMethodAnnotation(RequirePermission.class);

        // 如果方法没有 RequirePermission 注解，则允许访问
        if (annotation == null) {
            return true;
        }

        // 获取注解中指定的所需角色
        String requiredRole = annotation.value();
        // 获取当前用户ID
        String userId = getCurrentUserId(request);

        // 如果无法获取用户ID，则拒绝访问
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未登录或会话已过期");
            return false;
        }

        // 获取用户角色
        String userRole = permissionService.getUserRole(userId);

        // 检查用户是否有足够的权限
        if (!UserRole.hasPermission(userRole, requiredRole)) {
            // 如果权限不足，返回 403 Forbidden 错误
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "没有足够的权限");
            return false;
        }

        // 用户有足够的权限，允许请求继续
        return true;
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
