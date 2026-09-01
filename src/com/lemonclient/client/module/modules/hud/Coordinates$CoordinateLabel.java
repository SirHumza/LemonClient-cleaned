/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package com.lemonclient.client.module.modules.hud;

import com.lukflug.panelstudio.hud.HUDList;
import java.awt.Color;
import net.minecraft.client.entity.EntityPlayerSP;

private class Coordinates.CoordinateLabel
implements HUDList {
    private Coordinates.CoordinateLabel() {
    }

    @Override
    public int getSize() {
        int dimension;
        EntityPlayerSP player = mc.field_71439_g;
        int n = dimension = player != null ? player.field_71093_bK : 1;
        if (((Boolean)Coordinates.this.showNetherOverworld.getValue()).booleanValue() && (dimension == -1 || dimension == 0)) {
            return 2;
        }
        return 1;
    }

    @Override
    public String getItem(int index) {
        return Coordinates.this.coordinateString[index];
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
