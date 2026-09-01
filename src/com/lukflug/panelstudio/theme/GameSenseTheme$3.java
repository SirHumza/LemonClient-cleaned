/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import com.lukflug.panelstudio.theme.ITheme;

class GameSenseTheme.3
implements IPanelRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.3(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public int getLeft() {
        return this.val$graphicalLevel == 0 ? 1 : 0;
    }

    @Override
    public int getRight() {
        return this.val$graphicalLevel == 0 ? 1 : 0;
    }

    @Override
    public void renderPanelOverlay(Context context, boolean focus, T state, boolean open) {
        if (this.val$graphicalLevel == 0) {
            ITheme.drawRect(context.getInterface(), context.getRect(), GameSenseTheme.this.scheme.getColor("Outline Color"));
        }
    }

    @Override
    public void renderTitleOverlay(Context context, boolean focus, T state, boolean open) {
    }
}
