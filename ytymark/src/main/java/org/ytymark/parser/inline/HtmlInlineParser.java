package org.ytymark.parser.inline;

import org.ytymark.node.inline.HtmlInlineNode;
import org.ytymark.node.inline.InlineNode;
import org.ytymark.parser.ParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行内嵌的html标签
 *      不允许嵌套其它语法
 */
public class HtmlInlineParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext parserContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        line = line.substring(i);
        Matcher matcher = Pattern.compile("<([a-zA-Z][a-zA-Z0-9]*)[^>]*\\/?>|<\\/([a-zA-Z][a-zA-Z0-9]*)>").matcher(line);
        if (matcher.find()) {
            String tag = matcher.group(0);
            i = i + tag.length() ;
            sourceLine.setIndex(i);
            HtmlInlineNode htmlInlineNode = new HtmlInlineNode(tag);
            return htmlInlineNode;
        }
        return null;
    }

}
