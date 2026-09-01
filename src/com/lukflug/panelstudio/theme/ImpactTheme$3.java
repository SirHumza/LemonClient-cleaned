/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class ImpactTheme.3
implements IPanelRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.3(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public int getBorder() {
        return this.val$graphicalLevel <= 0 ? 1 : 0;
    }

    @Override
    public int getLeft() {
        return 1;
    }

    @Override
    public int getRight() {
        return 1;
    }

    @Override
    public int getTop() {
        return 1;
    }

    @Override
    public int getBottom() {
        return 1;
    }

    @Override
    public void renderPanelOverlay(Context context, boolean focus, T state, boolean open) {
        Color color = this.val$graphicalLevel <= 0 ? ImpactTheme.this.scheme.getColor("Panel Outline Color") : ImpactTheme.this.scheme.getColor("Component Outline Color");
        ITheme.drawRect(context.getInterface(), context.getRect(), color);
    }

    @Override
    public void renderTitleOverlay(Context context, boolean focus, T state, boolean open) {
        if (this.val$graphicalLevel <= 0) {
            Color colorA = ImpactTheme.this.scheme.getColor("Panel Outline Color");
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height, context.getSize().width, 1), colorA, colorA, colorA, colorA);
        } else {
            ImpactTheme.this.renderOverlay(context);
            Context subContext = new Context(context, ImpactTheme.this.height, new Point(ImpactTheme.this.padding / 2, ImpactTheme.this.padding / 2), true, true);
            subContext.setHeight(context.getSize().height - ImpactTheme.this.padding);
            ImpactTheme.this.renderSmallButton(subContext, null, open ? 7 : 5, focus);
        }
    }
}
