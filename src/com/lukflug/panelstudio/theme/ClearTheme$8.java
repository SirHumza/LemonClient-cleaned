/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;

class ClearTheme.8
implements IButtonRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ClearTheme.8(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
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
        if (effFocus) {
            color = ClearTheme.this.getMainColor(effFocus, true);
        }
        ClearTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + ClearTheme.this.padding, context.getPos().y + ClearTheme.this.padding), ClearTheme.this.height, title + ClearTheme.this.separator + (focus ? "..." : state), color);
    }

    @Override
    public int getDefaultHeight() {
        return ClearTheme.this.getBaseHeight();
    }
}
