package com.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.dao.MusicDao;
import com.myblog.dto.Result;
import com.myblog.entity.Music;
import com.myblog.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * (Music)表服务实现类
 *
 * @author makejava
 * @since 2024-12-28 18:30:41
 */
@Service("musicService")
@CacheConfig(cacheNames = "music")
public class MusicServiceImpl extends ServiceImpl<MusicDao, Music> implements MusicService {

    @Value("${music.root.path}")
    private String musicRootPath;
    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    @Override
    @CacheEvict(value = "musics", allEntries = true)
    public Result uploadMusic(MultipartFile file, String description) {
        try {
            String originalFilename = file.getOriginalFilename();
            String safeFilename = sanitizeFilename(originalFilename);
            String uniqueId = UUID.randomUUID().toString().substring(0, 8); // 使用UUID的前8个字符
            String fileName =  uniqueId +"_" +safeFilename;
            String filePath = Paths.get(musicRootPath, fileName).toString();

            // 确保目录存在
            Files.createDirectories(Paths.get(musicRootPath));

            // 保存文件
            file.transferTo(new File(filePath));

            // 创建Music对象
            Music music = new Music()
                    .setFileName(originalFilename)
                    .setFilePath(filePath)
                    .setDescription(description);

            // 保存到数据库
            save(music);

            return Result.ok("音乐上传成功");
        } catch (IOException e) {
            logger.error("上传音乐错误", e);
            return Result.ok("音乐上传可能成功，注意查看日志");
        }
    }
    private String sanitizeFilename(String filename) {
        if (filename == null) {
            return "unnamed";
        }
        // 移除路径分隔符和其他潜在的危险字符
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    @Override
    @CacheEvict(value = "musics", allEntries = true)
    public Result deleteMusic(String musicId) {
        Music music = getById(musicId);
        if (music != null) {
            // 删除文件
            try {
                Files.deleteIfExists(Paths.get(music.getFilePath()));
            } catch (IOException e) {
               logger.error("删除音乐错误", e);

                return Result.fail("文件删除失败");
            }
            // 从数据库中删除记录
            if (removeById(musicId)) {
                return Result.ok("音乐删除成功");
            }
        }
        return Result.fail("音乐删除失败");
    }

    @Override
    @CacheEvict(value = "musics", allEntries = true)
    public Result updateMusic(Music music) {
        if (updateById(music)) {
            return Result.ok(getById(music.getMusicId()));
        }
        return Result.fail("更新失败");
    }


    @Override
    @Cacheable(value = "musics",unless = "#result == null")
    public List<Music> getAllMusic() {
        return list();
    }

    @Override
    public ResponseEntity<Resource> streamMusic(String musicId) {
        Music music = getById(musicId);
        if (music == null) {
            throw new RuntimeException("音乐文件不存在");
        }

        Resource resource = new FileSystemResource(music.getFilePath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + music.getFileName() + "\"")
                .body(resource);
    }


}
