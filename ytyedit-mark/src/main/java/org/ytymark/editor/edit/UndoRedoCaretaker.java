package org.ytymark.editor.edit;


import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：看护者：管理撤销和恢复的栈
 */
public class UndoRedoCaretaker {

    private Deque<TextMemento> undoStack = new ArrayDeque<>();
    private Deque<TextMemento> redoStack = new ArrayDeque<>();
    private TextEditorOriginator originator;
    private boolean isUndoRedo = false;
    private static final int MAX_HISTORY = 1000; // 最多保存 1000 步

    public UndoRedoCaretaker(TextEditorOriginator originator) {
        this.originator = originator;
        // 存入初始状态，确保撤销可用
        undoStack.push(originator.save());
    }

    // 保存
    public void saveState(String text, int caretPosition) {
        if (isUndoRedo) return; // 撤销/恢复时不存状态
        if (undoStack.size() >= MAX_HISTORY) {
            undoStack.removeFirst();
        }
        TextMemento newState = new TextMemento(text, caretPosition);
        // 避免重复存入同样的状态
        if (undoStack.isEmpty() || !undoStack.peek().getText().equals(newState.getText())) {
            undoStack.push(newState);
        }
        redoStack.clear();
    }

    // 撤销
    public void undo() {
        if (!undoStack.isEmpty()) {
            isUndoRedo = true;
            // 保存当前状态到 redoStack
            TextMemento currentState = originator.save();
            if (redoStack.isEmpty() || !redoStack.peek().getText().equals(currentState.getText())) {
                redoStack.push(currentState);
            }
            // 弹出上一个状态
            originator.restore(undoStack.pop());
            isUndoRedo = false;
        }
    }
    // 恢复
    public void redo() {
        if (!redoStack.isEmpty()) {
            isUndoRedo = true;
            TextMemento currentState = originator.save();
            if (undoStack.isEmpty() || !undoStack.peek().getText().equals(currentState.getText())) {
                undoStack.push(currentState);
            }
            originator.restore(redoStack.pop());
            isUndoRedo = false;
        }
    }
    public boolean isUndoRedo() {
        return isUndoRedo;
    }
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

}
