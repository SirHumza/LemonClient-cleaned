/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Rectangle;

class Windows31Theme.3
implements IPanelRenderer<T> {
    Windows31Theme.3() {
    }

    @Override
    public void renderBackground(Context context, boolean focus) {
        Rectangle rect = context.getRect();
        Color c = Windows31Theme.this.getMainColor(focus, false);
        context.getInterface().fillRect(new Rectangle(rect.x + 3, rect.y + 3, rect.width - 6, rect.height - 6), c, c, c, c);
    }

    @Override
    public int getBorder() {
        return 1;
    }

    @Override
    public int getLeft() {
        return 4;
    }

    @Override
    public int getRight() {
        return 4;
    }

    @Override
    public int getTop() {
        return 4;
    }

    @Override
    public int getBottom() {
        return 4;
    }

    @Override
    public void renderPanelOverlay(Context context, boolean focus, T state, boolean open) {
        Rectangle rect = context.getRect();
        ITheme.drawRect(context.getInterface(), rect, Windows31Theme.this.getFontColor(focus));
        ITheme.drawRect(context.getInterface(), new Rectangle(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2), Windows31Theme.this.getMainColor(focus, focus));
        ITheme.drawRect(context.getInterface(), new Rectangle(rect.x + 2, rect.y + 2, rect.width - 4, rect.height - 4), Windows31Theme.this.getMainColor(focus, focus));
    }

    @Override
    public void renderTitleOverlay(Context context, boolean focus, T state, boolean open) {
    }
}
