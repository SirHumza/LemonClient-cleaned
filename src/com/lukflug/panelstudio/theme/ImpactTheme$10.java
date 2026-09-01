/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class ImpactTheme.10
implements IRadioRenderer {
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.10(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderItem(Context context, ILabeled[] items, boolean focus, int target, double state, boolean horizontal) {
        if (this.val$graphicalLevel <= 0) {
            ImpactTheme.this.renderBackground(context, focus);
        }
        for (int i = 0; i < items.length; ++i) {
            Rectangle rect = this.getItemRect(context, items, i, horizontal);
            Context subContext = new Context(context.getInterface(), rect.width, rect.getLocation(), context.hasFocus(), context.onTop());
            subContext.setHeight(rect.height);
            ImpactTheme.this.renderOverlay(subContext);
            Color color = ImpactTheme.this.getFontColor(focus);
            if (i == target) {
                color = ImpactTheme.this.scheme.getColor("Active Font Color");
            } else if (!subContext.isHovered()) {
                color = ImpactTheme.this.scheme.getColor("Inactive Font Color");
            }
            context.getInterface().drawString(new Point(rect.x + ImpactTheme.this.padding, rect.y + ImpactTheme.this.padding), ImpactTheme.this.height, items[i].getDisplayName(), color);
        }
    }

    @Override
    public int getDefaultHeight(ILabeled[] items, boolean horizontal) {
        return (horizontal ? 1 : items.length) * ImpactTheme.this.getBaseHeight();
    }
}
