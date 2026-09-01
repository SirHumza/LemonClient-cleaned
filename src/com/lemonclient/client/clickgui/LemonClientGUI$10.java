/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.layout.CSGOLayout;
import com.lukflug.panelstudio.layout.ChildUtil;
import com.lukflug.panelstudio.popup.PopupTuple;
import com.lukflug.panelstudio.setting.ILabeled;
import java.awt.Point;
import java.util.function.Supplier;

class LemonClientGUI.10
extends CSGOLayout {
    LemonClientGUI.10(ILabeled x0, Point x1, int x2, int x3, Supplier x4, String x5, boolean x6, boolean x7, int x8, ChildUtil.ChildMode x9, PopupTuple x10) {
        super(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10);
    }

    @Override
    public int getScrollHeight(Context context, int componentHeight) {
        return 320;
    }

    @Override
    protected boolean isUpKey(int key) {
        return key == 200;
    }

    @Override
    protected boolean isDownKey(int key) {
        return key == 208;
    }

    @Override
    protected boolean isLeftKey(int key) {
        return key == 203;
    }

    @Override
    protected boolean isRightKey(int key) {
        return key == 205;
    }
}
