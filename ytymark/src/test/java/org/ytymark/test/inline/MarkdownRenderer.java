package org.ytymark.test.inline;

import org.ytymark.node.inline.*;

import java.util.List;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述： 渲染测试
 */
public class MarkdownRenderer {

    public String render(List<InlineNode> nodes) {
        StringBuilder sb = new StringBuilder();
        for (InlineNode node : nodes) {
            if (node instanceof TextNode) {
                sb.append(((TextNode) node).getText());
            } else if (node instanceof ItalicNode) {
                sb.append("<em>").append((((ItalicNode) node).getText())).append("</em>");
            } else if (node instanceof StrongNode) {
                sb.append("<strong>").append(((StrongNode) node).getText()).append("</strong>");
            } else if (node instanceof CodeNode) {
                sb.append("<code>").append(((CodeNode) node).getCode()).append("</code>");
            } else if (node instanceof LinkNode) {
                LinkNode linkNode = (LinkNode) node;
                sb.append("<a href=\"").append(linkNode.getUri()).append("\">")
                        .append((linkNode.getText())).append("</a>");
            }
            // 可以根据需要添加其他类型的渲染
        }
        return sb.toString();
    }
}
