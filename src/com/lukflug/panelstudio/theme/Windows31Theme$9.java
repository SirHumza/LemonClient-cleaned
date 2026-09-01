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

class Windows31Theme.9
implements IRadioRenderer {
    Windows31Theme.9() {
    }

    @Override
    public void renderItem(Context context, ILabeled[] items, boolean focus, int target, double state, boolean horizontal) {
        for (int i = 0; i < items.length; ++i) {
            Rectangle rect = this.getItemRect(context, items, i, horizontal);
            Color color = Windows31Theme.this.getMainColor(focus, true);
            if (i == target) {
                context.getInterface().fillRect(rect, color, color, color, color);
            }
            context.getInterface().drawString(new Point(rect.x + Windows31Theme.this.padding, rect.y + Windows31Theme.this.padding), Windows31Theme.this.height, items[i].getDisplayName(), i == target ? Windows31Theme.this.getMainColor(focus, false) : Windows31Theme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight(ILabeled[] items, boolean horizontal) {
        return (horizontal ? 1 : items.length) * Windows31Theme.this.getBaseHeight();
    }
}
