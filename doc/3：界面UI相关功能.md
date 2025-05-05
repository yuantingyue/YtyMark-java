
## 说明文档目录

> [项目介绍](../README.md)
>
> [1：项目的整体总结](./1：项目的整体总结.md)
>
> [2：核心功能（解析和渲染）](./2：核心功能（解析和渲染）.md)
>
> [3：界面UI相关功能](./3：界面UI相关功能.md)

## 📌1. 简述

YtyMark-java项目分为两大模块：

- UI界面（ytyedit-mark）

- markdown文本解析和渲染（ytymark）

本文主要内容为**UI界面**相关功能。

关于markdown文本解析器UI界面的实现。在这整个流程中，如果通过设计模式实现高内聚低耦合，可重用，易于阅读，易于扩展，易于维护等。

### 1.1. ytyedit-mark模块结构

```markdown
YtyMark-java
├── ytyedit-mark/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── editor/            # JavaFX UI 界面
│   │   │   │   │   ├── about/           # 关于菜单相关功能
│   │   │   │   │   ├── bar/             # 滚动条和状态栏相关
│   │   │   │   │   ├── edit/            # 编辑菜单相关功能
│   │   │   │   │   ├── file/            # 文件菜单相关功能
│   │   │   │   │   ├── theme/           # 主题菜单相关功能
│   │   │   │   │   ├── TabFileInfo      # 自定义Tab用户信息
│   │   │   │   │   ├── TabManager       # 自定义Tab管理器
│   │   │   │   ├── enums/             # Icon图标等
│   │   │   │   ├── utils/             # 资源读取等
│   │   │   │   ├── window/            # 自定义窗口（主窗口、弹框）
│   │   │   │   ├── RenderMarkdown     # 解析和渲染
│   │   │   │   ├── YtyEditApplication # 主程序入口
│   │   │   └── resources/             
│   │   │       └── css/               # 主题样式（CSS）
│   │   │       └── fonts/             # 字体集
│   │   │       └── images/            # 图片
│   ├── README.md
│   └── pom.xml义Tab管理器
```

## 🧩2. 用户界面使用到的设计模式

**目标**：为用户提供可视化的文本输入、实时预览、编辑、保存、导出（PDF/HTML)和主题切换等功能。

**使用到的设计模式**：

- **工厂模式**：**样式的创建**通过工厂模式来完成。

- **策略模式**：动态选择工具界面样式，完成UI界面的**样式切换**。

- **观察者模式**：识别到主题发生变化时执行**重新渲染**操作；样式切换后，渲染的文字样式也需要同步调整，再结合监听器（观察者模式）来实现主题变化后重新渲染文本内容，除此之外JavaFX使用了大量的监听器。

- **单例模式**：主题样式管理器统一管理全局样式，并提供统一访问入口。

- **装饰模式**：对自定义基础弹框做**定制化的扩展**，实现不同场景所需的弹框。

- **命令模式**：封装工具界面中的功能点及快捷键命令。

- **备忘录模式**：负责实现**撤销**和**恢复**功能，实现精细到单字符的撤销/恢复机制。

### 2.1. 工厂模式

通过工厂创建样式，将默认的样式创建好，统一放入Map中保存，需要时直接从工厂中获取。

```java
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

    ...
}
```

### 2.2. 策略模式

动态选择工具界面样式，完成UI界面的**样式切换**。并支持对已有窗口样式的自由组合，比如深色的主窗口+浅色的弹框。

使用`setTheme(ThemeType)`方法可以快速切换已经搭配好的**主题**；使用`setStyle`可以灵活指定不同窗口的样式。

```java
public class ThemeContext {
    ...

    // 设置样式
    public void setTheme(ThemeType theme) {
        WindowType[] values = WindowType.values();
        for (WindowType windowType : values) {
            this.themeManager.setStyle(windowType, theme);
        }
    }

    // 自定义设置不同窗体不同样式
    public void setStyle(WindowType type, ThemeType theme) {
        this.themeManager.setStyle(type, theme);
    }

    // 主题切换
    public void switchTheme() {
        // 清空之前的样式
        scene.getStylesheets().clear();
        this.themeManager.applyStyle(WindowType.MAIN_WINDOW, scene);
    }

}
```

### 2.3. 观察者模式和单例模式

识别到主题发生变化时执行**重新渲染**操作。样式切换后，渲染的文字样式也需要同步调整，结合监听器（观察者模式）来实现主题变化后重新渲染文本内容，除此之外JavaFX使用了大量的监听器。

自定义的**主题监听器**接口：

```java
public interface ThemeChangeListener {
    void onThemeChanged();
}
```

