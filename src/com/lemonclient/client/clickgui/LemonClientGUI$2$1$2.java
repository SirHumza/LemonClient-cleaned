/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.setting.IBooleanSetting;

class LemonClientGUI.2
implements IBooleanSetting {
    LemonClientGUI.2() {
    }

    @Override
    public String getDisplayName() {
        return "Toggle Msgs";
    }

    @Override
    public void toggle() {
        val$module.setToggleMsg(!val$module.isToggleMsg());
    }

    @Override
    public boolean isOn() {
        return val$module.isToggleMsg();
    }
}
