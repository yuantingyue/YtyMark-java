import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MultiLevelMenuDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

        // 一级菜单 A（必须是 Menu 类型）
        Menu menuA = new Menu("A");

        // 二级菜单 B（必须是 Menu 类型）
        Menu menuB = new Menu("B");

        // 三级菜单项 C（实际是 MenuItem）
        MenuItem menuItemC = new MenuItem("C");

        // 组装菜单结构
        menuB.getItems().add(menuItemC);
        menuA.getItems().add(menuB);
        menuBar.getMenus().add(menuA);


        // 详细样式为：modena.css
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}