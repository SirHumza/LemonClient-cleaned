/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.FixedComponent;
import com.lukflug.panelstudio.tabgui.Tab;
import com.lukflug.panelstudio.tabgui.TabGUI;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

class TabGUI.ChildTab.1
extends FixedComponent<Tab> {
    final /* synthetic */ TabGUI val$this$0;
    final /* synthetic */ int val$index;

    TabGUI.ChildTab.1(Tab component, Point position, int width, IToggleable state, boolean savesState, String configName, TabGUI tabGUI, int n) {
        this.val$this$0 = tabGUI;
        this.val$index = n;
        super(component, position, width, state, savesState, configName);
    }

    @Override
    public Point getPosition(IInterface inter) {
        Rectangle rect = new Rectangle(ChildTab.this.this$0.fixedComponent.getPosition(inter), new Dimension(this.width, ChildTab.this.this$0.getHeight()));
        Dimension dim = new Dimension(this.width, ((Tab)this.component).getHeight());
        return ChildTab.this.this$0.positioner.getPosition(inter, dim, ChildTab.this.this$0.renderer.getItemRect(inter, rect, ChildTab.this.this$0.contents.size(), this.val$index), rect);
    }
}
