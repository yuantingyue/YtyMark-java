package org.ytymark.enums;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：图标路径配置
 */
public enum IconPath {
    QUESTION_DIALOG("/images/dialog-question.png"),
    LOGO("/images/logo.png"),
    YTY_ICON("/images/yty-icon.png"),
    YTY_ICON_BIG("/images/yty-icon-big.png"),
    ;

    IconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    private final String iconPath;

    public String getIconPath() {
        return iconPath;
    }
}
