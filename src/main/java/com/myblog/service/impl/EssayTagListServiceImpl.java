package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.EssayTagListDao;
import com.myblog.entity.EssayTag;
import com.myblog.entity.EssayTagList;
import com.myblog.service.EssayTagListService;
import com.myblog.service.EssayTagService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EssayTagList服务实现类
 * 该类处理文章和标签之间的关联关系
 */
@Service("essayTagListService")
public class EssayTagListServiceImpl extends ServiceImpl<EssayTagListDao, EssayTagList> implements EssayTagListService {

    /**
     * 注入EssayTagService，用于获取标签信息
     */
    @Resource
    private EssayTagService essayTagService;

    /**
     * 从文章中移除指定标签
     *
     * @param essayId    文章ID
     * @param essayTagId 标签ID
     * @return 如果移除成功返回true，否则返回false
     */
    @Override
    @CacheEvict(cacheNames = "essay_tags:", key = "#essayId" , allEntries = true)
    public boolean removeTagFromEssay(String essayId, String essayTagId) {
        QueryWrapper<EssayTagList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("essay_id", essayId).eq("essay_tag_id", essayTagId);
        return this.remove(queryWrapper);
    }

    /**
     * 为文章添加多个标签
     * 该方法使用@Transactional注解确保操作的原子性
     *
     * @param essayId 文章ID
     * @param tagIds  要添加的标签ID列表
     * @return 如果所有标签都添加成功返回true，否则返回false
     */
    @Override
    @Transactional
    @CacheEvict(cacheNames = "essay_tags:", key = "#essayId" , allEntries = true)
    public boolean addTagsToEssay(String essayId, List<String> tagIds) {
        List<EssayTagList> essayTagLists = tagIds.stream()
                .map(tagId -> new EssayTagList().setEssayId(essayId).setEssayTagId(tagId))
                .collect(Collectors.toList());
        return this.saveBatch(essayTagLists);
    }

    /**
     * 获取指定文章的所有标签
     *
     * @param essayId 文章ID
     * @return 文章关联的所有标签列表
     */
    @Override
    @Cacheable(value = "essay_tags:" ,key = "#essayId",unless = "#result == null")
    public List<EssayTag> getTagsByEssay(String essayId) {
        // 首先查询文章-标签关联表
        QueryWrapper<EssayTagList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("essay_id", essayId);
        List<EssayTagList> essayTagLists = this.list(queryWrapper);

        // 提取所有关联的标签ID
        List<String> tagIds = essayTagLists.stream()
                .map(EssayTagList::getEssayTagId)
                .collect(Collectors.toList());

        // 通过标签ID列表获取完整的标签信息
        return essayTagService.listByIds(tagIds);
    }

    /**
     * 获取包含指定标签的所有文章ID
     *
     * @param essayTagId 标签ID
     * @return 包含该标签的所有文章ID列表
     */
    @Override
    public List<String> getEssaysByTag(String essayTagId) {
        QueryWrapper<EssayTagList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("essay_tag_id", essayTagId);
        List<EssayTagList> essayTagLists = this.list(queryWrapper);
        return essayTagLists.stream()
                .map(EssayTagList::getEssayId)
                .collect(Collectors.toList());
    }
}
