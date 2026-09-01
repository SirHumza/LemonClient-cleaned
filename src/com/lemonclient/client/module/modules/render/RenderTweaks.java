/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.ItemRenderer
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import net.minecraft.client.renderer.ItemRenderer;

@Module.Declaration(name="RenderTweaks", category=Category.Render)
public class RenderTweaks
extends Module {
    public BooleanSetting viewClip = this.registerBoolean("View Clip", false);
    public BooleanSetting noAnimation = this.registerBoolean("No Animation", false);
    public BooleanSetting noEat = this.registerBoolean("No Eat", false);
    BooleanSetting lowOffhand = this.registerBoolean("Low Offhand", false);
    DoubleSetting lowOffhandSlider = this.registerDouble("Offhand Height", 1.0, 0.1, 1.0, () -> (Boolean)this.lowOffhand.getValue());
    BooleanSetting fovChanger = this.registerBoolean("FOV", false);
    IntegerSetting fovChangerSlider = this.registerInteger("FOV Slider", 90, 70, 200, () -> (Boolean)this.fovChanger.getValue());
    ItemRenderer itemRenderer;
    private float oldFOV;

    public RenderTweaks() {
        this.itemRenderer = RenderTweaks.mc.field_71460_t.field_78516_c;
    }

    @Override
    public void onUpdate() {
        if (((Boolean)this.lowOffhand.getValue()).booleanValue()) {
            this.itemRenderer.field_187471_h = ((Double)this.lowOffhandSlider.getValue()).floatValue();
        }
        if (((Boolean)this.fovChanger.getValue()).booleanValue()) {
            RenderTweaks.mc.field_71474_y.field_74334_X = ((Integer)this.fovChangerSlider.getValue()).intValue();
        }
        if (!((Boolean)this.fovChanger.getValue()).booleanValue()) {
            RenderTweaks.mc.field_71474_y.field_74334_X = this.oldFOV;
        }
    }

    @Override
    public void onEnable() {
        this.oldFOV = RenderTweaks.mc.field_71474_y.field_74334_X;
    }

    @Override
    public void onDisable() {
        RenderTweaks.mc.field_71474_y.field_74334_X = this.oldFOV;
    }
}
