package com.myblog.controller;

import com.myblog.annotation.RequirePermission;
import com.myblog.dto.Result;
import com.myblog.entity.Image;
import com.myblog.service.ImageService;
import com.myblog.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Image)图片表控制层
 *
 * @author makejava
 * @since 2024-12-28 18:30:16
 */
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 根据图片ID获取图片路径
     *
     * @param imageId 图片ID
     * @return 包含图片路径的Result对象
     */
    @RequirePermission()
    @GetMapping("/path/{imageId}")
    public Result getImagePath(@PathVariable String imageId) {
        String imagePath = imageService.getImagePath(imageId);
        return imagePath != null ? Result.ok(imagePath) : Result.fail("图片不存在");
    }

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param essayId 关联的文章ID（可选）
     * @param albumId 关联的相册ID（可选）
     * @param description 图片描述（可选）
     * @return 包含上传后的图片信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/upload")
    public Result uploadImage(@RequestParam("file") MultipartFile file,
                              @RequestParam(required = false) String essayId,
                              @RequestParam(required = false) String albumId,
                              @RequestParam(required = false) String description) {
        Image image = imageService.uploadImage(file, essayId, albumId, description);
        return image != null ? Result.ok(image) : Result.fail("图片上传失败");
    }

    /**
     * 获取文章的所有图片
     *
     * @param essayId 文章ID
     * @return 包含文章所有图片信息的Result对象
     */
    @RequirePermission()
    @GetMapping("/essay/{essayId}")
    public Result getImagesByEssay(@PathVariable String essayId) {
        List<Image> images = imageService.getImagesByEssay(essayId);
        return Result.ok(images);
    }

    /**
     * 删除图片
     *
     * @param imageId 图片ID
     * @return 删除结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @DeleteMapping("/{imageId}/del")
    public Result deleteImage(@PathVariable String imageId) {
        boolean success = imageService.deleteImage(imageId);
        return success ? Result.ok() : Result.fail("图片删除失败");
    }

    /**
     * 更新图片信息
     *
     * @param imageId 图片ID
     * @param image 更新的图片信息
     * @return 更新后的图片信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PutMapping("/{imageId}/update")
    public Result updateImage(@PathVariable String imageId, @RequestBody Image image) {
        image.setImageId(imageId);
        Image updatedImage = imageService.updateImage(image);
        return updatedImage != null ? Result.ok(updatedImage) : Result.fail("图片更新失败");
    }

    /**
     * 获取相册中的所有图片
     *
     * @param albumId 相册ID
     * @return 包含相册所有图片信息的Result对象
     */
    @RequirePermission()
    @GetMapping("/album/{albumId}")
    public Result getImagesByAlbum(@PathVariable String albumId) {
        List<Image> images = imageService.getImagesByAlbum(albumId);
        return Result.ok(images);
    }

    /**
     * 获取所有未被收录到相册且未被文章引用的图片
     *
     * @return 包含未收录和未引用图片信息的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @GetMapping("/uncategorized")
    public Result getUncategorizedImages() {
        List<Image> images = imageService.getUncategorizedImages();
        return Result.ok(images);
    }

    /**
     * 将图片添加到相册
     *
     * @param imageId 图片ID
     * @param albumId 相册ID
     * @return 添加结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/{imageId}/addToAlbum/{albumId}")
    public Result addImageToAlbum(@PathVariable String imageId, @PathVariable String albumId) {
        boolean success = imageService.addImageToAlbum(imageId, albumId);
        return success ? Result.ok() : Result.fail("添加图片到相册失败");
    }

    /**
     * 从相册中移除图片
     *
     * @param imageId 图片ID
     * @param albumId 相册ID
     * @return 移除结果的Result对象
     */
    @RequirePermission(UserRole.ADMIN)
    @PostMapping("/{imageId}/removeFromAlbum/{albumId}")
    public Result removeImageFromAlbum(@PathVariable String imageId, @PathVariable String albumId) {
        boolean success = imageService.removeImageFromAlbum(imageId, albumId);
        return success ? Result.ok() : Result.fail("从相册中移除图片失败");
    }
}
