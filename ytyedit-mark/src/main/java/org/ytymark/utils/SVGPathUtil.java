package org.ytymark.utils;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;



/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：创建SVG对象工具
 */
public class SVGPathUtil {


    public static SVGPath createSVGPath(String content, double scaleX, double scaleY, Color fill) {
        SVGPath path = new SVGPath();
        path.setContent(content);
        // 调整大小
        path.setScaleX(scaleX); // 缩小宽度
        path.setScaleY(scaleY); // 缩小高度
        path.setFill(fill); // 设置颜色
        return path;
    }

}
