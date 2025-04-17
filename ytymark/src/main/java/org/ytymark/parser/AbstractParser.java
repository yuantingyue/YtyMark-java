package org.ytymark.parser;

import org.ytymark.node.Node;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：解析器抽象类
 */
public abstract class AbstractParser implements Parser{
    /**
     * 解析markdown 文本
     * @param markdownText 文本
     * @return 返回节点树
     */
    public abstract Node parse(String markdownText);

    /**
     * 对参数节点进行行内元素解析，并将解析结果接入到节点树
     * @param node 父节点
     */
    public abstract void parseInlines(Node node);
}
