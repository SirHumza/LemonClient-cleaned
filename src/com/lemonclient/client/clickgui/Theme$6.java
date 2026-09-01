/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;

class Theme.6
implements IButtonRenderer<T> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ Class val$type;

    Theme.6(boolean bl, int n, Class clazz) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$type = clazz;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, T state) {
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$container && this.val$graphicalLevel <= 0) {
            Color colorA = Theme.this.title;
            Color colorB = Theme.this.gradient.isOn() ? Theme.this.getBackgroundColor(effFocus) : colorA;
            context.getInterface().fillRect(context.getRect(), colorA, colorA, colorB, colorB);
        } else {
            Theme.this.renderBackground(context, effFocus, this.val$graphicalLevel);
        }
        Color color = Theme.this.getFontColor(effFocus);
        if (this.val$type == Boolean.class && ((Boolean)state).booleanValue()) {
            color = Theme.this.getMainColor(effFocus, true);
        } else if (this.val$type == Color.class) {
            color = (Color)state;
        }
        if (this.val$graphicalLevel > 0) {
            Theme.this.renderOverlay(context);
        }
        if (this.val$type == String.class) {
            context.getInterface().drawString(new Point(context.getPos().x + Theme.this.padding, context.getPos().y + Theme.this.padding), Theme.this.height, title + Theme.this.separator + state, color);
        } else {
            context.getInterface().drawString(new Point(context.getPos().x + Theme.this.padding, context.getPos().y + Theme.this.padding), Theme.this.height, title, color);
        }
    }

    @Override
    public int getDefaultHeight() {
        return Theme.this.getBaseHeight();
    }
}
