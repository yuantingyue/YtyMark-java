package org.ytymark.node.block.table;

import org.ytymark.node.block.BlockNode;
import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：表格体元素
 */
public class TableBodyNode extends BlockNode {
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}