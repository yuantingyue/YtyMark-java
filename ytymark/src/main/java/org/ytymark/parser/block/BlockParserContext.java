package org.ytymark.parser.block;

import org.ytymark.enums.BlockParserHandlerEnum;
import org.ytymark.node.Node;
import org.ytymark.parser.ParserContext;
import org.ytymark.parser.ParserHandler;
import org.ytymark.annotation.BlockParserHandlerType;
import org.ytymark.parser.block.state.DefaultState;
import org.ytymark.parser.block.state.BlockStateContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：块解析上下文：
 *      支持扩展块解析器；
 *      多行块语法的解析问题：解析过程需要记录块开始和关闭状态；
 *      多行块语法的块嵌套问题：多行块是否支持块级嵌套，及块嵌套语法解析
 *      解析下标位置：在一行文本中可能存在多个块识别符，需要记录列光标移动的位置。
 */
public class BlockParserContext implements ParserContext {
    private final BlockParserChain blockParserChain;
    private List<ParserHandler> handlers;


    public BlockParserContext() {
        handlers = initBlockParserHandler();
        this.blockParserChain = new BlockParserChain(handlers);
    }

    public void parser(String text, Node node) {
        List<String> blocks = this.splitBlock(text);

        // 逐块处理文本
        for (String block : blocks) {
            blockParserChain.parser(block, node);
        }

    }

    // 块扩展除了扫描注解还支持直接添加块解析类方式
    // 这种方式添加的默认排在解析器链的前面
    public void addBlockParser(ParserHandler parserHandler){
        List<ParserHandler> parsers = new ArrayList<>();
        parsers.add(parserHandler);
        parsers.addAll(handlers);
        // 指向新的解析器链
        handlers = parsers;
    }
    public void addBlockParser(List<ParserHandler> parserHandlers){
        List<ParserHandler> parsers = new ArrayList<>();
        parsers.addAll(parserHandlers);
        parsers.addAll(handlers);
        // 指向新的解析器链
        handlers = parsers;
    }


