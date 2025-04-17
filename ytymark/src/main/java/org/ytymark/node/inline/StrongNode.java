package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：粗体：**text** 或 __text__
 */
public class StrongNode extends InlineNode{
    private String text;

    public StrongNode(String text) {
        this.text = text;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
