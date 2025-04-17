package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：强制换行
 */
public class LineBreakNode extends InlineNode{
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
