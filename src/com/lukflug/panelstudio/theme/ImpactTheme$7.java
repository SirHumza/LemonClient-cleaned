/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;

class ImpactTheme.7
implements IButtonRenderer<Void> {
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$symbol;

    ImpactTheme.7(int n, boolean bl, int n2, int n3) {
        this.val$graphicalLevel = n;
        this.val$container = bl;
        this.val$logicalLevel = n2;
        this.val$symbol = n3;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Void state) {
        Color color;
        if (this.val$graphicalLevel <= 0) {
            if (this.val$container) {
                color = ImpactTheme.this.scheme.getColor("Title Color");
                context.getInterface().fillRect(context.getRect(), color, color, color, color);
            } else {
                ImpactTheme.this.renderBackground(context, focus);
            }
        }
        if (!this.val$container) {
            color = this.val$graphicalLevel <= 0 ? ImpactTheme.this.scheme.getColor("Panel Outline Color") : ImpactTheme.this.scheme.getColor("Component Outline Color");
            ITheme.drawRect(context.getInterface(), context.getRect(), color);
            ImpactTheme.this.renderOverlay(context);
        }
        ImpactTheme.this.renderOverlay(context);
        if (!this.val$container || this.val$logicalLevel <= 0) {
            ImpactTheme.this.renderSmallButton(context, title, this.val$symbol, focus);
        }
    }

    @Override
    public int getDefaultHeight() {
        return ImpactTheme.this.getBaseHeight();
    }
}
