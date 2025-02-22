package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * (Collection)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:41:05
 */
@Data
@Accessors(chain = true)
@TableName(value = "collection", autoResultMap = true)
public class Collection implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    //合集号
    @TableId(type = IdType.ASSIGN_UUID)
    private String collectionId;
    //合集名
    private String collectionName;
    //合集内容摘要
    private String collectionAbstract;

}

