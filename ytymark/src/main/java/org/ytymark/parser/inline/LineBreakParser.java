package org.ytymark.parser.inline;

import org.ytymark.node.inline.InlineNode;
import org.ytymark.node.inline.LineBreakNode;
import org.ytymark.parser.ParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：强制换行
 *     不允许嵌套其它语法
 */
public class LineBreakParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext inlineContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        line = line.substring(i);
        // 正则表达式匹配行末尾有两个或更多空格+换行符的行
        Matcher strongMatcher = Pattern.compile("[ ]{2,}\\n").matcher(line);
        if (strongMatcher.find()) {
            String text = strongMatcher.group();
            i = i + text.length();
            sourceLine.setIndex(i);
            LineBreakNode lineBreakNode = new LineBreakNode();
            return lineBreakNode;
        }
        return null;
    }

}
