/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.theme.IDescriptionRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

class Windows31Theme.1
implements IDescriptionRenderer {
    Windows31Theme.1() {
    }

    @Override
    public void renderDescription(IInterface inter, Point pos, String text) {
        Rectangle rect = new Rectangle(pos, new Dimension(inter.getFontWidth(Windows31Theme.this.height, text) + 4, Windows31Theme.this.height + 4));
        Color color = Windows31Theme.this.getMainColor(true, false);
        inter.fillRect(rect, color, color, color, color);
        inter.drawString(new Point(pos.x + 2, pos.y + 2), Windows31Theme.this.height, text, Windows31Theme.this.getFontColor(true));
        ITheme.drawRect(inter, rect, Windows31Theme.this.getMainColor(true, true));
    }
}
