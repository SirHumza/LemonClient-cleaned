/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISwitchRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class RainbowTheme.12
implements ISwitchRenderer<Boolean> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    RainbowTheme.12(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, effFocus);
        }
        Color color = RainbowTheme.this.getBackgroundColor(effFocus);
        if (this.val$graphicalLevel <= 0 && this.val$container) {
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - 1, context.getSize().width, 1), color, color, color, color);
        }
        RainbowTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + RainbowTheme.this.padding, context.getPos().y + RainbowTheme.this.padding), RainbowTheme.this.height, title + RainbowTheme.this.separator + (state != false ? "On" : "Off"), RainbowTheme.this.getFontColor(focus));
        Rectangle rect = state != false ? this.getOnField(context) : this.getOffField(context);
        context.getInterface().fillRect(rect, color, color, color, color);
        rect = context.getRect();
        rect = new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * RainbowTheme.this.padding, rect.y + RainbowTheme.this.padding, 2 * rect.height - 4 * RainbowTheme.this.padding, rect.height - 2 * RainbowTheme.this.padding);
        ITheme.drawRect(context.getInterface(), rect, color);
    }

    @Override
    public int getDefaultHeight() {
        return RainbowTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getOnField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - rect.height + RainbowTheme.this.padding, rect.y + RainbowTheme.this.padding, rect.height - 2 * RainbowTheme.this.padding, rect.height - 2 * RainbowTheme.this.padding);
    }

    @Override
    public Rectangle getOffField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * RainbowTheme.this.padding, rect.y + RainbowTheme.this.padding, rect.height - 2 * RainbowTheme.this.padding, rect.height - 2 * RainbowTheme.this.padding);
    }
}
