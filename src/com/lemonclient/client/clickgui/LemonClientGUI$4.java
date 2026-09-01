/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IScrollSize;
import java.util.function.BiFunction;

class LemonClientGUI.4
implements IScrollSize {
    final /* synthetic */ BiFunction val$scrollHeight;

    LemonClientGUI.4(BiFunction biFunction) {
        this.val$scrollHeight = biFunction;
    }

    @Override
    public int getScrollHeight(Context context, int componentHeight) {
        return (Integer)this.val$scrollHeight.apply(context, componentHeight);
    }
}
