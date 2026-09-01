/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.TextFormatting
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IBooleanSetting;
import com.lukflug.panelstudio.setting.IColorSetting;
import com.lukflug.panelstudio.setting.ISetting;
import java.awt.Color;
import java.util.stream.Stream;
import net.minecraft.util.text.TextFormatting;

class LemonClientGUI.15
implements IColorSetting {
    final /* synthetic */ Setting val$setting;

    LemonClientGUI.15(Setting setting) {
        this.val$setting = setting;
    }

    @Override
    public String getDisplayName() {
        return TextFormatting.BOLD + this.val$setting.getName();
    }

    @Override
    public IBoolean isVisible() {
        return () -> this.val$setting.isVisible();
    }

    @Override
    public Color getValue() {
        return ((ColorSetting)this.val$setting).getValue();
    }

    @Override
    public void setValue(Color value) {
        ((ColorSetting)this.val$setting).setValue(new GSColor(value));
    }

    @Override
    public Color getColor() {
        return ((ColorSetting)this.val$setting).getColor();
    }

    @Override
    public boolean getRainbow() {
        return ((ColorSetting)this.val$setting).getRainbow();
    }

    @Override
    public void setRainbow(boolean rainbow) {
        ((ColorSetting)this.val$setting).setRainbow(rainbow);
    }

    @Override
    public boolean hasAlpha() {
        return ((ColorSetting)this.val$setting).alphaEnabled();
    }

    @Override
    public boolean allowsRainbow() {
        return ((ColorSetting)this.val$setting).rainbowEnabled();
    }

    @Override
    public boolean hasHSBModel() {
        return ((String)ModuleManager.getModule(ColorMain.class).colorModel.getValue()).equalsIgnoreCase("HSB");
    }

    @Override
    public Stream<ISetting<?>> getSubSettings() {
        Stream<ISetting> temp = this.val$setting.getSubSettings().map(subSetting -> LemonClientGUI.this.createSetting(subSetting));
        return Stream.concat(temp, Stream.of(new IBooleanSetting(){

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
        }));
    }
}
