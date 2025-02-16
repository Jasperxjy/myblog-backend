package com.myblog.controller;

import com.myblog.dto.Result;
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
@RequestMapping("/api/permission")
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
    public Result hasPermission(@RequestParam String userId, @RequestParam String targetId) {
        boolean hasPermission = permissionService.hasPermission(userId, targetId);
        return hasPermission ? Result.ok("用户具有访问权限") : Result.fail("用户无访问权限");
    }

    /**
     * 设置特定对象的权限等级
     *
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @param level 权限等级
     * @return 返回操作结果
     */
    @PostMapping("/setPermission")
    public Result setPermission(@RequestParam String targetId, @RequestParam String level) {
        boolean success = permissionService.setPermission(targetId, level);
        return success ? Result.ok("权限设置成功") : Result.fail("权限设置失败");
    }

    /**
     * 获取特定对象的权限等级
     *
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @return 返回目标对象的权限等级
     */
    @GetMapping("/getPermissionLevel")
    public Result getPermissionLevel(@RequestParam String targetId) {
        String level = permissionService.getPermissionLevel(targetId);
        return Result.ok(level);
    }

    /**
     * 更新用户的权限角色
     *
     * @param userId 用户ID
     * @param newRole 新的权限角色
     * @return 返回更新结果
     */
    @PostMapping("/updateUserRole")
    public Result updateUserRole(@RequestParam String userId, @RequestParam String newRole) {
        boolean success = permissionService.updateUserRole(userId, newRole);
        return success ? Result.ok("用户角色更新成功") : Result.fail("用户角色更新失败");
    }

    /**
     * 获取用户的权限角色
     *
     * @param userId 用户ID
     * @return 返回用户的权限角色
     */
    @GetMapping("/getUserRole")
    public Result getUserRole(@RequestParam String userId) {
        String role = permissionService.getUserRole(userId);
        return Result.ok(role);
    }
}
