/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;
import java.awt.Color;

class RainbowTheme.5
implements IEmptySpaceRenderer<T> {
    RainbowTheme.5() {
    }

    @Override
    public void renderSpace(Context context, boolean focus, T state) {
        Color color = RainbowTheme.this.getBackgroundColor(focus);
        context.getInterface().fillRect(context.getRect(), color, color, color, color);
    }
}
