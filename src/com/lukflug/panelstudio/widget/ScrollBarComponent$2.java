/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.ScrollableComponent;
import com.lukflug.panelstudio.theme.IEmptySpaceRenderer;
import java.awt.Rectangle;

class ScrollBarComponent.2
extends ScrollableComponent<T> {
    final /* synthetic */ IComponent val$component;
    final /* synthetic */ IEmptySpaceRenderer val$emptyRenderer;

    ScrollBarComponent.2(IComponent iComponent, IEmptySpaceRenderer iEmptySpaceRenderer) {
        this.val$component = iComponent;
        this.val$emptyRenderer = iEmptySpaceRenderer;
    }

    @Override
    public T getComponent() {
        return this.val$component;
    }

    @Override
    public int getScrollHeight(Context context, int height) {
        return ScrollBarComponent.this.getScrollHeight(context, height);
    }

    @Override
    public int getComponentWidth(Context context) {
        return ScrollBarComponent.this.getComponentWidth(context);
    }

    @Override
    public void fillEmptySpace(Context context, Rectangle rect) {
        Context subContext = new Context(context.getInterface(), rect.width, rect.getLocation(), context.hasFocus(), context.onTop());
        subContext.setHeight(rect.height);
        this.val$emptyRenderer.renderSpace(subContext, context.hasFocus(), ScrollBarComponent.this.getState());
    }
}
