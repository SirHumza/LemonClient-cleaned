/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.client.module.Module;
import com.lukflug.panelstudio.base.IToggleable;

class LemonClientGUI.5
implements IToggleable {
    final /* synthetic */ Module val$module;

    LemonClientGUI.5(Module module) {
        this.val$module = module;
    }

    @Override
    public boolean isOn() {
        return this.val$module.isEnabled();
    }

    @Override
    public void toggle() {
        this.val$module.toggle();
    }
}
