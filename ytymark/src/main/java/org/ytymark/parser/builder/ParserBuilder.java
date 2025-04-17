package org.ytymark.parser.builder;

import org.ytymark.parser.DocumentParser;
import org.ytymark.parser.Parser;
import org.ytymark.parser.ParserHandler;
import org.ytymark.parser.block.BlockParserContext;
import org.ytymark.parser.inline.InlineParser;
import org.ytymark.parser.inline.InlineParserContext;

import java.util.List;
import java.util.Map;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：解析器构建器
 */
public class ParserBuilder {
    private final BlockParserContext blockParserContext = new BlockParserContext();
    private final InlineParserContext inlineParserContext = new InlineParserContext();

    public static ParserBuilder builder(){
        return new ParserBuilder();
    }
    // 构建解析器
    public Parser build(){
        return new DocumentParser(blockParserContext,inlineParserContext);
    }

    // 加入自定义块级解析器
    // 块扩展除了扫描注解方式，还支持直接添加块解析类方式
    //   这种方式添加的默认排在解析器链的前面
    public ParserBuilder addBlockParser(ParserHandler parserHandler){
        blockParserContext.addBlockParser(parserHandler);
        return this;
    }
    public ParserBuilder addBlockParser(List<ParserHandler> parserHandlers){
        blockParserContext.addBlockParser(parserHandlers);
        return this;
    }

    // 加入自定义行内解析器
    // 自定义标识符
    public ParserBuilder addDelimiter(String singleDelimiter){
        inlineParserContext.addDelimiter(singleDelimiter);
        return this;
    }
    // 自定义解析器
    public ParserBuilder addInlineParser(String delimiter, InlineParser inlineParser){
        inlineParserContext.addInlineParser(delimiter, inlineParser);
        return this;
    }
    public ParserBuilder addInlineParser(Map<String,InlineParser> inlineParserMap){
        inlineParserContext.addInlineParser(inlineParserMap);
        return this;
    }

}
