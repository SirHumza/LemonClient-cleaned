/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IScrollSize;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import com.lukflug.panelstudio.widget.ScrollBarComponent;
import java.util.function.Supplier;

static final class ClosableComponent.7
extends ScrollBarComponent<U, T> {
    final /* synthetic */ IScrollSize val$scrollSize;
    final /* synthetic */ Supplier val$state;

    ClosableComponent.7(IComponent x0, IScrollBarRenderer x1, IEmptySpaceRenderer x2, IEmptySpaceRenderer x3, IScrollSize iScrollSize, Supplier supplier) {
        this.val$scrollSize = iScrollSize;
        this.val$state = supplier;
        super(x0, x1, x2, x3);
    }

    @Override
    public int getScrollHeight(Context context, int componentHeight) {
        return this.val$scrollSize.getScrollHeight(context, componentHeight);
    }

    @Override
    public int getComponentWidth(Context context) {
        return this.val$scrollSize.getComponentWidth(context);
    }

    @Override
    protected U getState() {
        return this.val$state.get();
    }
}
