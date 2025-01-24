package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * (Permission)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:31:02
 */
@Data
@Accessors(chain = true)
@TableName(value = "permission",autoResultMap = true)
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //权限id
    @TableId(type = IdType.AUTO)
    private Integer permissionId;
    //权限作用对象id
    private String targetid;
    //权限等级
    private String level;
    //创建时间
    private Date createTime;



    }

