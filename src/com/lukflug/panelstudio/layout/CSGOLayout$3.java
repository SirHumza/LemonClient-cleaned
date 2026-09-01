/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.theme.IRadioRenderer;
import com.lukflug.panelstudio.widget.RadioButton;

class CSGOLayout.3
extends RadioButton {
    CSGOLayout.3(IEnumSetting x0, IRadioRenderer x1, Animation x2, boolean x3) {
        super(x0, x1, x2, x3);
    }

    @Override
    protected boolean isUpKey(int key) {
        if (this.horizontal) {
            return CSGOLayout.this.isLeftKey(key);
        }
        return CSGOLayout.this.isUpKey(key);
    }

    @Override
    protected boolean isDownKey(int key) {
        if (this.horizontal) {
            return CSGOLayout.this.isRightKey(key);
        }
        return CSGOLayout.this.isDownKey(key);
    }
}
