/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import java.awt.Point;
import java.awt.Rectangle;

class GameSenseTheme.9
implements IRadioRenderer {
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.9(int n, int n2) {
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
    }

    @Override
    public void renderItem(Context context, ILabeled[] items, boolean focus, int target, double state, boolean horizontal) {
        for (int i = 0; i < items.length; ++i) {
            Rectangle rect = this.getItemRect(context, items, i, horizontal);
            Context subContext = new Context(context.getInterface(), rect.width, rect.getLocation(), context.hasFocus(), context.onTop());
            subContext.setHeight(rect.height);
            GameSenseTheme.this.fillBaseRect(subContext, focus, i == target, this.val$logicalLevel, this.val$graphicalLevel, null);
            GameSenseTheme.this.renderOverlay(subContext);
            context.getInterface().drawString(new Point(rect.x + GameSenseTheme.this.padding, rect.y + GameSenseTheme.this.padding), GameSenseTheme.this.height, items[i].getDisplayName(), GameSenseTheme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight(ILabeled[] items, boolean horizontal) {
        return (horizontal ? 1 : items.length) * GameSenseTheme.this.getBaseHeight();
    }
}
