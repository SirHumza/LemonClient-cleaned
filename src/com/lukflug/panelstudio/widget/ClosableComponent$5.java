/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.PopupComponent;
import com.lukflug.panelstudio.widget.ClosableComponent;
import com.lukflug.panelstudio.widget.ScrollBarComponent;

static final class ClosableComponent.5
extends PopupComponent<ClosableComponent<S, ScrollBarComponent<U, T>>> {
    final /* synthetic */ IToggleable val$shown;

    ClosableComponent.5(ClosableComponent component, int width, IToggleable iToggleable) {
        this.val$shown = iToggleable;
        super(component, width);
    }

    @Override
    public void handleButton(Context context, int button) {
        this.doOperation(context, subContext -> ((ClosableComponent)this.getComponent()).handleButton((Context)subContext, button));
        if (context.getInterface().getButton(button) && !context.isHovered() && this.val$shown.isOn()) {
            this.val$shown.toggle();
        }
    }

    @Override
    public boolean isVisible() {
        return ((ClosableComponent)this.getComponent()).isVisible() && this.val$shown.isOn();
    }
}
