package org.ytymark;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：表格测试
 */
import org.ytymark.node.Node;
import org.ytymark.parser.Parser;
import org.ytymark.parser.builder.ParserBuilder;
import org.ytymark.parser.builder.RendererBuilder;
import org.ytymark.renderer.HtmlRenderer;
import org.ytymark.renderer.Renderer;

// 示例使用
public class TableDemo {
    public static void main(String[] args) {
        String markdown =
                "| Name | Age | City |\n"+
                "|------|-----|------|\n"+
                "| Alice | 30 | NY |\n"+
                "| Bob | 25 | LA |\n";
        // 可自定义解析器
        Parser parser = ParserBuilder.builder().build();
        Renderer renderer = RendererBuilder.builder().build(HtmlRenderer.class);
        Node root = parser.parse(markdown);
        String html = renderer.processRender(root);
        System.out.println(html);
    }
}
