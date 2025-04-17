package org.ytymark.editor.edit;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import org.ytymark.editor.TabFileInfo;
import org.ytymark.editor.bar.status.StatusBarInfo;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：编辑菜单，并绑定备忘录实现撤销/恢复（也可关闭）
 */
public class EditMenu {
    private TabPane tabPane;
    // 是否开启备忘录模式(撤销/恢复）
    private Boolean openFlag = true;

    public EditMenu(TabPane tabPane){
        this.tabPane = tabPane;
    }

    /**
     * 创建编辑菜单
     */
    public Menu createMenu(){

        Menu editMenu = new Menu("编辑");
        MenuItem undoItem = new MenuItem("撤销");
        undoItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        undoItem.setOnAction(event -> {
            // 获取当前 Tab 的 Caretaker
            UndoRedoCaretaker caretaker = this.getCurrentCaretaker();
            if (caretaker != null)
                caretaker.undo();
            event.consume();
        });

        MenuItem redoItem = new MenuItem("恢复");
        redoItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
        redoItem.setOnAction(event -> {
            // 获取当前 Tab 的 Caretaker
            UndoRedoCaretaker caretaker = this.getCurrentCaretaker();
            if (caretaker != null)
                caretaker.redo();
            event.consume();
        });
        editMenu.getItems().addAll(undoItem,redoItem);
        // 是否开启备忘录模式
        if(openFlag) {
            // 监听 Tab 切换事件，动态绑定快捷键和文本监听
            tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (oldTab != newTab && tabPane.getTabs().contains(newTab)) {
                    // 动态绑定快捷键和文本监听
                    this.bindUndoRedoToCurrentTab(newTab);

                    TabFileInfo info = (TabFileInfo) newTab.getUserData();
                    String status = (info.getFilePath() == null) ? (info.getFileName() == null) ? "New File" : info.getFileName() : info.getFilePath();
                    StatusBarInfo.updateStatus(status);
                }
                if (tabPane.getTabs().isEmpty()) {
                    StatusBarInfo.updateStatus("No file opened");
                }
            });
            // 初始检查：如果已有 Tab，手动触发一次
            if (!tabPane.getTabs().isEmpty()) {
                Tab initialTab = tabPane.getSelectionModel().getSelectedItem();
                if (initialTab != null) {
                    // 动态绑定快捷键和文本监听
                    this.bindUndoRedoToCurrentTab(initialTab);
                }
            }
        }
        return editMenu;
    }

    /**
     * 获取当前选中 Tab 的 Caretaker
     */
    private UndoRedoCaretaker getCurrentCaretaker() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab == null) return null;
        TabFileInfo info = (TabFileInfo) currentTab.getUserData();
        return info.getCaretaker();
    }

    /**
     * 绑定当前 Tab 的撤销/恢复逻辑
     */
    private void bindUndoRedoToCurrentTab(Tab tab) {
        TabFileInfo info = (TabFileInfo) tab.getUserData();
        TextArea textArea = info.getTextArea();
        UndoRedoCaretaker caretaker = info.getCaretaker();

        // 监听快捷键撤销/恢复：绑定快捷键到当前 TextArea
        textArea.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                caretaker.undo();
                event.consume();
            } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                caretaker.redo();
                event.consume();
            }
        });

        // 监听文本变化，保存状态到当前 Tab 的 Caretaker
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!caretaker.isUndoRedo()) {
                int caretPos = textArea.getCaretPosition();
                caretaker.saveState(oldValue, caretPos);
            }
        });
    }

}