    /**
     * 裁剪成块，解析 markdown 文本，将普通文本、代码块、列表块分割出来
     *  通过状态模式来实现
     */
    public List<String> splitBlock(String markdownText) {
        BlockStateContext context = new BlockStateContext();
        String[] lines = markdownText.split("\n");
        // 初始状态为默认状态
        context.setCurrentState(DefaultState.getInstance());

        for (String line : lines) {
            // 委托当前状态处理当前行
            context.getCurrentState().handleLine(line, context);
        }
        // 遇到文件末尾，对残留文本进行归档
        context.finishBlock();
        return context.getBlocks();
    }
//    /**
//     * 裁剪成块：仅供对比学习
//     *  如果不使用状态模式，那处理逻辑就是这个方法，可以对比下哪块代码更好调试和维护？
//     */
//    public List<String> splitBlock(String markdownText) {
//        String[] lines = markdownText.split("\n");
//        StringBuilder currentBlock = new StringBuilder();
//        List<String> blocks = new ArrayList<>();
//        // 是否进入了代码块
//        boolean inCodeBlock = false;
//        // 当前代码块使用的标识符（"```" 或 "~~~"），避免混淆
//        String currentCodeMarker = null;
//        boolean inListBlock = false;
//        // 当前列表类型：ordered/unordered
//        String currentListType = null;
//        // 定义列表正则表达式
//        Pattern orderedListPattern = Pattern.compile("^\\s*\\d+\\.\\s+.*");
//        Pattern unorderedListPattern = Pattern.compile("^\\s*([-+*])\\s+.*");
//
//        for (String line : lines) {
//            String trimmed = line.trim();
//            // 非代码块状态下检测是否为代码块开始，支持 ``` 或 ~~~
//            if (!inCodeBlock && (trimmed.startsWith("```") || trimmed.startsWith("~~~"))) {
//                // 如果当前存在列表块，先结束列表块
//                if (inListBlock) {
//                    blocks.add(currentBlock.toString());
//                    currentBlock.setLength(0);
//                    inListBlock = false;
//                    currentListType = null;
//                }
//                inCodeBlock = true;
//                currentCodeMarker = trimmed.substring(0, 3);
//                currentBlock.append(line).append("\n");
//                continue;
//            }
//            if(inCodeBlock){
//                // 代码块内部，不管是否为空行，都直接追加到 currentBlock 中
//                currentBlock.append(line).append("\n");
//                // 只有当行内容与起始标识符完全一致时，才视为代码块的结束
//                if (trimmed.equals(currentCodeMarker)) {
//                    blocks.add(currentBlock.toString());
//                    currentBlock.setLength(0);  // 清空当前块
//                    inCodeBlock = false;
//                    currentCodeMarker = null;
//                }
//                continue;
//            }
//
//            // 检查是否是列表项：优先有序，再无序。如果同时不符合，视为普通文本
//            boolean isOrderedItem = orderedListPattern.matcher(line).matches();
//            boolean isUnorderedItem = unorderedListPattern.matcher(line).matches();
//
//            if (isOrderedItem || isUnorderedItem) {
//                String listType = isOrderedItem ? "ordered" : "unordered";
//                // 如果当前已在列表块，并且列表类型发生变化，则先结束上一个列表块
//                if (inListBlock && !listType.equals(currentListType)) {
//                    blocks.add(currentBlock.toString());
//                    currentBlock.setLength(0);
//                    inListBlock = false;
//                    currentListType = null;
//                }
//                // 启动或延续列表块
//                if (!inListBlock) {
//                    // 如果当前存在普通文本内容，则先保存
//                    if (currentBlock.length() > 0) {
//                        blocks.add(currentBlock.toString());
//                        currentBlock.setLength(0);
//                    }
//                    inListBlock = true;
//                    currentListType = listType;
//                }
//                // 添加列表行
//                currentBlock.append(line).append("\n");
//            } else {
//                // 当前行不是列表项
//                if (inListBlock) {
//                    // 如果处于列表块状态，判断当前行是否为空
//                    if (trimmed.isEmpty()) {
//                        // 列表内部的空行直接忽略（不追加到块中）
//                        continue;
//                    } else {
//                        // 非空且不符合列表规则，则结束当前列表块
//                        blocks.add(currentBlock.toString());
//                        currentBlock.setLength(0);
//                        inListBlock = false;
//                        currentListType = null;
//                        // 当前行作为普通文本处理
//                        currentBlock.append(line).append("\n");
//                    }
//                } else {
//                    // 普通文本处理：空行表示块结束
//                    if (trimmed.isEmpty()) {
//                        if (currentBlock.length() > 0) {
//                            blocks.add(currentBlock.toString());
//                            currentBlock.setLength(0);
//                        }
//                        // 此处可根据需求决定是否保存空行作为单独的块
//                    } else {
//                        currentBlock.append(line).append("\n");
//                    }
//                }
//            }
//        }
//
//
//        // 添加最后残留的文本块
//        if (currentBlock.length() > 0) {
//            blocks.add(currentBlock.toString());
//        }
//
//        return blocks;
//    }

    /**
     * 扫描 org.ytymark.parser 包下带有ParserHandlerType 注解的解析器
     */
    public List<ParserHandler> initBlockParserHandler() {
        // 创建一个 HandlerRegistry 来扫描并注册所有处理器
        BlockParserHandlerRegistry registry;
        try {
            registry = new BlockParserHandlerRegistry("org.ytymark.parser");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("注册块解析器异常", e);
        }

        // 获取所有注册的处理器
        Map<String, ParserHandler> handlerMap = registry.getHandlerMap();

        // 按照优先级排序处理器
        List<ParserHandler> handlers = handlerMap.values().stream()
                .sorted((h1, h2) -> {
                    BlockParserHandlerType parserHandlerType1 = h1.getClass().getAnnotation(BlockParserHandlerType.class);
                    BlockParserHandlerType parserHandlerType2 = h2.getClass().getAnnotation(BlockParserHandlerType.class);
                    BlockParserHandlerEnum type1 = parserHandlerType1.type();
                    String value1 = parserHandlerType1.value();
                    int priority1 = parserHandlerType1.priority();
                    BlockParserHandlerEnum type2 = parserHandlerType2.type();
                    String value2 = parserHandlerType2.value();
                    int priority2 = parserHandlerType2.priority();
                    if("".equals(value1)){
                        priority1 = type1.getPriority();
                    }
                    if("".equals(value2)){
                        priority2 = type2.getPriority();
                    }
                    return Integer.compare(priority2, priority1);  // 优先级高的排在前面
                })
                .collect(Collectors.toList());
        return handlers;
    }
}
