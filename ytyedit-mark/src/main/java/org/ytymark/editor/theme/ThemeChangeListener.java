package org.ytymark.editor.theme;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：样式变化监听器，
 *      比如：样式变更后更新窗口样式、自动重新渲染文本内容样式、弹窗样式自动切换
 */
public interface ThemeChangeListener {
    void onThemeChanged();
}
