package org.ytymark.editor.bar.scroll;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.util.Duration;
import netscape.javascript.JSObject;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：滚动条同步
 */
public class BindScroll {

    // 分别用于标记 TextArea 和 WebView 的同步状态
    private volatile boolean isSyncingTextArea = false;
    private volatile boolean isSyncingWebView = false;
    private TextArea textArea;

    // 用于节流 WebView 的滚动事件，设置延时，不然1毫秒就会产生大量事件，导致短时间内大量事件积压
    private final PauseTransition webViewScrollThrottle = new PauseTransition(Duration.millis(1));

    public BindScroll(TextArea textArea) {
        this.textArea = textArea;
    }

    public void bindScrollToSync(WebEngine webEngine) {
        // 绑定 TextArea 滚动事件到 WebView 的滚动同步
        Platform.runLater(() -> {
            ScrollBar textAreaVBar = findScrollBar(textArea, Orientation.VERTICAL);
            if (textAreaVBar != null) {
                textAreaVBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                    // 这里避免同时两个方向的同步冲突
                    if (!isSyncingTextArea && !isSyncingWebView) {
                        isSyncingTextArea = true;
                        double min = textAreaVBar.getMin();
                        double max = textAreaVBar.getMax();
                        double ratio = (newVal.doubleValue() - min) / (max - min);
                        // 使用相同的比例控制 WebView 的滚动位置
                        String script = "window.scrollTo(0, (document.body.scrollHeight - window.innerHeight) * " + ratio + ");";
                        webEngine.executeScript(script);
                        isSyncingTextArea = false;
                    }
                });
            }
        });

        // 为 WebView 注入 JavaScript 滚动监听器
        Platform.runLater(() -> {
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("javaApp", new JavaApp());
            String jsCode =
                    "window.addEventListener('scroll', function() {" +
                            "  var scrollableHeight = document.body.scrollHeight - window.innerHeight;" +
                            "  var ratio = scrollableHeight > 0 ? window.scrollY / scrollableHeight : 0;" +
                            "  if(window.javaApp) {" +
                            "      window.javaApp.handleScroll(ratio);" +
                            "  }" +
                            "});";
            webEngine.executeScript(jsCode);
        });
    }

    // 查找指定方向的滚动条
    public ScrollBar findScrollBar(TextArea textArea, Orientation orientation) {
        for (Node node : textArea.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation() == orientation) {
                    return scrollBar;
                }
            }
        }
        return null;
    }

    // 根据 WebView 的比例，同步更新 TextArea 的滚动位置
    public void syncTextAreaScroll(double ratio) {
        Platform.runLater(() -> {
            ScrollBar textAreaVBar = findScrollBar(textArea, Orientation.VERTICAL);
            if (textAreaVBar != null && !isSyncingWebView) {
                isSyncingWebView = true;
                double min = textAreaVBar.getMin();
                double max = textAreaVBar.getMax();
                double newValue = min + ratio * (max - min);
                textAreaVBar.setValue(newValue);
                isSyncingWebView = false;
            }
        });
    }


    // 暴露给 JavaScript 的回调对象
    public class JavaApp {
        // 当 JS 端事件触发时，调用该方法，并使用节流处理
        public void handleScroll(double ratio) {
            // 使用 PauseTransition 节流：每次滚动事件开始时，重置计时器
            webViewScrollThrottle.setOnFinished(e -> syncTextAreaScroll(ratio));
            webViewScrollThrottle.playFromStart();
        }
    }
}
