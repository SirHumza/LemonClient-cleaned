/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.ILabeled;
import java.util.stream.Stream;

class SearchableLayout.2
implements IEnumSetting {
    private int state = 0;
    private ILabeled[] array = (ILabeled[])this.val$labels.toArray(ILabeled[]::new);
    final /* synthetic */ Stream val$labels;
    final /* synthetic */ ILabeled val$label;

    SearchableLayout.2(Stream stream, ILabeled iLabeled) {
        this.val$labels = stream;
        this.val$label = iLabeled;
    }

    @Override
    public String getDisplayName() {
        return this.val$label.getDisplayName();
    }

    @Override
    public String getDescription() {
        return this.val$label.getDescription();
    }

    @Override
    public IBoolean isVisible() {
        return this.val$label.isVisible();
    }

    @Override
    public void increment() {
        this.state = (this.state + 1) % this.array.length;
    }

    @Override
    public void decrement() {
        --this.state;
        if (this.state < 0) {
            this.state = this.array.length - 1;
        }
    }

    @Override
    public String getValueName() {
        return this.array[this.state].getDisplayName();
    }

    @Override
    public void setValueIndex(int index) {
        this.state = index;
    }

    @Override
    public int getValueIndex() {
        return this.state;
    }

    @Override
    public ILabeled[] getAllowedValues() {
        return this.array;
    }
}
