package org.ytymark.parser.inline;

import org.ytymark.node.inline.InlineNode;
import org.ytymark.node.inline.LinkNode;
import org.ytymark.parser.ParserContext;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：链接：[text](url)
 *     text 文本允许嵌套其它语法
 */
public class LinkParser implements InlineParser{
    @Override
    public InlineNode parser(SourceLine sourceLine, ParserContext inlineContext) {
        String line = sourceLine.getContent();
        Integer i = sourceLine.getIndex();
        // 检查合法性，并获取到合适的右中括号下标值
        int end = this.findMatching(line, i,'[',']');
        if (end == -1)
            return null;

        int openParen = end + 1;
        char c = line.charAt(openParen);
        if(c != '(')
            return null;
        // 中括号的内容
        String linkText = line.substring(i + 1, end);

        // 检查合法性，并获取到合适的右小括号下标值
        int closeParen = this.findMatching(line, end+1,'(',')');
        if (closeParen == -1) {
            return null;
        }

        String uri = line.substring(openParen + 1 , closeParen);
        LinkNode linkNode = new LinkNode(linkText,uri);

        i = closeParen + 1;  // 跳过`)`字符
        sourceLine.setIndex(i);
        inlineContext.parser(linkNode.getText(),linkNode);
        return linkNode;
    }


    /**
     * 查找匹配括号的位置--解析处理嵌套的括号
     *      实现的原来是：通过入栈出栈，最终栈的深度为0，则为合法语法
     * @param s 输入字符串
     * @param start 开始位置，应指向开括号
     * @param open 开括号字符，例如 '(' 或 '['
     * @param close 闭括号字符，例如 ')' 或 ']'
     * @return 成功时返回闭括号的位置索引，否则返回 -1
     */
    public int findMatching(String s, int start, char open, char close) {
        // 检测首字符
        if (s.charAt(start) != open) return -1;
        // 首字符已检测，为符号条件：则栈的深度为1
        int depth = 1;
        // 首字符已检测，从下一个字符继续检测
        int i = start + 1;
        int n = s.length();
        while (i < n && depth > 0) {
            char c = s.charAt(i);
            if (c == open) depth++; //如果等于 open 符号，栈的深度加1
            else if (c == close) depth--;//如果等于 close 符号，栈的深度减1
            i++;
        }
        // 栈的深度最终为 0，则open 和 close 为可合并的
        return (depth == 0 ? i - 1 : -1);
    }
}
