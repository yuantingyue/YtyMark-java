package org.ytymark.parser.block;


import org.ytymark.node.Node;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;

import java.util.List;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块级元素解析器链
 */
public class BlockParserChain implements ParserChain {
    private int currentPosition = 0;
    private final List<ParserHandler> handlers;

    public BlockParserChain(List<ParserHandler> handlers) {
        this.handlers = handlers;
    }

    // 责任链的入口
    @Override
    public void parser(String block, Node parent) {
        // 进入责任链前，重置链信息
        currentPosition=0;
        this.handleParser(block, parent);
    }

    @Override
    public void handleParser(String block, Node parent) {
        if (currentPosition == handlers.size()) {
            currentPosition=0;
        }else{
            ParserHandler firstHandler = handlers.get(currentPosition++);
            firstHandler.handleParser(block, parent, this);
        }
    }

}
