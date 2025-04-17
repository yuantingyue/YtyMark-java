package org.ytymark.parser.block.state;


import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块解析前的预处理
 *      保存解析过程中的共享数据以及当前状态
 */
public class BlockStateContext {
    private final List<String> blocks = new ArrayList<>();
    private final StringBuilder currentBlock = new StringBuilder();
    // 当前状态对象
    private BlockState currentState;
    // 代码块辅助数据：代码块开始标记（如 "```" 或 "~~~"）
    private String codeBlockMarker = null;
    // 列表块辅助数据：当前列表类型，"ordered" 或 "unordered"
    private String listBlockType = null;

    public List<String> getBlocks() {
        return blocks;
    }

    public StringBuilder getCurrentBlock() {
        return currentBlock;
    }

    public BlockState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BlockState state) {
        this.currentState = state;
    }

    public String getCodeBlockMarker() {
        return codeBlockMarker;
    }

    public void setCodeBlockMarker(String marker) {
        this.codeBlockMarker = marker;
    }

    public String getListBlockType() {
        return listBlockType;
    }

    public void setListBlockType(String listType) {
        this.listBlockType = listType;
    }

    /**
     * 结束当前 block，将当前内容存入 blocks 集合后清空 currentBlock
     */
    public void finishBlock() {
        if (currentBlock.length() > 0) {
            blocks.add(currentBlock.toString());
            currentBlock.setLength(0);
        }
    }
}
