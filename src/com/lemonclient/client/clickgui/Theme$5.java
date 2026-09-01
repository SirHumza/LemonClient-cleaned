/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;

class Theme.5
implements IEmptySpaceRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    Theme.5(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderSpace(Context context, boolean focus, T state) {
        Theme.this.renderBackground(context, focus, this.val$graphicalLevel);
    }
}
