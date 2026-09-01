/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.container;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.popup.IPopup;
import com.lukflug.panelstudio.popup.IPopupPositioner;
import java.awt.Rectangle;

protected final class FixedContainer.PopupPair {
    public final IPopup popup;
    public final Rectangle rect;
    public final IToggleable visible;
    public final IPopupPositioner positioner;

    public FixedContainer.PopupPair(IPopup popup, Rectangle rect, IToggleable visible, IPopupPositioner positioner) {
        this.popup = popup;
        this.rect = rect;
        this.visible = visible;
        this.positioner = positioner;
    }
}
