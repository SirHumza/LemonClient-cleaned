/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.container;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.container.Container;

protected static final class Container.ComponentState {
    public final T component;
    public final IBoolean externalVisibility;
    private boolean lastVisible = false;
    final /* synthetic */ Container this$0;

    public Container.ComponentState(T component, IBoolean externalVisibility) {
        this.this$0 = this$0;
        this.component = component;
        this.externalVisibility = externalVisibility;
        this.update();
    }

    public void update() {
        if ((this.component.isVisible() && this.externalVisibility.isOn() && this.this$0.visible) != this.lastVisible) {
            if (this.lastVisible) {
                this.lastVisible = false;
                this.component.exit();
            } else {
                this.lastVisible = true;
                this.component.enter();
            }
        }
    }

    public boolean lastVisible() {
        return this.lastVisible;
    }

    static /* synthetic */ boolean access$000(Container.ComponentState x0) {
        return x0.lastVisible;
    }
}
