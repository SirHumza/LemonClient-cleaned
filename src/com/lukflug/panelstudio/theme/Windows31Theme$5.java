/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;
import java.awt.Color;

class Windows31Theme.5
implements IEmptySpaceRenderer<T> {
    final /* synthetic */ boolean val$container;

    Windows31Theme.5(boolean bl) {
        this.val$container = bl;
    }

    @Override
    public void renderSpace(Context context, boolean focus, T state) {
        Color color = this.val$container ? Windows31Theme.this.getMainColor(focus, false) : Windows31Theme.this.getBackgroundColor(focus);
        context.getInterface().fillRect(context.getRect(), color, color, color, color);
    }
}
