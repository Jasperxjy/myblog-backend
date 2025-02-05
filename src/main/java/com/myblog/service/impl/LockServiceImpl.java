package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.LockDao;
import com.myblog.dto.Result;
import com.myblog.entity.Lock;
import com.myblog.service.LockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (Lock)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:29
 */
@Service("lockService")
public class LockServiceImpl extends ServiceImpl<LockDao, Lock> implements LockService {

    @Override
    @Transactional
    public Result lockEssay(String essayId, String userId) {
        // 检查文章是否已被锁定
        Lock existingLock = getOne(new QueryWrapper<Lock>().eq("essay_id", essayId));
        if (existingLock != null) {
            return Result.fail("文章已被锁定");
        }

        // 创建新的锁
        Lock newLock = new Lock()
                .setEssayId(essayId)
                .setUserId(userId)
                .setLockTime(LocalDateTime.now());
        save(newLock);

        return Result.ok("文章锁定成功");
    }

    @Override
    @Transactional
    public Result unlockEssay(String essayId, String userId, boolean force) {
        QueryWrapper<Lock> wrapper = new QueryWrapper<Lock>().eq("essay_id", essayId);
        Lock lock = getOne(wrapper);

        if (lock == null) {
            return Result.fail("文章未被锁定");
        }

        if (!force && !lock.getUserId().equals(userId)) {
            return Result.fail("无权解锁此文章");
        }

        remove(wrapper);
        return Result.ok("文章解锁成功");
    }

    @Override
    public Result checkLock(String essayId) {
        Lock lock = getOne(new QueryWrapper<Lock>().eq("essay_id", essayId));
        if (lock != null) {
            return Result.ok(lock);
        }
        return Result.fail("文章未被锁定");
    }

    @Override
    @Transactional
    public Result forceUnlockEssay(String essayId, String adminId) {
        return unlockEssay(essayId, adminId, true);
    }

    @Override
    public Result getAllLockedEssays() {
        List<Lock> locks = list();
        return Result.ok(locks);
    }
}
