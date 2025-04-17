package org.ytymark.editor.theme.style;

import javafx.scene.Scene;
import org.ytymark.utils.ResourceUtils;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：样式抽象类
 */
public abstract class AbstractStyle implements Style {
    private static final String CONTENT_STYLE_PATH = "/css/content/fonts.css";
    private static final String fonts;

    static {
        // 使用本地字体
        String fontRegular = ResourceUtils.getResource("/fonts/SourceHanSerifSC-Regular.otf").toExternalForm();
        String fontBold = ResourceUtils.getResource("/fonts/SourceHanSerifSC-Bold.otf").toExternalForm();
        String cssContent = ResourceUtils.getFileString(CONTENT_STYLE_PATH);
        fonts = String.format(cssContent, fontRegular, fontBold);
    }

    public static String getFonts() {
        return fonts;
    }

    @Override
    public String getCss() {
        throw new RuntimeException("非主窗口不提供CSS相关样式");
    }

    @Override
    public abstract void applyStyle(Scene scene);
}
