/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;

class ClearTheme.6
implements IButtonRenderer<T> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ Class val$type;

    ClearTheme.6(boolean bl, int n, Class clazz) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$type = clazz;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, T state) {
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$container && this.val$graphicalLevel <= 0) {
            Color colorA = ClearTheme.this.getColor(ClearTheme.this.scheme.getColor("Title Color"));
            Color colorB = ClearTheme.this.gradient.isOn() ? ClearTheme.this.getBackgroundColor(effFocus) : colorA;
            context.getInterface().fillRect(context.getRect(), colorA, colorA, colorB, colorB);
        } else {
            ClearTheme.this.renderBackground(context, effFocus, this.val$graphicalLevel);
        }
        Color color = ClearTheme.this.getFontColor(effFocus);
        if (this.val$type == Boolean.class && ((Boolean)state).booleanValue()) {
            color = ClearTheme.this.getMainColor(effFocus, true);
        } else if (this.val$type == Color.class) {
            color = (Color)state;
        }
        if (this.val$graphicalLevel > 0) {
            ClearTheme.this.renderOverlay(context);
        }
        if (this.val$type == String.class) {
            context.getInterface().drawString(new Point(context.getPos().x + ClearTheme.this.padding, context.getPos().y + ClearTheme.this.padding), ClearTheme.this.height, title + ClearTheme.this.separator + state, color);
        } else {
            context.getInterface().drawString(new Point(context.getPos().x + ClearTheme.this.padding, context.getPos().y + ClearTheme.this.padding), ClearTheme.this.height, title, color);
        }
    }

    @Override
    public int getDefaultHeight() {
        return ClearTheme.this.getBaseHeight();
    }
}
