package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Lock;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Lock)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:30:29
 */
@Mapper
public interface LockDao extends BaseMapper<Lock> {

}

