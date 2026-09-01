/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISliderRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class ClearTheme.9
implements ISliderRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ClearTheme.9(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderSlider(Context context, String title, String state, boolean focus, double value) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        ClearTheme.this.renderBackground(context, effFocus, this.val$graphicalLevel);
        Color color = ClearTheme.this.getFontColor(effFocus);
        Color colorA = ClearTheme.this.getMainColor(effFocus, true);
        Rectangle rect = this.getSlideArea(context, title, state);
        int divider = (int)((double)rect.width * value);
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y, divider, rect.height), colorA, colorA, colorA, colorA);
        ClearTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + ClearTheme.this.padding, context.getPos().y + ClearTheme.this.padding), ClearTheme.this.height, title + ClearTheme.this.separator + state, color);
    }

    @Override
    public int getDefaultHeight() {
        return ClearTheme.this.getBaseHeight();
    }
}
