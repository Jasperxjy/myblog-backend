package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.AlbumDao;
import com.myblog.entity.Album;
import com.myblog.service.AlbumService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Album)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:28:16
 */
@Service("albumService")
public class AlbumServiceImpl extends ServiceImpl<AlbumDao, Album> implements AlbumService {

    @Override
    public Album createAlbum(Album album) {
        return null;
    }

    @Override
    public Album getAlbumById(String id) {
        return null;
    }

    @Override
    public List<Album> listAllAlbums() {
        return List.of();
    }

    @Override
    public Album updateAlbum(Album album) {
        return null;
    }

    @Override
    public boolean deleteAlbum(String id) {
        return false;
    }

    @Override
    public boolean mergeAlbums(String sourceId, String targetId) {
        return false;
    }
}

