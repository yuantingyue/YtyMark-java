package org.ytymark.parser.inline;

import org.ytymark.node.inline.DeleteLineNode;
import org.ytymark.node.inline.InlineNode;
import org.ytymark.parser.ParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：删除线：~~text~~
 *     允许嵌套其它语法
 */
public class DeleteLineParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext parserContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        line = line.substring(i);
        Matcher deleteLineMatcher = Pattern.compile("~~(.+?)~~").matcher(line);
        if (deleteLineMatcher.find()) {
            String text = deleteLineMatcher.group(1);
            i = i + text.length() + 4;
            sourceLine.setIndex(i);
            DeleteLineNode deleteLineNode = new DeleteLineNode(text);
            parserContext.parser(deleteLineNode.getText(),deleteLineNode);
            return deleteLineNode;
        }
        return null;
    }
}
