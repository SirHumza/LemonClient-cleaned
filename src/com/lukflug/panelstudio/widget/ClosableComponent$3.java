/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.AnimatedToggleable;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import java.util.function.Supplier;

class ClosableComponent.3
extends ComponentProxy<IComponent> {
    final /* synthetic */ IPanelRenderer val$panelRenderer;
    final /* synthetic */ Supplier val$state;
    final /* synthetic */ AnimatedToggleable val$open;

    ClosableComponent.3(IComponent x0, IPanelRenderer iPanelRenderer, Supplier supplier, AnimatedToggleable animatedToggleable) {
        this.val$panelRenderer = iPanelRenderer;
        this.val$state = supplier;
        this.val$open = animatedToggleable;
        super(x0);
    }

    @Override
    public void render(Context context) {
        super.render(context);
        this.val$panelRenderer.renderTitleOverlay(context, ClosableComponent.this.hasFocus(context), this.val$state.get(), this.val$open.isOn());
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        if (button == 1 && context.isClicked(button)) {
            ClosableComponent.this.collapsible.getToggle().toggle();
        }
    }
}
