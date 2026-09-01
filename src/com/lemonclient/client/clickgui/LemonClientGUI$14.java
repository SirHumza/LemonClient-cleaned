/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IEnumSetting;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.setting.ISetting;
import com.lukflug.panelstudio.setting.Labeled;
import java.util.stream.Stream;

class LemonClientGUI.14
implements IEnumSetting {
    private final ILabeled[] states;
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.14(Setting setting) {
        this.val$setting = setting;
        this.states = (ILabeled[])((ModeSetting)this.val$setting).getModes().stream().map(mode -> new Labeled((String)mode, null, () -> true)).toArray(ILabeled[]::new);
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
    public void increment() {
        ((ModeSetting)this.val$setting).increment();
    }

    @Override
    public void decrement() {
        ((ModeSetting)this.val$setting).decrement();
    }

    @Override
    public String getValueName() {
        return (String)((ModeSetting)this.val$setting).getValue();
    }

    @Override
    public int getValueIndex() {
        return ((ModeSetting)this.val$setting).getModes().indexOf(this.getValueName());
    }

    @Override
    public void setValueIndex(int index) {
        ((ModeSetting)this.val$setting).setValue(((ModeSetting)this.val$setting).getModes().get(index));
    }

    @Override
    public ILabeled[] getAllowedValues() {
        return this.states;
    }

    @Override
    public Stream<ISetting<?>> getSubSettings() {
        if (this.val$setting.getSubSettings().count() == 0L) {
            return null;
        }
        return this.val$setting.getSubSettings().map(subSetting -> LemonClientGUI.this.createSetting(subSetting));
    }
}
