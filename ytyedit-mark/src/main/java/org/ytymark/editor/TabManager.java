package org.ytymark.editor;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import org.ytymark.RenderMarkdown;

import java.io.File;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：对Tab的管理
 */
public class TabManager {
    private static Integer tabNum = 0;

    // 创建新 Tab 的通用方法
    public static Tab createNewTab(String initialText, String filePath) {
        /* 每个 Tab 内部创建独立的 TextArea 和 WebView*/
        // 初始化 TextArea，填充足够文本使其出现滚动条
        TextArea textArea = new TextArea(initialText);
        textArea.setWrapText(true);
        textArea.setPromptText("Enter your Markdown text here...");
        // 字体大小属性
        IntegerProperty fontSize = new SimpleIntegerProperty(14); // 默认14px
        // 动态绑定 TextArea 的字体大小样式
        textArea.styleProperty().bind(
                Bindings.concat("-fx-font-size: ", fontSize.asString(), "px;")
        );
        // 初始化 WebView
        WebView webView = new WebView();
        // 使用 SplitPane 布局左右两个区域
        SplitPane splitPane = new SplitPane(textArea, webView);
        splitPane.setDividerPositions(0.5);

        // 创建 Tab 并绑定信息
        Tab tab = new Tab();
        // 设置 Tab 标题（显示文件名）
        String fileName = (filePath == null) ? "Untitled-"+tabNum++ : new File(filePath).getName();
        tab.setText(fileName);
        TabFileInfo info = new TabFileInfo(fileName,filePath,textArea,webView);
        // 将信息绑定到 Tab
        tab.setUserData(info);
        // 绑定文本编辑及解析渲染
        RenderMarkdown renderMarkdown = new RenderMarkdown(tab);
        info.setRenderMarkdown(renderMarkdown);
        tab.setContent(splitPane);

        return tab;
    }

}
