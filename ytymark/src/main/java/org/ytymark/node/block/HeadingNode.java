package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：标题
 */
public class HeadingNode extends BlockNode{
    private int level;
    private String text;
    public HeadingNode() {
    }
    public HeadingNode(int level, String text){
        this.level = level;
        this.text = text;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
