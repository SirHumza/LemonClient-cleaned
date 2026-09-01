/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.hud;

import com.lukflug.panelstudio.hud.HUDList;
import java.awt.Color;

private class Speedometer.SpeedLabel
implements HUDList {
    private Speedometer.SpeedLabel() {
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public String getItem(int index) {
        return Speedometer.this.speedString;
    }

    @Override
    public Color getItemColor(int index) {
        return new Color(255, 255, 255);
    }

    @Override
    public boolean sortUp() {
        return false;
    }

    @Override
    public boolean sortRight() {
        return false;
    }
}
