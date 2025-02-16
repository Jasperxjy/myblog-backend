package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.service.LockService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lock")
public class LockController {

    @Autowired
    private LockService lockService;

    /**
     * 检查文章是否被锁定
     *
     * @param essayId 文章ID
     * @return 锁定状态的Result对象
     */
    @RequirePermission()
    @GetMapping("/check/{essayId}")
    public Result checkLock(@PathVariable String essayId) {
        return lockService.checkLock(essayId);
    }

    /**
     * 强制解锁文章（管理员功能）
     *
     * @param essayId 文章ID
     * @param adminId 管理员ID
     * @return 解锁结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/force-unlock/{essayId}")
    public Result forceUnlock(@PathVariable String essayId, @RequestParam String adminId) {
        return lockService.forceUnlockEssay(essayId, adminId);
    }

    /**
     * 获取所有当前被锁定的文章
     *
     * @return 包含所有锁定文章信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @GetMapping("/all-locked")
    public Result getAllLockedEssays() {
        return lockService.getAllLockedEssays();
    }
}
