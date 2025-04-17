package org.ytymark.node.block;


import org.ytymark.renderer.Renderer;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：抽象的 ListNode，表示列表，包含列表所在的缩进信息。
 */
public abstract class ListNode extends BlockNode {
    protected int indent;

    public ListNode(int indent) {
        this.indent = indent;
    }

    public int getIndent() {
        return indent;
    }

    /** 当前列表是否为有序列表 */
    public abstract boolean isOrdered();

    @Override
    public abstract void render(Renderer renderer);
}
