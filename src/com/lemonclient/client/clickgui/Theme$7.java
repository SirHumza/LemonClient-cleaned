/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;

class Theme.7
implements IButtonRenderer<Void> {
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$symbol;

    Theme.7(int n, boolean bl, int n2, int n3) {
        this.val$graphicalLevel = n;
        this.val$container = bl;
        this.val$logicalLevel = n2;
        this.val$symbol = n3;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Void state) {
        Theme.this.renderBackground(context, focus, this.val$graphicalLevel);
        Theme.this.renderOverlay(context);
        if (!this.val$container || this.val$logicalLevel <= 0) {
            Theme.this.renderSmallButton(context, title, this.val$symbol, focus);
        }
    }

    @Override
    public int getDefaultHeight() {
        return Theme.this.getBaseHeight();
    }
}
