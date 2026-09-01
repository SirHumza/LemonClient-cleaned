/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.theme.IDescriptionRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

class Theme.1
implements IDescriptionRenderer {
    Theme.1() {
    }

    @Override
    public void renderDescription(IInterface inter, Point pos, String text) {
        Rectangle rect = new Rectangle(pos, new Dimension(inter.getFontWidth(Theme.this.height, text) + 2, Theme.this.height + 2));
        Color color = Theme.this.getBackgroundColor(true);
        inter.fillRect(rect, color, color, color, color);
        inter.drawString(new Point(pos.x + 1, pos.y + 1), Theme.this.height, text, Theme.this.getFontColor(true));
    }
}
