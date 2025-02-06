package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Music;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Music)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:30:40
 */
@Mapper
public interface MusicDao extends BaseMapper<Music> {

}

