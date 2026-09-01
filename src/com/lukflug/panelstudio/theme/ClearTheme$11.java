/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IResizeBorderRenderer;
import java.awt.Color;
import java.awt.Rectangle;

class ClearTheme.11
implements IResizeBorderRenderer {
    ClearTheme.11() {
    }

    @Override
    public void drawBorder(Context context, boolean focus) {
        Color color = ClearTheme.this.getBackgroundColor(focus);
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
