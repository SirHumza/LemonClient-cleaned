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

class GameSenseTheme.12
implements ISwitchRenderer<Boolean> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.12(boolean bl, int n, int n2) {
        this.val$container = bl;
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        GameSenseTheme.this.fillBaseRect(context, effFocus, false, this.val$logicalLevel, this.val$graphicalLevel, null);
        Color color = GameSenseTheme.this.scheme.getColor("Outline Color");
        if (this.val$graphicalLevel <= 0 && this.val$container) {
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - 1, context.getSize().width, 1), color, color, color, color);
        }
        GameSenseTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + GameSenseTheme.this.padding, context.getPos().y + GameSenseTheme.this.padding), GameSenseTheme.this.height, title + GameSenseTheme.this.separator + (state != false ? "On" : "Off"), GameSenseTheme.this.getFontColor(focus));
        Color fillColor = GameSenseTheme.this.getMainColor(effFocus, true);
        Rectangle rect = state != false ? this.getOnField(context) : this.getOffField(context);
        context.getInterface().fillRect(rect, fillColor, fillColor, fillColor, fillColor);
        rect = context.getRect();
        rect = new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * GameSenseTheme.this.padding, rect.y + GameSenseTheme.this.padding, 2 * rect.height - 4 * GameSenseTheme.this.padding, rect.height - 2 * GameSenseTheme.this.padding);
        ITheme.drawRect(context.getInterface(), rect, color);
    }

    @Override
    public int getDefaultHeight() {
        return GameSenseTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getOnField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - rect.height + GameSenseTheme.this.padding, rect.y + GameSenseTheme.this.padding, rect.height - 2 * GameSenseTheme.this.padding, rect.height - 2 * GameSenseTheme.this.padding);
    }

    @Override
    public Rectangle getOffField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * GameSenseTheme.this.padding, rect.y + GameSenseTheme.this.padding, rect.height - 2 * GameSenseTheme.this.padding, rect.height - 2 * GameSenseTheme.this.padding);
    }
}
