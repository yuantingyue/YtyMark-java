package org.ytymark.node.block.table;

import org.ytymark.node.block.BlockNode;
import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：表体单元格
 */
public class TableBodyCellNode extends BlockNode {
    private String text;

    public TableBodyCellNode(String text) {
        this.text = text.trim();
    }
    public String getText() {
        return text;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}