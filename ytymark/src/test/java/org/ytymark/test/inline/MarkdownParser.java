package org.ytymark.test.inline;

import org.ytymark.node.inline.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行级元素解析（仅仅测试，代码不完整的）
 */
public class MarkdownParser {

    public List<InlineNode> parseInline(String markdown) {
        List<InlineNode> nodes = new ArrayList<>();
        int i = 0;

        while (i < markdown.length()) {
            char c = markdown.charAt(i);

            if (c == '*' || c == '_') {
                // 处理斜体或粗体
                int start = i + 1;
                char delimiter = c;
                // 找到匹配的结束符
                i = findClosingDelimiter(markdown, i, delimiter);
                String content = markdown.substring(start, i);

                InlineNode node;
                if (delimiter == '*') {
                    node = new ItalicNode(content);
                } else {
                    node = new StrongNode(content);
                }
                nodes.add(node);
            }
            // 处理链接
            else if (c == '[') {
                // 检查合法性，并获取到合适的右中括号下标值
                int end = this.findMatching(markdown, i,'[',']');
                if (end == -1)
                    return null;

                int openParen = end + 1;
                char c1 = markdown.charAt(openParen);
                if(c1 != '(')
                    return null;
                // 中括号的内容
                String linkText = markdown.substring(i + 1, end);

                // 检查合法性，并获取到合适的右小括号下标值
                int closeParen = this.findMatching(markdown, end+1,'(',')');
                if (closeParen == -1) {
                    return null;
                }

                String uri = markdown.substring(openParen + 1 , closeParen);
                LinkNode linkNode = new LinkNode(linkText,uri);

                i = closeParen + 1;  // 跳过`)`字符
            }
            // 处理图片
            else if (c == '!') {
                int end = markdown.indexOf(']', i);
                String altText = markdown.substring(i + 2, end); // 跳过`![`
                i = markdown.indexOf('(', end) + 1;
                int closeParen = markdown.indexOf(')', i);
                String imageUrl = markdown.substring(i, closeParen);

                ImageNode imageNode = new ImageNode(altText, imageUrl);
                nodes.add(imageNode);
                i = closeParen + 1;  // 跳过`)`字符
            }
            // 其他行内元素或普通文本
            else {
                int start = i;
                while (i < markdown.length() && !isInlineDelimiter(markdown.charAt(i))) {
                    i++;
                }
                nodes.add(new TextNode(markdown.substring(start, i)));
            }
        }
        return nodes;
    }

    private boolean isInlineDelimiter(char c) {
        return c == '*' || c == '_' || c == '[' || c == ']' || c == '(' || c == ')'
                || c == '!' || c == '`'; // 包括图片的感叹号
    }

    private int findClosingDelimiter(String markdown, int start, char delimiter) {
        int i = start;
        while (i < markdown.length() && markdown.charAt(i) != delimiter) {
            i++;
        }
        return i;
    }

    // 解析链接时，处理嵌套的括号
    private int findMatching(String s, int start, char open, char close) {
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
