package com.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.entity.Note;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Note)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-28 18:30:51
 */
@Mapper
public interface NoteDao extends BaseMapper<Note> {

}

