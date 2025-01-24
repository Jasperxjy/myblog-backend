package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:29:09
 */
@Data
@Accessors(chain = true)
@TableName(value = "comment",autoResultMap = true)
public class Comment implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    //文章评论id
    @TableId(type = IdType.ASSIGN_UUID)
    private String commentId;
    //文章id
    private String essayId;
    //评论发送时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;
    //评论发送者id
    private String userId;
    //评论点赞数
    private Integer commentLikeNum;
    //评论是否可见
    private String commentVisible;
    //指向该评论所回复的评论的id，如果没有则为空
    private String commentFatherId;
    //评论内容
    private String content;

    }

