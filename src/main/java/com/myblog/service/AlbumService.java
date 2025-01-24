package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Album;

import java.util.List;

/**
 * (Album)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:28:13
 */
public interface AlbumService extends IService<Album> {

    Album createAlbum(Album album);

    Album getAlbumById(String id);

    List<Album> listAllAlbums();

    Album updateAlbum(Album album);

    boolean deleteAlbum(String id);

    boolean mergeAlbums(String sourceId, String targetId);
}

