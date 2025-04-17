package org.ytymark.parser;

import org.ytymark.node.Iterator;
import org.ytymark.node.Node;
import org.ytymark.node.block.DocumentNode;
import org.ytymark.node.block.HeadingNode;
import org.ytymark.node.block.ParagraphNode;
import org.ytymark.parser.block.BlockParserContext;
import org.ytymark.parser.inline.InlineParserContext;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：文档解析器，解析的入口
 */
public class DocumentParser extends AbstractParser{
    private final BlockParserContext blockParserContext;
    private final InlineParserContext inlineParserContext;


    public DocumentParser(BlockParserContext blockParserContext, InlineParserContext inlineParserContext) {
        // 块级元素解析链上下文初始化
        this.blockParserContext = blockParserContext;
        // 行解析
        this.inlineParserContext = inlineParserContext;
    }

    /**
     * 按块解析，返回 AST 节点树
     * @param markdownText
     * @return Node 节点树
     */
    @Override
    public Node parse(String markdownText) {
        Node root = new DocumentNode();
        // 统一换行符，替换所有 \r\n 或 \r 为 \n
        String normalizedText = markdownText.replaceAll("\r\n|\r", "\n");

        // 块级元素解析
        blockParserContext.parser(normalizedText, root);

        // 行级元素解析
        this.parseInlines(root);

        return root;
    }

    /**
     * 行级元素解析
     * @param parent 父节点
     */
    @Override
    public void parseInlines(Node parent) {
        Iterator<Node> iterator = parent.createIterator();
        while (iterator.hasNext()) {
            // 获取下一个兄弟节点
            Node node = iterator.next();
            // 解析子节点行
            if(node instanceof ParagraphNode){
                inlineParserContext.parser(((ParagraphNode) node).getText(),node);
            }
            if(node instanceof HeadingNode) {
                inlineParserContext.parser(((HeadingNode) node).getText(), node);
            }

            if(node.getFirstChild()!=null)
                parseInlines(node);
        }
    }


}
