package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.AlbumDao;
import com.myblog.entity.Album;
import com.myblog.entity.Image;
import com.myblog.service.AlbumService;
import com.myblog.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (Album)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:28:16
 */
@Service("albumService")
public class AlbumServiceImpl extends ServiceImpl<AlbumDao, Album> implements AlbumService {

    @Autowired
    private ImageService imageService;

    @Value("${image.default.album.id}")
    private String defaultAlbumId;

    @Cacheable(value = "album", key = "#album.albumId")
    @Override
    public Album createAlbum(Album album) {
        save(album);
        return album;
    }

    @Cacheable(value = "album", key = "#id",unless = "#result == null")
    @Override
    public Album getAlbumById(String id) {

        return getById(id);
    }

    @Cacheable(value = "albums",unless = "#result == null")
    @Override
    public List<Album> listAllAlbums() {
        return list();
    }

    @CachePut(value = "album", key = "#album.albumId",unless = "#result == null")
    @Override
    public Album updateAlbum(Album album) {
        if (updateById(album)) {
            return album;
        }
        return null;
    }

    @CacheEvict(value = "album", key = "#id")
    @Override
    public boolean deleteAlbum(String id) {
        Album album = getById(id);
        if (album == null) {
            return false;
        }
        // 将相册中的所有图片迁移到默认相册
        List<Image> albumImages = imageService.getImagesByAlbum(id);
        for (Image image : albumImages) {
            imageService.addImageToAlbum(image.getImageId(), defaultAlbumId);
        }

        // 删除相册
        return removeById(id);
    }

    @CacheEvict(value = {"album","albums"}, allEntries = true)
    @Transactional
    @Override
    public boolean mergeAlbums(String sourceId, String targetId) {
        Album sourceAlbum = getById(sourceId);
        Album targetAlbum = getById(targetId);

        if (sourceAlbum == null || targetAlbum == null) {
            return false;
        }

        // 更新目标相册的信息
        targetAlbum.setDescription(targetAlbum.getDescription() + "\n" + sourceAlbum.getDescription());
        targetAlbum.setUpdatetime(sourceAlbum.getUpdatetime().isAfter(targetAlbum.getUpdatetime()) ?
                sourceAlbum.getUpdatetime() : targetAlbum.getUpdatetime());

        // 更新目标相册
        updateById(targetAlbum);

        // 迁移源相册中的所有图片到目标相册
        List<Image> sourceImages = imageService.getImagesByAlbum(sourceId);
        for (Image image : sourceImages) {
            imageService.addImageToAlbum(image.getImageId(), targetId);
        }

        // 删除源相册
        removeById(sourceId);

        return true;
    }
}

