package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.EssayTag;
import com.myblog.entity.EssayTagList;

import java.util.List;

/**
 * (EssayTagList)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:29:59
 */
public interface EssayTagListService extends IService<EssayTagList> {
    

    boolean removeTagFromEssay(String essayId, String essayTagId);

    boolean addTagsToEssay(String essayId, List<String> tagIds);

    List<EssayTag> getTagsByEssay(String essayId);

    List<String> getEssaysByTag(String essayTagId);
}

