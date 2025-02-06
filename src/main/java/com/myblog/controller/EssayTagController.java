package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.EssayTag;
import com.myblog.service.EssayTagService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (EssayTag)文章标签表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:29:38
 */
@RestController
@RequestMapping("/essayTag")
public class EssayTagController {

    @Autowired
    private EssayTagService essayTagService;

    /**
     * 获取所有标签
     *
     * @return 包含所有标签的Result对象
     */
    @RequirePermission()
    @GetMapping("/all")
    public Result getAllTags() {
        List<EssayTag> tags = essayTagService.getAllTags();
        return Result.ok(tags);
    }

    /**
     * 新增标签
     *
     * @param essayTag 要新增的标签对象
     * @return 新增结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/add")
    public Result addTag(@RequestBody EssayTag essayTag) {
        boolean success = essayTagService.addTag(essayTag);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("新增标签失败");
        }
    }
}
