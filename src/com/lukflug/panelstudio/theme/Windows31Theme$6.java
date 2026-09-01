/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class Windows31Theme.6
implements IButtonRenderer<T> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ Class val$type;

    Windows31Theme.6(boolean bl, Class clazz) {
        this.val$container = bl;
        this.val$type = clazz;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, T state) {
        Color color;
        boolean active;
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        boolean bl = active = this.val$type == Boolean.class ? (Boolean)state : effFocus;
        if (!this.val$container && this.val$type == Boolean.class) {
            ITheme.drawRect(context.getInterface(), new Rectangle(context.getPos().x, context.getPos().y, Windows31Theme.this.height, Windows31Theme.this.height), Windows31Theme.this.getFontColor(effFocus));
            if (((Boolean)state).booleanValue()) {
                context.getInterface().drawLine(context.getPos(), new Point(context.getPos().x + Windows31Theme.this.height - 1, context.getPos().y + Windows31Theme.this.height - 1), Windows31Theme.this.getFontColor(effFocus), Windows31Theme.this.getFontColor(effFocus));
                context.getInterface().drawLine(new Point(context.getPos().x + Windows31Theme.this.height - 1, context.getPos().y + 1), new Point(context.getPos().x, context.getPos().y + Windows31Theme.this.height), Windows31Theme.this.getFontColor(effFocus), Windows31Theme.this.getFontColor(effFocus));
            }
            context.getInterface().drawString(new Point(context.getPos().x + Windows31Theme.this.height + Windows31Theme.this.padding, context.getPos().y), Windows31Theme.this.height, title, Windows31Theme.this.getFontColor(effFocus));
            return;
        }
        if (this.val$container) {
            color = Windows31Theme.this.getMainColor(effFocus, active);
            context.getInterface().fillRect(context.getRect(), color, color, color, color);
            Color lineColor = Windows31Theme.this.getFontColor(effFocus);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - 1, context.getSize().width, 1), lineColor, lineColor, lineColor, lineColor);
        } else {
            Windows31Theme.this.drawButton(context.getInterface(), context.getRect(), effFocus, context.isClicked(0), false);
        }
        color = this.val$container && active ? Windows31Theme.this.getMainColor(effFocus, false) : Windows31Theme.this.getFontColor(effFocus);
        String string = title;
        if (this.val$type == String.class) {
            string = string + Windows31Theme.this.separator + state;
        } else if (this.val$type == Color.class) {
            color = (Color)state;
        }
        context.getInterface().drawString(new Point(context.getPos().x + context.getSize().width / 2 - context.getInterface().getFontWidth(Windows31Theme.this.height, string) / 2, context.getPos().y + (this.val$container ? 0 : 3) + Windows31Theme.this.padding), Windows31Theme.this.height, string, color);
    }

    @Override
    public int getDefaultHeight() {
        if (!this.val$container && this.val$type == Boolean.class) {
            return Windows31Theme.this.height;
        }
        return this.val$container ? Windows31Theme.this.getBaseHeight() : Windows31Theme.this.getBaseHeight() + 6;
    }
}
