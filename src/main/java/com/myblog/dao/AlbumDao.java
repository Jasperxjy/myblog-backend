package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Album;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Album)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:28:09
 */
@Mapper
public interface AlbumDao extends BaseMapper<Album> {

}

