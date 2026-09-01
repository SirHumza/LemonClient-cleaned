/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.hud;

import com.lukflug.panelstudio.hud.HUDList;
import java.awt.Color;

private class Watermark.WatermarkList
implements HUDList {
    private Watermark.WatermarkList() {
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public String getItem(int index) {
        if (((Boolean)Watermark.this.custom.getValue()).booleanValue()) {
            return Watermark.this.text.getText();
        }
        return "LemonClient v0.0.9";
    }

    @Override
    public Color getItemColor(int index) {
        return Watermark.this.color.getValue();
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
