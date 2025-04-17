package org.ytymark.parser;

import org.ytymark.node.Node;
/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：解析器上下文
 */
public interface ParserContext {
    void parser(String line, Node node);
}
