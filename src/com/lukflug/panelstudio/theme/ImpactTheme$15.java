/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.StandardColorPicker;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class ImpactTheme.15
extends StandardColorPicker {
    ImpactTheme.15() {
    }

    @Override
    public int getPadding() {
        return ImpactTheme.this.padding;
    }

    @Override
    public int getBaseHeight() {
        return ImpactTheme.this.getBaseHeight();
    }

    @Override
    public void renderCursor(Context context, Point p, Color color) {
        Color fontColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        context.getInterface().fillRect(new Rectangle(p.x - 1, p.y - 1, 2, 2), fontColor, fontColor, fontColor, fontColor);
    }
}
