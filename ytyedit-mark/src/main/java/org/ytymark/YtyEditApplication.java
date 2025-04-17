package org.ytymark;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ytymark.editor.about.AboutMenu;
import org.ytymark.editor.bar.status.StatusBarInfo;
import org.ytymark.editor.edit.EditMenu;
import org.ytymark.editor.file.FileMenu;
import org.ytymark.editor.theme.ThemeContext;
import org.ytymark.editor.theme.ThemeMenu;
import org.ytymark.enums.IconPath;
import org.ytymark.utils.ResourceUtils;
import org.ytymark.utils.WindowUtil;
import org.ytymark.window.TitleBar;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：程序主入口
 */
public class YtyEditApplication extends Application {

    @Override
    public void start(Stage stage) {
        // 用于管理多个文件的 Tab
        TabPane tabPane = new TabPane();
        // 隐藏默认系统标题栏
        stage.initStyle(StageStyle.UNDECORATED);
        // 布局
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        // 样式
        ThemeContext themeContext = new ThemeContext(scene);
        // 图标
        Image icon = ResourceUtils.loadImage(IconPath.YTY_ICON_BIG.getIconPath());  // 设置你的图标路径
        stage.getIcons().add(icon);


        // 创建自定义标题栏
        TitleBar customTitleBar = new TitleBar();
        HBox titleBar = customTitleBar.createTitleBar(stage);

        // 创建菜单栏（包含文件、编辑、视图）
        MenuBar menuBar = new MenuBar();
        // 文件菜单：打开、保存、导出（PDF/HTML)
        Menu fileMenu = new FileMenu(stage,tabPane).createMenu();
        // 编辑菜单：撤销、恢复等
        Menu editMenu = new EditMenu(tabPane).createMenu();
        // 主题菜单：主题切换（白天／夜间）
        Menu themeMenu = new ThemeMenu(themeContext).createMenu();
        // 关于菜单
        Menu aboutMenu = new AboutMenu(stage).createMenu();
        menuBar.getMenus().addAll(fileMenu, editMenu, themeMenu, aboutMenu);

        WindowUtil.addResizeSupport(stage, pane);

        // 整体布局
        VBox topVBox = new VBox(titleBar, menuBar);
        VBox.setMargin(menuBar, new Insets(0,3,0,3)); // 给 topVBox 四周加空隙
        pane.setTop(topVBox);
        pane.setCenter(tabPane);
        pane.setBottom(StatusBarInfo.getStatusLabel());
        BorderPane.setMargin(tabPane, new Insets(0,3,0,3)); // 给 tabPane 四周加空隙
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setMinWidth(400);
        stage.setMinHeight(300);

        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
