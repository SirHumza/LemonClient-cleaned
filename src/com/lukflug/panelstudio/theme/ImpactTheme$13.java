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

class ImpactTheme.13
implements ISwitchRenderer<Boolean> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.13(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
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
        ImpactTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + ImpactTheme.this.padding, context.getPos().y + ImpactTheme.this.padding), ImpactTheme.this.height, title, ImpactTheme.this.getFontColor(focus));
        Color fillColor = ImpactTheme.this.getMainColor(focus, state);
        Rectangle rect = state != false ? this.getOnField(context) : this.getOffField(context);
        context.getInterface().fillRect(rect, fillColor, fillColor, fillColor, fillColor);
        rect = context.getRect();
        rect = new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * ImpactTheme.this.padding, rect.y + ImpactTheme.this.padding, 2 * rect.height - 4 * ImpactTheme.this.padding, rect.height - 2 * ImpactTheme.this.padding);
        ITheme.drawRect(context.getInterface(), rect, ImpactTheme.this.scheme.getColor("Component Outline Color"));
    }

    @Override
    public int getDefaultHeight() {
        return ImpactTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getOnField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - rect.height + ImpactTheme.this.padding, rect.y + ImpactTheme.this.padding, rect.height - 2 * ImpactTheme.this.padding, rect.height - 2 * ImpactTheme.this.padding);
    }

    @Override
    public Rectangle getOffField(Context context) {
        Rectangle rect = context.getRect();
        return new Rectangle(rect.x + rect.width - 2 * rect.height + 3 * ImpactTheme.this.padding, rect.y + ImpactTheme.this.padding, rect.height - 2 * ImpactTheme.this.padding, rect.height - 2 * ImpactTheme.this.padding);
    }
}
