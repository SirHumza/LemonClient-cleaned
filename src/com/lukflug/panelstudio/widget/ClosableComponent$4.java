/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.DraggableComponent;
import com.lukflug.panelstudio.component.FixedComponent;
import com.lukflug.panelstudio.widget.ClosableComponent;
import com.lukflug.panelstudio.widget.ScrollBarComponent;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntSupplier;

static final class ClosableComponent.4
extends DraggableComponent<FixedComponent<ClosableComponent<ComponentProxy<S>, ScrollBarComponent<U, T>>>> {
    FixedComponent<ClosableComponent<ComponentProxy<S>, ScrollBarComponent<U, T>>> fixedComponent = null;
    final /* synthetic */ boolean val$closeOnClick;
    final /* synthetic */ IToggleable val$shown;
    final /* synthetic */ AtomicReference val$panel;
    final /* synthetic */ IntSupplier val$widthSupplier;
    final /* synthetic */ boolean val$savesState;
    final /* synthetic */ String val$configName;

    ClosableComponent.4(boolean bl, IToggleable iToggleable, AtomicReference atomicReference, IntSupplier intSupplier, boolean bl2, String string) {
        this.val$closeOnClick = bl;
        this.val$shown = iToggleable;
        this.val$panel = atomicReference;
        this.val$widthSupplier = intSupplier;
        this.val$savesState = bl2;
        this.val$configName = string;
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        if (context.getInterface().getButton(button) && (!context.isHovered() || this.val$closeOnClick) && this.val$shown.isOn()) {
            this.val$shown.toggle();
        }
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && this.val$shown.isOn();
    }

    @Override
    public FixedComponent<ClosableComponent<ComponentProxy<S>, ScrollBarComponent<U, T>>> getComponent() {
        if (this.fixedComponent == null) {
            this.fixedComponent = new FixedComponent<ClosableComponent<ComponentProxy<S>, ScrollBarComponent<U, T>>>((ClosableComponent)this.val$panel.get(), new Point(0, 0), this.val$widthSupplier.getAsInt(), ((ClosableComponent)this.val$panel.get()).getCollapsible().getToggle(), this.val$savesState, this.val$configName){

                @Override
                public int getWidth(IInterface inter) {
                    return val$widthSupplier.getAsInt();
                }
            };
        }
        return this.fixedComponent;
    }
}
