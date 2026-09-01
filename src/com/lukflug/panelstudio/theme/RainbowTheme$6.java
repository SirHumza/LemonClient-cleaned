/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;

class RainbowTheme.6
implements IButtonRenderer<T> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ Class val$type;
    final /* synthetic */ int val$logicalLevel;

    RainbowTheme.6(boolean bl, int n, Class clazz, int n2) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$type = clazz;
        this.val$logicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Object state) {
        boolean active;
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        boolean bl = active = this.val$container && this.val$graphicalLevel != 0;
        if (this.val$type == Boolean.class) {
            boolean bl2 = active = (Boolean)state != false || RainbowTheme.this.ignoreDisabled.isOn() && this.val$container;
        }
        if (!active) {
            Color color = RainbowTheme.this.getBackgroundColor(effFocus);
            context.getInterface().fillRect(context.getRect(), color, color, color, color);
        } else if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, effFocus);
        }
        RainbowTheme.this.renderOverlay(context);
        String text = (this.val$logicalLevel >= 2 ? "> " : "") + title + (this.val$type == String.class ? RainbowTheme.this.separator + state : "");
        context.getInterface().drawString(new Point(context.getPos().x + RainbowTheme.this.padding, context.getPos().y + RainbowTheme.this.padding), RainbowTheme.this.height, text, RainbowTheme.this.getFontColor(effFocus));
    }

    @Override
    public int getDefaultHeight() {
        return RainbowTheme.this.getBaseHeight();
    }
}
