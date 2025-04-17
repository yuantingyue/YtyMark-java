package org.ytymark.node.block;

import org.ytymark.renderer.Renderer;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：围栏代码块
 */
public class FenceCodeBlockNode extends BlockNode {
    private String fenceCharacter;     // 围栏字符，如 '```' 或 '~~~'
    private String language;               // 代码语言（例如 'java'）
    private String code; // 具体的代码


    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public String getFenceCharacter() {
        return fenceCharacter;
    }

    public void setFenceCharacter(String fenceCharacter) {
        this.fenceCharacter = fenceCharacter;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
