package org.ytymark.annotation;


import org.ytymark.enums.BlockParserHandlerEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：快解析器处理注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)  // 只能用在类上
public @interface BlockParserHandlerType {
    // ParserHandlerEnum 类型包含：value 和 priority属性。两者二选一即可
    BlockParserHandlerEnum type() default BlockParserHandlerEnum.PARAGRAPH;  // 解析的类型

    String value() default "" ;  // 解析的类型
    int priority() default 0 ;   // 优先级，数值越大表示优先级越高
}
