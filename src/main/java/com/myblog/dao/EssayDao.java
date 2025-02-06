package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Essay;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Essay)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:29:24
 */
@Mapper
public interface EssayDao extends BaseMapper<Essay> {

}

