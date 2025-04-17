package org.ytymark.parser.inline;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：保存行级元素解析过程的内容和坐标
 */
public class SourceLine {
    private String content;
    private Integer index;

    public SourceLine() {
    }

    public SourceLine(String content, Integer index) {
        this.content = content;
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}