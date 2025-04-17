package org.ytymark.parser.block.state;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：BlockState 状态接口：所有状态都必须实现 handleLine 方法来处理传入的行
 */
public interface BlockState {
    void handleLine(String line, BlockStateContext context);
}
