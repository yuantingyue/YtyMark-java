package org.ytymark.editor.bar.status;

import javafx.scene.control.Label;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：状态栏信息输出
 */
public class StatusBarInfo {
    private static final Label statusLabel = new Label("就绪");;
    private StatusBarInfo() {}
    // 获取状态栏
    public static Label getStatusLabel() {
        return statusLabel;
    }

    // 更新状态栏信息
    public static void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
