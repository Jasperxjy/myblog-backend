package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.EssayTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * (EssayTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:29:38
 */
@Mapper
public interface EssayTagDao extends BaseMapper<EssayTag> {

}

