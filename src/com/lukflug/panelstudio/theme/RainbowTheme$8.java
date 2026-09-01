/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISliderRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class RainbowTheme.8
implements ISliderRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ int val$logicalLevel;

    RainbowTheme.8(boolean bl, int n, int n2) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$logicalLevel = n2;
    }

    @Override
    public void renderSlider(Context context, String title, String state, boolean focus, double value) {
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, effFocus);
        }
        int divider = (int)((double)context.getSize().width * value);
        Color color = RainbowTheme.this.getBackgroundColor(effFocus);
        context.getInterface().fillRect(new Rectangle(context.getPos().x + divider, context.getPos().y, context.getSize().width - divider, context.getSize().height), color, color, color, color);
        RainbowTheme.this.renderOverlay(context);
        String text = (this.val$logicalLevel >= 2 ? "> " : "") + title + RainbowTheme.this.separator + state;
        context.getInterface().drawString(new Point(context.getPos().x + RainbowTheme.this.padding, context.getPos().y + RainbowTheme.this.padding), RainbowTheme.this.height, text, RainbowTheme.this.getFontColor(effFocus));
    }

    @Override
    public int getDefaultHeight() {
        return RainbowTheme.this.getBaseHeight();
    }
}
