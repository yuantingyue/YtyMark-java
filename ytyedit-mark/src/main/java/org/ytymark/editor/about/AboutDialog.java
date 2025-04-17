package org.ytymark.editor.about;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.ytymark.editor.bar.status.StatusBarInfo;
import org.ytymark.enums.IconPath;
import org.ytymark.utils.ResourceUtils;
import org.ytymark.window.dialog.DialogDecorator;
import org.ytymark.window.dialog.GenericDialog;

import java.awt.*;
import java.net.URI;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：关于信息展示的弹窗
 */
public class AboutDialog extends DialogDecorator {

    /**
     * 构造函数
     */
    public AboutDialog(GenericDialog dialog) {
        super(dialog);
    }

    public void showAboutDialog() {
        // 在主窗口中调用
        super.removeLogo(true);
        super.setTitle("");
        // 自定义主体内容（例如一个带有文本的 VBox）
        VBox content = new VBox(5);

        Text title = new Text("YtyMark");
        title.getStyleClass().add("about-title");
        Text description = new Text("这是一款极其简单的markdown文本编辑器。\n");
        description.getStyleClass().add("subtitle");
        Text purposeInfo = new Text("主要目的是用于学习设计模式的实战项目，将所学的设计模式用于实战。");
        purposeInfo.getStyleClass().add("text");


        HBox openSourceBox = new HBox(5);
        openSourceBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(openSourceBox, new Insets(10, 0, 0, 10)); // 增加左边距
        Text openSourceLabel = new Text("开源地址：");
        openSourceLabel.getStyleClass().add("text");
        // 使用 Hyperlink 让 URL 可点击
        Hyperlink openSourceLink = new Hyperlink("https://github.com/yuantingyue/YtyMark-java");
        openSourceLink.getStyleClass().add("link-text");
        openSourceLink.setStyle("-fx-text-fill: #4ea1f3;"); // 设置蓝色+下划线
        // 点击跳转到默认浏览器
        openSourceLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(openSourceLink.getText()));
            } catch (Exception e) {
                StatusBarInfo.updateStatus("跳转异常："+e.getMessage());
            }
        });
        openSourceBox.getChildren().addAll(openSourceLabel, openSourceLink);

        // Logo
        ImageView logo = ResourceUtils.loadImageView(IconPath.YTY_ICON_BIG.getIconPath(), 50);

        content.getChildren().addAll(title,logo, description, purposeInfo, openSourceBox);

        // 整体布局
        HBox bodyBox = new HBox(10, logo, content);
        bodyBox.setAlignment(Pos.CENTER_LEFT);

        super.setContent(bodyBox);
        super.showAndWait();
    }

}
