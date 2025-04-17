package org.ytymark.window.dialog;


import javafx.scene.Node;

/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：装饰器基类，封装了 GenericDialog 并委托常用操作。
 */
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
