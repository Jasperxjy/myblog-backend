package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.LoginDTO;
import com.myblog.dto.RegisterDTO;
import com.myblog.dto.Result;
import com.myblog.dto.UserInfoDTO;
import com.myblog.entity.User;
import com.myblog.service.UserService;
import com.myblog.utility.TokenUtil;
import com.myblog.utility.UserRole;
import com.myblog.utility.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * (User)用户表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:31:13
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;

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
        if (user == null) {
            return Result.fail("用户不存在");
        } else if (Objects.equals(user.getStatus(), UserStatus.NORMAL)) { // 为正常使用
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setEmail(email);
            userInfoDTO.setUserName(user.getUserName());
            userInfoDTO.setUserRole(user.getUserRole());
            userInfoDTO.setUserId(user.getUserId());
            userInfoDTO.setStatus(user.getStatus());
            return Result.ok(userInfoDTO);
        } else {
            return Result.fail("用户状态异常：" + user.getStatus().toString());
        }
    }


    /**
     * 用户登录
     * 接收LoginDTO，包含邮箱和密码，登录后返回jwt
     *
     * @param loginDTO 登录信息DTO
     * @return 包含JWT的Result对象
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        String jwt = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
        if (jwt != null) {
            return Result.ok(jwt);
        }
        return Result.fail("登录失败");
    }

    /**
     * 用户注册
     * 接收RegisterDTO，包含用户注册信息，注册后返回用户状态
     *
     * @param registerDTO 注册信息DTO
     * @return 包含用户状态的Result对象
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        // 邮箱格式验证正则表达式
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(registerDTO.getEmail()).matches()) {
            return Result.fail("邮箱格式有误");
        }
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUserPassword(registerDTO.getPassword());
        user.setUserName(registerDTO.getUserName());
        user.setUserRole(registerDTO.getRole());

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

    /**
     * 登出
     * 接收JWT，登出后返回操作结果
     *
     * @param request HttpServletRequest对象，用于获取JWT
     * @return 操作结果的Result对象
     */
    @RequirePermission()
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            String userId = tokenUtil.getUserIdFromToken(token);
            if (userId != null) {
                boolean logoutSuccess = userService.logout(userId);
                if (logoutSuccess) {
                    return Result.ok("登出成功");
                }
            }
        }
        return Result.fail("登出失败");
    }

}
