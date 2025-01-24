package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.dto.Result;
import com.myblog.entity.Music;
import org.springframework.core.io.Resource;;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Music)表服务接口
 *
 * @author makejava
 * @since 2024-12-28 18:30:41
 */
public interface MusicService extends IService<Music> {

    Result uploadMusic(MultipartFile file, String description);

    Result updateMusic(Music music);

    List<Music> getAllMusic();

    ResponseEntity<Resource> streamMusic(String musicId);

    Result deleteMusic(String musicId);
}

