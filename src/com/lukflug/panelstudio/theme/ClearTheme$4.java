/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Rectangle;

class ClearTheme.4
implements IScrollBarRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    ClearTheme.4(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public int renderScrollBar(Context context, boolean focus, T state, boolean horizontal, int height, int position) {
        ClearTheme.this.renderBackground(context, focus, this.val$graphicalLevel);
        Color color = ITheme.combineColors(ClearTheme.this.scheme.getColor("Scroll Bar Color"), ClearTheme.this.getBackgroundColor(focus));
        if (horizontal) {
            int a = (int)((double)position / (double)height * (double)context.getSize().width);
            int b = (int)((double)(position + context.getSize().width) / (double)height * (double)context.getSize().width);
            context.getInterface().fillRect(new Rectangle(context.getPos().x + a + 1, context.getPos().y + 1, b - a - 2, 2), color, color, color, color);
            context.getInterface().drawRect(new Rectangle(context.getPos().x + a + 1, context.getPos().y + 1, b - a - 2, 2), color, color, color, color);
        } else {
            int a = (int)((double)position / (double)height * (double)context.getSize().height);
            int b = (int)((double)(position + context.getSize().height) / (double)height * (double)context.getSize().height);
            context.getInterface().fillRect(new Rectangle(context.getPos().x + 1, context.getPos().y + a + 1, 2, b - a - 2), color, color, color, color);
            context.getInterface().drawRect(new Rectangle(context.getPos().x + 1, context.getPos().y + a + 1, 2, b - a - 2), color, color, color, color);
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
