/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IStringSetting;
import com.lukflug.panelstudio.theme.ITextFieldRenderer;
import com.lukflug.panelstudio.widget.ITextFieldKeys;
import com.lukflug.panelstudio.widget.TextField;

class IComponentGenerator.2
extends TextField {
    IComponentGenerator.2(IStringSetting x0, ITextFieldKeys x1, int x2, IToggleable x3, ITextFieldRenderer x4) {
        super(x0, x1, x2, x3, x4);
    }

    @Override
    public boolean allowCharacter(char character) {
        return false;
    }
}
