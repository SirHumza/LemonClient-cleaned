/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;

@Module.Declaration(name="NameProtect", category=Category.Misc)
public class NameProtect
extends Module {
    public static NameProtect INSTANCE;
    StringSetting name = this.registerString("Name", "");

    public NameProtect() {
        INSTANCE = this;
    }

    public String replaceName(String string) {
        if (string != null && this.isEnabled()) {
            String username = mc.func_110432_I().func_111285_a();
            return string.replace(username, this.name.getText()).replace(username.toLowerCase(), this.name.getText().toLowerCase()).replace(username.toUpperCase(), this.name.getText().toUpperCase());
        }
        return string;
    }

    public String getName(String original) {
        if (this.name.getText().length() > 0) {
            return this.name.getText();
        }
        return original;
    }
}
