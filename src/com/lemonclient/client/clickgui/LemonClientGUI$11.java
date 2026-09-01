/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IBooleanSetting;
import com.lukflug.panelstudio.setting.ISetting;
import java.util.stream.Stream;

class LemonClientGUI.11
implements IBooleanSetting {
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.11(Setting setting) {
        this.val$setting = setting;
    }

    @Override
    public String getDisplayName() {
        return this.val$setting.getName();
    }

    @Override
    public IBoolean isVisible() {
        return () -> this.val$setting.isVisible();
    }

    @Override
    public void toggle() {
        ((BooleanSetting)this.val$setting).setValue((Boolean)((BooleanSetting)this.val$setting).getValue() == false);
    }

    @Override
    public boolean isOn() {
        return (Boolean)((BooleanSetting)this.val$setting).getValue();
    }

    @Override
    public Stream<ISetting<?>> getSubSettings() {
        if (this.val$setting.getSubSettings().count() == 0L) {
            return null;
        }
        return this.val$setting.getSubSettings().map(subSetting -> LemonClientGUI.this.createSetting(subSetting));
    }
}
