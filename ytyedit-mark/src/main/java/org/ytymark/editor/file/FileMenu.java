package org.ytymark.editor.file;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.ytymark.editor.TabFileInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：文件菜单
 */
public class FileMenu {
    private Stage stage;
    private TabPane tabPane;
    private FileManager fileManager;
    private Exporter exporter;

    public FileMenu(Stage stage, TabPane tabPane){
        this.stage = stage;
        this.tabPane = tabPane;
        fileManager = new FileManager(stage, tabPane);
        exporter = new Exporter(stage, tabPane);
    }

    public Menu createMenu(){
        // 首次打开软件时创建
        fileManager.newFile();
        // 文件菜单：打开和保存
        Menu fileMenu = new Menu("文件");

        MenuItem newItem = new MenuItem("新建");
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newItem.setOnAction(e -> fileManager.newFile());

        MenuItem openItem = new MenuItem("打开");
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openItem.setOnAction(e -> fileManager.openFile());

        MenuItem saveItem = new MenuItem("保存");
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveItem.setOnAction(e -> fileManager.saveFile());

        MenuItem saveAsItem = new MenuItem("另存为");
        saveAsItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        saveAsItem.setOnAction(e -> fileManager.saveFile());

        // 创建 "导出" 子菜单
        Menu exportMenu = new Menu("导出");
        MenuItem exportPdfItem = new MenuItem("PDF");
        exportPdfItem.setOnAction(e -> exporter.exportHtmlToPdf());

        MenuItem exportHtmlItem = new MenuItem("HTML");
        exportHtmlItem.setOnAction(e -> exporter.exportToHtml());

        exportMenu.getItems().addAll(exportPdfItem, exportHtmlItem);
        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, exportMenu);

        // 设置关闭事件：关闭前提示保存
        stage.setOnCloseRequest(event -> {
            event.consume(); // 消耗事件，防止默认关闭行为
            ObservableList<Tab> tabs = tabPane.getTabs();
            // 创建副本，避免List集合并发修改异常
            List<Tab> modifiedList = tabs.stream().filter(tab -> {
                TabFileInfo info = (TabFileInfo)tab.getUserData();
                // 未保存的才做提示
                return info.isModified();
            }).collect(Collectors.toList());
            // 逐一检查提示
            modifiedList.forEach(tab -> {
                fileManager.checkBeforeClose(tab);
            });
            // 有修改的tab是否都关闭了
            modifiedList = tabs.stream().filter(tab -> {
                TabFileInfo info = (TabFileInfo)tab.getUserData();
                return info.isModified();
            }).collect(Collectors.toList());
            // 关闭应用
            if(tabs.isEmpty() || modifiedList.isEmpty())
                stage.close();
        });

        return fileMenu;
    }

}
