/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISliderRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class Windows31Theme.8
implements ISliderRenderer {
    final /* synthetic */ boolean val$container;

    Windows31Theme.8(boolean bl) {
        this.val$container = bl;
    }

    @Override
    public void renderSlider(Context context, String title, String state, boolean focus, double value) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Color colorA = Windows31Theme.this.getMainColor(effFocus, true);
        if (this.val$container && effFocus) {
            context.getInterface().fillRect(context.getRect(), colorA, colorA, colorA, colorA);
        }
        Rectangle rect = this.getSlideArea(context, title, state);
        Color colorB = Windows31Theme.this.getBackgroundColor(effFocus);
        context.getInterface().fillRect(rect, colorB, colorB, colorB, colorB);
        ITheme.drawRect(context.getInterface(), rect, Windows31Theme.this.getFontColor(effFocus));
        int divider = (int)((double)(rect.width - rect.height) * value);
        Rectangle buttonRect = new Rectangle(rect.x + divider, rect.y, rect.height, rect.height);
        boolean clicked = context.isClicked(0) && buttonRect.contains(context.getInterface().getMouse());
        Windows31Theme.this.drawButton(context.getInterface(), buttonRect, effFocus, clicked, true);
        Color color = this.val$container && effFocus ? Windows31Theme.this.getMainColor(effFocus, false) : Windows31Theme.this.getFontColor(effFocus);
        String string = title + Windows31Theme.this.separator + state;
        context.getInterface().drawString(new Point(context.getPos().x + Windows31Theme.this.padding, context.getPos().y + Windows31Theme.this.padding), Windows31Theme.this.height, string, color);
    }

    @Override
    public Rectangle getSlideArea(Context context, String title, String state) {
        if (this.val$container) {
            return context.getRect();
        }
        return new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - Windows31Theme.this.height, context.getSize().width, Windows31Theme.this.height);
    }

    @Override
    public int getDefaultHeight() {
        return Windows31Theme.this.getBaseHeight() + Windows31Theme.this.height;
    }
}
