package org.ytymark.parser.block.state;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：默认状态：处理普通文本，同时检测是否需要进入代码块或列表块
 */
public class DefaultState implements BlockState {

    // 单例模式
    private static final DefaultState INSTANCE = new DefaultState();
    public static DefaultState getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleLine(String line, BlockStateContext context) {
        String trimmed = line.trim();

        // 检测代码块入口：以 ``` 或 ~~~ 开头的行
        if (trimmed.startsWith("```") || trimmed.startsWith("~~~")) {
            // 如果之前处于列表块内，则先结束列表块
            context.finishBlock();
            // 保存代码块标记，切换到代码块状态
            context.setCodeBlockMarker(trimmed.substring(0, 3));
            context.getCurrentBlock().append(line).append("\n");
            context.setCurrentState(CodeBlockState.getInstance());
            return;
        }

        // 检查是否为列表项，有序列表优先判断
        boolean isOrdered = ListBlockState.ORDERED_LIST_PATTERN.matcher(line).matches();
        boolean isUnordered = ListBlockState.UNORDERED_LIST_PATTERN.matcher(line).matches();
        if (isOrdered || isUnordered) {
            // 如果当前存在普通文本内容，则先结束该块
            context.finishBlock();
            String listType = isOrdered ? "ordered" : "unordered";
            context.setListBlockType(listType);
            // 切换到列表块状态，并让该状态处理当前行
            context.setCurrentState(ListBlockState.getInstance());
            context.getCurrentState().handleLine(line, context);
            return;
        }

        // 默认处理普通文本：空行则结束当前块，否则追加内容
        if (trimmed.isEmpty()) {
            context.finishBlock();
        } else {
            context.getCurrentBlock().append(line).append("\n");
        }
    }
}
