package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：HTML标签
 */
public class HtmlInlineNode extends InlineNode{
    private String tag;

    public HtmlInlineNode(String tag) {
        this.tag = tag;
    }
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
