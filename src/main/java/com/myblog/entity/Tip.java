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
 * (Tip)表实体类
 *
 * @author makejava
 * @since 2024-12-28 22:36:09
 */
@Data
@Accessors(chain = true)
@TableName(value = "tip",autoResultMap = true)
public class Tip implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //贴士id
    @TableId(type = IdType.ASSIGN_UUID)
    private String tipId;
    //贴士内容
    private String content;
    //贴士发布时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    //贴士状态
    private Integer status;



    }

