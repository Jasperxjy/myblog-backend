package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Permission)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:31:02
 */
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {

}

