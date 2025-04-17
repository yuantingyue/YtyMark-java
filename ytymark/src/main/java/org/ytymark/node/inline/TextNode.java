package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：纯文本节点
 */
public class TextNode extends InlineNode{
    private String text;

    public TextNode(String text) {
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
