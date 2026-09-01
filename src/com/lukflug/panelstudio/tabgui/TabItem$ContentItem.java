/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.tabgui;

import java.util.function.Supplier;

protected static final class TabItem.ContentItem<S extends Supplier<T>, T> {
    public final String name;
    public final S content;

    public TabItem.ContentItem(String name, S content) {
        this.name = name;
        this.content = content;
    }
}
