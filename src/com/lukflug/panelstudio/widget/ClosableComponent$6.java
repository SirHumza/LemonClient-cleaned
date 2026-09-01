/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.component.DraggableComponent;
import com.lukflug.panelstudio.component.FixedComponent;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.widget.ClosableComponent;
import java.awt.Point;
import java.util.function.Supplier;

static final class ClosableComponent.6
extends DraggableComponent<FixedComponent<ClosableComponent<S, T>>> {
    FixedComponent<ClosableComponent<S, T>> fixedComponent = null;
    final /* synthetic */ Supplier val$panel;
    final /* synthetic */ Point val$position;
    final /* synthetic */ int val$width;
    final /* synthetic */ boolean val$savesState;
    final /* synthetic */ String val$configName;

    ClosableComponent.6(Supplier supplier, Point point, int n, boolean bl, String string) {
        this.val$panel = supplier;
        this.val$position = point;
        this.val$width = n;
        this.val$savesState = bl;
        this.val$configName = string;
    }

    @Override
    public FixedComponent<ClosableComponent<S, T>> getComponent() {
        if (this.fixedComponent == null) {
            this.fixedComponent = new FixedComponent<IComponent>((IComponent)this.val$panel.get(), this.val$position, this.val$width, ((ClosableComponent)this.val$panel.get()).getCollapsible().getToggle(), this.val$savesState, this.val$configName);
        }
        return this.fixedComponent;
    }
}
