/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.hud;

import com.lukflug.panelstudio.hud.HUDList;
import java.awt.Color;

private class Welcomer.WelcomerList
implements HUDList {
    private Welcomer.WelcomerList() {
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public String getItem(int index) {
        return Welcomer.this.prefix.getText() + mc.field_71439_g.func_70005_c_() + Welcomer.this.suffix.getText();
    }

    @Override
    public Color getItemColor(int index) {
        return Welcomer.this.color.getValue();
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
