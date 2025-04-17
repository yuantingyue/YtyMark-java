package org.ytymark.parser.inline;

import org.ytymark.node.inline.InlineNode;
import org.ytymark.node.inline.StrongNode;
import org.ytymark.parser.ParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：粗体：**text** 或 __text__
 *      允许嵌套其它语法
 */
public class StrongParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext inlineContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        line = line.substring(i);
        Matcher strongMatcher = Pattern.compile("(\\*\\*|__)(.+?)\\1").matcher(line);
        if (strongMatcher.find()) {
            String text = strongMatcher.group(2);
            i = i + text.length() + 4;
            sourceLine.setIndex(i);
            StrongNode strongNode = new StrongNode(text);
            inlineContext.parser(strongNode.getText(),strongNode);
            return strongNode;
        }
        return null;
    }

}
