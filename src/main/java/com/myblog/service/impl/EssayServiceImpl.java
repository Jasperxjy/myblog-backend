package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.EssayDao;
import com.myblog.dto.EssayBriefDTO;
import com.myblog.dto.Result;
import com.myblog.entity.Essay;
import com.myblog.service.EssayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Result getEssayWithLockCheck(String id) {
        return null;
    }

    @Override
    public Result updateEssayWithLock(String id, Essay essay, String userId) {
        return null;
    }

    @Override
    public List<EssayBriefDTO> listAllEssayBriefs() {
        return this.list().stream()
                .map(essay -> {
                    EssayBriefDTO brief = new EssayBriefDTO();
                    brief.setEssayId(essay.getEssayId());
                    brief.setEssayTitle(essay.getEssayTitle());
                    brief.setClassId(essay.getClassId());
                    return brief;
                })
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

