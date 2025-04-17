package org.ytymark.editor.edit;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：备忘录类，存储文本状态
 */
public class TextMemento {
    private final String text;
    private final int caretPosition;

    public TextMemento(String text, int caretPosition) {
        this.text = text;
        this.caretPosition = caretPosition;
    }

    public String getText() {
        return text;
    }

    public int getCaretPosition() {
        return caretPosition;
    }
}
