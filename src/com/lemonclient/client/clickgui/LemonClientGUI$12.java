/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.setting.ISetting;
import java.util.stream.Stream;

class LemonClientGUI.12
implements INumberSetting {
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.12(Setting setting) {
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
    public double getNumber() {
        return ((Integer)((IntegerSetting)this.val$setting).getValue()).intValue();
    }

    @Override
    public void setNumber(double value) {
        ((IntegerSetting)this.val$setting).setValue((int)Math.round(value));
    }

    @Override
    public double getMaximumValue() {
        return ((IntegerSetting)this.val$setting).getMax();
    }

    @Override
    public double getMinimumValue() {
        return ((IntegerSetting)this.val$setting).getMin();
    }

    @Override
    public int getPrecision() {
        return 0;
    }

    @Override
    public Stream<ISetting<?>> getSubSettings() {
        if (this.val$setting.getSubSettings().count() == 0L) {
            return null;
        }
        return this.val$setting.getSubSettings().map(subSetting -> LemonClientGUI.this.createSetting(subSetting));
    }
}
