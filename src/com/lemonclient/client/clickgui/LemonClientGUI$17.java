/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.ISetting;
import java.util.stream.Stream;

class LemonClientGUI.17
implements ISetting<Void> {
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.17(Setting setting) {
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
    public Void getSettingState() {
        return null;
    }

    @Override
    public Class<Void> getSettingClass() {
        return Void.class;
    }

    @Override
    public Stream<ISetting<?>> getSubSettings() {
        if (this.val$setting.getSubSettings().count() == 0L) {
            return null;
        }
        return this.val$setting.getSubSettings().map(subSetting -> LemonClientGUI.this.createSetting(subSetting));
    }
}
