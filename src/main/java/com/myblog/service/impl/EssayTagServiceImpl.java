package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.EssayTagDao;
import com.myblog.entity.EssayTag;
import com.myblog.service.EssayTagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (EssayTag)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:29:38
 */
@Service("essayTagService")
public class EssayTagServiceImpl extends ServiceImpl<EssayTagDao, EssayTag> implements EssayTagService {

    @Override
    public List<EssayTag> getAllTags() {
        return List.of();
    }

    @Override
    public boolean addTag(EssayTag essayTag) {
        return false;
    }
}

