/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IContainerRenderer;

class Theme.2
implements IContainerRenderer {
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ boolean val$horizontal;

    Theme.2(int n, boolean bl) {
        this.val$graphicalLevel = n;
        this.val$horizontal = bl;
    }

    @Override
    public void renderBackground(Context context, boolean focus) {
        Theme.this.renderBackground(context, focus, this.val$graphicalLevel);
    }

    @Override
    public int getBorder() {
        return this.val$horizontal ? 0 : Theme.this.border;
    }

    @Override
    public int getTop() {
        return this.val$horizontal ? 0 : Theme.this.border;
    }
}
