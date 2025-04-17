import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import org.ytymark.enums.IconPath;
import org.ytymark.utils.ResourceUtils;

public class AboutDialog extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Main Window");
        Button btn = new Button("Show About Dialog");
        btn.setOnAction(e -> showAboutDialog(stage));

        StackPane root = new StackPane(btn);
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    private void showAboutDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.initStyle(StageStyle.UNDECORATED); // 取消默认标题栏

        VBox content = new VBox();
        content.setPadding(new Insets(20));
        content.getStyleClass().add("dialog-pane");

        // 自定义标题栏
        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setAlignment(Pos.CENTER_RIGHT);

        Button closeBtn = new Button("✕");
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

        Text title = new Text("IntelliJ IDEA 2024.3 (Community Edition)");
        title.getStyleClass().add("title");

        Text buildInfo = new Text("Build #IC-243.21565.193, built on November 13, 2024");
        buildInfo.getStyleClass().add("subtitle");

        Text runtimeInfo = new Text("               ");
        runtimeInfo.getStyleClass().add("text");

        Text copyright = new Text("Powered by open-source software\nCopyright © 2000–2025 JetBrains s.r.o.");
        copyright.getStyleClass().add("text");

        textContent.getChildren().addAll(title, buildInfo, runtimeInfo, copyright);

        // 按钮
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button copyCloseBtn = new Button("Copy and Close");
        copyCloseBtn.getStyleClass().add("copy-close-button");
        copyCloseBtn.setOnAction(e -> dialog.close());

        Button simpleCloseBtn = new Button("Close");
        simpleCloseBtn.getStyleClass().add("close-button");
        simpleCloseBtn.setOnAction(e -> dialog.close());

        buttonBox.getChildren().addAll(copyCloseBtn, simpleCloseBtn);

        // 整体布局
        HBox header = new HBox(10, logo, textContent);
        header.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(titleBar, header, buttonBox);

        Scene scene = new Scene(content);
        scene.getStylesheets().add(getClass().getResource("about.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

