package org.ytymark.renderer;


import org.ytymark.node.Node;
import org.ytymark.node.block.*;
import org.ytymark.node.block.table.*;
import org.ytymark.node.inline.*;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述： 渲染器接口
 */
public interface Renderer {

    String processRender(Node node);

    void render(Node node);

    void render(DocumentNode document);
    void render(HeadingNode headingNode);
    void render(ParagraphNode paragraphNode);
    void render(FenceCodeBlockNode fenceCodeBlock);
    void render(BlockQuoteNode blockquoteNode);
    void render(ListItemNode listItemNode);
    void render(OrderedListNode orderedListNode);
    void render(UnorderedListNode unorderedListNode);
    void render(HorizontalLineNode horizontalLineNode);

    void render(TableNode tableNode);
    void render(TableHeaderNode tableHeaderNode);
    void render(TableBodyNode tableBodyNode);
    void render(TableRowNode tableRowNode);
    void render(TableHeaderCellNode tableHeaderCellNode);
    void render(TableBodyCellNode tableBodyCellNode);


    /*行内渲染*/
    void render(CodeNode codeNode);
    void render(ImageNode imageNode);
    void render(LinkNode linkNode);
    void render(DeleteLineNode deleteLineNode);
    void render(StrongNode strongNode);
    void render(ItalicNode italicNode);
    void render(HtmlInlineNode htmlInlineNode);
    void render(LineBreakNode lineBreakNode);

    void render(TextNode textNode);
}

