/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.util.player.PredictUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

class BedAura.EntityInfo {
    EntityPlayer player = null;
    EntityPlayer defaultPlayer = null;
    Vec3d position;
    AxisAlignedBB boundingBox;
    EntityLivingBase entity = null;
    double hp;

    public BedAura.EntityInfo(EntityPlayer player, boolean predict) {
        if (player == null) {
            return;
        }
        this.defaultPlayer = player;
        this.player = predict ? PredictUtil.predictPlayer((EntityLivingBase)player, new PredictUtil.PredictSettings(BedAura.this.playerSpeed.get((Object)player).tick, (Boolean)BedAura.this.calculateYPredict.getValue(), (Integer)BedAura.this.startDecrease.getValue(), (Integer)BedAura.this.exponentStartDecrease.getValue(), (Integer)BedAura.this.decreaseY.getValue(), (Integer)BedAura.this.exponentDecreaseY.getValue(), (Boolean)BedAura.this.splitXZ.getValue(), (Boolean)BedAura.this.manualOutHole.getValue(), (Boolean)BedAura.this.aboveHoleManual.getValue(), (Boolean)BedAura.this.stairPredict.getValue(), (Integer)BedAura.this.nStair.getValue(), (Double)BedAura.this.speedActivationStair.getValue())) : player;
        this.position = this.player.func_174791_d();
        this.boundingBox = this.player.func_174813_aQ();
        this.hp = player.func_110143_aJ() + player.func_110139_bj();
    }

    public BedAura.EntityInfo(EntityLivingBase entity) {
        if (entity == null) {
            return;
        }
        this.entity = entity;
        this.hp = entity.func_110143_aJ() + entity.func_110139_bj();
    }
}
