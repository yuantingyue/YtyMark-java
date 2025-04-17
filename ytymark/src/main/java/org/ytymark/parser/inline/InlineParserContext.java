package org.ytymark.parser.inline;

import org.ytymark.node.Node;
import org.ytymark.node.inline.*;
import org.ytymark.parser.ParserContext;

import java.util.*;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行级元素解析上下文
 */
public class InlineParserContext implements ParserContext {
    // 语法的标识符
    private final Set<String>  delimiterSet = new HashSet<>();
    // 解析器映射：不同字符组合对应不同的解析器
    private final Map<String, InlineParser> inlineParserMap = new HashMap<>();

    public InlineParserContext() {
        // 注册各种解析器
        inlineParserMap.put("__", new StrongParser()); // 粗体解析器
        inlineParserMap.put("**", new StrongParser()); // 粗体解析器
        inlineParserMap.put("*", new ItalicParser());  // 斜体解析器
        inlineParserMap.put("_", new ItalicParser());  // 斜体解析器
        inlineParserMap.put("`", new CodeParser());    // 代码解析器
        inlineParserMap.put("[", new LinkParser());    // 链接解析器
        inlineParserMap.put("!", new ImageParser());   // 图片解析器
        inlineParserMap.put("~~", new DeleteLineParser());   // 删除线解析器
        inlineParserMap.put("<", new HtmlInlineParser());   // html 标签
        inlineParserMap.put("  ", new LineBreakParser());   // html 标签
        delimiterSet.add(" ");
        delimiterSet.add("~");
        delimiterSet.addAll(inlineParserMap.keySet());
    }
    // 自定义标识符
    public void addDelimiter(String singleDelimiter){
        delimiterSet.add(singleDelimiter);
    }
    // 自定义解析器
    public void addInlineParser(String delimiter, InlineParser inlineParser){
        this.inlineParserMap.put(delimiter, inlineParser);
    }
    public void addInlineParser(Map<String,InlineParser> inlineParserMap){
        this.inlineParserMap.putAll(inlineParserMap);
    }


    public void parser(String line, Node node) {
        if(line == null || node == null)
            return;
        int i = 0;

        SourceLine sourceLine = new SourceLine(line, i);
        while (i < line.length()) {
            char c;
            sourceLine.setIndex(i);
            // 检查字符对或单个字符，选择对应的解析器
            String possibleDelimiter = this.getPossibleDelimiter(line, i);
            InlineParser inlineParser = inlineParserMap.get(possibleDelimiter);

            if (inlineParser != null) {
                // 找到合适地解析器，尝试解析
                InlineNode inlineNode = inlineParser.parser(sourceLine, this);
                if(inlineNode!=null){
                    node.addChildNode(inlineNode);
                }
            }
            if(i==sourceLine.getIndex()){
                int start = i;
                do{
                    i++;
                    if(i == line.length())
                        break;
                    c = line.charAt(i);
                }while (!delimiterSet.contains(String.valueOf(c)));
                TextNode textNode = new TextNode(line.substring(start, i));
                node.addChildNode(textNode);
            }else {
                i = sourceLine.getIndex();
            }

        }

    }

    // 获取可能的分隔符（例如，** 或 * 等）
    private String getPossibleDelimiter(String line, int index) {
        if (index + 1 < line.length() && line.charAt(index) == line.charAt(index + 1)) {
            return String.valueOf(line.charAt(index)) + line.charAt(index + 1); // 例如，** 或 __
        }
        return String.valueOf(line.charAt(index)); // 单个字符处理
    }
}
