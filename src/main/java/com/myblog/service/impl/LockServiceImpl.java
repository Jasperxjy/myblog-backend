package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.LockDao;
import com.myblog.dto.Result;
import com.myblog.entity.EssayLock;
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
public class LockServiceImpl extends ServiceImpl<LockDao, EssayLock> implements LockService {

    @Override
    @Transactional
    public Result lockEssay(String essayId, String userId) {
        // 检查文章是否已被锁定
        EssayLock existingEssayLock = getOne(new QueryWrapper<EssayLock>().eq("essay_id", essayId));
        if (existingEssayLock != null) {
            return Result.fail("文章已被锁定");
        }

        // 创建新的锁
        EssayLock newEssayLock = new EssayLock()
                .setEssayId(essayId)
                .setUserId(userId)
                .setLockTime(LocalDateTime.now());
        save(newEssayLock);

        return Result.ok("文章锁定成功");
    }

    @Override
    @Transactional
    public Result unlockEssay(String essayId, String userId, boolean force) {
        QueryWrapper<EssayLock> wrapper = new QueryWrapper<EssayLock>().eq("essay_id", essayId);
        EssayLock essayLock = getOne(wrapper);

        if (essayLock == null) {
            return Result.fail("文章未被锁定");
        }

        if (!force && !essayLock.getUserId().equals(userId)) {
            return Result.fail("无权解锁此文章");
        }

        remove(wrapper);
        return Result.ok("文章解锁成功");
    }

    @Override
    public Result checkLock(String essayId) {
        EssayLock essayLock = getOne(new QueryWrapper<EssayLock>().eq("essay_id", essayId));
        if (essayLock != null) {
            return Result.ok(essayLock);
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
        List<EssayLock> essayLocks = list();
        return Result.ok(essayLocks);
    }
}
