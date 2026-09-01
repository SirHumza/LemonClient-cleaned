/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IBooleanSetting;

protected final class ColorComponent.RainbowToggle
implements IBooleanSetting {
    protected ColorComponent.RainbowToggle() {
    }

    @Override
    public String getDisplayName() {
        return "Rainbow";
    }

    @Override
    public IBoolean isVisible() {
        return () -> ColorComponent.this.setting.allowsRainbow();
    }

    @Override
    public boolean isOn() {
        return ColorComponent.this.setting.getRainbow();
    }

    @Override
    public void toggle() {
        ColorComponent.this.setting.setRainbow(!ColorComponent.this.setting.getRainbow());
    }
}
