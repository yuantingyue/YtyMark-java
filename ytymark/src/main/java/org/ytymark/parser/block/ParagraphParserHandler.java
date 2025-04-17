package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.block.ParagraphNode;
import org.ytymark.node.Node;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：段落解析器
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.PARAGRAPH)
public class ParagraphParserHandler extends AbstractBlockParser implements ParserHandler {

    @Override
    public Node parser(String block){
        ParagraphNode paragraphNode = new ParagraphNode(block);
        return paragraphNode;
    }

    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        if (block!=null && !block.isEmpty()) {
            Node node = this.parser(block);
            parent.addChildNode(node);
        } else{
            parserChain.handleParser(block, parent);  // 将请求传递给下一个处理器
        }

    }
}