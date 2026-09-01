/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Declaration(name="ChorusViewer", category=Category.Render)
public class ChorusViewer
extends Module {
    ModeSetting render = this.registerMode("Render", Arrays.asList("None", "Rectangle", "Circle"), "None");
    IntegerSetting life = this.registerInteger("Life", 300, 0, 1000);
    DoubleSetting circleRange = this.registerDouble("Circle Range", 1.0, 0.0, 3.0);
    ColorSetting color = this.registerColor("Color", new GSColor(255, 255, 255, 150), true);
    BooleanSetting desyncCircle = this.registerBoolean("Desync Circle", false);
    IntegerSetting stepRainbowCircle = this.registerInteger("Step Rainbow Circle", 1, 1, 100);
    BooleanSetting increaseHeight = this.registerBoolean("Increase Height", true);
    DoubleSetting speedIncrease = this.registerDouble("Speed Increase", 0.01, 0.3, 0.001);
    ArrayList<renderClass> toRender = new ArrayList();
    @EventHandler
    private final Listener<PacketEvent.Receive> sendListener = new Listener<PacketEvent.Receive>(event -> {
        SPacketSoundEffect soundPacket;
        if (event.getPacket() instanceof SPacketSoundEffect && (soundPacket = (SPacketSoundEffect)event.getPacket()).func_186978_a() == SoundEvents.field_187544_ad) {
            this.toRender.add(new renderClass(new Vec3d(soundPacket.func_149207_d(), soundPacket.func_149211_e(), soundPacket.func_149210_f()), ((Integer)this.life.getValue()).intValue(), (String)this.render.getValue(), this.color.getValue(), (Double)this.circleRange.getValue(), (Boolean)this.desyncCircle.getValue(), (Integer)this.stepRainbowCircle.getValue(), (Double)this.circleRange.getValue(), (Integer)this.stepRainbowCircle.getValue(), (Boolean)this.increaseHeight.getValue(), (Double)this.speedIncrease.getValue()));
        }
    }, new Predicate[0]);

    @Override
    public void onWorldRender(RenderEvent event) {
        if (ChorusViewer.mc.field_71441_e == null || ChorusViewer.mc.field_71439_g == null) {
            return;
        }
        for (int i = 0; i < this.toRender.size(); ++i) {
            if (!this.toRender.get(i).update()) continue;
            this.toRender.remove(i);
            --i;
        }
        this.toRender.forEach(renderClass::render);
    }

    static class renderClass {
        final Vec3d center;
        long start;
        final long life;
        final String mode;
        final double circleRange;
        final GSColor color;
        final boolean desyncCircle;
        final int stepRainbowCircle;
        final double range;
        final int desync;
        final boolean increaseHeight;
        final double speedIncrease;
        double nowHeigth = 0.0;
        boolean up = true;

        public renderClass(Vec3d center, long life, String mode, GSColor color, double circleRange, boolean desyncCircle, int stepRainbowCircle, double range, int desync, boolean increaseHeight, double speedIncrease) {
            this.center = center;
            this.increaseHeight = increaseHeight;
            this.speedIncrease = speedIncrease;
            this.range = range;
            this.start = System.currentTimeMillis();
            this.life = life;
            this.mode = mode;
            this.desync = desync;
            this.circleRange = circleRange;
            this.color = color;
            this.desyncCircle = desyncCircle;
            this.stepRainbowCircle = stepRainbowCircle;
        }

        boolean update() {
            return System.currentTimeMillis() - this.start > this.life;
        }

        void render() {
            switch (this.mode) {
                case "Rectangle": {
                    RenderUtil.drawBox(new BlockPos(this.center.field_72450_a, this.center.field_72448_b, this.center.field_72449_c), 1.8, this.color, 63);
                    break;
                }
                case "Circle": {
                    double inc = 0.0;
                    if (this.increaseHeight) {
                        this.nowHeigth += this.speedIncrease * (double)(this.up ? 1 : -1);
                        if (this.nowHeigth > 1.8) {
                            this.up = false;
                        } else if (this.nowHeigth < 0.0) {
                            this.up = true;
                        }
                        inc = this.nowHeigth;
                    }
                    if (this.desyncCircle) {
                        RenderUtil.drawCircle((float)this.center.field_72450_a, (float)(this.center.field_72448_b + inc), (float)this.center.field_72449_c, this.range, this.desync, this.color.getAlpha());
                        break;
                    }
                    RenderUtil.drawCircle((float)this.center.field_72450_a, (float)(this.center.field_72448_b + inc), (float)this.center.field_72449_c, this.range, this.color);
                }
            }
        }
    }
}
