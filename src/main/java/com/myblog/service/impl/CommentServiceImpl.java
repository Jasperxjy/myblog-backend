package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.CommentDao;
import com.myblog.dto.CommentDTO;
import com.myblog.entity.Comment;
import com.myblog.entity.User;
import com.myblog.service.CommentService;
import com.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        // 批量查询用户，避免N+1
        Set<String> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());
        Map<String, User> userMap = userIds.isEmpty()
                ? java.util.Collections.emptyMap()
                : userService.listByIds(userIds).stream()
                        .collect(Collectors.toMap(User::getUserId, u -> u));

        return comments.stream()
                .map(c -> convertToDTO(c, userMap))
                .collect(Collectors.toList());
    }

    private CommentDTO convertToDTO(Comment comment, Map<String, User> userMap) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getCommentId());
        dto.setEssayId(comment.getEssayId());
        dto.setContent(comment.getContent());
        dto.setSenderUserId(comment.getUserId());
        User user = userMap.get(comment.getUserId());
        dto.setSenderUsername(user != null ? user.getUserName() : null);
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
