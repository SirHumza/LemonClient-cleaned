/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.popup.IPopupPositioner;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

class DropDownList.6
implements IPopupPositioner {
    DropDownList.6() {
    }

    @Override
    public Point getPosition(IInterface inter, Dimension popup, Rectangle component, Rectangle panel) {
        return new Point(component.x, component.y + component.height);
    }
}
