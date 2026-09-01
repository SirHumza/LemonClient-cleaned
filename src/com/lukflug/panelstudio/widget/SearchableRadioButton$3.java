/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IStringSetting;
import com.lukflug.panelstudio.theme.ITextFieldRenderer;
import com.lukflug.panelstudio.widget.ITextFieldKeys;
import com.lukflug.panelstudio.widget.TextField;

class SearchableRadioButton.3
extends TextField {
    SearchableRadioButton.3(IStringSetting x0, ITextFieldKeys x1, int x2, IToggleable x3, ITextFieldRenderer x4) {
        super(x0, x1, x2, x3, x4);
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        if (this.hasFocus(context)) {
            SearchableRadioButton.this.transferFocus = true;
        }
    }

    @Override
    public boolean allowCharacter(char character) {
        return SearchableRadioButton.this.allowCharacter(character);
    }
}
