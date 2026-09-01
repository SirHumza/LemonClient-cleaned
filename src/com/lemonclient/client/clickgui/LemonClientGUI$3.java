/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.client.module.modules.gui.ClickGuiModule;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.base.SimpleToggleable;

class LemonClientGUI.3
extends SimpleToggleable {
    final /* synthetic */ IToggleable val$guiToggle;
    final /* synthetic */ ClickGuiModule val$clickGuiModule;

    LemonClientGUI.3(boolean x0, IToggleable iToggleable, ClickGuiModule clickGuiModule) {
        this.val$guiToggle = iToggleable;
        this.val$clickGuiModule = clickGuiModule;
        super(x0);
    }

    @Override
    public boolean isOn() {
        if (this.val$guiToggle.isOn() && super.isOn()) {
            return (Boolean)this.val$clickGuiModule.showHUD.getValue();
        }
        return super.isOn();
    }
}
