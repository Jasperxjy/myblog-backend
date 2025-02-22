package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.Album;
import com.myblog.service.AlbumService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (Album)相册表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:28:09
 */
@RestController
@RequestMapping("/api/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * 获取所有相册
     *
     * @return 包含所有相册基本信息的Result对象
     */
    @RequirePermission()
    @GetMapping
    public Result getAllAlbums() {
        List<Album> albums = albumService.listAllAlbums();
        return Result.ok(albums);
    }

    /**
     * 获取相册详细信息
     *
     * @param id 相册ID
     * @return 包含相册详细信息的Result对象
     */
    @RequirePermission()
    @GetMapping("/{id}")
    public Result getAlbumDetails(@PathVariable String id) {
        Album album = albumService.getAlbumById(id);
        if (album != null) {
            return Result.ok(album);
        } else {
            return Result.fail("相册不存在");
        }
    }

    /**
     * 新建相册
     *
     * @param album 包含相册名和描述的Album对象
     * @return 包含新建相册信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping
    public Result createAlbum(@RequestBody Album album) {
        album.setCreatetime(LocalDateTime.now())
                .setUpdatetime(LocalDateTime.now());
        Album createdAlbum = albumService.createAlbum(album);
        return Result.ok(createdAlbum);
    }

    /**
     * 修改相册
     *
     * @param id 相册ID
     * @param album 包含更新信息的Album对象
     * @return 包含更新后相册信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{id}")
    public Result updateAlbum(@PathVariable String id, @RequestBody Album album) {
        album.setAlbumId(id).setUpdatetime(LocalDateTime.now());
        Album updatedAlbum = albumService.updateAlbum(album);
        if (updatedAlbum != null) {
            return Result.ok(updatedAlbum);
        } else {
            return Result.fail("相册不存在或更新失败");
        }
    }

    /**
     * 删除相册
     *
     * @param id 相册ID
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/{id}")
    public Result deleteAlbum(@PathVariable String id) {
        boolean deleted = albumService.deleteAlbum(id);
        if (deleted) {
            return Result.ok();
        } else {
            return Result.fail("相册不存在或删除失败");
        }
    }

    /**
     * 合并相册
     *
     * @param sourceId 当前相册ID
     * @param targetId 目标相册ID
     * @return 操作结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @GetMapping("/merge")
    public Result mergeAlbums(@RequestParam String sourceId, @RequestParam String targetId) {
        boolean merged = albumService.mergeAlbums(sourceId, targetId);
        if (merged) {
            return Result.ok();
        } else {
            return Result.fail("合并失败，请检查相册是否存在");
        }
    }
}
