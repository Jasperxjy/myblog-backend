package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.dto.Result;
import com.myblog.entity.Note;

/**
 * (Note)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:30:51
 */
public interface NoteService extends IService<Note> {
    Result addNote(Note note);
    Result updateNote(Note note);
    Result deleteNote(String noteId,String essayId);
    Result getNotesByEssay(String essayId);
}

