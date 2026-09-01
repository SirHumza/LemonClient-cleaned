/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.IStringSetting;
import java.util.concurrent.atomic.AtomicReference;

class SearchableRadioButton.2
implements IStringSetting {
    final /* synthetic */ IEnumSetting val$setting;
    final /* synthetic */ AtomicReference val$searchTerm;

    SearchableRadioButton.2(IEnumSetting iEnumSetting, AtomicReference atomicReference) {
        this.val$setting = iEnumSetting;
        this.val$searchTerm = atomicReference;
    }

    @Override
    public String getDisplayName() {
        return this.val$setting.getDisplayName();
    }

    @Override
    public String getValue() {
        return (String)this.val$searchTerm.get();
    }

    @Override
    public void setValue(String string) {
        this.val$searchTerm.set(string);
    }
}
