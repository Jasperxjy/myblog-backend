package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.EssayTagList;
import org.apache.ibatis.annotations.Mapper;

/**
 * (EssayTagList)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:29:59
 */
@Mapper
public interface EssayTagListDao extends BaseMapper<EssayTagList> {

}

