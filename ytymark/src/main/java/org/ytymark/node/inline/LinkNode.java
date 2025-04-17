package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：链接：[text](uri)
 */
public class LinkNode extends InlineNode{
    private String text;
    private String uri;

    public LinkNode(String text, String uri) {
        this.text = text;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
