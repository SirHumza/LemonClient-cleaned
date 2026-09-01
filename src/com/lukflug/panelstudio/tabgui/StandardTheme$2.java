/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.tabgui.StandardTheme;
import com.lukflug.panelstudio.theme.IColorScheme;
import java.awt.Color;

class StandardTheme.2
extends StandardTheme.RendererBase<Boolean> {
    final /* synthetic */ IColorScheme val$scheme;

    StandardTheme.2(IColorScheme iColorScheme) {
        this.val$scheme = iColorScheme;
        super(StandardTheme.this);
    }

    @Override
    protected Color getFontColor(Boolean itemState) {
        if (itemState.booleanValue()) {
            return this.val$scheme.getColor("Enabled Color");
        }
        return this.val$scheme.getColor("Font Color");
    }
}
