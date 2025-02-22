package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.CommentDao;
import com.myblog.dto.CommentDTO;
import com.myblog.entity.Comment;
import com.myblog.service.CommentService;
import com.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 获取某篇文章所有可见评论
     *
     * @param essayId 文章id
     * @return 包含所有可见评论的列表
     */
    @Cacheable(value = "essayComments", key = "#essayId",unless = "#result == null")
    @Override
    public List<CommentDTO> getVisibleCommentsByEssayId(String essayId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getEssayId, essayId)
                .eq(Comment::getCommentVisible, "1")
                .orderByDesc(Comment::getCommentTime);
        List<Comment> comments = list(queryWrapper);
        return comments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getCommentId());
        dto.setEssayId(comment.getEssayId());
        dto.setContent(comment.getContent());
        dto.setSenderUserId(comment.getUserId());
        dto.setSenderUsername(userService.getById(comment.getUserId()).getUserName());
        dto.setCommentTime(comment.getCommentTime());
        dto.setCommentLikeNum(comment.getCommentLikeNum());
        dto.setReplyCommentId(comment.getCommentFatherId());
        return dto;
    }
    /**
     * 新增评论
     *
     * @param comment 包含文章id、评论内容的Comment对象，可选包含回复评论id
     * @return 新增的评论对象
     */
    @CacheEvict(value = "essayComments", key = "#comment.essayId",condition = "#comment.essayId!= null")
    @Override
    public boolean addComment(Comment comment) {
        comment.setCommentTime(LocalDateTime.now())
                .setCommentLikeNum(0)
                .setCommentVisible("1");
        return save(comment);
    }

    /**
     * 隐藏评论（软删除）
     *
     * @param commentId 评论id
     * @return 操作是否成功
     */
    @Override
    @CacheEvict(value = "essayComments", key = "#result.essayId",condition = "#result!= null")
    public Comment hideComment(String commentId) {
        Comment comment = getById(commentId);
        if (comment != null) {
            comment.setCommentVisible("0");
            if(updateById(comment)){
                return comment;
            }
        }
        return null;
    }


    /**
     * 评论点赞数加1
     *
     * @param commentId 评论id
     * @return 更新后的评论对象，如果评论不存在则返回null
     */
    @Override
    @CacheEvict(value = "essayComments", key = "#result.essayId",condition = "#result != null")
    public Comment updateCommentLikes(String commentId) {
        Comment comment = getById(commentId);
        if (comment != null) {
            comment.setCommentLikeNum(comment.getCommentLikeNum() + 1);
            boolean updated = updateById(comment);
            if (updated) {
                return comment;
            }
        }
        return null;
    }
}
