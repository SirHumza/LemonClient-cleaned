/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.setting.IKeybindSetting;
import org.lwjgl.input.Keyboard;

class LemonClientGUI.3
implements IKeybindSetting {
    LemonClientGUI.3() {
    }

    @Override
    public String getDisplayName() {
        return "Keybind";
    }

    @Override
    public int getKey() {
        return val$module.getBind();
    }

    @Override
    public void setKey(int key) {
        val$module.setBind(key);
    }

    @Override
    public String getKeyName() {
        return Keyboard.getKeyName((int)val$module.getBind());
    }
}
