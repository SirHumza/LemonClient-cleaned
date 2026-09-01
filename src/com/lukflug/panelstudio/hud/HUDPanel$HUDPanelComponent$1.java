/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.hud;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.hud.HUDPanel;

class HUDPanel.HUDPanelComponent.1
implements IToggleable {
    final /* synthetic */ HUDPanel val$this$0;
    final /* synthetic */ IToggleable val$state;

    HUDPanel.HUDPanelComponent.1(HUDPanel hUDPanel, IToggleable iToggleable) {
        this.val$this$0 = hUDPanel;
        this.val$state = iToggleable;
    }

    @Override
    public boolean isOn() {
        return this.val$state.isOn();
    }

    @Override
    public void toggle() {
    }
}
