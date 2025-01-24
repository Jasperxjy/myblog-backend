package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.Tip;
import com.myblog.service.TipService;
import com.myblog.utility.UserRole;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (Tip)贴士表控制层
 *
 * @author makejava
 * @since 2024-12-28 22:36:09
 */
@RestController
@RequestMapping("tip")
public class TipController {

    private final TipService tipService;

    public TipController(TipService tipService) {
        this.tipService = tipService;
    }

    /**
     * 新增贴士
     * 需要 ADMIN 权限
     *
     * @param tip 包含贴士内容的Tip对象
     * @return 包含新增贴士信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping
    public Result addTip(@RequestBody Tip tip) {
        tip.setTime(LocalDateTime.now());
        tip.setStatus(1); // 假设1表示正常状态
        Tip createdTip = tipService.addTip(tip);
        return Result.ok(createdTip);
    }

    /**
     * 以时间倒序获取所有贴士
     *
     * @return 包含所有贴士列表的Result对象
     */
    @RequirePermission(UserRole.GUEST)
    @GetMapping
    public Result getAllTips() {
        List<Tip> tips = tipService.getAllTipsOrderByTimeDesc();
        return Result.ok(tips);
    }

    /**
     * 删除某贴士
     * 需要 ADMIN 权限
     *
     * @param tipId 贴士ID
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/{tipId}")
    public Result deleteTip(@PathVariable String tipId) {
        boolean deleted = tipService.deleteTip(tipId);
        if (deleted) {
            return Result.ok();
        } else {
            return Result.fail("贴士不存在或删除失败");
        }
    }

    /**
     * 修改贴士内容
     * 需要 ADMIN 权限
     *
     * @param tipId 贴士ID
     * @param tip 包含更新内容的Tip对象
     * @return 包含更新后贴士信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{tipId}")
    public Result updateTip(@PathVariable String tipId, @RequestBody Tip tip) {
        tip.setTipId(tipId);
        tip.setTime(LocalDateTime.now()); // 更新修改时间
        Tip updatedTip = tipService.updateTip(tip);
        if (updatedTip != null) {
            return Result.ok(updatedTip);
        } else {
            return Result.fail("贴士不存在或更新失败");
        }
    }

    /**
     * 获取单个贴士详情
     *
     * @param tipId 贴士ID
     * @return 包含贴士详情的Result对象
     */
    @RequirePermission(UserRole.GUEST)
    @GetMapping("/{tipId}")
    public Result getTipById(@PathVariable String tipId) {
        Tip tip = tipService.getTipById(tipId);
        if (tip != null) {
            return Result.ok(tip);
        } else {
            return Result.fail("贴士不存在");
        }
    }

    /**
     * 更改贴士状态
     * 需要 ADMIN 权限
     *
     * @param tipId 贴士ID
     * @param status 新状态
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{tipId}/status")
    public Result updateTipStatus(@PathVariable String tipId, @RequestParam Integer status) {
        boolean updated = tipService.updateTipStatus(tipId, status);
        if (updated) {
            return Result.ok();
        } else {
            return Result.fail("贴士不存在或状态更新失败");
        }
    }
}
