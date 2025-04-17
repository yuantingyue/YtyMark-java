import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ytymark.enums.IconPath;
import org.ytymark.enums.SVGIconCode;
import org.ytymark.utils.ResourceUtils;
import org.ytymark.utils.SVGPathUtil;

import java.awt.*;
import java.net.URI;

public class About {
    private double xOffset = 0;
    private double yOffset = 0;


    public void showAboutDialog(Stage stage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.initStyle(StageStyle.UNDECORATED); // 取消默认标题栏

        VBox content = new VBox();
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dialog-pane");

        // 自定义标题栏
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_RIGHT);
        SVGPath closeIcon = SVGPathUtil.createSVGPath(SVGIconCode.CLOSE_ICON.getIconCode(), 0.6, 0.6, Color.WHITE);
        Button closeBtn = new Button();
        closeBtn.setGraphic(closeIcon);
        closeBtn.getStyleClass().add("title-close-button");
        closeBtn.setOnAction(e -> dialog.close());

        titleBar.getChildren().add(closeBtn);

        // 允许拖动窗口
        titleBar.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged((MouseEvent event) -> {
            dialog.setX(event.getScreenX() - xOffset);
            dialog.setY(event.getScreenY() - yOffset);
        });

        // Logo
        ImageView logo = ResourceUtils.loadImageView(IconPath.YTY_ICON_BIG.getIconPath(), 50);

        // 文本信息
        VBox textContent = new VBox(5);
        textContent.setPadding(new Insets(10, 0, 10, 0));

        Text title = new Text("YtyMark");
        title.getStyleClass().add("title");

        Text description = new Text("这是一款极其简单的markdown文本编辑器。\n");
        description.getStyleClass().add("subtitle");

        Text purposeInfo = new Text("主要目的是用于学习设计模式的实战项目，将所学的设计模式用于实战。");
        purposeInfo.getStyleClass().add("text");


        HBox openSourceBox = new HBox(5);
        openSourceBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(openSourceBox, new Insets(10, 0, 0, 10)); // 增加左边距
        Text openSourceLabel = new Text("开源地址：");
        openSourceLabel.getStyleClass().add("text");

        // 使用 Text 让 URL 可复制
//        Text openSourceLink = new Text("http://");
//        openSourceLink.getStyleClass().add("link-text");
//        openSourceLink.setOnMouseClicked(event -> {
//            Clipboard clipboard = Clipboard.getSystemClipboard();
//            ClipboardContent clipboardContent = new ClipboardContent();
//            clipboardContent.putString(openSourceLink.getText()); // 复制到剪贴板
//            clipboard.setContent(clipboardContent);
//        });

        // 使用 Hyperlink 让 URL 可点击
        Hyperlink openSourceLink = new Hyperlink("http://www.baidu.com/");
        openSourceLink.getStyleClass().add("link-text");
        openSourceLink.setStyle("-fx-text-fill: #4ea1f3;"); // 设置蓝色+下划线

        // 点击跳转到默认浏览器
        openSourceLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(openSourceLink.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        openSourceBox.getChildren().addAll(openSourceLabel, openSourceLink);

        textContent.getChildren().addAll(title, description, purposeInfo, openSourceBox);

        // 按钮
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 5, 5, 0));

        Button simpleCloseBtn = new Button("Close");
        simpleCloseBtn.getStyleClass().add("close-button");
        simpleCloseBtn.setOnAction(e -> dialog.close());

        buttonBox.getChildren().addAll(simpleCloseBtn);

        // 整体布局
        HBox header = new HBox(10, logo, textContent);
        header.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(titleBar, header, buttonBox);

        Scene scene = new Scene(content);
        scene.getStylesheets().add(getClass().getResource("/css/about.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

}
