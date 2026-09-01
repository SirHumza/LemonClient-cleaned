/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.hud;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.hud.HUDPanel;
import java.awt.Point;

class HUDPanel.HUDPanelComponent.3
extends ComponentProxy<T> {
    final /* synthetic */ HUDPanel val$this$0;
    final /* synthetic */ int val$border;

    HUDPanel.HUDPanelComponent.3(IFixedComponent x0, HUDPanel hUDPanel, int n) {
        this.val$this$0 = hUDPanel;
        this.val$border = n;
        super(x0);
    }

    @Override
    public int getHeight(int height) {
        return height + 2 * this.val$border;
    }

    @Override
    public Context getContext(Context context) {
        return new Context(context, context.getSize().width - 2 * this.val$border, new Point(this.val$border, this.val$border), context.hasFocus(), context.onTop());
    }
}
