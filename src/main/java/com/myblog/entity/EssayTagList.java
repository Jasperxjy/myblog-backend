package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * (EssayTagList)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:29:59
 */
@Data
@Accessors(chain = true)
@TableName(value = "essay_tag_list",autoResultMap = true)
public class EssayTagList implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //文章对应标签列表id
    @TableId(type = IdType.ASSIGN_UUID)
    private String essayTagListId;
    //关联文章的id
    private String essayId;
    //关联tag的id
    private String essayTagId;


    }

