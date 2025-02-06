package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.EssayBriefDTO;
import com.myblog.dto.Result;
import com.myblog.entity.Essay;
import com.myblog.service.EssayService;
import com.myblog.service.LockService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.myblog.utility.ConfigConstans.*;

/**
 * (Essay)文章表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:29:24
 */
@RestController
@RequestMapping("/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;
    @Autowired
    private LockService lockService;

    /**
     * 创建新文章
     *
     * @param essay 要创建的文章对象
     * @return 包含创建成功的文章信息的Result对象
     */
    @CacheEvict(value = "essayBriefs", allEntries = true)
    @RequirePermission(UserRole.ADMIN)
    @PostMapping
    public Result createEssay(@RequestBody Essay essay) {
        essay.setEssayAddTime(LocalDateTime.now())
                .setEssayLastChangeTime(LocalDateTime.now())
                .setEssayLikeNum(0)
                .setEssayViewNum(0)
                .setEssayCollectionNum(0)
                .setVersion(1)
                .setStatus(1);
        boolean savedEssay = essayService.save(essay);
        return Result.ok(savedEssay);
    }

    /**
     * 根据ID获取文章，包括所有状态的文章，包括草稿、已发布、已删除等，用于编辑文章时获取文章内容
     *
     * @param id 文章ID
     * @return 包含查询到的文章信息的Result对象，如果文章不存在则返回失败信息
     */
    @RequirePermission(UserRole.ADMIN)
    @GetMapping("/edit/{id}")
    @Cacheable(value = "essays", key = "#id")
    public Result getEssay(@PathVariable String id) {
        Essay essay = essayService.getById(id);
        if (essay != null) {
            return Result.ok(essay);
        } else {
            return Result.fail("文章不存在");
        }
    }

    /**
     * 获取文章（考虑锁定状态），用于一般用户获取未锁定的文章内容
     *
     * @param id 文章ID
     * @return 包含文章信息的Result对象
     */
    @RequirePermission()
    @GetMapping("/view/{id}")
    @Cacheable(value = "essays", key = "#id")
    public Result getLatestEssay(@PathVariable String id) {
        return essayService.getEssayWithLockCheck(id);
    }

    /**
     * 获取所有文章的缩略视图，用于全部文章列表的展示
     *
     * @return 包含所有文章列表的Result对象
     */
    @Cacheable(value = "essayBriefs")
    @RequirePermission()
    @GetMapping
    public Result getAllEssays() {
        List<EssayBriefDTO> essayBriefs = essayService.listAllEssayBriefs();
        return Result.ok(essayBriefs);
    }

    /**
     * 更新指定ID的文章的其他内容，包括合集、类型、状态等，不上锁
     *
     * @param id    要更新的文章ID
     * @param essay 包含更新信息的文章对象
     * @return 包含更新后的文章信息的Result对象，如果文章不存在则返回失败信息
     */
    @CacheEvict(value = "essays", key = "#id", allEntries = true)
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{id}")
    public Result updateEssay(@PathVariable String id, @RequestBody Essay essay) {
        Essay existingEssay = essayService.getById(id);
        if (existingEssay != null) {
            essay.setEssayId(id)
                    .setEssayLastChangeTime(LocalDateTime.now());
            boolean updatedEssay = essayService.updateById(essay);
            return Result.ok(updatedEssay);
        } else {
            return Result.fail("文章不存在");
        }
    }

    /**
     * 更新指定ID的文章内容Context，要求上锁后检查是否为上锁id编辑，用于实现编辑过程中的自动保存
     *
     * @param id    要更新的文章ID
     * @param essay 包含更新信息的文章对象
     * @return 包含更新后的文章信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{id}/context")
    public Result updateEssayContext(@PathVariable String id, @RequestBody Essay essay, @RequestAttribute("userId") String userId) {
        return essayService.updateEssayWithLock(id, essay, userId);
    }


    /**
     * 开始编辑文章内容，为文章上锁，阻止其他编辑者同时编辑该文章
     *
     * @param id     文章ID
     * @param userId 用户ID
     * @return 包含锁定信息的Result对象
     */
    @CacheEvict(value = "essays", key = "#id", allEntries = true)
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/{id}/edit")
    public Result startEditEssay(@PathVariable String id, @RequestParam String userId) {
        Essay existingEssay = essayService.getById(id);
        if (existingEssay != null) {
            existingEssay.setStatus(ESSAY_STATUS_EDITING);
        }
        return lockService.lockEssay(id, userId);
    }

    /**
     * 结束编辑文章，解锁文章，同时为文章的版本号+1
     *
     * @param id     文章ID
     * @param userId 用户ID
     * @return 操作结果的Result对象
     */
    @CacheEvict(value = "essayBriefs", allEntries = true)
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/{id}/end-edit")
    public Result endEditEssay(@PathVariable String id, @RequestParam String userId) {
        Essay existingEssay = essayService.getById(id);
        if (existingEssay != null) {
            existingEssay
                    .setEssayLastChangeTime(LocalDateTime.now())
                    .setVersion(existingEssay.getVersion() + 1)
                    .setStatus(ESSAY_STATUS_NORMAL);
            essayService.updateById(existingEssay);
        }
        return lockService.unlockEssay(id, userId, false);
    }


    /**
     * 为文章点赞
     *
     * @param id 文章ID
     * @return 包含更新后点赞数的Result对象
     */
    @CachePut(value = "essays", key = "#id")
    @RequirePermission()
    @PostMapping("/{id}/like")
    public Result likeEssay(@PathVariable String id) {
        return essayService.incrementLikeCount(id);
    }

    /**
     * 删除指定ID的文章
     *
     * @param id 要删除的文章ID
     * @return 包含操作结果的Result对象，如果删除成功返回成功信息，否则返回失败信息
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/{id}/del")
    public Result deleteEssay(@PathVariable String id) {
        boolean removed = essayService.update().set("status", ESSAY_STATUS_DEL).eq("essay_id", id).update();
        if (removed) {
            return Result.ok();
        } else {
            return Result.fail("文章不存在或删除失败");
        }
    }


}
