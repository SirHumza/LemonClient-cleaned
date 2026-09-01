/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;

class ImpactTheme.8
implements IButtonRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.8(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
        Color color;
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel <= 0) {
            if (this.val$container) {
                color = ImpactTheme.this.scheme.getColor("Title Color");
                context.getInterface().fillRect(context.getRect(), color, color, color, color);
            } else {
                ImpactTheme.this.renderBackground(context, effFocus);
            }
        }
        if (!this.val$container) {
            color = this.val$graphicalLevel <= 0 ? ImpactTheme.this.scheme.getColor("Panel Outline Color") : ImpactTheme.this.scheme.getColor("Component Outline Color");
            ITheme.drawRect(context.getInterface(), context.getRect(), color);
            ImpactTheme.this.renderOverlay(context);
        }
        Color valueColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        Color fontColor = ImpactTheme.this.getFontColor(effFocus);
        if (context.isHovered() && context.getInterface().getMouse().x > context.getPos().x + context.getSize().height - ImpactTheme.this.padding) {
            fontColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        }
        int xpos = context.getPos().x + context.getSize().height - ImpactTheme.this.padding;
        if (this.val$container && this.val$graphicalLevel <= 0) {
            xpos = context.getPos().x + context.getSize().width / 2 - context.getInterface().getFontWidth(ImpactTheme.this.height, title) / 2;
        }
        context.getInterface().drawString(new Point(xpos, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, title, fontColor);
        context.getInterface().drawString(new Point(context.getPos().x + context.getSize().width - ImpactTheme.this.padding - context.getInterface().getFontWidth(ImpactTheme.this.height, effFocus ? "..." : state), context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, effFocus ? "..." : state, valueColor);
    }

    @Override
    public int getDefaultHeight() {
        return this.val$container ? ImpactTheme.this.getBaseHeight() - 2 : ImpactTheme.this.getBaseHeight();
    }
}
