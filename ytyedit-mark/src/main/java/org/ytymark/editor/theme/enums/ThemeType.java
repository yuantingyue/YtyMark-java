package org.ytymark.editor.theme.enums;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：主题类型
 */
public enum ThemeType {
    LIGHT("白天模式"), DARK("夜间模式");

    ThemeType(String themeName) {
        this.name = themeName;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
