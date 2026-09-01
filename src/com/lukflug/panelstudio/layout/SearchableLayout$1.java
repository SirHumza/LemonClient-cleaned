/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.setting.IBooleanSetting;
import com.lukflug.panelstudio.setting.IModule;

class SearchableLayout.1
implements IBooleanSetting {
    final /* synthetic */ IModule val$module;

    SearchableLayout.1(IModule iModule) {
        this.val$module = iModule;
    }

    @Override
    public String getDisplayName() {
        return SearchableLayout.this.enabledButton;
    }

    @Override
    public void toggle() {
        this.val$module.isEnabled().toggle();
    }

    @Override
    public boolean isOn() {
        return this.val$module.isEnabled().isOn();
    }
}
