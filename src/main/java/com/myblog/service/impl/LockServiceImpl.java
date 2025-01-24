package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.LockDao;
import com.myblog.dto.Result;
import com.myblog.entity.Lock;
import com.myblog.service.LockService;
import org.springframework.stereotype.Service;

/**
 * (Lock)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:29
 */
@Service("lockService")
public class LockServiceImpl extends ServiceImpl<LockDao, Lock> implements LockService {

    @Override
    public Result lockEssay(String id, String userId) {
        return null;
    }

    @Override
    public Result unlockEssay(String id, String userId, boolean b) {
        return null;
    }

    @Override
    public Result checkLock(String essayId) {
        return null;
    }

    @Override
    public Result forceUnlockEssay(String essayId, String adminId) {
        return null;
    }

    @Override
    public Result getAllLockedEssays() {
        return null;
    }
}

