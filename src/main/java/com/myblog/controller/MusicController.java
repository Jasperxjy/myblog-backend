package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.Music;
import com.myblog.service.MusicService;
import com.myblog.utility.UserRole;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    /**
     * 上传新增音乐
     *
     * @param file 音乐文件
     * @param description 音乐描述
     * @return 上传结果
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/upload")
    public Result uploadMusic(@RequestParam("file") MultipartFile file,
                              @RequestParam(required = false) String description) {
        return musicService.uploadMusic(file, description);
    }

    /**
     * 编辑音乐信息
     *
     * @param musicId 音乐ID
     * @param music 更新的音乐信息
     * @return 更新结果
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{musicId}")
    public Result editMusic(@PathVariable String musicId, @RequestBody Music music) {
        music.setMusicId(musicId);
        return musicService.updateMusic(music);
    }

    /**
     * 获取所有音乐
     *
     * @return 音乐列表
     */
    @RequirePermission(UserRole.GUEST)
    @GetMapping("/all")
    public Result getAllMusic() {
        List<Music> musicList = musicService.getAllMusic();
        return Result.ok(musicList);
    }

    /**
     * 获取音乐文件
     *
     * @param musicId 音乐ID
     * @return 音乐文件流
     */
    @RequirePermission(UserRole.GUEST)
    @GetMapping("/stream/{musicId}")
    public ResponseEntity<Resource> streamMusic(@PathVariable String musicId) {
        return musicService.streamMusic(musicId);
    }

    /**
     * 删除音乐
     *
     * @param musicId 音乐ID
     * @return 删除结果
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/{musicId}")
    public Result deleteMusic(@PathVariable String musicId) {
        return musicService.deleteMusic(musicId);
    }
}
