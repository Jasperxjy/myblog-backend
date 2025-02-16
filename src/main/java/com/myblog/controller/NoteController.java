package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.Note;
import com.myblog.service.NoteService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * 添加批注
     *
     * @param note 批注信息
     * @return 添加结果
     */
    @RequirePermission(UserRole.CLOSE_FRIEND)
    @PostMapping("/add")
    public Result addNote(@RequestBody Note note) {
        return noteService.addNote(note);
    }

    /**
     * 修改批注
     *
     * @param noteId 批注ID
     * @param note 更新的批注信息
     * @return 修改结果
     */
    @RequirePermission(UserRole.CLOSE_FRIEND)
    @PutMapping("/{noteId}/update")
    public Result updateNote(@PathVariable String noteId, @RequestBody Note note) {
        note.setNoteId(noteId);
        return noteService.updateNote(note);
    }

    /**
     * 删除批注
     *
     * @param noteId 批注ID
     * @return 删除结果
     */
    @RequirePermission(UserRole.CLOSE_FRIEND)
    @DeleteMapping("/{noteId}/del")
    public Result deleteNote(@PathVariable String noteId,@RequestParam String essayId) {
        return noteService.deleteNote(noteId,essayId);
    }

    /**
     * 获取文章的所有批注
     *
     * @param essayId 文章ID
     * @return 批注列表
     */
    @RequirePermission()
    @GetMapping("/essay/{essayId}")
    public Result getNotesByEssay(@PathVariable String essayId) {
        return noteService.getNotesByEssay(essayId);
    }
}
