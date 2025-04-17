package org.ytymark.node;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：迭代器接口
 */
public interface Iterator<T> {
    boolean hasNext();
    T next();
}
