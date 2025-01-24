package com.myblog.annotation;

import com.myblog.utility.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 权限要求注解
 * 用于标记需要特定权限才能访问的方法或类
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * 所需的权限等级
     * @return 权限等级，默认为最低等级 1（对应 GUEST）
     */
    String value() default UserRole.GUEST;
}
