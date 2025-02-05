package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.entity.Comment;
import com.myblog.service.CommentService;
import com.myblog.dto.Result;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Comment)评论表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取某篇文章所有评论
     *
     * @param essayId 文章id
     * @return 包含所有可见评论内容的Result对象
     */
    @RequirePermission(UserRole.GUEST)
    @GetMapping("/essay/{essayId}")
    public Result getCommentsByEssayId(@PathVariable String essayId) {
        List<Comment> comments = commentService.getVisibleCommentsByEssayId(essayId);
        return Result.ok(comments);
    }

    /**
     * 新增评论
     *
     * @param comment 包含文章id、评论内容的Comment对象，可选包含回复评论id
     * @return 包含新增评论信息的Result对象
     */
    @RequirePermission(UserRole.FRIEND)
    @PostMapping
    public Result addComment(@RequestBody Comment comment) {
        Comment addedComment = commentService.addComment(comment);
        return Result.ok(addedComment);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.FRIEND)
    @DeleteMapping("/{commentId}")
    public Result deleteComment(@PathVariable String commentId) {
        boolean success = commentService.hideComment(commentId);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("评论不存在或删除失败");
        }
    }
    /**
     * 评论点赞
     *
     * @param commentId 评论id
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.FRIEND)
    @PostMapping("/{commentId}/like")
    public Result likeComment(@PathVariable String commentId) {
        Comment likedComment = commentService.updateCommentLikes(commentId);
        return Result.ok(likedComment);
    }
}
