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

class RainbowTheme.1
implements IDescriptionRenderer {
    RainbowTheme.1() {
    }

    @Override
    public void renderDescription(IInterface inter, Point pos, String text) {
        Rectangle rect = new Rectangle(pos, new Dimension(inter.getFontWidth(RainbowTheme.this.height, text) + 2, RainbowTheme.this.height + 2));
        Color color = RainbowTheme.this.getBackgroundColor(true);
        inter.fillRect(rect, color, color, color, color);
        inter.drawString(new Point(pos.x + 1, pos.y + 1), RainbowTheme.this.height, text, RainbowTheme.this.getFontColor(true));
    }
}
