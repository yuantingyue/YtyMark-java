package org.ytymark.parser.inline;

import org.ytymark.node.inline.InlineNode;
import org.ytymark.parser.ParserContext;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行级元素解析
 */
public interface InlineParser {

    InlineNode parser(SourceLine sourceLine, ParserContext parserContext);
}
