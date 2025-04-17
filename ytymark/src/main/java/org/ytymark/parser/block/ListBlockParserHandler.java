package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.Node;
import org.ytymark.node.block.*;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：列表解析器
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.LISTBLOCK)
public class ListBlockParserHandler extends AbstractBlockParser implements ParserHandler {
    // 匹配列表项的正则表达式：
    // 1: 行前的空格（缩进）
    // 2: 数字列表标记（如 "1."）
    // 3: 无序列表标记（如 "-"、“*”、“+”）
    // 4: 列表项内容
    private static final Pattern LIST_PATTERN = Pattern.compile("^(\\s*)(?:(\\d+\\.)|([-+*]))\\s+(.*)$");
    private Node parent;

    @Override
    public Node parser(String block){
        // 用一个栈保持当前解析上下文，其中保存了每个层级的 Container（包括当前父节点和上次添加的列表节点引用）
        Deque<Container> stack = new ArrayDeque<>();
        // 初始时
        stack.push(new Container(-1, parent));

        // 按行分割处理 Markdown 文本
        String[] lines = block.split("\\r?\\n");
        for (String line : lines) {
            Matcher matcher = LIST_PATTERN.matcher(line);
            if (matcher.matches()) {
                String spaces = matcher.group(1);
                int indent = this.getIndentNum(spaces);
                String orderedMarker = matcher.group(2);
                String unorderedMarker = matcher.group(3);
                String content = matcher.group(4);
                boolean isOrdered = (orderedMarker != null);

                // 调整栈，当遇到缩进小于或等于当前栈顶 Container 时弹出栈
                while (!stack.isEmpty() && indent <= stack.peek().indent) {
                    stack.pop();
                }
                // 当前所在层级的父节点
                Container parentContainer = stack.peek();

                // 尝试沿用上一列表节点：如果上一列表节点的缩进与当前行一致，并且列表类型匹配，则沿用之
                ListNode currentList;
                if (parentContainer.lastList != null
                        && parentContainer.lastList.getIndent() == indent
                        && parentContainer.lastList.isOrdered() == isOrdered) {
                    currentList = parentContainer.lastList;
                } else {
                    // 新建列表节点
                    if (isOrdered) {
                        currentList = new OrderedListNode(indent);
                    } else {
                        currentList = new UnorderedListNode(indent);
                    }
                    parentContainer.node.addChildNode(currentList);
                    // 更新当前容器最后使用的列表节点
                    parentContainer.lastList = currentList;
                }
                // 为当前行创建列表项
                ListItemNode listItem = new ListItemNode(content, indent);
                currentList.addChildNode(listItem);
                // 为可能的嵌套列表项建立新容器，栈中保存当前列表项节点
                stack.push(new Container(indent, listItem));
            }
        }
        return parent;
    }

    private int getIndentNum(String spaces) {
        int indent =0;
        for (char c : spaces.toCharArray()) {
            indent += (c == '\t' ? 4 : 1);
        }
        return indent;
    }

    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        String[] lines = block.split("\\r?\\n");
        // 将请求传递给下一个处理器
        if(lines.length==0)
            parserChain.handleParser(block, parent);

        String line = lines[0];
        Matcher matcher = LIST_PATTERN.matcher(line);
        if(!matcher.matches())
            parserChain.handleParser(block, parent);

        this.parent = parent;
        Node node = this.parser(block);
        if(node==null) {
            parserChain.handleParser(block, parent);
        }
    }

    /**
     * 用于保存解析过程中上下文信息的辅助类（包括当前行的缩进、父节点，以及上一列表节点引用）。
     */
    private static class Container {
        public int indent;
        public Node node;
        // 如果当前 Container 的最后添加的节点是列表，则记录引用，以便后续相同层级的列表项沿用
        public ListNode lastList;

        public Container(int indent, Node node) {
            this.indent = indent;
            this.node = node;
        }
    }


}
