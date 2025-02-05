package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Comment;

import java.util.List;

/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
public interface CommentService extends IService<Comment> {

    List<Comment> getVisibleCommentsByEssayId(String essayId);

    Comment addComment(Comment comment);

    boolean hideComment(String commentId);

    Comment updateCommentLikes(String commentId);
}

