package org.ytymark.editor.file;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ytymark.editor.TabFileInfo;
import org.ytymark.editor.bar.status.StatusBarInfo;

import java.io.*;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：导出功能
 */
public class Exporter {
    private TabPane tabPane;
    private Stage stage;

    public Exporter(Stage stage, TabPane tabPane){
        this.stage = stage;
        this.tabPane = tabPane;
    }

    public void exportHtmlToPdf() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab == null) return;
        TabFileInfo info = (TabFileInfo)currentTab.getUserData();
        WebView webView = info.getWebView();
        WebEngine webEngine = webView.getEngine();
        // 获取当前 WebView 渲染的 HTML 内容
        Object result = webEngine.executeScript("document.documentElement.outerHTML");
        if (result != null) {
            String currentHtml = result.toString();
            // 弹出文件保存对话框让用户选择导出路径
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择导出 PDF 文件");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF 文件", "*.pdf"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try (OutputStream os = new FileOutputStream(file)) {
                    // 设置字体提供者并添加系统字体
                    FontProvider fontProvider = new FontProvider();
                    // 添加标准字体
                    fontProvider.addStandardPdfFonts();
                    String fontDir = Exporter.class.getResource("/fonts").getPath();
                    // 添加自定义字体集
                    fontProvider.addDirectory(fontDir);

                    // 设置转换属性
                    ConverterProperties props = new ConverterProperties();
                    props.setFontProvider(fontProvider);
                    props.setCharset("UTF-8");

                    // 使用 iText 将 HTML 转换为 PDF
                    HtmlConverter.convertToPdf(currentHtml, os, props);
                    StatusBarInfo.updateStatus("PDF 导出成功：" + file.getAbsolutePath());
                } catch (Exception ex) {
                    StatusBarInfo.updateStatus("PDF 导出失败：" + ex.getMessage());
                }
            }
        }
    }

    public void exportToHtml() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab == null) return;
        TabFileInfo info = (TabFileInfo)currentTab.getUserData();
        WebView webView = info.getWebView();
        WebEngine webEngine = webView.getEngine();
        // 获取当前 WebView 渲染的 HTML 内容
        Object result = webEngine.executeScript("document.documentElement.outerHTML");
        if (result != null) {
            String htmlContent = result.toString();
            // 弹出文件保存对话框让用户选择导出路径
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("导出 HTML");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML 文件", "*.html"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("<!DOCTYPE html>\n"+htmlContent);
                    StatusBarInfo.updateStatus("HTML 导出成功：" + file.getAbsolutePath());
                } catch (IOException e) {
                    StatusBarInfo.updateStatus("HTML 导出失败：" + e.getMessage());
                }
            }

        }
    }
}
