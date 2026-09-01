/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISwitchRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class RainbowTheme.13
implements ISwitchRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ int val$logicalLevel;

    RainbowTheme.13(boolean bl, int n, int n2) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$logicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, effFocus);
        }
        Context subContext = new Context(context, context.getSize().width - 2 * context.getSize().height, new Point(0, 0), true, true);
        subContext.setHeight(context.getSize().height);
        RainbowTheme.this.renderOverlay(subContext);
        Color textColor = RainbowTheme.this.getFontColor(effFocus);
        context.getInterface().drawString(new Point(context.getPos().x + RainbowTheme.this.padding, context.getPos().y + RainbowTheme.this.padding), RainbowTheme.this.height, title + RainbowTheme.this.separator + state, textColor);
        Rectangle rect = this.getOnField(context);
        subContext = new Context(context, rect.width, new Point(rect.x - context.getPos().x, 0), true, true);
        subContext.setHeight(rect.height);
        RainbowTheme.this.getSmallButtonRenderer(5, this.val$logicalLevel, this.val$graphicalLevel, this.val$container).renderButton(subContext, null, effFocus, null);
        rect = this.getOffField(context);
        subContext = new Context(context, rect.width, new Point(rect.x - context.getPos().x, 0), true, true);
        subContext.setHeight(rect.height);
        RainbowTheme.this.getSmallButtonRenderer(4, this.val$logicalLevel, this.val$graphicalLevel, false).renderButton(subContext, null, effFocus, null);
    }

    @Override
    public int getDefaultHeight() {
        return RainbowTheme.this.getBaseHeight();
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
