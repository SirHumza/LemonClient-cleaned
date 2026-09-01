/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IResizeBorderRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Rectangle;

class GameSenseTheme.10
implements IResizeBorderRenderer {
    GameSenseTheme.10() {
    }

    @Override
    public void drawBorder(Context context, boolean focus) {
        Color color = ITheme.combineColors(GameSenseTheme.this.scheme.getColor("Outline Color"), GameSenseTheme.this.scheme.getColor("Enabled Color"));
        Rectangle rect = context.getRect();
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y, rect.width, this.getBorder()), color, color, color, color);
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y + rect.height - this.getBorder(), rect.width, this.getBorder()), color, color, color, color);
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y + this.getBorder(), this.getBorder(), rect.height - 2 * this.getBorder()), color, color, color, color);
        context.getInterface().fillRect(new Rectangle(rect.x + rect.width - this.getBorder(), rect.y + this.getBorder(), this.getBorder(), rect.height - 2 * this.getBorder()), color, color, color, color);
    }

    @Override
    public int getBorder() {
        return 2;
    }
}
