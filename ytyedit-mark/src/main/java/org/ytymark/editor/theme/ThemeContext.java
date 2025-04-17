package org.ytymark.editor.theme;


import javafx.scene.Scene;
import org.ytymark.editor.theme.enums.ThemeType;
import org.ytymark.editor.theme.enums.WindowType;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：样式上下文
 *      提供给页面一键切换样式
 */
public class ThemeContext {
    private Scene scene;
    private final ThemeManager themeManager = ThemeManager.getInstance();


    public ThemeContext(Scene scene){
        this.scene = scene;
        // 默认样式
        this.themeManager.applyStyle(WindowType.MAIN_WINDOW, scene);
    }

    // 设置样式
    public void setTheme(ThemeType theme) {
        WindowType[] values = WindowType.values();
        for (WindowType windowType : values) {
            this.themeManager.setStyle(windowType, theme);
        }
    }

    // 自定义设置不同窗体不同样式
    public void setStyle(WindowType type, ThemeType theme) {
        this.themeManager.setStyle(type, theme);
    }

    // 主题切换
    public void switchTheme() {
        // 清空之前的样式
        scene.getStylesheets().clear();
        this.themeManager.applyStyle(WindowType.MAIN_WINDOW, scene);
    }

}
