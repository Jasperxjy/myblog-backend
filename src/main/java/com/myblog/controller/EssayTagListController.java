package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.AddTagsToEssayDTO;
import com.myblog.dto.Result;
import com.myblog.entity.EssayTag;
import com.myblog.service.EssayTagListService;
import com.myblog.utility.UserRole;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (EssayTagList)文章标签组表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:29:59
 */
@RestController
@RequestMapping("/essayTagList")
public class EssayTagListController {

    @Resource
    private EssayTagListService essayTagListService;

    /**
     * 将多个标签添加到文章中
     *
     * @param addTagsToEssayDTO 包含文章ID和标签ID列表的DTO
     * @return 添加结果
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/addTagsToEssay")
    public Result addTagsToEssay(@RequestBody AddTagsToEssayDTO addTagsToEssayDTO) {
        boolean success = essayTagListService.addTagsToEssay(addTagsToEssayDTO.getEssayId(), addTagsToEssayDTO.getTagIds());
        return success ? Result.ok() : Result.fail("添加标签到文章失败");
    }

    /**
     * 将标签从文章中移除
     *
     * @param essayId 文章ID
     * @param essayTagId 标签ID
     * @return 移除结果
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/removeTagFromEssay")
    public Result removeTagFromEssay(@RequestParam String essayId, @RequestParam String essayTagId) {
        boolean success = essayTagListService.removeTagFromEssay(essayId, essayTagId);
        return success ? Result.ok() : Result.fail("从文章中移除标签失败");
    }

    /**
     * 获取文章的所有标签
     *
     * @param essayId 文章ID
     * @return 文章的所有标签
     */
    @RequirePermission()
    @GetMapping("/getTagsByEssay")
    public Result getTagsByEssay(@RequestParam String essayId) {
        List<EssayTag> tags = essayTagListService.getTagsByEssay(essayId);
        return Result.ok(tags);
    }

    /**
     * 获取标签下的所有文章
     *
     * @param essayTagId 标签ID
     * @return 标签下的所有文章ID列表
     */
    @RequirePermission()
    @GetMapping("/getEssaysByTag")
    public Result getEssaysByTag(@RequestParam String essayTagId) {
        List<String> essayIds = essayTagListService.getEssaysByTag(essayTagId);
        return Result.ok(essayIds);
    }
}
