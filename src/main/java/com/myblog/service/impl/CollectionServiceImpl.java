package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.CollectionDao;
import com.myblog.entity.Collection;
import com.myblog.service.CollectionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    /**
     * 查询所有合集
     * 使用缓存以提高性能
     *
     * @return 所有合集的列表
     */
    @Cacheable(value = "allCollections")
    @Override
    public List<Collection> listAllCollections() {
        return list();
    }

    /**
     * 根据ID查询合集
     * 使用缓存以提高性能
     *
     * @param id 合集ID
     * @return 查询到的合集，如果不存在则返回null
     */
    @Cacheable(value = "collection", key = "#id",unless = "#result == null")
    @Override
    public Collection getCollectionById(String id) {
        return getById(id);
    }

    /**
     * 创建新的合集
     * 创建后清除所有合集的缓存
     *
     * @param collection 要创建的合集对象
     * @return 创建成功后的合集对象
     */
    @CacheEvict(value = "allCollections", allEntries = true)
    @Override
    public Collection createCollection(Collection collection) {
        save(collection);
        return collection;
    }

    /**
     * 更新合集信息
     * 更新后清除相关缓存
     *
     * @param collection 要更新的合集对象
     * @return 更新后的合集对象，如果更新失败则返回null
     */
    @CacheEvict(value = {"collection", "allCollections"}, key = "#collection.collectionId", allEntries = true)
    @Override
    public Collection updateCollection(Collection collection) {
        if (updateById(collection)) {
            return collection;
        }
        return null;
    }

    /**
     * 删除合集
     * 删除后清除相关缓存
     *
     * @param id 要删除的合集ID
     * @return 删除是否成功
     */
    @CacheEvict(value = {"collection", "allCollections"}, key = "#id", allEntries = true)
    @Override
    public boolean deleteCollection(String id) {
        return removeById(id);
    }
}
