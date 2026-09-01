/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import com.lukflug.panelstudio.widget.ScrollBarComponent;

class SinglePanelAdder.2
extends ScrollBarComponent<Void, T> {
    SinglePanelAdder.2(IComponent x0, IScrollBarRenderer x1, IEmptySpaceRenderer x2, IEmptySpaceRenderer x3) {
        super(x0, x1, x2, x3);
    }

    @Override
    public int getScrollHeight(Context context, int componentHeight) {
        return SinglePanelAdder.this.size.getScrollHeight(context, componentHeight);
    }

    @Override
    protected Void getState() {
        return null;
    }
}
