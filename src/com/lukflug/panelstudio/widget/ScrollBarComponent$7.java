/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;
import com.lukflug.panelstudio.widget.EmptySpace;
import java.util.function.Supplier;

class ScrollBarComponent.7
extends EmptySpace<S> {
    ScrollBarComponent.7(ILabeled x0, Supplier x1, IEmptySpaceRenderer x2) {
        super(x0, x1, x2);
    }

    @Override
    protected S getState() {
        return ScrollBarComponent.this.getState();
    }
}
