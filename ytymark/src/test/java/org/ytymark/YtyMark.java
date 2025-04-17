package org.ytymark;

import org.ytymark.node.Node;
import org.ytymark.parser.Parser;
import org.ytymark.parser.block.ParagraphParserHandler;
import org.ytymark.parser.builder.ParserBuilder;
import org.ytymark.parser.builder.RendererBuilder;
import org.ytymark.parser.inline.ItalicParser;
import org.ytymark.renderer.HtmlRenderer;
import org.ytymark.renderer.Renderer;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：解析和渲染的测试
 */
public class YtyMark {
    public static void main(String[] args) {
        String markdown =
                        "# 我的标题\n\n" +
                        "### 我的标题3\n\n" +
                        "这是一个段落，用于展示段落解析功能，其中包含[行内格式，比如 **粗体**、*斜体*、~~删除线~~、`代码片段`、[链接](https://example.com) 和 ![图片](https://example.com/img.png)。\n\n" +
                        "> 这是引用的第一行，包含 *斜体*。\n" +
                        "> \n" +
                        "> > 这是引用的第二行，嵌套引用，包含 ~~删除线~~。\n" +
                        "> \n" +
                        "> 这是引用的第三行，包含 **粗体**。\n\n" +
                        "引用中包含列表：\n\n" +
                        "> 引用项\n" +
                        "> \n" +
                        "> - 列表项 A\n" +
                        "> - 列表项 B\n\n" +
                        "```java\n" +
                        "public static void main(String[] args) {\n" +
                        "    System.out.println(\"Hello, World!\");\n" +
                        "}\n" +
                        "```\n\n" +
                        "- 第一项，含有 [链接](https://example.com)\n" +
                        "  - 嵌套项 1，含有 `行内代码`\n" +
                        "  - 嵌套项 2，含有 **粗体**\n" +
                        "- 第二项\n" +
                        "  1. 有序嵌套项 1，含有 *斜体*\n" +
                        "  2. 有序嵌套项 2，含有 ~~删除线~~\n" +
                        "- > 第三项\n" +
                        "  > \n" +
                        "  > 有序嵌套项 1，含有 *斜体*\n" +
                        "  > \n" +
                        "  > 有序嵌套项 2，含有 ~~删除线~~";

        // 可自定义解析器
        Parser parser = ParserBuilder.builder()
                .addDelimiter("_")
                .addBlockParser(new ParagraphParserHandler())
                .addInlineParser("_", new ItalicParser()).build();
        Renderer renderer = RendererBuilder.builder().build(HtmlRenderer.class);
        Node root = parser.parse(markdown);
        String html = renderer.processRender(root);
        System.out.println(html);
    }
}
