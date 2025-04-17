package org.ytymark.parser.block;

import org.ytymark.node.Node;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块级元素解析器接口
 */
public interface BlockParser {
    Node parser(String block);

}
