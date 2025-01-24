package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.CommentDao;
import com.myblog.entity.Comment;
import com.myblog.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Override
    public List<Comment> getVisibleCommentsByEssayId(String essayId) {
        return List.of();
    }

    @Override
    public Comment addComment(Comment comment) {
        return null;
    }

    @Override
    public boolean hideComment(String commentId) {
        return false;
    }
}

