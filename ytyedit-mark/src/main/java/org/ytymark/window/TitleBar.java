package org.ytymark.window;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ytymark.enums.IconPath;
import org.ytymark.enums.SVGIconCode;
import org.ytymark.utils.ResourceUtils;
import org.ytymark.utils.SVGPathUtil;
import org.ytymark.utils.WindowUtil;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：自定义标题栏
 */
public class TitleBar {
    private boolean maximized = false; // 记录窗口是否最大化
    private Rectangle2D backupBounds;
    public TitleBar(){

    }

    public HBox createTitleBar(Stage stage) {

        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("titleBar");
        titleBar.setAlignment(Pos.CENTER_RIGHT);
        // 图标
        ImageView icon = ResourceUtils.loadImageView(IconPath.YTY_ICON.getIconPath(), 28);
        // 应用名称
        Label titleLabel = new Label("YtyMark");
        titleLabel.getStyleClass().add("titleLabel");
        // 创建左侧部分：图标 + 应用名称
        HBox leftSection = new HBox(icon, titleLabel);
        leftSection.setSpacing(5);  // 图标和标题之间的间距
        leftSection.setAlignment(Pos.CENTER_LEFT);  // 左对齐
        leftSection.setStyle("-fx-padding: 0px 0px 0px 4px;");

        // 最小化按钮
        SVGPath minimizeIcon = SVGPathUtil.createSVGPath(SVGIconCode.MINIMIZE_ICON.getIconCode(),0.6, 0.6, Color.WHITE);
        minimizeIcon.getStyleClass().add("window-button-icon"); // 添加CSS类
        Button minimizeButton = new Button();
        minimizeButton.setGraphic(minimizeIcon);
        minimizeButton.setOnAction(e -> stage.setIconified(true));

        // 最大化 / 还原按钮
        SVGPath maximizeIcon = SVGPathUtil.createSVGPath(SVGIconCode.MAXIMIZE_ICON.getIconCode(), 0.6, 0.6, Color.WHITE);
        maximizeIcon.getStyleClass().add("window-button-icon"); // 添加CSS类
        Button maximizeButton = new Button();
        maximizeButton.setGraphic(maximizeIcon);
        maximizeButton.setOnAction(e -> {
            this.maximize(stage);
        });
        // 关闭按钮
        SVGPath closeIcon = SVGPathUtil.createSVGPath(SVGIconCode.CLOSE_ICON.getIconCode(), 0.6, 0.6, Color.WHITE);
        closeIcon.getStyleClass().add("window-button-icon"); // 添加CSS类
        Button closeButton = new Button();
        closeButton.setGraphic(closeIcon);
        closeButton.setOnAction(e -> {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        // 添加 CSS 类
        minimizeButton.getStyleClass().add("window-button");
        maximizeButton.getStyleClass().add("window-button");
        closeButton.getStyleClass().add("window-button-close");
        // 确保按钮填充整个高度
        HBox.setHgrow(minimizeButton, Priority.ALWAYS);
        HBox.setHgrow(maximizeButton, Priority.ALWAYS);
        HBox.setHgrow(closeButton, Priority.ALWAYS);

        // 确保按钮填充整个高度
        minimizeButton.setPrefSize(30, 30);
        maximizeButton.setPrefSize(30, 30);
        closeButton.setPrefSize(30, 30);

        // 创建右侧部分：最小化、最大化、关闭按钮
        HBox rightSection = new HBox(minimizeButton, maximizeButton, closeButton);
        rightSection.setAlignment(Pos.CENTER_RIGHT);  // 右对齐

        titleBar.getChildren().addAll(leftSection, rightSection);
        HBox.setHgrow(leftSection, Priority.ALWAYS);  // 确保左侧部分不会影响右侧部分
        // 监听窗口最大化状态，动态修改按钮图标
        stage.maximizedProperty().addListener((obs, oldVal, isMaximized) -> {
            if (isMaximized) {
                maximizeIcon.setContent(SVGIconCode.REDUCTION_ICON.getIconCode());
            } else {
                maximizeIcon.setContent(SVGIconCode.MAXIMIZE_ICON.getIconCode());
            }
        });
        // 双击放大
        titleBar.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                this.maximize(stage);
            }
        });
        // 拖动支持
        WindowUtil.addDragSupport(stage, titleBar);

        return titleBar;
    }

    private void maximize(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        if (maximized) {
            stage.setX(backupBounds.getMinX());
            stage.setY(backupBounds.getMinY());
            stage.setWidth(backupBounds.getWidth());
            stage.setHeight(backupBounds.getHeight());
            stage.setMaximized(false);
        } else {
            backupBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.setMaximized(true);
        }
        maximized = !maximized;
    }


}
