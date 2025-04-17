package org.ytymark;

import org.ytymark.parser.block.state.DefaultState;
import org.ytymark.parser.block.state.BlockStateContext;

import java.util.List;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：拆分块，解析 markdown 文本，将普通文本、代码块、列表块分割出来
 */
public class MarkBlockParser {

    /**
     * 拆分块，解析 markdown 文本，将普通文本、代码块、列表块分割出来
     */
    public List<String> splitBlock(String markdownText) {
        BlockStateContext context = new BlockStateContext();
        String[] lines = markdownText.split("\n");
        // 初始状态为默认状态
        context.setCurrentState(DefaultState.getInstance());

        for (String line : lines) {
            // 委托当前状态处理当前行
            context.getCurrentState().handleLine(line, context);
        }
        // 遇到文件末尾，对残留文本进行归档
        context.finishBlock();
        return context.getBlocks();
    }



    // 测试方法，演示整个拆分过程
    public static void main(String[] args) {
        String markdown = "这是一段文本\n" +
                "\n" +
                "1. 列表项一\n" +
                "2. 列表项二\n" +
                "\n" +
                "~~~java\n" +
                "System.out.println(\"Hello World\");\n" +
                "~~~\n" +
                "\n" +
                "另一段文本";
        MarkBlockParser parser = new MarkBlockParser();
        List<String> blocks = parser.splitBlock(markdown);
        for (String block : blocks) {
            System.out.println("----- Block Start -----");
            System.out.print(block);
            System.out.println("----- Block End -----\n");
        }
    }
}
