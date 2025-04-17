package org.ytymark.editor.theme;

import javafx.scene.Scene;
import org.ytymark.editor.theme.enums.ThemeType;
import org.ytymark.editor.theme.enums.WindowType;
import org.ytymark.editor.theme.style.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：样式管理器：
 *      提供样式设置
 *      全局统一的样式控制
 *      样式切换后事件通知
 */
public class ThemeManager {
    private static ThemeManager instance;
    private final Map<WindowType, Style> currentStyles = new HashMap<>();
    private final List<ThemeChangeListener> listeners = new ArrayList<>();


    private ThemeManager() {
        // 默认主窗口白天模式，弹窗白色模式
        currentStyles.put(WindowType.MAIN_WINDOW, new MainWindowLightStyle());
        currentStyles.put(WindowType.DIALOG_WINDOW, new DialogWindowLightStyle());
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void setStyle(WindowType type, ThemeType theme) {
        currentStyles.put(type, StyleFactory.getStyle(type, theme));
        // 通知所有观察者主题已变更
        this.notifyThemeChanged();
    }

    public Style getMainStyle(){
        return currentStyles.get(WindowType.MAIN_WINDOW);
    }

    public void applyStyle(WindowType type, Scene scene) {
        if (currentStyles.containsKey(type)) {
            // 变更样式
            currentStyles.get(type).applyStyle(scene);
        }
    }

    // 注册观察者
    public void addThemeChangeListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }
    // 注销观察者
    public void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    // 通知所有观察者
    private void notifyThemeChanged() {
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChanged();
        }
    }
}
