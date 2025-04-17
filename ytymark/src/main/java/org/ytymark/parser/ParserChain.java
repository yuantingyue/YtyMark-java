package org.ytymark.parser;

import org.ytymark.node.Node;
/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：解析器链接口
 */
public interface ParserChain {
    // 责任链的入口
    void parser(String block, Node parent);

    void handleParser(String line, Node parent);
}
