package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.ImageDao;
import com.myblog.entity.Image;
import com.myblog.service.ImageService;
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

            // 创建Image对象
            Image image = new Image()
                    .setFileName(fileName)
                    .setFilePath(filePath)
                    .setEssayId(essayId)
                    .setAlbumId(essayId == null ? (albumId != null ? albumId : defaultAlbumId) : null)
                    .setUpdateTime(LocalDateTime.now())
                    .setDescription(description);

            // 保存到数据库
            save(image);

            return image;
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
            // 删除文件
            try {
                Files.deleteIfExists(Paths.get(image.getFilePath()));
            } catch (IOException e) {
                logger.error("删除图片错误", e);
                return false;
            }
            // 从数据库中删除记录
            return removeById(imageId);
        }
        return false;
    }
    @Cacheable(value = "image",key = "#image.imageId",unless = "#result == null")
    @Override
    public Image updateImage(Image image) {
        if (updateById(image)) {
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

    @CacheEvict(value = "Album_images", key = "#albumId")
    @Override
    public boolean addImageToAlbum(String imageId, String albumId) {
        Image image = getById(imageId);
        if (image != null) {
            image.setAlbumId(albumId);
            return updateById(image);
        }
        return false;
    }

    @CacheEvict(value = "Album_images", key = "#albumId")
    @Override
    public boolean removeImageFromAlbum(String imageId, String albumId) {
        Image image = getById(imageId);
        if (image != null && albumId.equals(image.getAlbumId())) {
            image.setAlbumId(null);
            return updateById(image);
        }
        return false;
    }
}
