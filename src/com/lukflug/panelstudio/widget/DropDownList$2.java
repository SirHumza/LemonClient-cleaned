/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.IStringSetting;
import java.util.concurrent.atomic.AtomicReference;

class DropDownList.2
implements IStringSetting {
    final /* synthetic */ IEnumSetting val$setting;
    final /* synthetic */ boolean val$allowSearch;
    final /* synthetic */ AtomicReference val$searchTerm;

    DropDownList.2(IEnumSetting iEnumSetting, boolean bl, AtomicReference atomicReference) {
        this.val$setting = iEnumSetting;
        this.val$allowSearch = bl;
        this.val$searchTerm = atomicReference;
    }

    @Override
    public String getDisplayName() {
        return this.val$setting.getDisplayName();
    }

    @Override
    public String getValue() {
        String returnValue = this.val$allowSearch && DropDownList.this.toggle.isOn() ? (String)this.val$searchTerm.get() : this.val$setting.getValueName();
        this.val$searchTerm.set(returnValue);
        return returnValue;
    }

    @Override
    public void setValue(String string) {
        this.val$searchTerm.set(string);
    }
}
