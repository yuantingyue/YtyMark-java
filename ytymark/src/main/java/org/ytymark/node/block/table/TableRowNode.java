package org.ytymark.node.block.table;

import org.ytymark.node.block.BlockNode;
import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：用于表头或表体的行
 */
public class TableRowNode extends BlockNode {
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}