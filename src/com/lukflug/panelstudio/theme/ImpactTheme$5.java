/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;

class ImpactTheme.5
implements IEmptySpaceRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.5(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderSpace(Context context, boolean focus, T state) {
        if (this.val$graphicalLevel == 0) {
            ImpactTheme.this.renderBackground(context, focus);
        }
    }
}
