/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import java.awt.Color;
import java.awt.Rectangle;

class Windows31Theme.4
implements IScrollBarRenderer<T> {
    Windows31Theme.4() {
    }

    @Override
    public int renderScrollBar(Context context, boolean focus, T state, boolean horizontal, int height, int position) {
        Color color = Windows31Theme.this.getBackgroundColor(focus);
        context.getInterface().fillRect(context.getRect(), color, color, color, color);
        int d = horizontal ? context.getSize().height : context.getSize().width;
        int x = context.getPos().x + (horizontal ? (int)((double)position / (double)(height - context.getSize().width) * (double)(context.getSize().width - 2 * d)) : 0);
        int y = context.getPos().y + (horizontal ? 0 : (int)((double)position / (double)(height - context.getSize().height) * (double)(context.getSize().height - 2 * d)));
        Rectangle rect = new Rectangle(x, y, d * (horizontal ? 2 : 1), d * (horizontal ? 1 : 2));
        Windows31Theme.this.drawButton(context.getInterface(), rect, focus, context.isClicked(0) && rect.contains(context.getInterface().getMouse()), true);
        if (horizontal) {
            return (int)Math.round((double)(context.getInterface().getMouse().x - context.getPos().x - d) / (double)(context.getSize().width - 2 * d) * (double)(height - context.getSize().width));
        }
        return (int)Math.round((double)(context.getInterface().getMouse().y - context.getPos().y - d) / (double)(context.getSize().height - 2 * d) * (double)(height - context.getSize().height));
    }

    @Override
    public int getThickness() {
        return Windows31Theme.this.scroll;
    }
}
