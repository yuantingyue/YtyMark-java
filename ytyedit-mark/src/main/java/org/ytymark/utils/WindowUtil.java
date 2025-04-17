package org.ytymark.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * 项目名称：ytyedit-mark
 * 作者：渊渟岳
 * 描述：自定义窗口标题栏必备工具：窗口拖动、放大缩小等支持
 */
public class WindowUtil {
    private static final int RESIZE_MARGIN = 5;
    private static boolean isMaximized = false;
    private static Rectangle2D backupBounds;

    /**
     * 拖动
     * @param stage 窗口
     * @param dragNode 拖动的节点位置
     */
    public static void addDragSupport(Stage stage, Node dragNode) {
        final Delta dragDelta = new Delta();

        dragNode.setOnMousePressed(event -> {
            dragDelta.x = event.getScreenX() - stage.getX();
            dragDelta.y = event.getScreenY() - stage.getY();
        });

        dragNode.setOnMouseDragged(event -> {
            if (!isMaximized) {
                stage.setX(event.getScreenX() - dragDelta.x);
                stage.setY(event.getScreenY() - dragDelta.y);
            }
        });

    }

    /**
     * 尺寸调整
     * @param stage 窗口
     * @param root 区域位置
     */
    public static void addResizeSupport(Stage stage, Region root) {
        final ResizeDelta resizeDelta = new ResizeDelta();

        // 记录初始状态
        root.setOnMousePressed(event -> {
            resizeDelta.mouseScreenX = event.getScreenX();
            resizeDelta.mouseScreenY = event.getScreenY();
            resizeDelta.stageX = stage.getX();
            resizeDelta.stageY = stage.getY();
            resizeDelta.stageWidth = stage.getWidth();
            resizeDelta.stageHeight = stage.getHeight();
        });

        // 根据当前鼠标位置更新光标样式
        root.setOnMouseMoved(event -> {
            event.consume();
            Cursor cursor = getCursorForPosition(stage, event);
            root.setCursor(cursor);
        });

        // 处理拖动逻辑
        root.setOnMouseDragged(event -> {
            Cursor cursor = root.getCursor();
            if (cursor != Cursor.DEFAULT) {
                double dx = event.getScreenX() - resizeDelta.mouseScreenX;
                double dy = event.getScreenY() - resizeDelta.mouseScreenY;

                // 处理左右拖动
                if (cursor == Cursor.W_RESIZE || cursor == Cursor.NW_RESIZE || cursor == Cursor.SW_RESIZE) {
                    double newWidth = resizeDelta.stageWidth - dx;
                    if(cursor == Cursor.W_RESIZE )
                        stage.setY(resizeDelta.stageY);// 定住上下
                    if (newWidth >= stage.getMinWidth()) {
                        stage.setX(resizeDelta.stageX + dx);
                        stage.setWidth(newWidth);
                    }else {
                        stage.setX(resizeDelta.stageX + (resizeDelta.stageWidth - stage.getMinWidth()));
                        stage.setWidth(stage.getMinWidth());
                    }
                } else if (cursor == Cursor.E_RESIZE || cursor == Cursor.NE_RESIZE || cursor == Cursor.SE_RESIZE) {
                    double newWidth = resizeDelta.stageWidth + dx;
                    if (newWidth >= stage.getMinWidth()) {
                        stage.setWidth(newWidth);
                    }
                }

                // 处理上下拖动
                if (cursor == Cursor.N_RESIZE || cursor == Cursor.NW_RESIZE || cursor == Cursor.NE_RESIZE) {
                    double newHeight = resizeDelta.stageHeight - dy;
                    if(cursor == Cursor.N_RESIZE)
                        stage.setX(resizeDelta.stageX);// 定住左右

                    if (newHeight >= stage.getMinHeight()) {
                        stage.setY(resizeDelta.stageY + dy);
                        stage.setHeight(newHeight);
                    } else {
                        stage.setY(resizeDelta.stageY + (resizeDelta.stageHeight - stage.getMinHeight()));
                        stage.setHeight(stage.getMinHeight());
                    }
                } else if (cursor == Cursor.S_RESIZE || cursor == Cursor.SW_RESIZE || cursor == Cursor.SE_RESIZE) {
                    double newHeight = resizeDelta.stageHeight + dy;
                    if (newHeight >= stage.getMinHeight()) {
                        stage.setHeight(newHeight);
                    }
                }
            }
        });
    }

    public static void setupControlButtons(Stage stage, Button minBtn, Button maxBtn, Button closeBtn) {
        minBtn.setOnAction(e -> stage.setIconified(true));
        maxBtn.setOnAction(e -> toggleMaximize(stage));
        closeBtn.setOnAction(e -> stage.close());
    }

    private static void toggleMaximize(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        if (isMaximized) {
            stage.setX(backupBounds.getMinX());
            stage.setY(backupBounds.getMinY());
            stage.setWidth(backupBounds.getWidth());
            stage.setHeight(backupBounds.getHeight());
        } else {
            backupBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
        isMaximized = !isMaximized;
    }

    /**
     * 鼠标区域位置识别
     * @param stage 窗口
     * @param e 鼠标事件
     * @return 位置
     */
    private static Cursor getCursorForPosition(Stage stage, MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        double w = stage.getWidth();
        double h = stage.getHeight();

        boolean left = x < RESIZE_MARGIN;
        boolean right = x > w - RESIZE_MARGIN;
        boolean top = y < RESIZE_MARGIN;
        boolean bottom = y > h - RESIZE_MARGIN;
        // 八个方位的检测，展示不同的鼠标样式
        if (left &&  top  ) return Cursor.NW_RESIZE;
        if (left && bottom) return Cursor.SW_RESIZE;
        if (right && top) return Cursor.NE_RESIZE;
        if (right && bottom) return Cursor.SE_RESIZE;
        if (left) return Cursor.W_RESIZE;
        if (right) return Cursor.E_RESIZE;
        if (top) return Cursor.N_RESIZE;
        if (bottom) return Cursor.S_RESIZE;

        return Cursor.DEFAULT;
    }

    // 用于拖动窗口的辅助类
    private static class Delta {
        double x, y;
    }

    // 用于记录缩放时的初始状态
    private static class ResizeDelta {
        double mouseScreenX, mouseScreenY;
        double stageX, stageY, stageWidth, stageHeight;
    }
}
