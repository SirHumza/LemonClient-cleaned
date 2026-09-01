/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.theme.IDescriptionRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

class ImpactTheme.1
implements IDescriptionRenderer {
    ImpactTheme.1() {
    }

    @Override
    public void renderDescription(IInterface inter, Point pos, String text) {
        Rectangle rect = new Rectangle(pos, new Dimension(inter.getFontWidth(ImpactTheme.this.height, text) + 2 * ImpactTheme.this.padding - 2, ImpactTheme.this.height + 2 * ImpactTheme.this.padding - 2));
        Color color = ImpactTheme.this.scheme.getColor("Tooltip Color");
        inter.fillRect(rect, color, color, color, color);
        inter.drawString(new Point(pos.x + ImpactTheme.this.padding - 1, pos.y + ImpactTheme.this.padding - 1), ImpactTheme.this.height, text, ImpactTheme.this.getFontColor(true));
    }
}
