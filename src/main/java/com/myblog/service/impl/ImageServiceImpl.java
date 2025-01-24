package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.ImageDao;
import com.myblog.entity.Image;
import com.myblog.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Image)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:16
 */
@Service("imageService")
public class ImageServiceImpl extends ServiceImpl<ImageDao, Image> implements ImageService {

    @Override
    public String getImagePath(String imageId) {
        return "";
    }

    @Override
    public Image uploadImage(MultipartFile file, String essayId, String albumId, String description) {
        return null;
    }

    @Override
    public List<Image> getImagesByEssay(String essayId) {
        return List.of();
    }

    @Override
    public boolean deleteImage(String imageId) {
        return false;
    }

    @Override
    public Image updateImage(Image image) {
        return null;
    }

    @Override
    public List<Image> getImagesByAlbum(String albumId) {
        return List.of();
    }

    @Override
    public List<Image> getUncategorizedImages() {
        return List.of();
    }

    @Override
    public boolean addImageToAlbum(String imageId, String albumId) {
        return false;
    }

    @Override
    public boolean removeImageFromAlbum(String imageId, String albumId) {
        return false;
    }
}

