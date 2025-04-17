package org.ytymark.parser.block;

import org.ytymark.annotation.BlockParserHandlerType;
import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.Node;
import org.ytymark.node.block.table.*;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：段落解析器
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.TABLE)
public class TableParserHandler extends AbstractBlockParser implements ParserHandler {


    @Override
    public Node parser(String block){
        List<String> lines = new ArrayList<>(Arrays.asList(block.split("\\r?\\n"))); // 创建一个新的 ArrayList

        String headerLine = lines.get(0);
        String[] headers = splitRow(headerLine);
        TableNode table = new TableNode();
        TableHeaderNode thead = new TableHeaderNode();
        TableBodyNode tbody = new TableBodyNode();
        table.addChildNode(thead);
        table.addChildNode(tbody);

        // 构造表头行节点并添加单元格
        TableRowNode headerRow = new TableRowNode();
        for (String h : headers) {
            headerRow.addChildNode(new TableHeaderCellNode(h));
        }
        thead.addChildNode(headerRow);

        // 忽略对齐线 lines[1]

        // 解析表体
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;
            String[] cells = this.splitRow(line);
            TableRowNode bodyRow = new TableRowNode();
            for (String c : cells) {
                bodyRow.addChildNode(new TableBodyCellNode(c));
            }
            tbody.addChildNode(bodyRow);
        }
        return table;
    }

    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        String[] lines = block.split("\\r?\\n");
        // 判断是否是table
        if (this.isTable(lines)) {
            // 解析
            Node node = this.parser(block);
            if(node!=null) {
                parent.addChildNode(node);
            }
            else {
                // 将请求传递给下一个处理器
                parserChain.handleParser(block, parent);
            }
        }else {
            // 将请求传递给下一个处理器
            parserChain.handleParser(block, parent);
        }
    }

    /**
     * 判断是否为表格块级元素
     * @param lines
     * @return
     */
    private boolean isTable(String[] lines) {
        int index = 0;
        if (index + 1 >= lines.length) return false;

        String line1 = lines[index].trim();
        String line2 = lines[index + 1].trim();

        // 必须至少有两行，并且第二行必须是分隔行
        if (!isSeparatorLine(line2)) return false;

        // 简单地要求第一行和第二行都包含 |
        return line1.contains("|") && line2.contains("|");
    }

    /**
     * 检测分割线
     */
    private boolean isSeparatorLine(String line) {
        // 匹配形如 |---|---|，或 ---|---，或 :--: 等
        String[] parts = line.split("\\|");
        for (String part : parts) {
            String trimmed = part.trim();
            // 跳过空项（首尾空字符串）
            if (trimmed.isEmpty()) continue;
            if (!trimmed.matches(":-{3,}:?|:-{3,}|-{3,}:?|-{3,}")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 行分割
     * @param line
     * @return
     */
    private String[] splitRow(String line) {
        line = line.trim();
        if (line.startsWith("|")) line = line.substring(1);
        if (line.endsWith("|")) line = line.substring(0, line.length() - 1);
        return line.split("\\|", -1);
    }

}
