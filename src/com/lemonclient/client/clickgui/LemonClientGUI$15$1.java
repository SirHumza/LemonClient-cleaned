/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IBooleanSetting;

class LemonClientGUI.1
implements IBooleanSetting {
    LemonClientGUI.1() {
    }

    @Override
    public String getDisplayName() {
        return "Sync Color";
    }

    @Override
    public IBoolean isVisible() {
        return () -> val$setting != ModuleManager.getModule(ColorMain.class).enabledColor;
    }

    @Override
    public void toggle() {
        ((ColorSetting)val$setting).setValue(ModuleManager.getModule(ColorMain.class).enabledColor.getColor());
        ((ColorSetting)val$setting).setRainbow(ModuleManager.getModule(ColorMain.class).enabledColor.getRainbow());
    }

    @Override
    public boolean isOn() {
        return ModuleManager.getModule(ColorMain.class).enabledColor.getColor().equals(((ColorSetting)val$setting).getColor());
    }
}
