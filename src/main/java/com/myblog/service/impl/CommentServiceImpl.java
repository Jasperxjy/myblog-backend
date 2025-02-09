package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.CommentDao;
import com.myblog.entity.Comment;
import com.myblog.service.CommentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    /**
     * 获取某篇文章所有可见评论
     *
     * @param essayId 文章id
     * @return 包含所有可见评论的列表
     */
    @Cacheable(value = "essayComments", key = "#essayId",unless = "#result == null")
    @Override
    public List<Comment> getVisibleCommentsByEssayId(String essayId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getEssayId, essayId)
                .eq(Comment::getCommentVisible, "1")
                .orderByDesc(Comment::getCommentTime);
        return list(queryWrapper);
    }

    /**
     * 新增评论
     *
     * @param comment 包含文章id、评论内容的Comment对象，可选包含回复评论id
     * @return 新增的评论对象
     */
    @CacheEvict(value = "essayComments", key = "#comment.essayId")
    @Override
    public Comment addComment(Comment comment) {
        comment.setCommentTime(LocalDateTime.now())
                .setCommentLikeNum(0)
                .setCommentVisible("1");
        save(comment);
        return comment;
    }

    /**
     * 隐藏评论（软删除）
     *
     * @param commentId 评论id
     * @return 操作是否成功
     */
    @CacheEvict(value = "essayComments", allEntries = true)
    @Override
    public boolean hideComment(String commentId) {
        Comment comment = getById(commentId);
        if (comment != null) {
            comment.setCommentVisible("0");
            return updateById(comment);
        }
        return false;
    }

    /**
     * 获取评论详情
     *
     * @param commentId 评论id
     * @return 评论对象，如果不存在则返回null
     */
    @Cacheable(value = "comment", key = "#commentId",unless = "#result == null")
    public Comment getCommentById(String commentId) {
        return getById(commentId);
    }

    /**
     * 评论点赞数加1
     *
     * @param commentId 评论id
     * @return 更新后的评论对象，如果评论不存在则返回null
     */
    @Override
    @CacheEvict(value = {"comment", "essayComments"}, allEntries = true)
    public Comment updateCommentLikes(String commentId) {
        Comment comment = getById(commentId);
        if (comment != null) {
            comment.setCommentLikeNum(comment.getCommentLikeNum() + 1);
            updateById(comment);
            return comment;
        }
        return null;
    }
}
