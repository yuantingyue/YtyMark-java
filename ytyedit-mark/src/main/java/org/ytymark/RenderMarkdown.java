package org.ytymark;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.ytymark.editor.TabFileInfo;
import org.ytymark.editor.bar.scroll.BindScroll;
import org.ytymark.editor.theme.ThemeChangeListener;
import org.ytymark.editor.theme.ThemeManager;
import org.ytymark.editor.theme.style.Style;
import org.ytymark.node.Node;
import org.ytymark.parser.Parser;
import org.ytymark.parser.builder.ParserBuilder;
import org.ytymark.parser.builder.RendererBuilder;
import org.ytymark.renderer.HtmlRenderer;
import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：监听TextArea编辑区域的变化，对markdown文本进行解析和渲染
 *      在此使用的是自定义的markdown解析和渲染（ytymark），
 *      如果您仅需要使用当前工具（ytyedit-mark)，那么你可以在此类中修改解析和渲染器。
 */
public class RenderMarkdown  implements ThemeChangeListener {
    private Parser parser;
    private Renderer htmlRenderer;
    private TextArea textArea;
    private WebEngine webEngine;
    private BindScroll bindScroll;

    public RenderMarkdown(Tab tab) {
        // 初始化 Markdown 解析器和渲染器
        this.parser = ParserBuilder.builder().build();
        this.htmlRenderer = RendererBuilder.builder().build(HtmlRenderer.class);
        TabFileInfo info = (TabFileInfo) tab.getUserData();
        this.textArea = info.getTextArea();
        WebView webView = info.getWebView();
        this.webEngine = webView.getEngine();
        this.bindScroll = new BindScroll(textArea);
        // 注册自己为 ThemeContext 的监听者
        ThemeManager.getInstance().addThemeChangeListener(this);
        // 初次渲染
        this.initRenderMarkdown("");
        //
        this.textAreaListener(tab);
    }

    // 开启监听
    public void textAreaListener(Tab tab){
        String text = tab.getText();
        TabFileInfo info = (TabFileInfo) tab.getUserData();
        // 添加监听器，实时更新 HTML 渲染
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!info.isModified()) {
                info.setModified(true);
                tab.setText(text + " *"); // 添加星号提示未保存
            }
            System.out.println("=====开始时间--" + System.currentTimeMillis() + "毫秒");
            Node node = parser.parse(newValue);
            String html = htmlRenderer.processRender(node);
            System.out.println("=====结束时间--" + System.currentTimeMillis() + "毫秒");
            System.out.println("渲染后的HTML:\n"+html);
            // 使用 executeScript 更新预览区域内容
            String script = "document.getElementById('preview').innerHTML = " + escapeForJS(html) + ";";
            webEngine.executeScript(script);
            bindScroll.bindScrollToSync(webEngine);
        });

    }

    // Markdown 解析与 HTML 渲染
    public void initRenderMarkdown(String markdown) {
        if(markdown==null)
            markdown = textArea.getText();
        Node node = parser.parse(markdown);
        String html = htmlRenderer.processRender(node);
        String fullHtml = this.generateHtml(html);
        webEngine.loadContent(fullHtml);
        bindScroll.bindScrollToSync(webEngine);
    }

    // Markdown 解析与 HTML 渲染
    public void renderMarkdown(String markdown) {
        if(markdown==null)
            markdown = textArea.getText();
        Node node = parser.parse(markdown);
        String html = htmlRenderer.processRender(node);
        Style currentTheme = ThemeManager.getInstance().getMainStyle();
        String themeCss = currentTheme.getCss();
        String scriptCss = "document.getElementById('theme-style').innerHTML = " + escapeForJS(themeCss) + ";";
        String script = "document.getElementById('preview').innerHTML = " + escapeForJS(html) + ";";
        webEngine.executeScript(scriptCss+script);
        bindScroll.bindScrollToSync(webEngine);
    }


    // 用于防止 CSS 内容中包含 '‘\n 等字符，导致 JavaScript 解析出错
    private String escapeForJS(String s) {
        return "'" + s.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "") + "'";
    }

    /**
     * 包装 HTML 内容为完整 HTML 页面
     */
    public String generateHtml(String content) {
        Style currentTheme = ThemeManager.getInstance().getMainStyle();
        String themeCss = currentTheme.getCss();
        return "<html lang=\"en\">\n" +
                "<head>\n " +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style id=\"theme-style\">\n" +
                themeCss +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"preview\">" +
                content + "\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * 订阅主题样式变更，主题变更后自动重新渲染内容
     */
    @Override
    public void onThemeChanged() {
        // 主题变更后自动渲染
        renderMarkdown(null);
    }
}
