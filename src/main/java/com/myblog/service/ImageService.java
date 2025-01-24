package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Image)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:30:16
 */
public interface ImageService extends IService<Image> {

    String getImagePath(String imageId);

    Image uploadImage(MultipartFile file, String essayId, String albumId, String description);

    List<Image> getImagesByEssay(String essayId);

    boolean deleteImage(String imageId);

    Image updateImage(Image image);

    List<Image> getImagesByAlbum(String albumId);

    List<Image> getUncategorizedImages();

    boolean addImageToAlbum(String imageId, String albumId);

    boolean removeImageFromAlbum(String imageId, String albumId);
}

