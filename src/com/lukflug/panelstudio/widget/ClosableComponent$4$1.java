/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.FixedComponent;
import com.lukflug.panelstudio.widget.ClosableComponent;
import com.lukflug.panelstudio.widget.ScrollBarComponent;
import java.awt.Point;

class ClosableComponent.1
extends FixedComponent<ClosableComponent<ComponentProxy<S>, ScrollBarComponent<U, T>>> {
    ClosableComponent.1(ClosableComponent component, Point position, int width, IToggleable state, boolean savesState, String configName) {
        super(component, position, width, state, savesState, configName);
    }

    @Override
    public int getWidth(IInterface inter) {
        return val$widthSupplier.getAsInt();
    }
}
