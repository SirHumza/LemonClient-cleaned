/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.setting.IStringSetting;

class Spinner.2
implements IStringSetting {
    private String value = null;
    private long lastTime;
    final /* synthetic */ INumberSetting val$setting;

    Spinner.2(INumberSetting iNumberSetting) {
        this.val$setting = iNumberSetting;
    }

    @Override
    public String getDisplayName() {
        return this.val$setting.getDisplayName();
    }

    @Override
    public String getValue() {
        if (this.value != null && System.currentTimeMillis() - this.lastTime > 500L) {
            double number;
            if (this.value.isEmpty()) {
                this.value = "0";
            }
            if (this.value.endsWith(".")) {
                this.value = this.value + '0';
            }
            if ((number = Double.parseDouble(this.value)) > this.val$setting.getMaximumValue()) {
                number = this.val$setting.getMaximumValue();
            } else if (number < this.val$setting.getMinimumValue()) {
                number = this.val$setting.getMinimumValue();
            }
            this.val$setting.setNumber(number);
            this.value = null;
        }
        if (this.value == null) {
            return this.val$setting.getSettingState();
        }
        return this.value;
    }

    @Override
    public void setValue(String string) {
        if (this.value == null) {
            this.lastTime = System.currentTimeMillis();
        }
        this.value = new String(string);
    }
}
