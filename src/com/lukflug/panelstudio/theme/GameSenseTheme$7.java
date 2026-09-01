/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Point;

class GameSenseTheme.7
implements IButtonRenderer<String> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.7(boolean bl, int n, int n2) {
        this.val$container = bl;
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, String state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        GameSenseTheme.this.fillBaseRect(context, effFocus, effFocus, this.val$logicalLevel, this.val$graphicalLevel, null);
        GameSenseTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + GameSenseTheme.this.padding, context.getPos().y + GameSenseTheme.this.padding), GameSenseTheme.this.height, title + GameSenseTheme.this.separator + (focus ? "..." : state), GameSenseTheme.this.getFontColor(focus));
    }

    @Override
    public int getDefaultHeight() {
        return GameSenseTheme.this.getBaseHeight();
    }
}
