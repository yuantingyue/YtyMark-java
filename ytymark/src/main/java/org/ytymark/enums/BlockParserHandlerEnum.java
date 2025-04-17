package org.ytymark.enums;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块解析器处理枚举值
 */
public enum BlockParserHandlerEnum {
    PARAGRAPH("Paragraph",0),
    LISTBLOCK("ListBlock",1),
    FENCECODE("FenceCode",2),
    BLOCKQUOTE("BlockQuote",3),
    HEADING("Heading",4),
    HORIZONTALLINE("HorizontalLine",5),
    TABLE("TABLE",6),
    ;

    private final String value;
    private final int priority;


    BlockParserHandlerEnum(String value, int priority){
        this.value = value;
        this.priority = priority;
    }

    public String getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }
}
