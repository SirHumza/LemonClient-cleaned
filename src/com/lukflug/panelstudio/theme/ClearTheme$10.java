/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import java.awt.Point;
import java.awt.Rectangle;

class ClearTheme.10
implements IRadioRenderer {
    final /* synthetic */ int val$graphicalLevel;

    ClearTheme.10(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderItem(Context context, ILabeled[] items, boolean focus, int target, double state, boolean horizontal) {
        ClearTheme.this.renderBackground(context, focus, this.val$graphicalLevel);
        for (int i = 0; i < items.length; ++i) {
            Rectangle rect = this.getItemRect(context, items, i, horizontal);
            Context subContext = new Context(context.getInterface(), rect.width, rect.getLocation(), context.hasFocus(), context.onTop());
            subContext.setHeight(rect.height);
            ClearTheme.this.renderOverlay(subContext);
            context.getInterface().drawString(new Point(rect.x + ClearTheme.this.padding, rect.y + ClearTheme.this.padding), ClearTheme.this.height, items[i].getDisplayName(), i == target ? ClearTheme.this.getMainColor(focus, true) : ClearTheme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight(ILabeled[] items, boolean horizontal) {
        return (horizontal ? 1 : items.length) * ClearTheme.this.getBaseHeight();
    }
}
