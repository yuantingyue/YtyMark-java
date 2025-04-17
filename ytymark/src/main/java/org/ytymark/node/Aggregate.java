package org.ytymark.node;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：集合接口
 */
public interface Aggregate<T> {
    Iterator<T> createIterator();
}