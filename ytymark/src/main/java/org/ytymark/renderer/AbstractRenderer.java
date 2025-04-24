package org.ytymark.renderer;

import org.ytymark.node.Iterator;
import org.ytymark.node.Node;
import org.ytymark.node.block.DocumentNode;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：渲染器抽象类
 */
public abstract class AbstractRenderer implements Renderer {

    @Override
    public void render(Node node) {
        renderChildren(node);
    }
    @Override
    public void render(DocumentNode document) {
        renderChildren(document);
    }


    /**
     * 循环渲染兄弟节点
     *    在实现这个抽象类的渲染器中，如果存在子节点行为就需要调用这个方法实现递归遍历子节点
     */
    protected void renderChildren(Node parent) {
        Iterator<Node> iterator = parent.createIterator();
        while (iterator.hasNext()) {
            // 获取下一个兄弟节点
            Node next = iterator.next();
            // 渲染节点
            next.render(this);
        }
    }

    public abstract String processRender(Node node);
}
