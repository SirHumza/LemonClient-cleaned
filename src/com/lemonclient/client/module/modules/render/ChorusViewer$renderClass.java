/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

static class ChorusViewer.renderClass {
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

    public ChorusViewer.renderClass(Vec3d center, long life, String mode, GSColor color, double circleRange, boolean desyncCircle, int stepRainbowCircle, double range, int desync, boolean increaseHeight, double speedIncrease) {
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
