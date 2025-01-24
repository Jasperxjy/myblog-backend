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
 * (Image)表实体类
 *
 * @author makejava
 * @since 2024-12-28 18:30:16
 */
@Data
@Accessors(chain = true)
@TableName(value = "image",autoResultMap = true)
public class Image implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    //图片id
    @TableId(type = IdType.ASSIGN_UUID)
    private String imageId;
    //图片名
    private String fileName;
    //图片文件路径
    private String filePath;
    //若被文章引用，写名文章id
    private String essayId;
    //若被收录到相册，写相册id
    private String albumId;
    //上传时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    //图片描述
    private String description;
    //图片缩略图路径
    private String previewPath;


    }

