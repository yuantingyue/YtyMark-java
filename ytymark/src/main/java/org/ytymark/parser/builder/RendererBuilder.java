package org.ytymark.parser.builder;

import org.ytymark.renderer.Renderer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：预留扩展用于复杂对象的创建
 */
public class RendererBuilder {

    //
    public static RendererBuilder builder(){
        return new RendererBuilder();
    }
    // 构建解析器
    public Renderer build(Class<? extends Renderer> clazz) {
        // 获取无参构造器并创建对象
        Constructor<?> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
            return (Renderer) constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
