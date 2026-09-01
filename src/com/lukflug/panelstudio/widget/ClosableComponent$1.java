/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.AnimatedToggleable;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.container.VerticalContainer;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IContainerRenderer;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import java.util.function.Supplier;

class ClosableComponent.1
extends VerticalContainer {
    final /* synthetic */ IPanelRenderer val$panelRenderer;
    final /* synthetic */ Supplier val$state;
    final /* synthetic */ AnimatedToggleable val$open;

    ClosableComponent.1(ILabeled x0, IContainerRenderer x1, IPanelRenderer iPanelRenderer, Supplier supplier, AnimatedToggleable animatedToggleable) {
        this.val$panelRenderer = iPanelRenderer;
        this.val$state = supplier;
        this.val$open = animatedToggleable;
        super(x0, x1);
    }

    @Override
    public void render(Context context) {
        super.render(context);
        this.val$panelRenderer.renderPanelOverlay(context, this.hasFocus(context), this.val$state.get(), this.val$open.isOn());
    }

    @Override
    protected boolean hasFocus(Context context) {
        return ClosableComponent.this.hasFocus(context);
    }
}
