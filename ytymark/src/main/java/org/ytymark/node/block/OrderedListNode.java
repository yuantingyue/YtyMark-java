package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;
/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：有序列表
 */
public class OrderedListNode extends ListNode {
    public OrderedListNode(int indent) {
        super(indent);
    }

    @Override
    public boolean isOrdered() {
        return true;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
