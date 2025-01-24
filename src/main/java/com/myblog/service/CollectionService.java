package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Collection;

import java.util.List;

/**
 * (Collection)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:41:05
 */
public interface CollectionService extends IService<Collection> {

    List<Collection> listAllCollections();

    Collection getCollectionById(String id);

    Collection createCollection(Collection collection);

    Collection updateCollection(Collection collection);

    boolean deleteCollection(String id);
}

