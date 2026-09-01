/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import com.lukflug.panelstudio.setting.ISetting;
import java.util.stream.Stream;

class LemonClientGUI.13
implements INumberSetting {
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.13(Setting setting) {
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
        return (Double)((DoubleSetting)this.val$setting).getValue();
    }

    @Override
    public void setNumber(double value) {
        ((DoubleSetting)this.val$setting).setValue(value);
    }

    @Override
    public double getMaximumValue() {
        return ((DoubleSetting)this.val$setting).getMax();
    }

    @Override
    public double getMinimumValue() {
        return ((DoubleSetting)this.val$setting).getMin();
    }

    @Override
    public int getPrecision() {
        return 2;
    }

    @Override
    public Stream<ISetting<?>> getSubSettings() {
        if (this.val$setting.getSubSettings().count() == 0L) {
            return null;
        }
        return this.val$setting.getSubSettings().map(subSetting -> LemonClientGUI.this.createSetting(subSetting));
    }
}
