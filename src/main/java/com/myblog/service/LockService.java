package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.dto.Result;
import com.myblog.entity.Lock;

/**
 * (Lock)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:30:29
 */
public interface LockService extends IService<Lock> {

    Result lockEssay(String id, String userId);

    Result unlockEssay(String id, String userId, boolean b);

    Result checkLock(String essayId);

    Result forceUnlockEssay(String essayId, String adminId);

    Result getAllLockedEssays();
}

