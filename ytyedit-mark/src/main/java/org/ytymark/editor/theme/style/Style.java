package org.ytymark.editor.theme.style;

import javafx.scene.Scene;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：窗口样式接口
 */
public interface Style {
    String getCss();
    void applyStyle(Scene scene);
}