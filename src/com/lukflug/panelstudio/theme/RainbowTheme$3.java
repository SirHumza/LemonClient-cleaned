/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import java.awt.Color;
import java.awt.Rectangle;

class RainbowTheme.3
implements IPanelRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    RainbowTheme.3(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public int getBorder() {
        return this.val$graphicalLevel == 0 ? 1 : 0;
    }

    @Override
    public void renderPanelOverlay(Context context, boolean focus, T state, boolean open) {
    }

    @Override
    public void renderTitleOverlay(Context context, boolean focus, T state, boolean open) {
        if (this.val$graphicalLevel <= 0) {
            Color color = RainbowTheme.this.getFontColor(focus);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height, context.getSize().width, 1), color, color, color, color);
        } else {
            Rectangle rect = context.getRect();
            rect = new Rectangle(rect.width - rect.height, 0, rect.height, rect.height);
            if (rect.width % 2 != 0) {
                --rect.width;
                --rect.height;
                ++rect.x;
            }
            Context subContext = new Context(context, rect.width, rect.getLocation(), true, true);
            subContext.setHeight(rect.height);
            if (open) {
                RainbowTheme.this.renderSmallButton(subContext, null, 7, focus);
            } else {
                RainbowTheme.this.renderSmallButton(subContext, null, 5, focus);
            }
        }
    }
}
