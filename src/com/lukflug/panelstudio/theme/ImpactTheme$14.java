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

class ImpactTheme.14
implements ISwitchRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ int val$logicalLevel;

    ImpactTheme.14(boolean bl, int n, int n2) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
        this.val$logicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
        Color color;
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Context subContext = new Context(context, context.getSize().width - 2 * context.getSize().height, new Point(0, 0), true, true);
        subContext.setHeight(context.getSize().height);
        if (this.val$graphicalLevel <= 0) {
            if (this.val$container) {
                color = ImpactTheme.this.scheme.getColor("Title Color");
                context.getInterface().fillRect(subContext.getRect(), color, color, color, color);
            } else {
                ImpactTheme.this.renderBackground(subContext, effFocus);
            }
        }
        if (!this.val$container) {
            color = this.val$graphicalLevel <= 0 ? ImpactTheme.this.scheme.getColor("Panel Outline Color") : ImpactTheme.this.scheme.getColor("Component Outline Color");
            ITheme.drawRect(context.getInterface(), subContext.getRect(), color);
            ImpactTheme.this.renderOverlay(subContext);
        }
        Color valueColor = ImpactTheme.this.getFontColor(effFocus);
        if (context.isHovered() && context.getInterface().getMouse().x > subContext.getPos().x + subContext.getSize().height - ImpactTheme.this.padding) {
            valueColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        }
        Color fontColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        int xpos = context.getPos().x + context.getSize().height - ImpactTheme.this.padding;
        if (this.val$container && this.val$graphicalLevel <= 0) {
            xpos = subContext.getPos().x + subContext.getSize().width / 2 - context.getInterface().getFontWidth(ImpactTheme.this.height, title) / 2;
        }
        context.getInterface().drawString(new Point(xpos, subContext.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, title, fontColor);
        context.getInterface().drawString(new Point(subContext.getPos().x + subContext.getSize().width - ImpactTheme.this.padding - context.getInterface().getFontWidth(ImpactTheme.this.height, state), subContext.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, state, valueColor);
        Rectangle rect = this.getOnField(context);
        subContext = new Context(context, rect.width, new Point(rect.x - context.getPos().x, 0), true, true);
        subContext.setHeight(rect.height);
        ImpactTheme.this.getSmallButtonRenderer(5, this.val$logicalLevel, this.val$graphicalLevel, false).renderButton(subContext, null, effFocus, null);
        rect = this.getOffField(context);
        subContext = new Context(context, rect.width, new Point(rect.x - context.getPos().x, 0), true, true);
        subContext.setHeight(rect.height);
        ImpactTheme.this.getSmallButtonRenderer(4, this.val$logicalLevel, this.val$graphicalLevel, false).renderButton(subContext, null, effFocus, null);
    }

    @Override
    public int getDefaultHeight() {
        return ImpactTheme.this.getBaseHeight();
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
