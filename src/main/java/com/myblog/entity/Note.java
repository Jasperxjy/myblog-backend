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
 * (Note)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:30:51
 */
@Data
@Accessors(chain = true)
@TableName(value = "note",autoResultMap = true)
public class Note implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    //批注id
    @TableId(type = IdType.ASSIGN_UUID)
    private String noteId;
    //批注的用户
    private String userId;
    //批注内容
    private String content;
    //批注在文章中的位置
    private Integer position;
    //创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    //所属文章id
    private String essayId;
}

