package org.ytymark.test.inline;

import org.ytymark.node.inline.InlineNode;

import java.util.List;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行级元素解析
 */
public class MarkdownProcessor {

    public String process(String markdownText) {
        MarkdownParser parser = new MarkdownParser();
        List<InlineNode> nodes = parser.parseInline(markdownText);

        MarkdownRenderer renderer = new MarkdownRenderer();
        return renderer.render(nodes);
    }
}
