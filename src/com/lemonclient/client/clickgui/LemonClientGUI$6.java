/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.client.module.modules.gui.ClickGuiModule;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.component.IFixedComponentProxy;
import com.lukflug.panelstudio.container.IContainer;
import java.awt.Point;

class LemonClientGUI.6
implements IContainer<IFixedComponent> {
    final /* synthetic */ ClickGuiModule val$clickGuiModule;

    LemonClientGUI.6(ClickGuiModule clickGuiModule) {
        this.val$clickGuiModule = clickGuiModule;
    }

    @Override
    public boolean addComponent(final IFixedComponent component) {
        return gui.addComponent(new IFixedComponentProxy<IFixedComponent>(){

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
                return component;
            }
        });
    }

    @Override
    public boolean addComponent(final IFixedComponent component, IBoolean visible) {
        return gui.addComponent(new IFixedComponentProxy<IFixedComponent>(){

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
                return component;
            }
        }, visible);
    }

    @Override
    public boolean removeComponent(IFixedComponent component) {
        return gui.removeComponent(component);
    }
}
