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
 * (Album)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:28:13
 */
@Data
@Accessors(chain = true)
@TableName(value = "album",autoResultMap = true)
public class Album implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    //相册id
    @TableId(type = IdType.ASSIGN_UUID)
    private String albumId;
    //相册名
    private String title;
    //相册描述
    private String description;
    //相册最后更新时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatetime;
    //相册创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createtime;

    }

