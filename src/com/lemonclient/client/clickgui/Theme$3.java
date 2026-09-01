/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import java.awt.Rectangle;

class Theme.3
implements IPanelRenderer<T> {
    final /* synthetic */ int val$graphicalLevel;

    Theme.3(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderPanelOverlay(Context context, boolean focus, T state, boolean open) {
    }

    @Override
    public void renderTitleOverlay(Context context, boolean focus, T state, boolean open) {
        if (this.val$graphicalLevel > 0) {
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
                Theme.this.renderSmallButton(subContext, null, 7, focus);
            } else {
                Theme.this.renderSmallButton(subContext, null, 5, focus);
            }
        }
    }
}
