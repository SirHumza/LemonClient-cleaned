/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.AnimatedToggleable;
import com.lukflug.panelstudio.component.CollapsibleComponent;
import com.lukflug.panelstudio.component.IComponent;

class ClosableComponent.2
extends CollapsibleComponent<T> {
    final /* synthetic */ IComponent val$content;

    ClosableComponent.2(AnimatedToggleable x0, IComponent iComponent) {
        this.val$content = iComponent;
        super(x0);
    }

    @Override
    public T getComponent() {
        return this.val$content;
    }
}
