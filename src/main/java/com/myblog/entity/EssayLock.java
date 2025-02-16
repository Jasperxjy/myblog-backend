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
 * (Lock)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:30:29
 */
@Data
@Accessors(chain = true)
@TableName(value = "essay_lock",autoResultMap = true)
public class EssayLock implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //锁id
    @TableId(type = IdType.AUTO)
    private Integer lockId;
    //对应文章id
    private String essayId;
    //上锁用户
    private String userId;
    //上锁时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lockTime;


    }

