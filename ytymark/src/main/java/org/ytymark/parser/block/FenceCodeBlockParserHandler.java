package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.Node;
import org.ytymark.node.block.FenceCodeBlockNode;
import org.ytymark.parser.ParserChain;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：围栏代码块
 */
@BlockParserHandlerType(type = BlockParserHandlerEnum.FENCECODE)
public class FenceCodeBlockParserHandler extends AbstractBlockParser implements ParserHandler {

    @Override
    public Node parser(String block){
        String[] lines = block.split("\\r?\\n");
        if (block.startsWith("```") ) {
            FenceCodeBlockNode fenceCodeBlock  = this.getNode(lines, "```");
            return fenceCodeBlock;
        }else if (block.startsWith("~~~")) {
            // 标识符“~~~”开头的
            FenceCodeBlockNode fenceCodeBlock = this.getNode(lines, "~~~");
            return fenceCodeBlock;
        }else {
            throw new RuntimeException("代码块元素解析异常");
        }
    }

    @Override
    public void handleParser(String block, Node parent, ParserChain parserChain) {
        block = block.trim();
        if (block.startsWith("```") || block.startsWith("~~~")) {
            Node node = this.parser(block);
            parent.addChildNode(node);
        }else{
            // 将请求传递给下一个处理器
            parserChain.handleParser(block, parent);
        }
    }

    /**
     * 解析代码块
     */
    private FenceCodeBlockNode getNode(String[] lines,String identifier) {
        FenceCodeBlockNode fenceCodeBlock = new FenceCodeBlockNode();
        String firstLine = lines[0];
        // 处理标识
        String language = firstLine.substring(3).trim();
        fenceCodeBlock.setFenceCharacter(identifier);
        fenceCodeBlock.setLanguage(language);

        StringBuilder codeBuilder = new StringBuilder();
        int lastIndex = lines.length - 1;
        String endSymbol = lines[lastIndex].trim();
        if (endSymbol.startsWith("```") || endSymbol.startsWith("~~~")) {
            for (int i = 1; i < lastIndex; i++) {
                String line = lines[i];
                String newLine = this.escapeHtml(line);
                codeBuilder.append(newLine);
                if (i < lastIndex - 1) {
                    codeBuilder.append("\n");
                }
            }
        } else {
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                String newLine = this.escapeHtml(line);
                codeBuilder.append(newLine);
                if (i < lines.length - 1) {
                    codeBuilder.append("\n");
                }
            }
        }
        fenceCodeBlock.setCode(codeBuilder.toString());
        return fenceCodeBlock;
    }


    private String escapeHtml(String text) {
        return text.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }
}
