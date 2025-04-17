package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.Node;
import org.ytymark.node.block.BlockQuoteNode;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：引用解析器
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.BLOCKQUOTE)
public class BlockQuoteParserHandler extends AbstractBlockParser implements ParserHandler {
    private ParserChain parserChain;

    @Override
    public Node parser(String block){
        String[] lines = block.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line.replaceFirst("^>\\s*", "")).append("\n");
        }
        BlockQuoteNode blockQuoteNode = new BlockQuoteNode();
        String innerContent = sb.toString().trim();
        // 递归解析引用内部的内容
        // 统一换行符，替换所有 \r\n 或 \r 为 \n
        String normalizedText = innerContent.replaceAll("\r\n|\r", "\n");
        // 使用正则表达式按空行分割（至少两个换行符）
        String[] blocks = normalizedText.split("\\n\\s*\\n");
        // 逐块处理文本
        for (String innerBlock : blocks) {
            parserChain.parser(innerBlock, blockQuoteNode);
        }
        return blockQuoteNode;
    }

    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        if (block.startsWith(">")) {
            this.parserChain = parserChain;
            Node node = this.parser(block);
            parent.addChildNode(node);
        } else{
            // 将请求传递给下一个处理器
            parserChain.handleParser(block, parent);
        }

    }
}
