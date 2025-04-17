package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：引用块
 */
public class BlockQuoteNode extends BlockNode {
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
