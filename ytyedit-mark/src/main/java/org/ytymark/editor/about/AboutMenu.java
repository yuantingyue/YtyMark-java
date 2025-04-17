package org.ytymark.editor.about;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.ytymark.window.dialog.GenericDialog;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：关于的菜单
 */
public class AboutMenu {
    private final AboutDialog aboutDialog;

    public AboutMenu(Stage stage){
        aboutDialog = new AboutDialog(new GenericDialog(stage));
    }

    public Menu createMenu(){
        // 关于菜单
        Menu aboutMenu = new Menu("关于");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> {
            aboutDialog.showAboutDialog();
        });

        aboutMenu.getItems().addAll(aboutItem);

        return aboutMenu;
    }

}
