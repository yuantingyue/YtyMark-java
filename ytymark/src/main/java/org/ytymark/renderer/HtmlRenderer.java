package org.ytymark.renderer;

import org.ytymark.node.Node;
import org.ytymark.node.block.*;
import org.ytymark.node.block.table.*;
import org.ytymark.node.inline.*;

import java.util.Objects;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：HTML渲染器
 */
public class HtmlRenderer extends AbstractRenderer {
    private StringBuilder sbHTML;

    public HtmlRenderer(){
    }

    @Override
    public String processRender(Node node){
        sbHTML = new StringBuilder();
        Objects.requireNonNull(node, "node节点不能为空");
        render(node);
        return sbHTML.toString();
    }

    @Override
    public void render(HeadingNode headingNode) {
        String htag = "h" + headingNode.getLevel();
        sbHTML.append("<").append(htag).append(">");
        renderChildren(headingNode);
        sbHTML.append("</").append(htag).append(">").append("\n");
    }

    @Override
    public void render(ParagraphNode paragraphNode) {
        sbHTML.append("<p>");
        renderChildren(paragraphNode);
        sbHTML.append("<p>\n");
    }

    @Override
    public void render(FenceCodeBlockNode fenceCodeBlock) {
        String language = fenceCodeBlock.getLanguage();
        if (language != null && !language.isEmpty()) {
            sbHTML.append("<pre class=\"").append(language).append("\">");
        } else {
            sbHTML.append("<pre>");
        }
        sbHTML.append("\n");
        // 代码
        sbHTML.append("<code>")
                .append(fenceCodeBlock.getCode())
                .append("</code>");
        sbHTML.append("\n");
        sbHTML.append("</pre>"); // 添加结束的围栏字符
        sbHTML.append("\n");
    }

    @Override
    public void render(BlockQuoteNode blockquoteNode) {
        sbHTML.append("<blockquote>\n");
        renderChildren(blockquoteNode);
        sbHTML.append("</blockquote>\n");
    }


    @Override
    public void render(ListItemNode listItemNode) {
        sbHTML.append("<li>");
        sbHTML.append(listItemNode.getContent());
        renderChildren(listItemNode);
        sbHTML.append("</li>\n");
    }

    @Override
    public void render(OrderedListNode orderedListNode) {
        sbHTML.append("<ol>\n");
        renderChildren(orderedListNode);
        sbHTML.append("</ol>\n");
    }

    @Override
    public void render(UnorderedListNode unorderedListNode) {
        sbHTML.append("<ul>\n");
        renderChildren(unorderedListNode);
        sbHTML.append("</ul>\n");
    }

    @Override
    public void render(HorizontalLineNode horizontalLineNode) {
        sbHTML.append("<hr />");
    }

    @Override
    public void render(TableNode tableNode) {
        sbHTML.append("<table>\n");
        renderChildren(tableNode);
        sbHTML.append("</table>\n");
    }

    @Override
    public void render(TableHeaderNode tableHeaderNode) {
        sbHTML.append("<thead>\n");
        renderChildren(tableHeaderNode);
        sbHTML.append("</thead>\n");
    }

    @Override
    public void render(TableBodyNode tableBodyNode) {
        sbHTML.append("<tbody>\n");
        renderChildren(tableBodyNode);
        sbHTML.append("</tbody>\n");
    }

    @Override
    public void render(TableRowNode tableRowNode) {
        sbHTML.append("<tr>\n");
        renderChildren(tableRowNode);
        sbHTML.append("</tr>\n");
    }

    @Override
    public void render(TableHeaderCellNode tableHeaderCellNode) {
        sbHTML.append("<th>").append(escapeHtml(tableHeaderCellNode.getText())).append("</th>\n");
    }

    @Override
    public void render(TableBodyCellNode tableBodyCellNode) {
        sbHTML.append("<td>").append(escapeHtml(tableBodyCellNode.getText())).append("</td>\n");
    }

    private String escapeHtml(String text) {
        return text.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    @Override
    public void render(CodeNode codeNode) {
        sbHTML.append("<code>");
        sbHTML.append(codeNode.getCode());
        sbHTML.append("</code>");
    }

    @Override
    public void render(ImageNode imageNode) {
        sbHTML.append("<img alt=\"");
        renderChildren(imageNode);
        sbHTML.append("\" src=\"file://")
                .append(imageNode.getUri())
                .append("\" />");
    }

    @Override
    public void render(LinkNode linkNode) {
        sbHTML.append("<a href=\"")
                .append(linkNode.getUri())
                .append("\">");
        renderChildren(linkNode);
        sbHTML.append("</a>");
    }

    @Override
    public void render(DeleteLineNode deleteLineNode) {
        sbHTML.append("<del style=\"text-decoration: line-through;\">");
        renderChildren(deleteLineNode);
        sbHTML.append("</del>");
    }

    @Override
    public void render(StrongNode strongNode) {
        sbHTML.append("<strong>");
        renderChildren(strongNode);
        sbHTML.append("</strong>");
    }

    @Override
    public void render(ItalicNode italicNode) {
        sbHTML.append("<em>");
        renderChildren(italicNode);
        sbHTML.append("</em>");
    }
    @Override
    public void render(HtmlInlineNode htmlInlineNode) {
        sbHTML.append(htmlInlineNode.getTag());
    }
    @Override
    public void render(LineBreakNode lineBreakNode) {
        sbHTML.append("<br/>");
    }

    @Override
    public void render(TextNode textNode) {
        sbHTML.append(textNode.getText());
    }

}
