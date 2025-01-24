package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.CollectionDao;
import com.myblog.entity.Collection;
import com.myblog.service.CollectionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Collection)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:41:05
 */
@Service("collectionService")
public class CollectionServiceImpl extends ServiceImpl<CollectionDao, Collection> implements CollectionService {

    @Override
    public List<Collection> listAllCollections() {
        return List.of();
    }

    @Override
    public Collection getCollectionById(String id) {
        return null;
    }

    @Override
    public Collection createCollection(Collection collection) {
        return null;
    }

    @Override
    public Collection updateCollection(Collection collection) {
        return null;
    }

    @Override
    public boolean deleteCollection(String id) {
        return false;
    }
}

