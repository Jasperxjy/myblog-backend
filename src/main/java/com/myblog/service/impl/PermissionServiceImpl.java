package com.myblog.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.PermissionDao;
import com.myblog.dao.UserDao;
import com.myblog.entity.Permission;
import com.myblog.entity.User;
import com.myblog.service.PermissionService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 权限服务实现类
 * 继承自 MyBatis-Plus 的 ServiceImpl，实现 PermissionService 接口
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 检查用户是否具有访问特定对象的权限
     *
     * @param userId   用户ID
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @return 如果用户具有所需权限则返回 true，否则返回 false
     */
    @Override
    @Cacheable(value = "haspermission", key =  "#userId + #targetId")
    public boolean hasPermission(String userId, String targetId) {
        // 根据用户ID获取用户信息
        User user = userDao.selectById(userId);
        if (user == null) {
            return false; // 如果用户不存在，返回 false
        }
        // 获取当前bean的代理对象
        PermissionService proxyPermissionService = applicationContext.getBean(PermissionService.class);
        // 使用代理对象调用getPermissionLevel方法
        String permissionlevel = proxyPermissionService.getPermissionLevel(targetId);
        // 检查用户角色是否满足目标对象的权限要求
        return UserRole.hasPermission(user.getUserRole(), permissionlevel);
    }

    /**
     * 设置特定对象的权限等级
     *
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @param level    权限等级
     * @return 更新是否成功
     */
    @Override
    @CachePut(value = "permission", key = "#targetId")
    @CacheEvict(value = "haspermission", allEntries = true)
    public String setPermission(String targetId, String level) {
        // 创建 Permission 对象并设置其属性
        Permission permission = query().eq("targetid", targetId).one();
        if (permission == null) {
            permission = new Permission();
            permission.setTargetid(targetId);
            permission.setLevel(level);
            permission.setCreateTime(DateTime.now()); // 设置创建时间为当前时间
            save(permission);
        } else {
            permission.setLevel(level);
            permission.setCreateTime(DateTime.now()); // 设置创建时间为当前时间
            updateById(permission);
        }
        // 返回设置的权限等级
        return level;
    }


    /**
     * 获取特定对象的权限等级
     *
     * @param targetId 目标对象ID（如文章ID、合集ID等）
     * @return 权限等级，如果未找到则返回最低权限等级（GUEST）
     */
    @Override
    @Cacheable(value = "permission", key = "#targetId", unless = "#result == null")
    public String getPermissionLevel(String targetId) {
        Permission permission = query().eq("targetid", targetId).one();
        if(permission == null||permission.getLevel()==null) {
            return UserRole.GUEST;
        }else{
            return permission.getLevel();
        }
    }

    /**
     * 更新用户的权限角色
     *
     * @param userId  用户ID
     * @param newRole 新的权限角色
     * @return 更新是否成功
     */
    @Override
    public boolean updateUserRole(String userId, String newRole) {
        User user = new User();
        user.setUserId(userId);
        user.setUserRole(newRole);
        // 使用 UserDao 更新用户角色
        return userDao.updateById(user) > 0;
    }

    /**
     * 获取用户的权限角色
     *
     * @param userId 用户ID
     * @return 用户的权限角色，如果未找到则返回 GUEST
     */
    @Override
    public String getUserRole(String userId) {
        User user = userDao.selectById(userId);
        // 如果找到用户，返回其角色；否则返回 GUEST 角色
        return user != null ? user.getUserRole() : UserRole.GUEST;
    }
}
