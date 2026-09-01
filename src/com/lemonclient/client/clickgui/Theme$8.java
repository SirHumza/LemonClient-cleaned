/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;

class Theme.8
implements IButtonRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    Theme.8(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
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
        if (effFocus) {
            color = Theme.this.getMainColor(effFocus, true);
        }
        Theme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + Theme.this.padding, context.getPos().y + Theme.this.padding), Theme.this.height, title + Theme.this.separator + (focus ? "..." : state), color);
    }

    @Override
    public int getDefaultHeight() {
        return Theme.this.getBaseHeight();
    }
}
