/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IContainerRenderer;

class ImpactTheme.2
implements IContainerRenderer {
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.2(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderBackground(Context context, boolean focus) {
        if (this.val$graphicalLevel == 0) {
            ImpactTheme.this.renderBackground(context, focus);
        }
    }

    @Override
    public int getBorder() {
        return 2;
    }

    @Override
    public int getLeft() {
        return 2;
    }

    @Override
    public int getRight() {
        return 2;
    }

    @Override
    public int getTop() {
        return 2;
    }

    @Override
    public int getBottom() {
        return 2;
    }
}
