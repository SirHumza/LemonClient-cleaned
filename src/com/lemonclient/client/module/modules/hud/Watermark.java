/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.hud;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.HUDModule;
import com.lemonclient.client.module.Module;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.setting.Labeled;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;

@Module.Declaration(name="Watermark", category=Category.HUD, drawn=false)
@HUDModule.Declaration(posX=0, posZ=0)
public class Watermark
extends HUDModule {
    BooleanSetting custom = this.registerBoolean("Custom", false);
    StringSetting text = this.registerString("Text", "", () -> (Boolean)this.custom.getValue());
    ColorSetting color = this.registerColor("Color", new GSColor(255, 0, 0, 255));

    @Override
    public void populate(ITheme theme) {
        this.component = new ListComponent(new Labeled(this.getName(), null, () -> true), this.position, this.getName(), new WatermarkList(), 9, 1);
    }

    private class WatermarkList
    implements HUDList {
        private WatermarkList() {
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
}
