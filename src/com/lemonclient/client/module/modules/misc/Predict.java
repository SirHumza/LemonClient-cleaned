/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@Module.Declaration(name="Predict", category=Category.Misc)
public class Predict
extends Module {
    IntegerSetting range = this.registerInteger("Range", 10, 0, 100);
    IntegerSetting tickPredict = this.registerInteger("Tick Predict", 8, 0, 30);
    BooleanSetting calculateYPredict = this.registerBoolean("Calculate Y Predict", true);
    IntegerSetting startDecrease = this.registerInteger("Start Decrease", 39, 0, 200, () -> (Boolean)this.calculateYPredict.getValue());
    IntegerSetting exponentStartDecrease = this.registerInteger("Exponent Start", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue());
    IntegerSetting decreaseY = this.registerInteger("Decrease Y", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue());
    IntegerSetting exponentDecreaseY = this.registerInteger("Exponent Decrease Y", 1, 1, 3, () -> (Boolean)this.calculateYPredict.getValue());
    BooleanSetting splitXZ = this.registerBoolean("Split XZ", true);
    BooleanSetting hideSelf = this.registerBoolean("Hide Self", false);
    IntegerSetting width = this.registerInteger("Line Width", 2, 1, 5);
    BooleanSetting justOnce = this.registerBoolean("Just Once", false);
    BooleanSetting manualOutHole = this.registerBoolean("Manual Out Hole", false);
    BooleanSetting aboveHoleManual = this.registerBoolean("Above Hole Manual", false, () -> (Boolean)this.manualOutHole.getValue());
    BooleanSetting stairPredict = this.registerBoolean("Stair Predict", false);
    IntegerSetting nStair = this.registerInteger("N Stair", 2, 1, 4, () -> (Boolean)this.stairPredict.getValue());
    DoubleSetting speedActivationStair = this.registerDouble("Speed Activation Stair", 0.3, 0.0, 1.0, () -> (Boolean)this.stairPredict.getValue());
    ColorSetting mainColor = this.registerColor("Color");

    @Override
    public void onWorldRender(RenderEvent event) {
        PredictUtil.PredictSettings settings = new PredictUtil.PredictSettings((Integer)this.tickPredict.getValue(), (Boolean)this.calculateYPredict.getValue(), (Integer)this.startDecrease.getValue(), (Integer)this.exponentStartDecrease.getValue(), (Integer)this.decreaseY.getValue(), (Integer)this.exponentDecreaseY.getValue(), (Boolean)this.splitXZ.getValue(), (Boolean)this.manualOutHole.getValue(), (Boolean)this.aboveHoleManual.getValue(), (Boolean)this.stairPredict.getValue(), (Integer)this.nStair.getValue(), (Double)this.speedActivationStair.getValue());
        Predict.mc.field_71441_e.field_73010_i.stream().filter(entity -> (Boolean)this.hideSelf.getValue() == false || entity != Predict.mc.field_71439_g).filter(this::rangeEntityCheck).forEach(entity -> {
            EntityPlayer clonedPlayer = PredictUtil.predictPlayer((EntityLivingBase)entity, settings);
            RenderUtil.drawBoundingBox(clonedPlayer.func_174813_aQ(), (double)((Integer)this.width.getValue()).intValue(), this.mainColor.getColor());
        });
        if (((Boolean)this.justOnce.getValue()).booleanValue()) {
            this.disable();
        }
    }

    private boolean rangeEntityCheck(Entity entity) {
        return entity.func_70032_d((Entity)Predict.mc.field_71439_g) <= (float)((Integer)this.range.getValue()).intValue();
    }
}
