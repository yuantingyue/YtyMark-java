package org.ytymark.parser.block.state;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：代码块状态：进入代码块后所有行均原样追加，直到遇到结束标识
 */
public class CodeBlockState  implements BlockState {
    private static final CodeBlockState INSTANCE = new CodeBlockState();
    public static CodeBlockState getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleLine(String line, BlockStateContext context) {
        context.getCurrentBlock().append(line).append("\n");
        String trimmed = line.trim();
        // 当行内容与开始时保存的标识符一致，则视为代码块结束
        if (trimmed.equals(context.getCodeBlockMarker())) {
            context.finishBlock();
            // 清理辅助数据，返回默认状态
            context.setCodeBlockMarker(null);
            context.setCurrentState(DefaultState.getInstance());
        }
    }
}