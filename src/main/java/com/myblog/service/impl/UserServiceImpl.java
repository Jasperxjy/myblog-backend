package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.UserDao;
import com.myblog.entity.User;
import com.myblog.service.UserService;
import com.myblog.utility.TokenUtil;
import com.myblog.utility.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Cacheable(value = "user",key = "#email",unless = "#result == null")
    @Override
    public User getUserByEmail(String email) {
        return userDao.selectOne(new QueryWrapper<User>().eq("email", email));
    }

    @Override
    public String login(String email, String password) {
        User user = userDao.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user != null && Objects.equals(user.getStatus(), UserStatus.NORMAL) && user.getUserPassword().equals(password)) {
            String token = tokenUtil.generateToken(user.getUserId(), user.getUserRole());
            // 将token存储到Redis，设置过期时间
            redisTemplate.opsForValue().set("token:" + user.getUserId(), token, tokenUtil.getExpiration(), TimeUnit.SECONDS);
            return token;
        }
        return null;
    }

    @Override
    public Integer register(User user) {
        User existingUser = userDao.selectOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        if (existingUser == null || !Objects.equals(existingUser.getStatus(), UserStatus.NORMAL)) {
            user.setUserRegisterTime(LocalDateTime.now());
            user.setStatus(UserStatus.PENDING); // 设置为待审核状态
            save(user);
            return user.getStatus();
        }
        return null;
    }

    @Override
    public List<User> getPendingUsers() {
        return list(new QueryWrapper<User>().eq("status",  UserStatus.PENDING));
    }

    @Override
    public boolean updateUserStatus(String userId, Integer status) {
        User user = getById(userId);
        if (user != null) {
            user.setStatus(status);
            updateUserCache(user.getEmail());
            return updateById(user);
        }
        return false;
    }
    @CachePut(value = "user", key = "#email")
    public void updateUserCache(String email) {
    }


    @Override
    public String guestLogin() {
        User guestUser = userDao.selectOne(new QueryWrapper<User>().eq("user_role", "GUEST"));
        if (guestUser != null) {
            String token = tokenUtil.generateToken(guestUser.getUserId(), guestUser.getUserRole());
            // 将token存储到Redis，设置过期时间
            redisTemplate.opsForValue().set("token:" + guestUser.getUserId(), token, tokenUtil.getExpiration(), TimeUnit.SECONDS);
            return token;
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return list();
    }

    @Override
    public boolean logout(String userId) {
        String key = "token:" + userId;
        Boolean deleted = redisTemplate.delete(key);
        return Boolean.TRUE.equals(deleted);
    }
}
