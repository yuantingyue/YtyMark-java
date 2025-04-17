package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.block.HeadingNode;
import org.ytymark.node.Node;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：标题解析器
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.HEADING)
public class HeadingParserHandler extends AbstractBlockParser implements ParserHandler {

    @Override
    public Node parser(String block){
        // 处理标题
        int level = 0;
        while (level<block.length() && block.charAt(level) == '#') {
            level++;
        }
        if(level<block.length()){
            char c = block.charAt(level);
            if(c == '\t' || c == ' '){
                String content = block.substring(level).trim();
                return new HeadingNode(level, content);
            }
        }
        return null;
    }
    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        String line = block.trim();
        if (line.startsWith("#")) {
            Node node = this.parser(line);
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
