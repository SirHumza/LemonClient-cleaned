/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IFixedComponent;

class ChildUtil.1
extends ComponentProxy<IComponent> {
    final /* synthetic */ IFixedComponent val$popup;
    final /* synthetic */ IToggleable val$toggle;

    ChildUtil.1(IComponent x0, IFixedComponent iFixedComponent, IToggleable iToggleable) {
        this.val$popup = iFixedComponent;
        this.val$toggle = iToggleable;
        super(x0);
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        if (button == 1 && context.isClicked(button)) {
            context.getPopupDisplayer().displayPopup(this.val$popup, context.getRect(), this.val$toggle, ChildUtil.this.popupType.popupPos);
            context.releaseFocus();
        }
    }
}
