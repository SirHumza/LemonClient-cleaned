/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.tabgui.StandardTheme;
import com.lukflug.panelstudio.theme.IColorScheme;
import java.awt.Color;

class StandardTheme.1
extends StandardTheme.RendererBase<Void> {
    final /* synthetic */ IColorScheme val$scheme;

    StandardTheme.1(IColorScheme iColorScheme) {
        this.val$scheme = iColorScheme;
        super(StandardTheme.this);
    }

    @Override
    protected Color getFontColor(Void itemState) {
        return this.val$scheme.getColor("Font Color");
    }
}
