/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.container;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IComponent;

@FunctionalInterface
protected static interface Container.ContextSensitiveConsumer<T extends IComponent> {
    public void accept(Context var1, T var2);
}
