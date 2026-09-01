/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.tabgui.ITabGUIRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

protected abstract class StandardTheme.RendererBase<T>
implements ITabGUIRenderer<T> {
    protected StandardTheme.RendererBase() {
    }

    @Override
    public void renderTab(Context context, int amount, double tabState) {
        Color color = StandardTheme.this.scheme.getColor("Selected Color");
        Color fill = StandardTheme.this.scheme.getColor("Background Color");
        Color border = StandardTheme.this.scheme.getColor("Outline Color");
        context.getInterface().fillRect(context.getRect(), fill, fill, fill, fill);
        context.getInterface().fillRect(this.getItemRect(context.getInterface(), context.getRect(), amount, tabState), color, color, color, color);
        context.getInterface().drawRect(this.getItemRect(context.getInterface(), context.getRect(), amount, tabState), border, border, border, border);
        context.getInterface().drawRect(context.getRect(), border, border, border, border);
    }

    @Override
    public void renderItem(Context context, int amount, double tabState, int index, String title, T itemState) {
        context.getInterface().drawString(new Point(context.getPos().x + StandardTheme.this.padding, context.getPos().y + context.getSize().height * index / amount + StandardTheme.this.padding), StandardTheme.this.height, title, this.getFontColor(itemState));
    }

    @Override
    public int getTabHeight(int amount) {
        return (StandardTheme.this.height + 2 * StandardTheme.this.padding) * amount;
    }

    @Override
    public Rectangle getItemRect(IInterface inter, Rectangle rect, int amount, double tabState) {
        return new Rectangle(rect.x, rect.y + (int)Math.round((double)rect.height * tabState / (double)amount), rect.width, StandardTheme.this.height + 2 * StandardTheme.this.padding);
    }

    protected abstract Color getFontColor(T var1);
}
