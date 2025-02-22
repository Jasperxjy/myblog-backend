package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.dto.EssayBriefDTO;
import com.myblog.dto.Result;
import com.myblog.entity.Essay;

import java.util.List;

/**
 * (Essay)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:29:24
 */
public interface EssayService extends IService<Essay> {

    Result getEssayWithLockCheck(String id);

    Result updateEssayWithLock(String id, Essay essay, String userId);

    Result incrementLikeCount(String id);

    List<EssayBriefDTO> listEssayBriefs(String collectionId);
}

