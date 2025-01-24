package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.NoteDao;
import com.myblog.dto.Result;
import com.myblog.entity.Note;
import com.myblog.service.NoteService;
import org.springframework.stereotype.Service;

/**
 * (Note)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:51
 */
@Service("noteService")
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {

    @Override
    public Result addNote(Note note) {
        return null;
    }

    @Override
    public Result updateNote(Note note) {
        return null;
    }

    @Override
    public Result deleteNote(String noteId) {
        return null;
    }

    @Override
    public Result getNotesByEssay(String essayId) {
        return null;
    }
}

