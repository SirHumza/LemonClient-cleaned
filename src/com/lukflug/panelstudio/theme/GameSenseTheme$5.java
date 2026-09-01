/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class GameSenseTheme.5
implements IButtonRenderer<T> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ Class val$type;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.5(boolean bl, Class clazz, int n, int n2) {
        this.val$container = bl;
        this.val$type = clazz;
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, T state) {
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$type == Boolean.class) {
            GameSenseTheme.this.fillBaseRect(context, effFocus, (Boolean)state, this.val$logicalLevel, this.val$graphicalLevel, null);
        } else if (this.val$type == Color.class) {
            GameSenseTheme.this.fillBaseRect(context, effFocus, this.val$graphicalLevel <= 0, this.val$logicalLevel, this.val$graphicalLevel, (Color)state);
        } else {
            GameSenseTheme.this.fillBaseRect(context, effFocus, this.val$graphicalLevel <= 0, this.val$logicalLevel, this.val$graphicalLevel, null);
        }
        if (this.val$graphicalLevel <= 0 && this.val$container) {
            Color color = GameSenseTheme.this.scheme.getColor("Outline Color");
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - 1, context.getSize().width, 1), color, color, color, color);
        }
        GameSenseTheme.this.renderOverlay(context);
        if (this.val$type == String.class) {
            context.getInterface().drawString(new Point(context.getPos().x + GameSenseTheme.this.padding, context.getPos().y + GameSenseTheme.this.padding), GameSenseTheme.this.height, title + GameSenseTheme.this.separator + state, GameSenseTheme.this.getFontColor(focus));
        } else {
            context.getInterface().drawString(new Point(context.getPos().x + GameSenseTheme.this.padding, context.getPos().y + GameSenseTheme.this.padding), GameSenseTheme.this.height, title, GameSenseTheme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight() {
        return GameSenseTheme.this.getBaseHeight();
    }
}
