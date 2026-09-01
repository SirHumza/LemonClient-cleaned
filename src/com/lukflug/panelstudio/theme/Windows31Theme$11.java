/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IResizeBorderRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Rectangle;

class Windows31Theme.11
implements IResizeBorderRenderer {
    Windows31Theme.11() {
    }

    @Override
    public void drawBorder(Context context, boolean focus) {
        Color color = Windows31Theme.this.getBackgroundColor(focus);
        Rectangle rect = context.getRect();
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y, rect.width, this.getBorder()), color, color, color, color);
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y + rect.height - this.getBorder(), rect.width, this.getBorder()), color, color, color, color);
        context.getInterface().fillRect(new Rectangle(rect.x, rect.y + this.getBorder(), this.getBorder(), rect.height - 2 * this.getBorder()), color, color, color, color);
        context.getInterface().fillRect(new Rectangle(rect.x + rect.width - this.getBorder(), rect.y + this.getBorder(), this.getBorder(), rect.height - 2 * this.getBorder()), color, color, color, color);
        Color borderColor = Windows31Theme.this.getFontColor(focus);
        ITheme.drawRect(context.getInterface(), rect, borderColor);
        ITheme.drawRect(context.getInterface(), new Rectangle(rect.x, rect.y + this.getBorder(), rect.width, rect.height - 2 * this.getBorder()), borderColor);
        ITheme.drawRect(context.getInterface(), new Rectangle(rect.x + this.getBorder(), rect.y, rect.width - 2 * this.getBorder(), rect.height), borderColor);
    }

    @Override
    public int getBorder() {
        return 4;
    }
}
