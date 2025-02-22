package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.NoteDao;
import com.myblog.dto.Result;
import com.myblog.entity.Note;
import com.myblog.service.NoteService;
import com.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (Note)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:51
 */
@Service("noteService")
public class NoteServiceImpl extends ServiceImpl<NoteDao, Note> implements NoteService {
    @Autowired
    private UserService userService;

    @Override
    @CacheEvict(value = "notesByEssay", key = "#note.essayId")
    public Result addNote(Note note) {
        // 校验批注内容
        if (note.getContent() == null || note.getContent().isEmpty()) {
            return Result.fail("批注内容不能为空");
        }

        // 设置创建时间
        note.setCreateTime(LocalDateTime.now());

        // 保存批注
        boolean saved = save(note);
        if (saved) {
            // 重新查询以获取完整的 Note 对象（包括生成的主键）
            Note savedNote = getById(note.getNoteId());
            String userId = savedNote.getUserId();
            savedNote.setUserId(userService.getById(userId).getUserName());
            return Result.ok(savedNote);
        } else {
            return Result.fail("批注添加失败");
        }
    }

    @Override
    @CacheEvict(value = "notesByEssay", key = "#note.essayId")
    public Result updateNote(Note note) {
        // 校验批注 ID
        if (note.getNoteId() == null || note.getNoteId().isEmpty()) {
            return Result.fail("批注 ID 不能为空");
        }

        // 查询现有批注
        Note existingNote = getById(note.getNoteId());
        if (existingNote == null) {
            return Result.fail("批注不存在");
        }

        // 更新批注内容
        existingNote.setContent(note.getContent());
        existingNote.setPosition(note.getPosition());

        // 保存更新
        boolean updated = updateById(existingNote);
        if (updated) {
            return Result.ok("批注更新成功");
        } else {
            return Result.fail("批注更新失败");
        }
    }

    @Override
    @CacheEvict(value = "notesByEssay", key = "#essayId")
    public Result deleteNote(String noteId,String essayId) {
        // 校验批注 ID
        if (noteId == null || noteId.isEmpty()) {
            return Result.fail("批注 ID 不能为空");
        }

        // 删除批注
        boolean removed = removeById(noteId);
        if (removed) {
            return Result.ok("批注删除成功");
        } else {
            return Result.fail("批注删除失败");
        }
    }

    @Override
    @Cacheable(value = "notesByEssay", key = "#essayId", unless = "#result == null")
    public Result getNotesByEssay(String essayId) {
        // 校验文章 ID
        if (essayId == null || essayId.isEmpty()) {
            return Result.fail("文章 ID 不能为空");
        }

        // 查询文章的所有批注
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("essay_id", essayId)
                .orderByAsc("position"); // 按位置排序
        List<Note> notes = list(queryWrapper);
        // 将用户 ID 转换为用户名
        for (Note note : notes) {
            String userId = note.getUserId();
            note.setUserId(userService.getById(userId).getUserName());
        }

        return Result.ok(notes);
    }
}

