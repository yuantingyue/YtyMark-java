package org.ytymark.node.inline;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：行内代码：`code`
 */
public class CodeNode extends InlineNode{
    private String code;

    public CodeNode(String code) {
        this.code = code;
    }
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
