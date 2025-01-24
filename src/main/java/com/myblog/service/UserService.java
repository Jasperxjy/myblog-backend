package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.User;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:31:13
 */
public interface UserService extends IService<User> {

    User getUserByEmail(String email);

    String login(String email, String password);

    Integer register(User user);

    List<User> getPendingUsers();

    boolean updateUserStatus(String userId, Integer status);

    String guestLogin();

    List<User> getAllUsers();
}

