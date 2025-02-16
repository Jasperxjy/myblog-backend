package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.ImageDao;
import com.myblog.entity.Image;
import com.myblog.service.ImageService;
import com.myblog.utility.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * (Image)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:16
 */
@Service("imageService")
public class ImageServiceImpl extends ServiceImpl<ImageDao, Image> implements ImageService {

    @Value("${image.root.path}")
    private String imageRootPath;

    // 定义日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Value("${image.default.album.id}")
    private String defaultAlbumId;

    @Cacheable(value = "image",key = "#imageId",unless = "#result == null")
    @Override
    public String getImagePath(String imageId) {
        Image image = getById(imageId);
        return image != null ? image.getFilePath() : null;
    }

    @Override
    public Image uploadImage(MultipartFile file, String essayId, String albumId, String description) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = Paths.get(imageRootPath, "image", fileName).toString();

            // 确保目录存在
            Files.createDirectories(Paths.get(imageRootPath, "image"));

            // 保存文件
            file.transferTo(new File(filePath));

            // 生成缩略图
            String thumbnailFileName = "thumbnail_" + fileName;
            String thumbnailPath = Paths.get(imageRootPath, "image", thumbnailFileName).toString();
            ImageUtils.generateThumbnail(filePath, thumbnailPath, 300); // 假设缩略图最大边长为300像素

            // 创建Image对象
            Image image = new Image()
                    .setFileName(fileName)
                    .setFilePath(filePath)
                    .setEssayId(essayId)
                    .setAlbumId(essayId == null ? (albumId != null ? albumId : defaultAlbumId) : null)
                    .setUpdateTime(LocalDateTime.now())
                    .setDescription(description)
                    .setPreviewPath(thumbnailPath);

            // 保存到数据库
            save(image);

            return query().eq("file_name", fileName).one();
        } catch (IOException e) {
            logger.error("保存文件错误", e);
            return null;
        }
    }

    @Cacheable(value = "Essay_images",key = "#essayId",unless = "#result == null")
    @Override
    public List<Image> getImagesByEssay(String essayId) {
        return list(new QueryWrapper<Image>().eq("essay_id", essayId));
    }

    @Override
    public boolean deleteImage(String imageId) {
        Image image = getById(imageId);
        if (image != null) {
            // 删除原始文件
            try {
                Files.deleteIfExists(Paths.get(image.getFilePath()));

                // 删除缩略图
                if (image.getPreviewPath() != null) {
                    Files.deleteIfExists(Paths.get(image.getPreviewPath()));
                }

                // 从数据库中删除记录
                return removeById(imageId);
            } catch (IOException e) {
                logger.error("删除图片错误", e);
                return false;
            }
        }
        return false;
    }

    @Override
    public Image updateImage(Image image) {
        if (updateById(image)) {
            // 清除相关缓存
            clearImageCache(image.getImageId());
            if (image.getEssayId() != null) {
                clearEssayImagesCache(image.getEssayId());
            }
            if (image.getAlbumId() != null) {
                clearAlbumImagesCache(image.getAlbumId());
            } else {
                clearUncategorizedImagesCache();
            }
            return getById(image.getImageId());
        }
        return null;
    }

    @Cacheable(value = "Album_images",key = "#albumId",unless = "#result == null")
    @Override
    public List<Image> getImagesByAlbum(String albumId) {
        return list(new QueryWrapper<Image>().eq("album_id", albumId));
    }

    @Override
    public List<Image> getUncategorizedImages() {
        return list(new QueryWrapper<Image>().isNull("essay_id").isNull("album_id"));
    }

    @Override
    public boolean addImageToAlbum(String imageId, String albumId) {
        Image image = getById(imageId);
        if (image != null) {
            String oldAlbumId = image.getAlbumId();
            image.setAlbumId(albumId);
            boolean updated = updateById(image);
            if (updated) {
                // 清除相关缓存
                clearImageCache(imageId);
                clearAlbumImagesCache(albumId);
                if (oldAlbumId != null) {
                    clearAlbumImagesCache(oldAlbumId);
                } else {
                    clearUncategorizedImagesCache();
                }
            }
            return updated;
        }
        return false;
    }

    @Override
    public boolean removeImageFromAlbum(String imageId, String albumId) {
        Image image = getById(imageId);
        if (image != null && albumId.equals(image.getAlbumId())) {
            image.setAlbumId(null);
            boolean updated = updateById(image);
            if (updated) {
                // 清除相关缓存
                clearImageCache(imageId);
                clearAlbumImagesCache(albumId);
                clearUncategorizedImagesCache();
            }
            return updated;
        }
        return false;
    }
    // 新增的缓存清除方法
    @CacheEvict(value = "image", key = "#imageId")
    public void clearImageCache(String imageId) {
        // 方法体可以为空，注解会处理缓存的清除
    }

    @CacheEvict(value = "Essay_images", key = "#essayId")
    public void clearEssayImagesCache(String essayId) {
        // 方法体可以为空，注解会处理缓存的清除
    }

    @CacheEvict(value = "Album_images", key = "#albumId")
    public void clearAlbumImagesCache(String albumId) {
        // 方法体可以为空，注解会处理缓存的清除
    }

    @CacheEvict(value = "Uncategorized_images", allEntries = true)
    public void clearUncategorizedImagesCache() {
        // 方法体可以为空，注解会处理缓存的清除
    }
}
