/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import java.awt.Point;
import java.awt.Rectangle;

class Theme.10
implements IRadioRenderer {
    final /* synthetic */ int val$graphicalLevel;

    Theme.10(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderItem(Context context, ILabeled[] items, boolean focus, int target, double state, boolean horizontal) {
        Theme.this.renderBackground(context, focus, this.val$graphicalLevel);
        for (int i = 0; i < items.length; ++i) {
            Rectangle rect = this.getItemRect(context, items, i, horizontal);
            Context subContext = new Context(context.getInterface(), rect.width, rect.getLocation(), context.hasFocus(), context.onTop());
            subContext.setHeight(rect.height);
            Theme.this.renderOverlay(subContext);
            context.getInterface().drawString(new Point(rect.x + Theme.this.padding, rect.y + Theme.this.padding), Theme.this.height, items[i].getDisplayName(), i == target ? Theme.this.getMainColor(focus, true) : Theme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight(ILabeled[] items, boolean horizontal) {
        return (horizontal ? 1 : items.length) * Theme.this.getBaseHeight();
    }
}
