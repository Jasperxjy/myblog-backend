package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.utility.AesTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:31:13
 */
@Data
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //使用uuid生成的用户id
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;
    //用户名
    @TableField(value = "password",typeHandler = AesTypeHandler.class)
    private String userName;
    //用户密码
    @TableField(value = "password",typeHandler = AesTypeHandler.class)
    private String userPassword;
    //用户注册时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userRegisterTime;
    //用户身份
    @TableField(value = "password",typeHandler = AesTypeHandler.class)
    private String userRole;
    //用户使用邮箱注册
    private String email;
    //用户状态，用于标识申请中、正常、废弃等状态
    private Integer status;

    }

