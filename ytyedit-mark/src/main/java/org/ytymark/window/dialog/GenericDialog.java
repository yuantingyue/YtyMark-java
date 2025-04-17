package org.ytymark.window.dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ytymark.editor.theme.ThemeChangeListener;
import org.ytymark.editor.theme.ThemeManager;
import org.ytymark.editor.theme.enums.WindowType;
import org.ytymark.enums.IconPath;
import org.ytymark.enums.SVGIconCode;
import org.ytymark.utils.ResourceUtils;
import org.ytymark.utils.SVGPathUtil;
import org.ytymark.utils.WindowUtil;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：通用弹框，包含固定的右上角关闭按钮、可拖动的标题栏，以及中间主体和底部按钮区，可通过 setContent/addContent 与 setFooter/addFooter 自定义。
 */
public class GenericDialog implements ThemeChangeListener {
    private Scene dialogScene;
    private Stage dialog;
    private VBox root;         // 整体容器
    private HBox titleBar;     // 标题栏（右上关闭按钮）
    private VBox body;         // 主体内容区域
    private HBox footer;       // 底部按钮区域

    // 标题头
    private boolean removeLogo = false;
    private ImageView icon;
    private String titleName = "YTYMark";
    private Label titleLabel;
    private HBox leftSection;

    // 样式
    private final ThemeManager themeManager = ThemeManager.getInstance();


    /**
     * 构造函数，传入父窗口。
     *
     * @param owner 父窗口
     */
    public GenericDialog(Stage owner) {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.initStyle(StageStyle.UNDECORATED); // 取消默认标题栏
        // 根容器
        root = new VBox();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dialog-pane");

        // 创建左侧部分：图标 + 应用名称
        leftSection = new HBox();
        leftSection.setSpacing(5);  // 图标和标题之间的间距
        leftSection.setAlignment(Pos.CENTER_LEFT);  // 左对齐
        // 应用名称
        titleLabel = new Label(titleName);
        titleLabel.getStyleClass().add("titleLabel");

        if(!removeLogo){
            // 图标
            icon = ResourceUtils.loadImageView(IconPath.YTY_ICON.getIconPath(), 28);
            leftSection.getChildren().addAll(icon,titleLabel);
        }else{
            leftSection.getChildren().addAll(titleLabel);
        }
        // 创建右侧部分：关闭按钮
        HBox rightSection = new HBox();
        rightSection.setAlignment(Pos.CENTER_RIGHT);  // 右对齐
        SVGPath closeIcon = SVGPathUtil.createSVGPath(SVGIconCode.CLOSE_ICON.getIconCode(),
                0.6, 0.6, Color.BLACK); // 初始颜色可以随意
        closeIcon.getStyleClass().add("close-icon"); // 添加CSS类
        Button closeButton = new Button();
        closeButton.getStyleClass().add("title-close-button");
        closeButton.setGraphic(closeIcon);
        closeButton.setOnAction(e -> dialog.close());
        rightSection.getChildren().add(closeButton);

        // 创建标题栏（包含右上关闭按钮）
        titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_RIGHT);
        titleBar.getChildren().addAll(leftSection, rightSection);
        HBox.setHgrow(leftSection, Priority.ALWAYS);  // 确保左侧部分不会影响右侧部分

        // 添加拖动效果
        WindowUtil.addDragSupport(dialog,titleBar);

        // 主体内容区域（用户可自定义内容）
        body = new VBox();
        body.setSpacing(10);
        body.setPadding(new Insets(10, 0, 10, 0));

        // 底部按钮区域（用户可自定义按钮）
        footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 5, 5, 0));

        // 自定义底部按钮（例如关闭按钮）
        Button closeBtn = new Button("close");
        closeBtn.getStyleClass().add("close-button");
        closeBtn.setOnAction(e -> dialog.close());

        footer.getChildren().addAll(closeBtn);

        // 将标题栏、主体内容和底部按钮依次添加到根容器中
        root.getChildren().addAll(titleBar, body, footer);

        dialogScene = new Scene(root);
        dialogScene.setFill(Color.TRANSPARENT);

        dialog.setScene(dialogScene);

        themeManager.applyStyle(WindowType.DIALOG_WINDOW,dialogScene);
        this.themeManager.addThemeChangeListener(this);
    }


    /**
     * 设置是否移除 logo，并更新左侧区域内容。
     *
     * @param flag 如果为 true，则不显示 logo；否则显示 logo
     */
    public void removeLogo(boolean flag) {
        this.removeLogo = flag;
        updateLeftSection();
    }

    /**
     * 设置窗口标题 logo，替换原有内容，并更新左侧区域。
     * 注：更新logo后，尺寸也需要自行设置
     * @param logo 自定义的 ImageView 对象
     */
    public void setLogo(ImageView logo) {
        this.icon = logo;
        // 如果当前要求显示 logo，则更新左侧区域
        if (!removeLogo) {
            updateLeftSection();
        }
    }

    /**
     * 设置窗口标题头，替换原有内容，同时更新显示的文本。
     *
     * @param title 新的标题文本
     */
    public void setTitle(String title) {
        this.titleName = title;
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }

    /**
     * 内部方法：更新左侧区域（logo + 标题）的内容。
     */
    private void updateLeftSection() {
        leftSection.getChildren().clear();
        if (!removeLogo && icon != null) {
            leftSection.getChildren().addAll(icon, titleLabel);
        } else {
            leftSection.getChildren().add(titleLabel);
        }
    }
    /**
     * 设置主体内容，替换原有内容。
     *
     * @param content 自定义内容节点
     */
    public void setContent(Node content) {
        body.getChildren().clear();
        body.getChildren().add(content);
    }

    /**
     * 添加内容到主体区域。
     *
     * @param node 自定义内容节点
     */
    public void addContent(Node node) {
        body.getChildren().add(node);
    }

    /**
     * 设置底部按钮区域内容，替换原有内容。
     *
     * @param node 自定义底部节点（例如按钮组）
     */
    public void setFooter(Node node) {
        footer.getChildren().clear();
        footer.getChildren().add(node);
    }

    /**
     * 添加内容到底部按钮区域。
     *
     * @param node 自定义节点（例如按钮）
     */
    public void addFooter(Node node) {
        footer.getChildren().add(node);
    }

    /**
     * 显示弹框，并阻塞等待关闭。
     */
    public void showAndWait() {
        dialog.showAndWait();
    }

    /**
     * 返回弹框对应的 Stage，可用于进一步自定义。
     *
     * @return 弹框 Stage
     */
    public Stage getDialogStage() {
        return dialog;
    }

    /**
     * 订阅主题样式变更，主题变更后自动切换弹窗样式
     */
    @Override
    public void onThemeChanged() {
        // 清空之前的样式
        dialogScene.getStylesheets().clear();
        themeManager.applyStyle(WindowType.DIALOG_WINDOW,dialogScene);
    }


}
