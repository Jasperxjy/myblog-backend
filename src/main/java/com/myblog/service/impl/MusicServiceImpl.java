package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.MusicDao;
import com.myblog.dto.Result;
import com.myblog.entity.Music;
import com.myblog.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Music)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:41
 */
@Service("musicService")
public class MusicServiceImpl extends ServiceImpl<MusicDao, Music> implements MusicService {
    @Autowired
    private MusicDao musicDao;

    @Override
    public Result uploadMusic(MultipartFile file, String description) {
        return null;
    }

    @Override
    public Result updateMusic(Music music) {
        return null;
    }

    @Override
    public List<Music> getAllMusic() {
        return List.of();
    }

    @Override
    public ResponseEntity<Resource> streamMusic(String musicId) {
        Music music = musicDao.selectById(musicId);
        if (music == null) {
            throw new RuntimeException("Music not found");
        }

        Resource resource = new FileSystemResource(music.getFilePath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + music.getFileName() + "\"")
                .body(resource);
    }

    @Override
    public Result deleteMusic(String musicId) {
        return null;
    }

}

