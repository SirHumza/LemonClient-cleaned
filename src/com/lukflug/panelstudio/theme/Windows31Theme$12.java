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

class Windows31Theme.12
implements ISwitchRenderer<Boolean> {
    final /* synthetic */ boolean val$container;

    Windows31Theme.12(boolean bl) {
        this.val$container = bl;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Color colorA = Windows31Theme.this.getMainColor(effFocus, true);
        if (this.val$container && effFocus) {
            context.getInterface().fillRect(context.getRect(), colorA, colorA, colorA, colorA);
        }
        context.getInterface().drawString(new Point(context.getPos().x + Windows31Theme.this.padding, context.getPos().y + Windows31Theme.this.padding), Windows31Theme.this.height, title + Windows31Theme.this.separator + (state != false ? "On" : "Off"), Windows31Theme.this.getFontColor(focus));
        Rectangle rect = new Rectangle(context.getPos().x + context.getSize().width - 2 * context.getSize().height, context.getPos().y, 2 * context.getSize().height, context.getSize().height);
        Color colorB = Windows31Theme.this.getMainColor(effFocus, state);
        context.getInterface().fillRect(rect, colorB, colorB, colorB, colorB);
        ITheme.drawRect(context.getInterface(), rect, Windows31Theme.this.getFontColor(effFocus));
        Rectangle field = state != false ? this.getOnField(context) : this.getOffField(context);
        Windows31Theme.this.drawButton(context.getInterface(), field, focus, context.isClicked(0) && field.contains(context.getInterface().getMouse()), true);
    }

    @Override
    public int getDefaultHeight() {
        return Windows31Theme.this.getBaseHeight();
    }

    @Override
    public Rectangle getOnField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - rect.height, rect.y, rect.height, rect.height);
    }

    @Override
    public Rectangle getOffField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - 2 * rect.height, rect.y, rect.height, rect.height);
    }
}
