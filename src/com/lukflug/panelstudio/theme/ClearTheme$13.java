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

class ClearTheme.13
implements ISwitchRenderer<Boolean> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ClearTheme.13(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        ClearTheme.this.renderBackground(context, effFocus, this.val$graphicalLevel);
        ClearTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + ClearTheme.this.padding, context.getPos().y + ClearTheme.this.padding), ClearTheme.this.height, title + ClearTheme.this.separator + (state != false ? "On" : "Off"), ClearTheme.this.getFontColor(focus));
        Color color = state != false ? ClearTheme.this.scheme.getColor("Enabled Color") : ClearTheme.this.scheme.getColor("Disabled Color");
        Color fillColor = ITheme.combineColors(color, ClearTheme.this.getBackgroundColor(effFocus));
        Rectangle rect = state != false ? this.getOnField(context) : this.getOffField(context);
        context.getInterface().fillRect(rect, fillColor, fillColor, fillColor, fillColor);
        rect = context.getRect();
        rect = new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * ClearTheme.this.padding, rect.y + ClearTheme.this.padding, 2 * rect.height - 4 * ClearTheme.this.padding, rect.height - 2 * ClearTheme.this.padding);
        context.getInterface().drawRect(rect, color, color, color, color);
    }

    @Override
    public int getDefaultHeight() {
        return ClearTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getOnField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - rect.height + ClearTheme.this.padding, rect.y + ClearTheme.this.padding, rect.height - 2 * ClearTheme.this.padding, rect.height - 2 * ClearTheme.this.padding);
    }

    @Override
    public Rectangle getOffField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * ClearTheme.this.padding, rect.y + ClearTheme.this.padding, rect.height - 2 * ClearTheme.this.padding, rect.height - 2 * ClearTheme.this.padding);
    }
}
