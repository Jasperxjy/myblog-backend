package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.EssayTagListDao;
import com.myblog.entity.EssayTag;
import com.myblog.entity.EssayTagList;
import com.myblog.service.EssayTagListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (EssayTagList)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:29:59
 */
@Service("essayTagListService")
public class EssayTagListServiceImpl extends ServiceImpl<EssayTagListDao, EssayTagList> implements EssayTagListService {

    @Override
    public boolean removeTagFromEssay(String essayId, String essayTagId) {
        return false;
    }

    @Override
    public boolean addTagsToEssay(String essayId, List<String> tagIds) {
        return false;
    }

    @Override
    public List<EssayTag> getTagsByEssay(String essayId) {
        return List.of();
    }

    @Override
    public List<String> getEssaysByTag(String essayTagId) {
        return List.of();
    }
}

