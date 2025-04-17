package org.ytymark.parser;

import org.ytymark.node.Node;
/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：解析处理器
 */
public interface ParserHandler {
    void handleParser(String block, Node parent, ParserChain parserChain);
}
