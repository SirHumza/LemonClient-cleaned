/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import com.lukflug.panelstudio.widget.RadioButton;

class SearchableRadioButton.5
extends RadioButton {
    SearchableRadioButton.5(IEnumSetting x0, IRadioRenderer x1, Animation x2, boolean x3) {
        super(x0, x1, x2, x3);
    }

    @Override
    protected boolean isUpKey(int key) {
        return SearchableRadioButton.this.isUpKey(key);
    }

    @Override
    protected boolean isDownKey(int key) {
        return SearchableRadioButton.this.isDownKey(key);
    }
}
