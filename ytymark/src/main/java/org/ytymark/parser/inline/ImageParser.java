package org.ytymark.parser.inline;

import org.ytymark.node.inline.ImageNode;
import org.ytymark.node.inline.InlineNode;
import org.ytymark.parser.ParserContext;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：图片：![alt](url)
 *      alt 文本允许嵌套其它语法
 */
public class ImageParser implements InlineParser{

    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext inlineContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        i++;
        if(i>=line.length())
            return null;
        // '!' 后面第一个字符不是'['，则认为不是图片语法
        char c1 = line.charAt(i);
        if(c1 != '['){
            return null;
        }
        // 检查合法性，并获取到合适的右中括号下标值
        int end = new LinkParser().findMatching(line, i,'[',']');
        if (end == -1)
            return null;

        int openParen = end + 1;
        char c = line.charAt(openParen);
        if(c != '(')
            return null;
        // 中括号的内容
        String alt = line.substring(i + 1, end);

        // 检查合法性，并获取到合适的右小括号下标值
        int closeParen = new LinkParser().findMatching(line, end+1,'(',')');
        if (closeParen == -1) {
            return null;
        }

        String uri = line.substring(openParen + 1 , closeParen);
        ImageNode imageNode = new ImageNode(alt, uri);

        i = closeParen + 1;  // 跳过`)`字符
        sourceLine.setIndex(i);

        inlineContext.parser(imageNode.getAlt(),imageNode);
        return imageNode;
    }
}
