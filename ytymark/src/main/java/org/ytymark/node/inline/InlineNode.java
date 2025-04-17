package org.ytymark.node.inline;

import org.ytymark.node.Node;
import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行内元素节点
 */
public abstract class InlineNode extends Node {

    /**
     * 节点渲染行为
     */
    public abstract void render(Renderer renderer);


}
