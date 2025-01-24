package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * (EssayTag)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:29:38
 */
@Data
@Accessors(chain = true)
@TableName(value = "essay_tag",autoResultMap = true)
public class EssayTag implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //文章标签id
    @TableId(type = IdType.ASSIGN_UUID)
    private String essayTagId;
    //标签名称
    private String essayTagName;

    }

