package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.EssayTag;

import java.util.List;

/**
 * (EssayTag)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:29:38
 */
public interface EssayTagService extends IService<EssayTag> {

    List<EssayTag> getAllTags();

    EssayTag addTag(EssayTag essayTag);
}

