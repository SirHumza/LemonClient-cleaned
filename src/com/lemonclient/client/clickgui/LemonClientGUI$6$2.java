/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.component.IFixedComponentProxy;
import java.awt.Point;

class LemonClientGUI.2
implements IFixedComponentProxy<IFixedComponent> {
    final /* synthetic */ IFixedComponent val$component;

    LemonClientGUI.2(IFixedComponent iFixedComponent) {
        this.val$component = iFixedComponent;
    }

    @Override
    public void handleScroll(Context context, int diff) {
        IFixedComponentProxy.super.handleScroll(context, diff);
        if (((String)val$clickGuiModule.scrolling.getValue()).equals("Screen")) {
            Point p = this.getPosition(guiInterface);
            p.translate(0, -diff);
            this.setPosition(guiInterface, p);
        }
    }

    @Override
    public IFixedComponent getComponent() {
        return this.val$component;
    }
}
