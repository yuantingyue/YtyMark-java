package org.ytymark.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：资源读取工具
 */
public class ResourceUtils {

    private static final Map<String, Image> imageCache = new HashMap<>();

    public static URL getResource(String path) {
        return ResourceUtils.class.getResource(path);
    }

    /**
     * 读取图片资源
     * @param path
     * @return Image
     */
    public static Image loadImage(String path) {
        return imageCache.computeIfAbsent(path, p -> {
            URL url = getResource(p);
            return url != null ? new Image(url.toExternalForm()) : null;
        });
    }

    /**
     * 读取图片资源
     * @param path
     * @param width
     * @param height
     * @return ImageView
     */
    public static ImageView loadImageView(String path, double width, double height) {
        Image image = loadImage(path);
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);// 图标的宽度
            imageView.setFitHeight(height);// 图标的高度
            return imageView;
        }
        return null;
    }

    /**
     * 读取图片资源
     * @param path
     * @param width
     * @return ImageView
     */
    public static ImageView loadImageView(String path, double width) {
        Image image = loadImage(path);
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);// 图标的宽度
            imageView.setPreserveRatio(true); // 开启保持比例
            return imageView;
        }
        return null;
    }

    /**
     * 读取文本内容成字符串
     * @param filePath
     * @return String
     */
    public static String getFileString(String filePath){
        try(InputStream inputStream = ResourceUtils.getResource(filePath).openStream()){
            if (inputStream == null) {
                throw new RuntimeException("文件找不到！");
            }
            String cssContent = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            return cssContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

