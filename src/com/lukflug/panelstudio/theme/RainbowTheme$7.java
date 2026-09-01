/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;

class RainbowTheme.7
implements IButtonRenderer<Void> {
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$symbol;

    RainbowTheme.7(int n, boolean bl, int n2, int n3) {
        this.val$graphicalLevel = n;
        this.val$container = bl;
        this.val$logicalLevel = n2;
        this.val$symbol = n3;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Void state) {
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, focus);
        }
        RainbowTheme.this.renderOverlay(context);
        if (!this.val$container || this.val$logicalLevel <= 0) {
            RainbowTheme.this.renderSmallButton(context, title, this.val$symbol, focus);
        }
    }

    @Override
    public int getDefaultHeight() {
        return RainbowTheme.this.getBaseHeight();
    }
}
