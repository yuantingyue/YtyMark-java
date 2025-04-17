package org.ytymark.window;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.ytymark.editor.bar.status.StatusBarInfo;
import org.ytymark.enums.IconPath;
import org.ytymark.utils.ResourceUtils;
import org.ytymark.window.dialog.DialogDecorator;
import org.ytymark.window.dialog.GenericDialog;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：关闭弹框
 */
public class CloseDialog extends DialogDecorator {
    private GenericDialog dialog;
    private String description = "文件尚未保存，是否保存？";
    /*
     * 构造函数
     */
    public CloseDialog(GenericDialog dialog) {
        super(dialog);
        this.dialog = dialog;
    }
    /**
     * 配置并显示退出确认弹框。
     * @param onSave   保存操作
     * @param unsave   不保存操作
     */
    public void showCloseDialog(Runnable onSave, Runnable unsave) {
        // 在主窗口中调用
        super.setTitle("确认退出");
        // 自定义主体内容（例如一个带有文本的 VBox）
        VBox content = new VBox(5);

        Text text = new Text();
        text.setText(description);
        text.getStyleClass().add("text");

        // Logo
        ImageView logo = ResourceUtils.loadImageView(IconPath.QUESTION_DIALOG.getIconPath(), 50);

        content.getChildren().addAll(logo, text);

        // 整体布局
        HBox bodyBox = new HBox(10, logo, content);
        bodyBox.setAlignment(Pos.CENTER_LEFT);

        HBox footerBox = new HBox(10);
        footerBox.setAlignment(Pos.CENTER_RIGHT);
        // 自定义底部按钮（例如关闭按钮）
        Button saveButton = new Button("保存");
        Button discardButton = new Button("不保存");
        Button cancelButton = new Button("取消");
        saveButton.getStyleClass().add("close-button");
        discardButton.getStyleClass().add("close-button");
        cancelButton.getStyleClass().add("close-button");
        saveButton.setOnAction(e -> {
            StatusBarInfo.updateStatus("保存");
            onSave.run();
            dialog.getDialogStage().close();
        });
        discardButton.setOnAction(e -> {
            StatusBarInfo.updateStatus("不保存");
            unsave.run();
            dialog.getDialogStage().close();
        });
        cancelButton.setOnAction(e -> {
            StatusBarInfo.updateStatus("取消");
            dialog.getDialogStage().close();
        });
        footerBox.getChildren().addAll(saveButton, discardButton, cancelButton);
        // 应用到装饰器内部的对话框中
        super.setContent(bodyBox);
        super.setFooter(footerBox);
        // 显示弹框
        super.showAndWait();
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
