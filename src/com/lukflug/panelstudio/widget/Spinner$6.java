/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.widget.Button;
import com.lukflug.panelstudio.widget.TextField;
import java.util.function.Supplier;

class Spinner.6
extends Button<Void> {
    final /* synthetic */ INumberSetting val$setting;
    final /* synthetic */ TextField val$textField;

    Spinner.6(ILabeled x0, Supplier x1, IButtonRenderer x2, INumberSetting iNumberSetting, TextField textField) {
        this.val$setting = iNumberSetting;
        this.val$textField = textField;
        super(x0, x1, x2);
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        if (button == 0 && context.isClicked(button)) {
            double number = this.val$setting.getNumber();
            if ((number -= Math.pow(10.0, -this.val$setting.getPrecision())) >= this.val$setting.getMinimumValue()) {
                this.val$setting.setNumber(number);
            }
        }
    }

    @Override
    public int getHeight() {
        return this.val$textField.getHeight() / 2;
    }
}
