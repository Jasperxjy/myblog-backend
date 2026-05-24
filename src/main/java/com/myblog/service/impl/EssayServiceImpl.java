package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.EssayDao;
import com.myblog.dto.EssayBriefDTO;
import com.myblog.dto.Result;
import com.myblog.entity.Essay;
import com.myblog.entity.EssayLock;
import com.myblog.service.EssayService;
import com.myblog.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Essay)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:29:24
 */
@Service("essayService")
public class EssayServiceImpl extends ServiceImpl<EssayDao, Essay> implements EssayService {
    @Autowired
    private LockService lockService;

    private EssayBriefDTO convertToEssayBriefDTO(Essay essay) {
        EssayBriefDTO brief = new EssayBriefDTO();
        brief.setEssayId(essay.getEssayId());
        brief.setEssayTitle(essay.getEssayTitle());
        brief.setClassId(essay.getClassId());
        brief.setStatus(essay.getStatus());
        return brief;
    }

    @Override
    @CachePut(value = "essays", key = "#id",unless = "#result.success == false")
    public Result getEssayWithLockCheck(String id) {
        Essay essay = this.getById(id);
        if (essay == null) {
            return Result.fail("文章不存在");
        }

        // 检查文章是否被锁定
        Result lockCheckResult = lockService.checkLock(id);
        if (lockCheckResult.getSuccess()) {
            return Result.fail("文章正在被编辑，暂时无法访问");
        }

        // 增加浏览次数
        essay.setEssayViewNum(essay.getEssayViewNum() + 1);
        this.updateById(essay);

        return Result.ok(essay);
    }

    @Override
    @Transactional
    public Result updateEssayWithLock(String id, Essay essay, String userId) {
        // 检查文章是否存在
        Essay existingEssay = this.getById(id);
        if (existingEssay == null) {
            return Result.fail("文章不存在");
        }

        // 检查锁定状态
        Result lockCheckResult = lockService.checkLock(id);
        if (!lockCheckResult.getSuccess()) {
            return Result.fail("文章未被锁定，无法更新");
        }

        EssayLock essayLock = (EssayLock) lockCheckResult.getData();
        if (!essayLock.getUserId().equals(userId)) {
            return Result.fail("您没有编辑权限");
        }

        // 更新文章内容
        existingEssay.setEssayContext(essay.getEssayContext());
        existingEssay.setEssayLastChangeTime(LocalDateTime.now());

        boolean updated = this.updateById(existingEssay);
        if (updated) {
            return Result.ok("文章内容已更新");
        } else {
            return Result.fail("更新失败");
        }
    }


    @Override
    public List<EssayBriefDTO> listEssayBriefs(String collectionId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Essay> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (collectionId == null) {
            // 返回所有未归属合集的文章（class_id 为 null 或空字符串）
            queryWrapper.isNull(Essay::getClassId).or().eq(Essay::getClassId, "");
        } else {
            // 返回指定合集的文章
            queryWrapper.eq(Essay::getClassId, collectionId);
        }
        return this.list(queryWrapper).stream()
                .map(this::convertToEssayBriefDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Result incrementLikeCount(String essayId) {
        Essay essay = this.getById(essayId);
        if (essay == null) {
            return Result.fail("文章不存在");
        }

        // 增加点赞数
        essay.setEssayLikeNum(essay.getEssayLikeNum() + 1);

        // 更新文章
        boolean updated = this.updateById(essay);
        if (updated) {
            return Result.ok(essay.getEssayLikeNum());
        } else {
            return Result.fail("点赞失败");
        }
    }


}

