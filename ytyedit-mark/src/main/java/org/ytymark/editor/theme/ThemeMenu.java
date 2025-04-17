package org.ytymark.editor.theme;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.ytymark.editor.bar.status.StatusBarInfo;
import org.ytymark.editor.theme.enums.ThemeType;
import org.ytymark.editor.theme.enums.WindowType;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：主题菜单
 */
public class ThemeMenu {

    private final ThemeContext themeContext;


    public ThemeMenu(ThemeContext themeContext) {
        this.themeContext = themeContext;
    }

    public Menu createMenu(){
        // 主题菜单：主题切换（白天／夜间）
        Menu viewMenu = new Menu("主题");
        // 白天模式
        MenuItem dayModeItem = new MenuItem(ThemeType.LIGHT.getName());
        dayModeItem.setOnAction(e -> {
            // 设置统一样式
            themeContext.setTheme(ThemeType.LIGHT);
            themeContext.switchTheme();
            StatusBarInfo.updateStatus("切换到 " + ThemeType.LIGHT.getName());
        });

        // 夜间模式
        MenuItem nightModeItem = new MenuItem(ThemeType.DARK.getName());
        nightModeItem.setOnAction(e -> {
            // 样式也可以随意组合：不同窗体可以设置不同样式
            themeContext.setStyle(WindowType.MAIN_WINDOW, ThemeType.DARK);
            themeContext.setStyle(WindowType.DIALOG_WINDOW, ThemeType.DARK);
            themeContext.switchTheme();
            StatusBarInfo.updateStatus("切换到 " + ThemeType.DARK.getName());
        });
        viewMenu.getItems().addAll(dayModeItem, nightModeItem);

        return viewMenu;
    }
}
