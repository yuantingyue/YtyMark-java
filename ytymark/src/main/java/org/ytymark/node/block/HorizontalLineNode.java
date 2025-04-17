package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：水平分割线
 */
public class HorizontalLineNode extends BlockNode{

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
