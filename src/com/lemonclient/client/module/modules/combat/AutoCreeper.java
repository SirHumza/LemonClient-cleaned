/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.qwq.AutoEz;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="AutoCreeper", category=Category.Combat)
public class AutoCreeper
extends Module {
    DoubleSetting minDamage = this.registerDouble("Min Damage", 6.0, 0.0, 36.0);
    IntegerSetting delay = this.registerInteger("Delay", 50, 0, 1000);
    DoubleSetting enemyRange = this.registerDouble("Enemy Range", 10.0, 0.0, 16.0);
    DoubleSetting range = this.registerDouble("Range", 5.0, 0.0, 6.0);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting packet = this.registerBoolean("Packet", false);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", false);
    BooleanSetting predict = this.registerBoolean("Predict", true);
    IntegerSetting tickPredict = this.registerInteger("TickPredict", 8, 0, 30, () -> (Boolean)this.predict.getValue());
    BooleanSetting calculateYPredict = this.registerBoolean("CalculateYPredict", true, () -> (Boolean)this.predict.getValue());
    IntegerSetting startDecrease = this.registerInteger("StartDecrease", 39, 0, 200, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.calculateYPredict.getValue() != false);
    IntegerSetting exponentStartDecrease = this.registerInteger("ExponentStart", 2, 1, 5, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.calculateYPredict.getValue() != false);
    IntegerSetting decreaseY = this.registerInteger("DecreaseY", 2, 1, 5, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.calculateYPredict.getValue() != false);
    IntegerSetting exponentDecreaseY = this.registerInteger("ExponentDecreaseY", 1, 1, 3, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.calculateYPredict.getValue() != false);
    BooleanSetting splitXZ = this.registerBoolean("SplitXZ", true, () -> (Boolean)this.predict.getValue());
    BooleanSetting manualOutHole = this.registerBoolean("ManualOutHole", false, () -> (Boolean)this.predict.getValue());
    BooleanSetting aboveHoleManual = this.registerBoolean("AboveHoleManual", false, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.manualOutHole.getValue() != false);
    BooleanSetting stairPredict = this.registerBoolean("StairPredict", false, () -> (Boolean)this.predict.getValue());
    IntegerSetting nStair = this.registerInteger("NStair", 2, 1, 4, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.stairPredict.getValue() != false);
    DoubleSetting speedActivationStair = this.registerDouble("SpeedActivationStair", 0.11, 0.0, 1.0, () -> (Boolean)this.predict.getValue() != false && (Boolean)this.stairPredict.getValue() != false);
    Timing timer = new Timing();
    EntityPlayer target;

    @Override
    public void onTick() {
        int slot = this.getSlot();
        if (slot == -1) {
            return;
        }
        EntityPlayer origin = this.target = PlayerUtil.getNearestPlayer((Double)this.enemyRange.getValue());
        if (this.target == null) {
            return;
        }
        if (AutoEz.INSTANCE.isEnabled()) {
            AutoEz.INSTANCE.addTargetedPlayer(this.target.func_70005_c_());
        }
        PredictUtil.PredictSettings settings = new PredictUtil.PredictSettings((Integer)this.tickPredict.getValue(), (Boolean)this.calculateYPredict.getValue(), (Integer)this.startDecrease.getValue(), (Integer)this.exponentStartDecrease.getValue(), (Integer)this.decreaseY.getValue(), (Integer)this.exponentDecreaseY.getValue(), (Boolean)this.splitXZ.getValue(), (Boolean)this.manualOutHole.getValue(), (Boolean)this.aboveHoleManual.getValue(), (Boolean)this.stairPredict.getValue(), (Integer)this.nStair.getValue(), (Double)this.speedActivationStair.getValue());
        if (((Boolean)this.predict.getValue()).booleanValue()) {
            this.target = PredictUtil.predictPlayer((EntityLivingBase)this.target, settings);
        }
        BlockPos blockPos = null;
        double dmg = 0.0;
        for (BlockPos pos : EntityUtil.getSphere(PlayerUtil.getEyesPos(), (Double)this.range.getValue(), (Double)this.range.getValue(), false, false, 0)) {
            double damage;
            if (BurrowUtil.getFirstFacing(pos) == null || (damage = (double)DamageUtil.calculateDamage((EntityLivingBase)origin, this.target.func_174791_d(), this.target.field_70121_D, (double)pos.field_177962_a + 0.5, pos.field_177960_b, (double)pos.field_177961_c + 0.5, 3.0f, "Default")) < (Double)this.minDamage.getValue() || !(dmg < damage)) continue;
            blockPos = pos;
            dmg = damage;
        }
        if (blockPos == null) {
            return;
        }
        if (this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            this.timer.reset();
            BlockPos finalBlockPos = blockPos;
            InventoryUtil.run(slot, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.placeBlock(finalBlockPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
        }
    }

    public int getSlot() {
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoCreeper.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || stack.func_77973_b() != Items.field_151063_bx) continue;
            newSlot = i;
            break;
        }
        return newSlot;
    }
}
