/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.Labeled;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

class DropDownList.4
implements IEnumSetting {
    ILabeled[] values;
    final /* synthetic */ IEnumSetting val$setting;
    final /* synthetic */ boolean val$allowSearch;
    final /* synthetic */ AtomicReference val$searchTerm;

    DropDownList.4(IEnumSetting iEnumSetting, boolean bl, AtomicReference atomicReference) {
        this.val$setting = iEnumSetting;
        this.val$allowSearch = bl;
        this.val$searchTerm = atomicReference;
        this.values = (ILabeled[])Arrays.stream(this.val$setting.getAllowedValues()).map(value -> new Labeled(value.getDisplayName(), value.getDescription(), () -> {
            if (!value.isVisible().isOn()) {
                return false;
            }
            if (!this.val$allowSearch) {
                return true;
            }
            return value.getDisplayName().toUpperCase().contains(((String)this.val$searchTerm.get()).toUpperCase());
        })).toArray(ILabeled[]::new);
    }

    @Override
    public String getDisplayName() {
        return this.val$setting.getDisplayName();
    }

    @Override
    public String getDescription() {
        return this.val$setting.getDescription();
    }

    @Override
    public IBoolean isVisible() {
        return this.val$setting.isVisible();
    }

    @Override
    public void increment() {
        this.val$setting.increment();
    }

    @Override
    public void decrement() {
        this.val$setting.decrement();
    }

    @Override
    public String getValueName() {
        return this.val$setting.getValueName();
    }

    @Override
    public void setValueIndex(int index) {
        this.val$setting.setValueIndex(index);
    }

    @Override
    public ILabeled[] getAllowedValues() {
        return this.values;
    }
}
