/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import java.awt.Color;
import java.awt.Rectangle;

class RainbowTheme.4
implements IScrollBarRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    RainbowTheme.4(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public int renderScrollBar(Context context, boolean focus, T state, boolean horizontal, int height, int position) {
        Color color = RainbowTheme.this.getBackgroundColor(focus);
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, focus);
        }
        if (horizontal) {
            int a = (int)((double)position / (double)height * (double)context.getSize().width);
            int b = (int)((double)(position + context.getSize().width) / (double)height * (double)context.getSize().width);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, a, context.getSize().height), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(context.getPos().x + b, context.getPos().y, context.getSize().width - b, context.getSize().height), color, color, color, color);
        } else {
            int a = (int)((double)position / (double)height * (double)context.getSize().height);
            int b = (int)((double)(position + context.getSize().height) / (double)height * (double)context.getSize().height);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, context.getSize().width, a), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + b, context.getSize().width, context.getSize().height - b), color, color, color, color);
        }
        if (horizontal) {
            return (int)((double)((context.getInterface().getMouse().x - context.getPos().x) * height) / (double)context.getSize().width - (double)context.getSize().width / 2.0);
        }
        return (int)((double)((context.getInterface().getMouse().y - context.getPos().y) * height) / (double)context.getSize().height - (double)context.getSize().height / 2.0);
    }

    @Override
    public int getThickness() {
        return 4;
    }
}
