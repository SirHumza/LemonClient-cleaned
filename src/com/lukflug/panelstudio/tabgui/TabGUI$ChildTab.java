/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.base.SimpleToggleable;
import com.lukflug.panelstudio.component.FixedComponent;
import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.tabgui.Tab;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.Supplier;

protected class TabGUI.ChildTab
implements Supplier<Void> {
    public final FixedComponent<Tab> tab;
    public final IToggleable visible;

    public TabGUI.ChildTab(ICategory category, Animation animation, final int index) {
        this.tab = new FixedComponent<Tab>(new Tab(category, TabGUI.this.childRenderer, animation, TabGUI.this.up, TabGUI.this.down, TabGUI.this.enter), new Point(0, 0), TabGUI.this.width, null, false, category.getDisplayName()){

            @Override
            public Point getPosition(IInterface inter) {
                Rectangle rect = new Rectangle(TabGUI.this.fixedComponent.getPosition(inter), new Dimension(this.width, TabGUI.this.getHeight()));
                Dimension dim = new Dimension(this.width, ((Tab)this.component).getHeight());
                return TabGUI.this.positioner.getPosition(inter, dim, TabGUI.this.renderer.getItemRect(inter, rect, TabGUI.this.contents.size(), index), rect);
            }
        };
        this.visible = new SimpleToggleable(false);
        TabGUI.this.container.addComponent(this.tab, this.visible);
    }

    @Override
    public Void get() {
        return null;
    }
}
