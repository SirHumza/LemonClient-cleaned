/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.layout;

import com.lukflug.panelstudio.setting.IKeybindSetting;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.widget.KeybindComponent;

class ComponentGenerator.1
extends KeybindComponent {
    ComponentGenerator.1(IKeybindSetting x0, IButtonRenderer x1) {
        super(x0, x1);
    }

    @Override
    public int transformKey(int scancode) {
        return ComponentGenerator.this.keybindKey.test(scancode) ? 0 : scancode;
    }
}
