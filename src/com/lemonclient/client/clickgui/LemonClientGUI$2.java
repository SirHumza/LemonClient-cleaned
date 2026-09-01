/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.setting.SettingsManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IBooleanSetting;
import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IKeybindSetting;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;
import java.util.Comparator;
import java.util.stream.Stream;
import org.lwjgl.input.Keyboard;

class LemonClientGUI.2
implements ICategory {
    final /* synthetic */ Category val$category;

    LemonClientGUI.2(Category category) {
        this.val$category = category;
    }

    @Override
    public String getDisplayName() {
        return this.val$category.toString();
    }

    @Override
    public Stream<IModule> getModules() {
        return ModuleManager.getModulesInCategory(this.val$category).stream().sorted(Comparator.comparing(Module::getName)).map(module -> new IModule((Module)module){
            final /* synthetic */ Module val$module;
            {
                this.val$module = module;
            }

            @Override
            public String getDisplayName() {
                return this.val$module.getName();
            }

            @Override
            public IToggleable isEnabled() {
                return new IToggleable(){

                    @Override
                    public boolean isOn() {
                        return val$module.isEnabled();
                    }

                    @Override
                    public void toggle() {
                        val$module.toggle();
                    }
                };
            }

            @Override
            public Stream<ISetting<?>> getSettings() {
                Stream<ISetting> temp = SettingsManager.getSettingsForModule(this.val$module).stream().map(setting -> LemonClientGUI.this.createSetting(setting));
                return Stream.concat(temp, Stream.concat(Stream.of(new IBooleanSetting(){

                    @Override
                    public String getDisplayName() {
                        return "Toggle Msgs";
                    }

                    @Override
                    public void toggle() {
                        val$module.setToggleMsg(!val$module.isToggleMsg());
                    }

                    @Override
                    public boolean isOn() {
                        return val$module.isToggleMsg();
                    }
                }), Stream.of(new IKeybindSetting(){

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
                })));
            }
        });
    }
}
