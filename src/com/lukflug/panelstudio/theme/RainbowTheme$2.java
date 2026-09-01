/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IContainerRenderer;

class RainbowTheme.2
implements IContainerRenderer {
    final /* synthetic */ int val$graphicalLevel;

    RainbowTheme.2(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderBackground(Context context, boolean focus) {
        if (this.val$graphicalLevel == 0 && !RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, focus);
        }
    }
}
