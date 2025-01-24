package com.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * (Music)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:30:40
 */
@Data
@Accessors(chain = true)
@TableName(value = "music",autoResultMap = true)
public class Music implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //音乐id
    @TableId(type = IdType.ASSIGN_UUID)
    private String musicId;
    //音乐名
    private String fileName;
    //音乐文件地址
    private String filePath;
    //音乐描述
    private String description;



    }

