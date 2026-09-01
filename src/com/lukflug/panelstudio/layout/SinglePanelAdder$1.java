/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IScrollSize;

class SinglePanelAdder.1
implements IScrollSize {
    SinglePanelAdder.1() {
    }

    @Override
    public int getComponentWidth(Context context) {
        return SinglePanelAdder.this.size.getComponentWidth(context);
    }
}
