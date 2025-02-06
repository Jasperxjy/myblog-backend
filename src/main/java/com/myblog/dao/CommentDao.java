package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {

}

