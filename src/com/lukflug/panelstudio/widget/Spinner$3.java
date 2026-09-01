/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IStringSetting;
import com.lukflug.panelstudio.theme.ITextFieldRenderer;
import com.lukflug.panelstudio.widget.ITextFieldKeys;
import com.lukflug.panelstudio.widget.TextField;

class Spinner.3
extends TextField {
    final /* synthetic */ boolean val$allowInput;

    Spinner.3(IStringSetting x0, ITextFieldKeys x1, int x2, IToggleable x3, ITextFieldRenderer x4, boolean bl) {
        this.val$allowInput = bl;
        super(x0, x1, x2, x3, x4);
    }

    @Override
    public boolean allowCharacter(char character) {
        if (!this.val$allowInput) {
            return false;
        }
        return character >= '0' && character <= '9' || character == '.' && !this.setting.getSettingState().contains(".");
    }
}
