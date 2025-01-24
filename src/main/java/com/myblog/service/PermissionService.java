package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Permission;

/**
 * (Permission)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:31:02
 */
public interface PermissionService extends IService<Permission> {

    boolean hasPermission(String userId, String targetId);
    boolean setPermission(String targetId, String level);
    String getPermissionLevel(String targetId);
    boolean updateUserRole(String userId, String newRole);
    String getUserRole(String userId);
}

