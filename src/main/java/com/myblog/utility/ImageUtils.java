package com.myblog.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 生成缩略图，保持原图的长宽比
     * @param sourcePath 原图路径
     * @param thumbnailPath 缩略图保存路径
     * @param maxDimension 缩略图的最大边长
     */
    public static void generateThumbnail(String sourcePath, String thumbnailPath, int maxDimension) {
        try {
            File sourceFile = new File(sourcePath);
            BufferedImage sourceImage = ImageIO.read(sourceFile);

            int originalWidth = sourceImage.getWidth();
            int originalHeight = sourceImage.getHeight();

            // 计算缩放比例
            double scale = calculateScale(originalWidth, originalHeight, maxDimension);

            // 计算新的尺寸
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            // 创建缩略图
            BufferedImage thumbnailImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = thumbnailImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(sourceImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // 保存缩略图
            String extension = getFileExtension(sourcePath);
            ImageIO.write(thumbnailImage, extension, new File(thumbnailPath));

            logger.info("缩略图生成成功: {}", thumbnailPath);
        } catch (IOException e) {
            logger.error("生成缩略图失败: {}", sourcePath, e);
        }
    }

    /**
     * 计算缩放比例
     */
    private static double calculateScale(int width, int height, int maxDimension) {
        if (width <= maxDimension && height <= maxDimension) {
            return 1.0; // 如果原图小于或等于指定尺寸，不进行缩放
        }
        return (double) maxDimension / Math.max(width, height);
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String filePath) {
        int lastIndexOf = filePath.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "jpg"; // 默认使用jpg格式
        }
        return filePath.substring(lastIndexOf + 1).toLowerCase();
    }
}
