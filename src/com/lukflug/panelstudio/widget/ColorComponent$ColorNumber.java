/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import java.awt.Color;

protected final class ColorComponent.ColorNumber
implements INumberSetting {
    private final int value;
    private final IBoolean model;

    public ColorComponent.ColorNumber(int value, IBoolean model) {
        this.value = value;
        this.model = model;
    }

    @Override
    public String getDisplayName() {
        switch (this.value) {
            case 0: {
                return this.model.isOn() ? "Hue" : "Red";
            }
            case 1: {
                return this.model.isOn() ? "Saturation" : "Green";
            }
            case 2: {
                return this.model.isOn() ? "Brightness" : "Blue";
            }
            case 3: {
                return this.model.isOn() ? "Opacity" : "Alpha";
            }
        }
        return "";
    }

    @Override
    public IBoolean isVisible() {
        return () -> this.value != 3 || ColorComponent.this.setting.hasAlpha();
    }

    @Override
    public double getNumber() {
        Color c = ColorComponent.this.setting.getColor();
        if (this.value < 3) {
            if (this.model.isOn()) {
                return (double)Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[this.value] * this.getMaximumValue();
            }
            switch (this.value) {
                case 0: {
                    return c.getRed();
                }
                case 1: {
                    return c.getGreen();
                }
                case 2: {
                    return c.getBlue();
                }
            }
        }
        return (double)c.getAlpha() * this.getMaximumValue() / 255.0;
    }

    @Override
    public void setNumber(double value) {
        Color c = ColorComponent.this.setting.getColor();
        float[] color = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        switch (this.value) {
            case 0: {
                c = this.model.isOn() ? Color.getHSBColor((float)value / 360.0f, color[1], color[2]) : new Color((int)Math.round(value), c.getGreen(), c.getBlue());
                if (ColorComponent.this.setting.hasAlpha()) {
                    ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), ColorComponent.this.setting.getColor().getAlpha()));
                    break;
                }
                ColorComponent.this.setting.setValue(c);
                break;
            }
            case 1: {
                c = this.model.isOn() ? Color.getHSBColor(color[0], (float)value / 100.0f, color[2]) : new Color(c.getRed(), (int)Math.round(value), c.getBlue());
                if (ColorComponent.this.setting.hasAlpha()) {
                    ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), ColorComponent.this.setting.getColor().getAlpha()));
                    break;
                }
                ColorComponent.this.setting.setValue(c);
                break;
            }
            case 2: {
                c = this.model.isOn() ? Color.getHSBColor(color[0], color[1], (float)value / 100.0f) : new Color(c.getRed(), c.getGreen(), (int)Math.round(value));
                if (ColorComponent.this.setting.hasAlpha()) {
                    ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), ColorComponent.this.setting.getColor().getAlpha()));
                    break;
                }
                ColorComponent.this.setting.setValue(c);
                break;
            }
            case 3: {
                ColorComponent.this.setting.setValue(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)Math.round(value / this.getMaximumValue() * 255.0)));
            }
        }
    }

    @Override
    public double getMaximumValue() {
        int max = 100;
        if (!this.model.isOn()) {
            max = 255;
        } else if (this.value == 0) {
            max = 360;
        }
        return max;
    }

    @Override
    public double getMinimumValue() {
        return 0.0;
    }

    @Override
    public int getPrecision() {
        return 0;
    }
}
