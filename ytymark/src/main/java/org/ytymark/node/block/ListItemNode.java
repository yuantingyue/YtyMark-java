package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：列表项
 */
public class ListItemNode extends BlockNode {
    private String content;
    private int indent;

    public ListItemNode() {
    }

    public ListItemNode(String content, int indent) {
        this.content = content;
        this.indent = indent;
    }

    public String getContent() {
        return content;
    }

    public int getIndent() {
        return indent;
    }
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
