/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISliderRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class GameSenseTheme.8
implements ISliderRenderer {
    final /* synthetic */ boolean val$container;

    GameSenseTheme.8(boolean bl) {
        this.val$container = bl;
    }

    @Override
    public void renderSlider(Context context, String title, String state, boolean focus, double value) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Color colorA = GameSenseTheme.this.getMainColor(effFocus, true);
        Color colorB = GameSenseTheme.this.getBackgroundColor(effFocus);
        Rectangle rect = this.getSlideArea(context, title, state);
        int divider = (int)((double)rect.width * value);
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y, divider, rect.height), colorA, colorA, colorA, colorA);
        context.getInterface().fillRect(new Rectangle(rect.x + divider, rect.y, rect.width - divider, rect.height), colorB, colorB, colorB, colorB);
        GameSenseTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + GameSenseTheme.this.padding, context.getPos().y + GameSenseTheme.this.padding), GameSenseTheme.this.height, title + GameSenseTheme.this.separator + state, GameSenseTheme.this.getFontColor(focus));
    }

    @Override
    public int getDefaultHeight() {
        return GameSenseTheme.this.getBaseHeight();
    }
}
