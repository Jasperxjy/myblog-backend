package com.myblog.controller;

import com.myblog.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Permission)权限表控制层
 * 用于管理用户权限相关的操作
 *
 * @author makejava
 * @since 2024-12-28 18:31:02
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 检查用户是否具有访问特定对象的权限
     *
     * @param userId 用户ID
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @return 返回是否有权限的结果
     */
    @GetMapping("/hasPermission")
    public boolean hasPermission(@RequestParam String userId, @RequestParam String targetId) {
        return permissionService.hasPermission(userId, targetId);
    }

    /**
     * 设置特定对象的权限等级
     *
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @param level 权限等级
     * @return 返回操作是否成功
     */
    @PostMapping("/setPermission")
    public boolean setPermission(@RequestParam String targetId, @RequestParam String level) {
        return permissionService.setPermission(targetId, level);
    }

    /**
     * 获取特定对象的权限等级
     *
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @return 返回目标对象的权限等级
     */
    @GetMapping("/getPermissionLevel")
    public String getPermissionLevel(@RequestParam String targetId) {
        return permissionService.getPermissionLevel(targetId);
    }

    /**
     * 更新用户的权限角色
     *
     * @param userId 用户ID
     * @param newRole 新的权限角色
     * @return 返回更新是否成功
     */
    @PostMapping("/updateUserRole")
    public boolean updateUserRole(@RequestParam String userId, @RequestParam String newRole) {
        return permissionService.updateUserRole(userId, newRole);
    }

    /**
     * 获取用户的权限角色
     *
     * @param userId 用户ID
     * @return 返回用户的权限角色
     */
    @GetMapping("/getUserRole")
    public String getUserRole(@RequestParam String userId) {
        return permissionService.getUserRole(userId);
    }
}
