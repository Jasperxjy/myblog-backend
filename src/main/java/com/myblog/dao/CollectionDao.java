package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Collection;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Collection)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:41:05
 */
@Mapper
public interface CollectionDao extends BaseMapper<Collection> {

}

