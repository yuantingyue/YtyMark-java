package org.ytymark.editor.edit;


import javafx.scene.control.TextArea;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：发起者，管理文本状态
 */
public class TextEditorOriginator {

    private TextArea textArea;

    public TextEditorOriginator(TextArea textArea) {
        this.textArea = textArea;
    }

    // 保存
    public TextMemento save() {
        return new TextMemento(textArea.getText(), textArea.getCaretPosition());
    }

    // 恢复
    public void restore(TextMemento memento) {
        textArea.setText(memento.getText());
        textArea.positionCaret(memento.getCaretPosition());
    }

}
