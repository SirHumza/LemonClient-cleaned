/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class RainbowTheme.9
implements IRadioRenderer {
    final /* synthetic */ int val$graphicalLevel;

    RainbowTheme.9(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderItem(Context context, ILabeled[] items, boolean focus, int target, double state, boolean horizontal) {
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, focus);
        }
        for (int i = 0; i < items.length; ++i) {
            Rectangle rect = this.getItemRect(context, items, i, horizontal);
            Context subContext = new Context(context.getInterface(), rect.width, rect.getLocation(), context.hasFocus(), context.onTop());
            subContext.setHeight(rect.height);
            if (i != target) {
                Color color = RainbowTheme.this.getBackgroundColor(focus);
                context.getInterface().fillRect(subContext.getRect(), color, color, color, color);
            }
            RainbowTheme.this.renderOverlay(subContext);
            context.getInterface().drawString(new Point(rect.x + RainbowTheme.this.padding, rect.y + RainbowTheme.this.padding), RainbowTheme.this.height, items[i].getDisplayName(), RainbowTheme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight(ILabeled[] items, boolean horizontal) {
        return (horizontal ? 1 : items.length) * RainbowTheme.this.getBaseHeight();
    }
}
