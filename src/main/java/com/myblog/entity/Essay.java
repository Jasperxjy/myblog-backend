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
 * (Essay)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:29:24
 */
@Data
@Accessors(chain = true)
@TableName(value = "essay",autoResultMap = true)
public class Essay implements Serializable  {
    @Serial
    private static final long serialVersionUID = 1L;
    //一般文章id
    @TableId(type = IdType.ASSIGN_UUID)
    private String essayId;
    //文章标题
    private String essayTitle;
    //文章添加时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime essayAddTime;
    //文章上一次修改时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime essayLastChangeTime;
    //文章类型
    private String essayType;
    //文章内容
    private String essayContext;
    //点赞次数
    private Integer essayLikeNum;
    //观看次数
    private Integer essayViewNum;
    //收藏次数
    private Integer essayCollectionNum;
    //作者用户id
    private String userId;
    //版本号用于加锁
    private Integer version;
    //状态，用于标识是否删除
    private Integer status;
    //用于添加到合集
    private String classId;



    }

