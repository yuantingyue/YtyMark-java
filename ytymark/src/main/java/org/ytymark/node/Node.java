package org.ytymark.node;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：在 markdown文档中，所有内容都会封装成 Node 节点
 *      所有元素元素都继承 Node 抽象类
 */
public abstract class Node implements Aggregate<Node> {
    private Node parent;  // 父节点
    private Node firstChild;  // 第一个子节点
    private Node tailChild;  // 最后一个子节点
    private Node prev;  // 前一个兄弟节点
    private Node next;  // 下一个兄弟节点

    // 节点渲染行为：使用访问者模式扩展渲染行为
    public abstract void render(Renderer renderer);

    public Node getFirstChild() {
        return firstChild;
    }

    public Node getTailChild() {
        return tailChild;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }

    public Node getParent() {
        return parent;
    }

    protected void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 尾插法
     */
    public void addChildNode(Node child) {
        child.unlink();
        child.parent = this;  // 设置父节点
        if (this.tailChild != null) {
            // 如果已经有子节点，挂到最后一个子节点后面
            this.tailChild.next = child;
            child.prev = tailChild;
            this.tailChild = child;  // 更新最后一个子节点
        } else {
            // 如果没有子节点，直接成为第一个子节点
            this.firstChild = child;
            this.tailChild = child;
        }
    }


    public void unlink() {
        if (this.prev != null) {
            this.prev.next = this.next;
        } else if (this.parent != null) {
            this.parent.firstChild = this.next;
        }
        if (this.next != null) {
            this.next.prev = this.prev;
        } else if (this.parent != null) {
            this.parent.tailChild = this.prev;
        }
        this.parent = null;
        this.next = null;
        this.prev = null;
    }

    @Override
    public Iterator<Node> createIterator() {
        return new NodeIterator(this);
    }

}
