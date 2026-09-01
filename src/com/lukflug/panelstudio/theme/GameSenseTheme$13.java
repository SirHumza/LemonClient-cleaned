/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISwitchRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class GameSenseTheme.13
implements ISwitchRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.13(boolean bl, int n, int n2) {
        this.val$container = bl;
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        GameSenseTheme.this.fillBaseRect(context, effFocus, false, this.val$logicalLevel, this.val$graphicalLevel, null);
        Color color = GameSenseTheme.this.scheme.getColor("Outline Color");
        if (this.val$graphicalLevel <= 0 && this.val$container) {
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - 1, context.getSize().width, 1), color, color, color, color);
        }
        Context subContext = new Context(context, context.getSize().width - 2 * context.getSize().height, new Point(0, 0), true, true);
        subContext.setHeight(context.getSize().height);
        GameSenseTheme.this.renderOverlay(subContext);
        Color textColor = GameSenseTheme.this.getFontColor(effFocus);
        context.getInterface().drawString(new Point(context.getPos().x + GameSenseTheme.this.padding, context.getPos().y + GameSenseTheme.this.padding), GameSenseTheme.this.height, title + GameSenseTheme.this.separator + state, textColor);
        Rectangle rect = this.getOnField(context);
        subContext = new Context(context, rect.width, new Point(rect.x - context.getPos().x, 0), true, true);
        subContext.setHeight(rect.height);
        GameSenseTheme.this.getSmallButtonRenderer(5, this.val$logicalLevel, this.val$graphicalLevel, this.val$container).renderButton(subContext, null, effFocus, null);
        rect = this.getOffField(context);
        subContext = new Context(context, rect.width, new Point(rect.x - context.getPos().x, 0), true, true);
        subContext.setHeight(rect.height);
        GameSenseTheme.this.getSmallButtonRenderer(4, this.val$logicalLevel, this.val$graphicalLevel, this.val$container).renderButton(subContext, null, effFocus, null);
    }

    @Override
    public int getDefaultHeight() {
        return GameSenseTheme.this.getBaseHeight();
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
