package org.ytymark.editor;

import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.ytymark.RenderMarkdown;
import org.ytymark.editor.edit.TextEditorOriginator;
import org.ytymark.editor.edit.UndoRedoCaretaker;
import org.ytymark.editor.theme.ThemeManager;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：自定义类：存储 Tab 关联的文件信息
 */
public class TabFileInfo {
    private String fileName;    // 文件名称（null 表示未保存的新文件）
    private String filePath;    // 文件路径（null 表示未保存的新文件）
    private boolean isModified; // 是否已修改
    private TextArea textArea;  // 关联的 TextArea
    private WebView webView;    // 关联的 WebView

    // 撤销/恢复相关状态
    private UndoRedoCaretaker caretaker;
    //
    private RenderMarkdown renderMarkdown;

    public TabFileInfo(String fileName,String filePath, TextArea textArea, WebView webView) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.textArea = textArea;
        this.webView = webView;
        TextEditorOriginator originator = new TextEditorOriginator(textArea);
        this.caretaker = new UndoRedoCaretaker(originator);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public WebView getWebView() {
        return webView;
    }

    public UndoRedoCaretaker getCaretaker() {
        return caretaker;
    }

    public RenderMarkdown getRenderMarkdown() {
        return renderMarkdown;
    }

    public void setRenderMarkdown(RenderMarkdown renderMarkdown) {
        this.renderMarkdown = renderMarkdown;
    }

    public void clear(){
        // 注销观察者
        ThemeManager.getInstance().removeThemeChangeListener(renderMarkdown);
        // 销毁 WebView 及其引擎
        if (webView != null) {
            WebEngine engine = webView.getEngine();
            engine.load(null);    // 清空当前内容
            engine.getLoadWorker().cancel(); // 取消正在加载的任务
            engine.setJavaScriptEnabled(false); // 禁用 JS 减少残留
            webView.getEngine().loadContent(""); // 彻底清空内容
        }
        this.textArea = null;
        this.webView = null;
        this.caretaker = null;
        this.renderMarkdown = null;
    }

}