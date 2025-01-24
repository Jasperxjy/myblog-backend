package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.User;
import com.myblog.service.UserService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (User)用户表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:31:13
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检查用户是否存在
     * 在输入邮箱后，检查是否有这个用户，如果有且状态为正常使用那么返回用户名
     * 此接口也用于返回用户是否存在以确定是否能够注册
     *
     * @param email 用户邮箱
     * @return 包含用户信息的Result对象
     */
    @GetMapping("/check")
    public Result checkUser(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        if (user != null && user.getStatus() == 1) { // 假设状态1为正常使用
            return Result.ok(user.getUserName());
        }
        return Result.fail("用户不存在或状态异常");
    }

    /**
     * 用户登录
     * 在输入邮箱和密码后，登录，查看状态，返回jwt
     *
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 包含JWT的Result对象
     */
    @PostMapping("/login")
    public Result login(@RequestParam String email, @RequestParam String password) {
        String jwt = userService.login(email, password);
        if (jwt != null) {
            return Result.ok(jwt);
        }
        return Result.fail("登录失败");
    }

    /**
     * 用户注册
     * 在没有此用户或者状态为不通过、废弃时，输入邮箱、密码、身份（选项框）后注册，返回用户状态
     *
     * @param user 包含用户信息的User对象
     * @return 包含用户状态的Result对象
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        Integer status = userService.register(user);
        if (status != null) {
            return Result.ok(status);
        }
        return Result.fail("注册失败");
    }

    /**
     * 查看所有等待审核用户
     * 返回用户id，用户名，邮箱
     *
     * @return 包含待审核用户列表的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @GetMapping("/pending")
    public Result getPendingUsers() {
        List<User> pendingUsers = userService.getPendingUsers();
        return Result.ok(pendingUsers);
    }

    /**
     * 对某用户进行审核或者废弃
     * 传入用户id，将状态更改为正常使用、不通过、废弃
     *
     * @param userId 用户ID
     * @param status 新状态
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/review/{userId}")
    public Result reviewUser(@PathVariable String userId, @RequestParam Integer status) {
        boolean updated = userService.updateUserStatus(userId, status);
        if (updated) {
            return Result.ok();
        }
        return Result.fail("审核失败");
    }

    /**
     * 游客登录
     * 使用默认游客账号密码登录，返回jwt
     *
     * @return 包含游客JWT的Result对象
     */
    @GetMapping("/guest")
    public Result guestLogin() {
        String jwt = userService.guestLogin();
        return Result.ok(jwt);
    }

    /**
     * 查看所有用户
     * 返回用户id，用户名，邮箱
     *
     * @return 包含所有用户列表的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @GetMapping("/all")
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.ok(users);
    }
}
