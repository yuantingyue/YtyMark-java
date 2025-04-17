package org.ytymark.node.block;

import org.ytymark.node.Node;
import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块节点抽象类
 */
public abstract class BlockNode extends Node {
    // 节点渲染行为
    public abstract void render(Renderer renderer);

}
