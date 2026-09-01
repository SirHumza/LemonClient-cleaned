/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.hud;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.hud.HUDPanel;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import com.lukflug.panelstudio.theme.IPanelRendererProxy;

class HUDPanel.HUDPanelComponent.4
implements IPanelRendererProxy<Boolean> {
    final /* synthetic */ HUDPanel val$this$0;
    final /* synthetic */ IBoolean val$renderState;

    HUDPanel.HUDPanelComponent.4(HUDPanel hUDPanel, IBoolean iBoolean) {
        this.val$this$0 = hUDPanel;
        this.val$renderState = iBoolean;
    }

    @Override
    public void renderBackground(Context context, boolean focus) {
        if (this.val$renderState.isOn()) {
            IPanelRendererProxy.super.renderBackground(context, focus);
        }
    }

    @Override
    public void renderPanelOverlay(Context context, boolean focus, Boolean state, boolean open) {
        if (this.val$renderState.isOn()) {
            IPanelRendererProxy.super.renderPanelOverlay(context, focus, state, open);
        }
    }

    @Override
    public void renderTitleOverlay(Context context, boolean focus, Boolean state, boolean open) {
        if (this.val$renderState.isOn()) {
            IPanelRendererProxy.super.renderTitleOverlay(context, focus, state, open);
        }
    }

    @Override
    public IPanelRenderer<Boolean> getRenderer() {
        return HUDPanelComponent.this.panelRenderer;
    }
}