主题监听器将有**主题管理类**来统一管理，并结合**单例模式**，使得主题管理器全局唯一，并提供统一访问入口`ThemeManager.getInstance()`。

```java
public class ThemeManager {

    private static ThemeManager instance;
    private final Map<WindowType, Style> currentStyles = new HashMap<>();
    private final List<ThemeChangeListener> listeners = new ArrayList<>();


    private ThemeManager() {
        // 默认主窗口白天模式，弹窗白色模式
        currentStyles.put(WindowType.MAIN_WINDOW, new MainWindowLightStyle());
        currentStyles.put(WindowType.DIALOG_WINDOW, new DialogWindowLightStyle());
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void setStyle(WindowType type, ThemeType theme) {
        currentStyles.put(type, StyleFactory.getStyle(type, theme));
        // 通知所有观察者主题已变更
        this.notifyThemeChanged();
    }

    ...

    // 注册观察者
    public void addThemeChangeListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }
    // 注销观察者
    public void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    // 通知所有观察者
    private void notifyThemeChanged() {
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChanged();
        }
    }
}
```

**通用弹框**和**渲染处理类**实现了监听器接口，在创建时注册到监听器中

```java
public class GenericDialog implements ThemeChangeListener {

    public GenericDialog(Stage owner) {
        ...

        this.themeManager.addThemeChangeListener(this);

        ...
    }

    /**
     * 订阅主题样式变更，主题变更后自动切换弹窗样式
     */
    @Override
    public void onThemeChanged() {
        // 清空之前的样式
        dialogScene.getStylesheets().clear();
        themeManager.applyStyle(WindowType.DIALOG_WINDOW,dialogScene);
    }
}
```

渲染处理类监听相关源码

```java
public class RenderMarkdown  implements ThemeChangeListener {
    public RenderMarkdown(Tab tab) {
        ...

        // 注册自己为 ThemeContext 的监听者
        ThemeManager.getInstance().addThemeChangeListener(this);

        ...
    }
    /**
     * 订阅主题样式变更，主题变更后自动重新渲染内容
     */
    @Override
    public void onThemeChanged() {
        // 主题变更后自动渲染
        renderMarkdown(null);
    }
}
```

### 2.4. 装饰模式

对自定义基础弹框做**定制化的扩展**，实现不同场景所需的弹框。弹框装饰类通过组合的方式，对通用弹框做定制化设置

```java
public abstract class DialogDecorator {
    private GenericDialog dialog;

    public DialogDecorator(GenericDialog dialog) {
        this.dialog = dialog;
    }

    public void removeLogo(boolean flag){
        dialog.removeLogo(flag);
    }
    // 委托设置标题
    public void setTitle(String title) {
        dialog.setTitle(title);
    }

    // 委托设置主体内容
    public void setContent(Node content) {
        dialog.setContent(content);
    }

    // 委托添加内容
    public void addContent(Node node) {
        dialog.addContent(node);
    }

    // 委托设置底部按钮区域
    public void setFooter(Node node) {
        dialog.setFooter(node);
    }

    // 委托添加底部按钮
    public void addFooter(Node node) {
        dialog.addFooter(node);
    }

    // 显示并阻塞，返回用户操作结果
    public void showAndWait() {
        dialog.showAndWait();
    }

}
```

### 2.5. 命令模式和备忘录模式

封装工具界面中的功能点及快捷键命令。备忘录模式负责实现**撤销**和**恢复**功能，实现精细到单字符的撤销/恢复机制，主要涉及的类：管理文本状态`TextEditorOriginator`、管理撤销和恢复的栈`UndoRedoCaretaker`。

```java
public class UndoRedoCaretaker {

    private Deque<TextMemento> undoStack = new ArrayDeque<>();
    private Deque<TextMemento> redoStack = new ArrayDeque<>();
    private TextEditorOriginator originator;
    private boolean isUndoRedo = false;
    private static final int MAX_HISTORY = 1000; // 最多保存 1000 步

    public UndoRedoCaretaker(TextEditorOriginator originator) {
        this.originator = originator;
        // 存入初始状态，确保撤销可用
        undoStack.push(originator.save());
    }

    // 保存
    public void saveState(String text, int caretPosition) {
       ...
    }

    // 撤销
    public void undo() {
        ...
    }
    // 恢复
    public void redo() {
        ...
    }
    public boolean isUndoRedo() {
        return isUndoRedo;
    }
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

}
```

## 📸 3. 界面截图预览

### 白天模式的截图：

![](../image/白天模式.jpg)

### 夜间模式的截图：

![](../image/黑夜模式.jpg)

## ✏️4. 总结

YtyMark 编辑器界面UI相关功能，将多种设计模式融入到实际应用中，从实践中积累程序设计经验。
