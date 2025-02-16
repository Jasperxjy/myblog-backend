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
    @Override
    public boolean hideComment(String commentId) {
        Comment comment = getById(commentId);
        if (comment != null) {
            comment.setCommentVisible("0");
            boolean result = updateById(comment);
            if (result) {
                // 手动清除缓存
                clearCommentCache(comment.getEssayId());
            }
            return result;
        }
        return false;
    }

    /**
     * 清除指定文章的评论缓存
     *
     * @param essayId 文章id
     */
    @CacheEvict(value = "essayComments", key = "#essayId")
    public void clearCommentCache(String essayId) {
        // 这个方法体可以为空，@CacheEvict 注解会处理缓存的清除
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
    public Comment updateCommentLikes(String commentId) {
        Comment comment = getById(commentId);
        if (comment != null) {
            comment.setCommentLikeNum(comment.getCommentLikeNum() + 1);
            boolean updated = updateById(comment);
            if (updated) {
                // 清除特定评论的缓存
                clearCommentCache(commentId);
                // 清除该评论所属文章的评论列表缓存
                clearEssayCommentsCache(comment.getEssayId());
            }
            return comment;
        }
        return null;
    }

    /**
     * 清除指定文章的评论列表缓存
     *
     * @param essayId 文章id
     */
    @CacheEvict(value = "essayComments", key = "#essayId")
    public void clearEssayCommentsCache(String essayId) {
        // 方法体可以为空，注解会处理缓存的清除
    }

}
