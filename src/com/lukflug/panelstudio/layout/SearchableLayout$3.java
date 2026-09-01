/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.theme.ThemeTuple;
import com.lukflug.panelstudio.widget.ITextFieldKeys;
import com.lukflug.panelstudio.widget.SearchableRadioButton;

class SearchableLayout.3
extends SearchableRadioButton {
    final /* synthetic */ boolean val$horizontal;

    SearchableLayout.3(IEnumSetting x0, ThemeTuple x1, boolean x2, ITextFieldKeys x3, boolean bl) {
        this.val$horizontal = bl;
        super(x0, x1, x2, x3);
    }

    @Override
    protected Animation getAnimation() {
        return SearchableLayout.this.animation.get();
    }

    @Override
    public boolean allowCharacter(char character) {
        return SearchableLayout.this.charFilter.test(character);
    }

    @Override
    protected boolean isUpKey(int key) {
        if (this.val$horizontal) {
            return SearchableLayout.this.isLeftKey(key);
        }
        return SearchableLayout.this.isUpKey(key);
    }

    @Override
    protected boolean isDownKey(int key) {
        if (this.val$horizontal) {
            return SearchableLayout.this.isRightKey(key);
        }
        return SearchableLayout.this.isDownKey(key);
    }
}
