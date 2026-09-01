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

class ImpactTheme.6
implements IButtonRenderer<T> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ Class val$type;

    ImpactTheme.6(boolean bl, int n, Class clazz) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$type = clazz;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, T state) {
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
        int colorLevel = 1;
        if (this.val$type == Boolean.class) {
            colorLevel = (Boolean)state != false ? 2 : 0;
        } else if (this.val$type == String.class) {
            colorLevel = 2;
        }
        if (this.val$container && this.val$graphicalLevel <= 0) {
            colorLevel = 2;
        }
        Color valueColor = ImpactTheme.this.getFontColor(effFocus);
        if (context.isHovered() && context.getInterface().getMouse().x > context.getPos().x + context.getSize().height - ImpactTheme.this.padding) {
            if (colorLevel < 2) {
                ++colorLevel;
            }
            valueColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        }
        Color fontColor = ImpactTheme.this.getFontColor(effFocus);
        if (colorLevel == 2) {
            fontColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        } else if (colorLevel == 0) {
            fontColor = ImpactTheme.this.scheme.getColor("Inactive Font Color");
        }
        int xpos = context.getPos().x + context.getSize().height - ImpactTheme.this.padding;
        if (this.val$container && this.val$graphicalLevel <= 0) {
            xpos = context.getPos().x + context.getSize().width / 2 - context.getInterface().getFontWidth(ImpactTheme.this.height, title) / 2;
        }
        context.getInterface().drawString(new Point(xpos, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, title, fontColor);
        if (this.val$type == String.class) {
            context.getInterface().drawString(new Point(context.getPos().x + context.getSize().width - ImpactTheme.this.padding - context.getInterface().getFontWidth(ImpactTheme.this.height, (String)state), context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, (String)state, valueColor);
        } else if (this.val$type == Boolean.class) {
            if (context.isHovered() && this.val$container) {
                int width = context.getInterface().getFontWidth(ImpactTheme.this.height, "OFF") + 2 * ImpactTheme.this.padding;
                Rectangle rect = new Rectangle(context.getPos().x + context.getSize().width - width, context.getPos().y + ImpactTheme.this.padding / 2, width, context.getSize().height - 2 * (ImpactTheme.this.padding / 2));
                String text = (Boolean)state != false ? "ON" : "OFF";
                Color color2 = ImpactTheme.this.getMainColor(effFocus, (Boolean)state);
                context.getInterface().fillRect(rect, color2, color2, color2, color2);
                context.getInterface().drawString(new Point(rect.x + (rect.width - context.getInterface().getFontWidth(ImpactTheme.this.height, text)) / 2, context.getPos().y + ImpactTheme.this.padding / 2), ImpactTheme.this.height, text, ImpactTheme.this.scheme.getColor("Active Font Color"));
            } else if (!this.val$container && ((Boolean)state).booleanValue()) {
                Point a = new Point(context.getPos().x + context.getSize().width - context.getSize().height + ImpactTheme.this.padding, context.getPos().y + context.getSize().height / 2);
                Point b = new Point(context.getPos().x + context.getSize().width - context.getSize().height / 2, context.getPos().y + context.getSize().height - ImpactTheme.this.padding);
                Point c = new Point(context.getPos().x + context.getSize().width - ImpactTheme.this.padding, context.getPos().y + ImpactTheme.this.padding);
                Color checkColor = ImpactTheme.this.scheme.getColor("Active Font Color");
                context.getInterface().drawLine(a, b, checkColor, checkColor);
                context.getInterface().drawLine(b, c, checkColor, checkColor);
            }
        }
    }

    @Override
    public int getDefaultHeight() {
        return this.val$container ? ImpactTheme.this.getBaseHeight() - 2 : ImpactTheme.this.getBaseHeight();
    }
}
