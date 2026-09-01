/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.component.HorizontalComponent;
import com.lukflug.panelstudio.container.VerticalContainer;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;

class ScrollBarComponent.8
extends HorizontalComponent<VerticalContainer> {
    final /* synthetic */ IScrollBarRenderer val$renderer;

    ScrollBarComponent.8(VerticalContainer x0, int x1, int x2, IScrollBarRenderer iScrollBarRenderer) {
        this.val$renderer = iScrollBarRenderer;
        super(x0, x1, x2);
    }

    @Override
    public int getWidth(IInterface inter) {
        return this.val$renderer.getThickness();
    }
}
