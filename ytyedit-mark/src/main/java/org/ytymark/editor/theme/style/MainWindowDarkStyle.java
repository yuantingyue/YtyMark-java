package org.ytymark.editor.theme.style;

import javafx.scene.Scene;
import org.ytymark.utils.ResourceUtils;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：主窗口的黑夜模式
 */
public class MainWindowDarkStyle extends AbstractStyle {
    private static final String STYLE_PATH = "/css/ui/main-dark.css";
    private String backgroundColor = "#3c3f41";
    private String fontColor = "#a9b7c6";

    /**
     * WebView 的HTML样式。
     * 面对更复杂的css样式可以考虑抽出来，放到/css/content目录下，再通过ResourceUtils.getFileString()读取进来
     */
    @Override
    public String getCss() {
        return  getFonts() +
                "body { font-family: 'SourceHanSerifSC Regular', sans-serif; line-height: 1.6; background: "+backgroundColor+"; color: "+fontColor+"; font-size: 0.9em; }" +
                "h1, h2, h3, h4, h5, h6 { font-family: 'SourceHanSansSC-Bold', sans-serif;    margin-bottom: 10px; }" +
                "h1 { font-size: 2em; } h2 { font-size: 1.8em; } h3 { font-size: 1.6em; } h4 { font-size: 1.4em; } h5 { font-size: 1.2em; } h6 { font-size: 1em; }" +
                "pre { background: #2a2a2a; padding: 10px; border-radius: 4px; }" +
                "code { font-family: 'Courier New', monospace; background: #2a2a2a; padding: 2px 0px; border-radius: 4px; }" +
                "ul { margin: 20px 0; padding-left: 20px; }" +
                "ul li { margin: 5px 0; }" +
                "strong { font-family: 'SourceHanSansSC-Bold', sans-serif;  font-weight: bold;  }"+
                "table {border: 1px solid #ccc; border-collapse: collapse;}"+
                "table th,table td {border: 1px solid #ddd; padding: 8px; text-align: left;}"+
                "table th { font-family: 'SourceHanSansSC-Bold', sans-serif;  font-weight: bold;}"+
                "blockquote { border-left: 2px solid #21B56F;  padding-left: 1em;  margin: 0 0 1em;  }"+
                "a {text-decoration: none; color: #21B56F}";
    }

    @Override
    public void applyStyle(Scene scene) {// 先清除已有的 CSS 样式
        scene.getStylesheets().add(ResourceUtils.getResource(STYLE_PATH).toExternalForm());
    }
}