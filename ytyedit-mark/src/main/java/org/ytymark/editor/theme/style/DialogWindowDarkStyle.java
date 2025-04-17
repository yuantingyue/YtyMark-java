package org.ytymark.editor.theme.style;

import javafx.scene.Scene;
import org.ytymark.utils.ResourceUtils;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：弹窗的黑夜模式
 */
public class DialogWindowDarkStyle extends AbstractStyle {
    private static final String STYLE_PATH = "/css/ui/dialog-dark.css";

    @Override
    public void applyStyle(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(ResourceUtils.getResource(STYLE_PATH).toExternalForm());
    }
}