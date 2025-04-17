package org.ytymark.node;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：节点迭代器
 *      规范和简化节点树的遍历操作，还可以将 Node 树的遍历过程抽象化，使得代码更加模块化、可复用。
 *      如果你需要进一步控制遍历过程，比如只遍历某些类型的子节点，可以在 NodeIterator 中增加额外的条件。
 */
public class NodeIterator implements Iterator<Node> {
    private Node currentNode;

    public NodeIterator(Node root) {
        // 初始化时，将当前节点设置为第一个子节点
        this.currentNode = root != null ? root.getFirstChild() : null;
    }

    @Override
    public boolean hasNext() {
        return currentNode != null;
    }

    @Override
    public Node next() {
        Node node = currentNode;
        // 递归遍历子节点
        currentNode = currentNode.getNext();
        return node;
    }

}