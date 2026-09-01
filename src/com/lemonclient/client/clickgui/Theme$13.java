/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISwitchRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class Theme.13
implements ISwitchRenderer<Boolean> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    Theme.13(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Theme.this.renderBackground(context, effFocus, this.val$graphicalLevel);
        Theme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + Theme.this.padding, context.getPos().y + Theme.this.padding), Theme.this.height, title + Theme.this.separator + (state != false ? "On" : "Off"), Theme.this.getFontColor(focus));
        Color color = state != false ? Theme.this.enable : Theme.this.disable;
        Color fillColor = ITheme.combineColors(color, Theme.this.getBackgroundColor(effFocus));
        Rectangle rect = state != false ? this.getOnField(context) : this.getOffField(context);
        context.getInterface().fillRect(rect, fillColor, fillColor, fillColor, fillColor);
        rect = context.getRect();
        rect = new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * Theme.this.padding, rect.y + Theme.this.padding, 2 * rect.height - 4 * Theme.this.padding, rect.height - 2 * Theme.this.padding);
        context.getInterface().drawRect(rect, color, color, color, color);
    }

    @Override
    public int getDefaultHeight() {
        return Theme.this.getBaseHeight();
    }

    @Override
    public Rectangle getOnField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - rect.height + Theme.this.padding, rect.y + Theme.this.padding, rect.height - 2 * Theme.this.padding, rect.height - 2 * Theme.this.padding);
    }

    @Override
    public Rectangle getOffField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * Theme.this.padding, rect.y + Theme.this.padding, rect.height - 2 * Theme.this.padding, rect.height - 2 * Theme.this.padding);
    }
}
