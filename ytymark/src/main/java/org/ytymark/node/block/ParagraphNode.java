package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：段落
 */
public class ParagraphNode extends BlockNode{
    private String text;

    public  ParagraphNode(){
        this("");
    }

    public ParagraphNode(String text){
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
