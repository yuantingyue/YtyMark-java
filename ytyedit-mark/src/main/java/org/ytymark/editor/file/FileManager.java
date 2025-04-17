package org.ytymark.editor.file;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ytymark.editor.TabFileInfo;
import org.ytymark.editor.TabManager;
import org.ytymark.editor.bar.status.StatusBarInfo;
import org.ytymark.window.CloseDialog;
import org.ytymark.window.dialog.GenericDialog;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：文件管理：新建、打开、保存、另存为等
 */
public class FileManager {
    private Stage stage;
    private CloseDialog closeDialog;
    private TabPane tabPane;

    public FileManager(Stage stage, TabPane tabPane){
        this.stage = stage;
        this.tabPane = tabPane;
        this.closeDialog = new CloseDialog(new GenericDialog(stage));
    }

    public void newFile() {
        Tab tab = TabManager.createNewTab("",null);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab); // 自动显示新 Tab
        this.setupTabCloseHandler(tab);
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Markdown 文件", "*.md", "*.markdown", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                // 将文件内容显示到文本区域
                Tab tab = TabManager.createNewTab(content.toString(),file.getPath());
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab); // 自动显示新 Tab
                TabFileInfo info = (TabFileInfo)tab.getUserData();
                info.getRenderMarkdown().initRenderMarkdown(content.toString());
                this.setupTabCloseHandler(tab);
                StatusBarInfo.updateStatus("打开文件：" + file.getName());
            } catch (IOException e) {
                StatusBarInfo.updateStatus("打开文件错误：" + e.getMessage());
            }
        }
    }

    public void saveFile() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab == null) return;

        TabFileInfo info = (TabFileInfo) currentTab.getUserData();
        String filePath = info.getFilePath();
        if (filePath == null) {
            // 未保存过的新文件，执行另存为
            this.saveAsCurrentTab();
        } else {
            this.writeFile(new File(filePath));
        }
    }

    // 如果有修改未保存，询问用户
    public void checkBeforeClose(Tab tab) {
        TabFileInfo info = (TabFileInfo)tab.getUserData();
        String fileName = info.getFileName();
        closeDialog.setDescription("\"" + fileName + "\"未保存，是否保存？");
        closeDialog.showCloseDialog(()->{
            this.saveFile();
            // 确保真的保存才关闭
            if(!info.isModified()){
                this.clear(tab, info);
            }
        }, () -> {
            // 点击不保存时执行
            this.clear(tab, info);
        });
    }

    private void clear(Tab tab, TabFileInfo info) {
        // 1. 清理撤销/重做历史
        info.getCaretaker().clear();
        // 2. 清除info
        info.clear();
        // 3. 清理 SplitPane 及其子组件
        SplitPane splitPane = (SplitPane) tab.getContent();
        splitPane.getItems().clear();
        // 4. 移除 Tab 所有关联数据
        tab.setContent(null);
        tab.setUserData(null);
        tab.getProperties().clear(); // 清除可能存在的绑定属性
        // 5. 从 TabPane 中移除
        tabPane.getTabs().remove(tab);
    }
    /**
     * 关闭前检测是否未保存
     */
    public void setupTabCloseHandler(Tab tab) {
        tab.setOnCloseRequest(event -> {
            TabFileInfo info = (TabFileInfo) tab.getUserData();
            if (info.isModified()) {
                event.consume(); // 取消关闭操作
                this.checkBeforeClose(tab);
            }
        });
    }

    /**
     * 另存为
     */
    public void saveAsCurrentTab() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("文件另存为");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Markdown 文件", "*.md", "*.markdown", "*.txt")
        );
        File file = fileChooser.showSaveDialog(stage);
        this.writeFile(file);
    }

    /**
     * 统一的文件写入方法
     */
    private void writeFile(File file) {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab == null) return;

        if (file != null) {
            TabFileInfo info = (TabFileInfo) currentTab.getUserData();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(info.getTextArea().getText());
                info.setFileName(file.getName());
                info.setFilePath(file.getAbsolutePath());
                info.setModified(false);
                currentTab.setText(file.getName()); // 更新标题
                StatusBarInfo.updateStatus("保存文件成功：" + file.getName());
            } catch (IOException e) {
                StatusBarInfo.updateStatus("保存文件错误：" + e.getMessage());
            }
        }
    }


}
