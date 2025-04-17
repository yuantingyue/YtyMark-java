package org.ytymark.editor.theme.style;

import org.ytymark.editor.theme.enums.ThemeType;
import org.ytymark.editor.theme.enums.WindowType;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：样式工厂
 */
public class StyleFactory {
    private static final Map<Key, Style> STYLE_MAP = new HashMap<>();

    // 预先注册默认样式
    static {
        registerStyle(WindowType.MAIN_WINDOW, ThemeType.LIGHT, new MainWindowLightStyle());
        registerStyle(WindowType.MAIN_WINDOW, ThemeType.DARK, new MainWindowDarkStyle());
        registerStyle(WindowType.DIALOG_WINDOW, ThemeType.LIGHT, new DialogWindowLightStyle());
        registerStyle(WindowType.DIALOG_WINDOW, ThemeType.DARK, new DialogWindowDarkStyle());
    }

    public static void registerStyle(WindowType type, ThemeType theme, Style style) {
        STYLE_MAP.put(new Key(type, theme), style);
    }

    public static Style getStyle(WindowType type, ThemeType theme) {
        Style style = STYLE_MAP.get(new Key(type, theme));
        if (style == null) {
            throw new RuntimeException("窗口类型或主题类型不支持: " + type + " - " + theme);
        }
        return style;
    }

    // 用于作为 Map 的 Key
    private static class Key {
        private final WindowType type;
        private final ThemeType theme;

        public Key(WindowType type, ThemeType theme) {
            this.type = type;
            this.theme = theme;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Key key = (Key) obj;
            return type == key.type && theme == key.theme;
        }

        @Override
        public int hashCode() {
            return type.hashCode() * 31 + theme.hashCode();
        }
    }
}
