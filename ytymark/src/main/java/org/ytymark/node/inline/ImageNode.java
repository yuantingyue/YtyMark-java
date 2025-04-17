package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：图片：![alt](uri)
 */
public class ImageNode extends InlineNode{
    private String alt;
    private String uri;

    public ImageNode(String alt, String uri) {
        this.alt = alt;
        this.uri = uri;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
