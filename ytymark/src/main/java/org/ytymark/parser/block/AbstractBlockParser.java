package org.ytymark.parser.block;


import org.ytymark.node.Node;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块级元素解析器抽象类
 */
public abstract class AbstractBlockParser implements BlockParser {
    public Node parser(String block){
        throw new RuntimeException("无法解析");
    }

}
