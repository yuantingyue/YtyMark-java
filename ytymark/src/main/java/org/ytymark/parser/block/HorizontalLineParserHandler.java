package org.ytymark.parser.block;

import org.ytymark.annotation.BlockParserHandlerType;
import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.Node;
import org.ytymark.node.block.HorizontalLineNode;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：水平线解析器
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.HORIZONTALLINE)
public class HorizontalLineParserHandler extends AbstractBlockParser implements ParserHandler {

    @Override
    public Node parser(String block){
        // 处理标题
        int level = 0;
        while (level<block.length() && block.charAt(level) == '-') {
            level++;
        }
        if(level>=3){
            return new HorizontalLineNode();
        }
        return null;
    }

    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        if (block.startsWith("---")) {
            Node node = this.parser(block);
            if(node != null)
                parent.addChildNode(node);
            else
                parserChain.handleParser(block, parent);
        } else{
            // 将请求传递给下一个处理器
            parserChain.handleParser(block, parent);
        }

    }
}
