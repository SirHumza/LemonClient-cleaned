/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.component.IResizable;
import com.lukflug.panelstudio.component.IScrollSize;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.layout.PanelAdder;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

class LemonClientGUI.7
extends PanelAdder {
    final /* synthetic */ BiFunction val$scrollHeight;

    LemonClientGUI.7(IContainer x0, boolean x1, IBoolean x2, UnaryOperator x3, BiFunction biFunction) {
        this.val$scrollHeight = biFunction;
        super(x0, x1, x2, x3);
    }

    @Override
    protected IScrollSize getScrollSize(IResizable size) {
        return new IScrollSize(){

            @Override
            public int getScrollHeight(Context context, int componentHeight) {
                return (Integer)val$scrollHeight.apply(context, componentHeight);
            }
        };
    }
}
