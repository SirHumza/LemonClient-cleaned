/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.component.ScrollableComponent;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IScrollBarRenderer;
import com.lukflug.panelstudio.widget.ScrollBar;

class ScrollBarComponent.4
extends ScrollBar<S> {
    final /* synthetic */ ScrollableComponent val$scrollComponent;

    ScrollBarComponent.4(ILabeled label, boolean horizontal, IScrollBarRenderer renderer, ScrollableComponent scrollableComponent) {
        this.val$scrollComponent = scrollableComponent;
        super(label, horizontal, renderer);
    }

    @Override
    protected int getLength() {
        return this.val$scrollComponent.getScrollSize().width;
    }

    @Override
    protected int getContentHeight() {
        return this.val$scrollComponent.getContentSize().width;
    }

    @Override
    protected int getScrollPosition() {
        return this.val$scrollComponent.getScrollPos().x;
    }

    @Override
    protected void setScrollPosition(int position) {
        this.val$scrollComponent.setScrollPosX(position);
    }

    @Override
    protected S getState() {
        return ScrollBarComponent.this.getState();
    }
}
