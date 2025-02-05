package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.Collection;
import com.myblog.service.CollectionService;
import com.myblog.utility.UserRole;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Collection)合集表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:41:05
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Resource
    private CollectionService collectionService;

    /**
     * 查看所有合集
     *
     * @return 包含所有合集基本信息的Result对象
     */
    @RequirePermission()
    @GetMapping
    public Result getAllCollections() {
        List<Collection> collections = collectionService.listAllCollections();
        return Result.ok(collections);
    }

    /**
     * 查看某合集
     *
     * @param id 合集ID
     * @return 包含合集详细信息的Result对象
     */
    @RequirePermission()
    @GetMapping("/{id}")
    public Result getCollection(@PathVariable String id) {
        Collection collection = collectionService.getCollectionById(id);
        if (collection != null) {
            return Result.ok(collection);
        } else {
            return Result.fail("合集不存在");
        }
    }

    /**
     * 新建合集
     *
     * @param collection 包含合集名和摘要的Collection对象
     * @return 包含新建合集信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping
    public Result createCollection(@RequestBody Collection collection) {
        Collection createdCollection = collectionService.createCollection(collection);
        return Result.ok(createdCollection);
    }

    /**
     * 修改合集
     *
     * @param id 合集ID
     * @param collection 包含更新信息的Collection对象
     * @return 包含更新后合集信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{id}")
    public Result updateCollection(@PathVariable String id, @RequestBody Collection collection) {
        collection.setCollectionId(id);
        Collection updatedCollection = collectionService.updateCollection(collection);
        if (updatedCollection != null) {
            return Result.ok(updatedCollection);
        } else {
            return Result.fail("合集不存在或更新失败");
        }
    }

    /**
     * 删除合集
     *
     * @param id 合集ID
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/{id}")
    public Result deleteCollection(@PathVariable String id) {
        boolean deleted = collectionService.deleteCollection(id);
        if (deleted) {
            return Result.ok();
        } else {
            return Result.fail("合集不存在或删除失败");
        }
    }
}
