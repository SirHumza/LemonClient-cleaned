/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;

class ClearTheme.5
implements IEmptySpaceRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    ClearTheme.5(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderSpace(Context context, boolean focus, T state) {
        ClearTheme.this.renderBackground(context, focus, this.val$graphicalLevel);
    }
}
