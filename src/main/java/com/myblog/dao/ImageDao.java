package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Image;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Image)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:30:16
 */
@Mapper
public interface ImageDao extends BaseMapper<Image> {

}

