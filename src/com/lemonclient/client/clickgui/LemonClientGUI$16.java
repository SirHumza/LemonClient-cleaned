/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lukflug.panelstudio.setting.IStringSetting;

class LemonClientGUI.16
implements IStringSetting {
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.16(Setting setting) {
        this.val$setting = setting;
    }

    @Override
    public String getValue() {
        return ((StringSetting)this.val$setting).getText();
    }

    @Override
    public void setValue(String string) {
        ((StringSetting)this.val$setting).setText(string);
    }

    @Override
    public String getDisplayName() {
        return this.val$setting.getName();
    }
}
