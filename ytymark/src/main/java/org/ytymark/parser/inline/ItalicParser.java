package org.ytymark.parser.inline;

import org.ytymark.node.inline.InlineNode;
import org.ytymark.node.inline.ItalicNode;
import org.ytymark.parser.ParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：斜体：*text* 或 _text_，TODO 目前中文不支持倾斜
 */
public class ItalicParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext inlineContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        line = line.substring(i);
        Matcher italicMatcher = Pattern.compile("(\\*|_)(.+?)\\1").matcher(line);
        if (italicMatcher.find()) {
            String text = italicMatcher.group(2);
            i = i + text.length() + 2;
            sourceLine.setIndex(i);
            ItalicNode italicNode = new ItalicNode(text);
            inlineContext.parser(italicNode.getText(),italicNode);
            return italicNode;
        }
        return null;
    }
}
