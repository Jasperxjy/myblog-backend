package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Tip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (Tip)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 22:36:09
 */
@Mapper
public interface TipDao extends BaseMapper<Tip> {

    @Select("SELECT * FROM tip ORDER BY time DESC")
    List<Tip> selectListByTimeDesc();
}

