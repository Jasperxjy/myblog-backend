package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.UserDao;
import com.myblog.entity.User;
import com.myblog.service.UserService;
import com.myblog.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public User getUserByEmail(String email) {
        return userDao.selectOne(new QueryWrapper<User>().eq("email", email));
    }

    @Override
    public String login(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getStatus() == 1 && user.getUserPassword().equals(password)) {
            return tokenUtil.generateToken(user.getUserId(), user.getUserRole());
        }
        return null;
    }

    @Override
    public Integer register(User user) {
        User existingUser = getUserByEmail(user.getEmail());
        if (existingUser == null || existingUser.getStatus() != 1) {
            user.setUserRegisterTime(LocalDateTime.now());
            user.setStatus(0); // 设置为待审核状态
            save(user);
            return user.getStatus();
        }
        return null;
    }

    @Override
    public List<User> getPendingUsers() {
        return list(new QueryWrapper<User>().eq("status", 0));
    }

    @Override
    public boolean updateUserStatus(String userId, Integer status) {
        User user = getById(userId);
        if (user != null) {
            user.setStatus(status);
            return updateById(user);
        }
        return false;
    }

    @Override
    public String guestLogin() {
        User guestUser = userDao.selectOne(new QueryWrapper<User>().eq("user_role", "GUEST"));
        if (guestUser != null) {
            return tokenUtil.generateToken(guestUser.getUserId(), guestUser.getUserRole());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return list();
    }
}
