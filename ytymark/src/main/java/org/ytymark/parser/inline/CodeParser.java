package org.ytymark.parser.inline;

import org.ytymark.node.inline.CodeNode;
import org.ytymark.node.inline.InlineNode;
import org.ytymark.parser.ParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行内代码：`code`
 *      不允许嵌套其它语法
 */
public class CodeParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext parserContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        line = line.substring(i);
        Matcher codeMatcher = Pattern.compile("`([^`]+)`").matcher(line);
        if (codeMatcher.find()) {
            String text = codeMatcher.group(1);
            i = i + text.length() + 2;
            sourceLine.setIndex(i);
            CodeNode codeNode = new CodeNode(text);
            return codeNode;
        }
        return null;
    }
}
