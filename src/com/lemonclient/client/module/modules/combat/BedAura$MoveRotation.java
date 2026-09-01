/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.client.LemonClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

class BedAura.MoveRotation {
    double yaw;
    double lastYaw;
    int tick;

    public BedAura.MoveRotation(EntityPlayer player, double lastYaw, int tick) {
        this.yaw = RotationUtil.getRotationTo((Vec3d)player.func_174791_d(), (Vec3d)new Vec3d((double)player.field_70169_q, (double)player.field_70167_r, (double)player.field_70166_s)).field_189982_i;
        this.lastYaw = lastYaw;
        double difference = this.yaw - lastYaw;
        if (lastYaw != 512.0 && (difference > (Double)BedAura.this.resetRotate.getValue() || difference < -((Double)BedAura.this.resetRotate.getValue()).doubleValue()) || LemonClient.speedUtil.getPlayerSpeed(player) == 0.0) {
            this.tick = 0;
            return;
        }
        this.tick = tick;
    }
}
