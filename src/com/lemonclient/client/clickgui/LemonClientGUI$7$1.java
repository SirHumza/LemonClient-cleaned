/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IScrollSize;

class LemonClientGUI.1
implements IScrollSize {
    LemonClientGUI.1() {
    }

    @Override
    public int getScrollHeight(Context context, int componentHeight) {
        return (Integer)val$scrollHeight.apply(context, componentHeight);
    }
}
