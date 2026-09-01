/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import java.awt.Color;
import java.awt.Rectangle;

class GameSenseTheme.4
implements IScrollBarRenderer<T> {
    GameSenseTheme.4() {
    }

    @Override
    public int renderScrollBar(Context context, boolean focus, T state, boolean horizontal, int height, int position) {
        int a;
        Color activecolor = GameSenseTheme.this.getMainColor(focus, true);
        Color inactivecolor = GameSenseTheme.this.getMainColor(focus, false);
        if (horizontal) {
            a = (int)((double)position / (double)height * (double)context.getSize().width);
            int b = (int)((double)(position + context.getSize().width) / (double)height * (double)context.getSize().width);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, a, context.getSize().height), inactivecolor, inactivecolor, inactivecolor, inactivecolor);
            context.getInterface().fillRect(new Rectangle(context.getPos().x + a, context.getPos().y, b - a, context.getSize().height), activecolor, activecolor, activecolor, activecolor);
            context.getInterface().fillRect(new Rectangle(context.getPos().x + b, context.getPos().y, context.getSize().width - b, context.getSize().height), inactivecolor, inactivecolor, inactivecolor, inactivecolor);
        } else {
            a = (int)((double)position / (double)height * (double)context.getSize().height);
            int b = (int)((double)(position + context.getSize().height) / (double)height * (double)context.getSize().height);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, context.getSize().width, a), inactivecolor, inactivecolor, inactivecolor, inactivecolor);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + a, context.getSize().width, b - a), activecolor, activecolor, activecolor, activecolor);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + b, context.getSize().width, context.getSize().height - b), inactivecolor, inactivecolor, inactivecolor, inactivecolor);
        }
        Color bordercolor = GameSenseTheme.this.scheme.getColor("Outline Color");
        if (horizontal) {
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, context.getSize().width, 1), bordercolor, bordercolor, bordercolor, bordercolor);
        } else {
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, 1, context.getSize().height), bordercolor, bordercolor, bordercolor, bordercolor);
        }
        if (horizontal) {
            return (int)((double)((context.getInterface().getMouse().x - context.getPos().x) * height) / (double)context.getSize().width - (double)context.getSize().width / 2.0);
        }
        return (int)((double)((context.getInterface().getMouse().y - context.getPos().y) * height) / (double)context.getSize().height - (double)context.getSize().height / 2.0);
    }

    @Override
    public int getThickness() {
        return GameSenseTheme.this.scroll;
    }
}
