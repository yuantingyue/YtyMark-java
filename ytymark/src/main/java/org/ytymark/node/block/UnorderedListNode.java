package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：无序列表节点
 */
public class UnorderedListNode extends ListNode {
    public UnorderedListNode(int indent) {
        super(indent);
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
