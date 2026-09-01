/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.IToggleable;

class LemonClientGUI.1
implements IToggleable {
    LemonClientGUI.1() {
    }

    @Override
    public boolean isOn() {
        return val$module.isEnabled();
    }

    @Override
    public void toggle() {
        val$module.toggle();
    }
}
