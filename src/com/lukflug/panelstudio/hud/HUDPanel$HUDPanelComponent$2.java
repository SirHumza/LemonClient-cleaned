/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.hud;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.hud.HUDPanel;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.theme.IButtonRendererProxy;

class HUDPanel.HUDPanelComponent.2
implements IButtonRendererProxy<Boolean> {
    final /* synthetic */ HUDPanel val$this$0;
    final /* synthetic */ IBoolean val$renderState;

    HUDPanel.HUDPanelComponent.2(HUDPanel hUDPanel, IBoolean iBoolean) {
        this.val$this$0 = hUDPanel;
        this.val$renderState = iBoolean;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Boolean state) {
        if (this.val$renderState.isOn()) {
            IButtonRendererProxy.super.renderButton(context, title, focus, state);
        }
    }

    @Override
    public IButtonRenderer<Boolean> getRenderer() {
        return HUDPanelComponent.this.titleRenderer;
    }
}
