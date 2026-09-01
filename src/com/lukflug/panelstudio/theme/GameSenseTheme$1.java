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

class GameSenseTheme.1
implements IDescriptionRenderer {
    GameSenseTheme.1() {
    }

    @Override
    public void renderDescription(IInterface inter, Point pos, String text) {
        Rectangle rect = new Rectangle(pos, new Dimension(inter.getFontWidth(GameSenseTheme.this.height, text) + 4, GameSenseTheme.this.height + 4));
        Color color = GameSenseTheme.this.getMainColor(true, false);
        inter.fillRect(rect, color, color, color, color);
        inter.drawString(new Point(pos.x + 2, pos.y + 2), GameSenseTheme.this.height, text, GameSenseTheme.this.getFontColor(true));
        ITheme.drawRect(inter, rect, GameSenseTheme.this.scheme.getColor("Outline Color"));
    }
}
