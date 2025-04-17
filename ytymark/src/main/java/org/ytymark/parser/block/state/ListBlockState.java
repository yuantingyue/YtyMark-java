package org.ytymark.parser.block.state;

import java.util.regex.Pattern;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：列表块状态：在同一列表类型内连续处理，如果遇到非列表项或列表类型变更，则结束当前列表块
 */
public class ListBlockState  implements BlockState {
    private static final ListBlockState INSTANCE = new ListBlockState();
    public static ListBlockState getInstance() {
        return INSTANCE;
    }
    // 与默认状态相同的列表匹配正则
    public static final Pattern ORDERED_LIST_PATTERN = Pattern.compile("^\\s*\\d+\\.\\s+.*");
    public static final Pattern UNORDERED_LIST_PATTERN = Pattern.compile("^\\s*([-+*])\\s+.*");
    public boolean continuousFlag=false;
    public int trimLineNum;

    @Override
    public void handleLine(String line, BlockStateContext context) {
        String trimmed = line.trim();
        // 列表内部空行直接略过（也可根据需要结束列表块）
        if (trimmed.isEmpty()) {
            trimLineNum++;
            // 连续两个以上的空行，识别为不同的列表块
            if(continuousFlag && trimLineNum>=2){
                trimLineNum=0;
                this.finishListBlock(line, context);
            }
            continuousFlag = true;
            return;
        }
        // 只有遇到非空行才重置
        continuousFlag = false;

        boolean isOrdered = ORDERED_LIST_PATTERN.matcher(line).matches();
        boolean isUnordered = UNORDERED_LIST_PATTERN.matcher(line).matches();
        // 根据当前行内容判断列表类型
        String lineListType = isOrdered ? "ordered" : (isUnordered ? "unordered" : null);

        // 如果当前行不匹配列表项，则说明列表块结束
        if (lineListType == null) {
            this.finishListBlock(line, context);
        } else {
            // 继续追加列表行
            context.getCurrentBlock().append(line).append("\n");
        }
    }

    // 结束列表块，返回默认状态，并让默认状态处理当前行（可能是普通文本或者新块入口）
    private void finishListBlock(String line, BlockStateContext context) {
        context.finishBlock();
        context.setListBlockType(null);
        context.setCurrentState(DefaultState.getInstance());
        context.getCurrentState().handleLine(line, context);
    }

}