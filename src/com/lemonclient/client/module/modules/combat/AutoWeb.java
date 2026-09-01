/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="AutoWeb", category=Category.Combat)
public class AutoWeb
extends Module {
    ModeSetting page = this.registerMode("Page", Arrays.asList("Settings", "Predict"), "Settings");
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting rotate = this.registerBoolean("Rotate", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting packet = this.registerBoolean("Packet", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting swing = this.registerBoolean("Swing", true, () -> ((String)this.page.getValue()).equals("Settings"));
    IntegerSetting delay = this.registerInteger("Delay", 50, 0, 2000, () -> ((String)this.page.getValue()).equals("Settings"));
    IntegerSetting multiPlace = this.registerInteger("MultiPlace", 1, 1, 8, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting strict = this.registerBoolean("Strict", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting raytrace = this.registerBoolean("Raytrace", false, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting noInWeb = this.registerBoolean("NoInWeb", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting checkSelf = this.registerBoolean("CheckSelf", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting onlyGround = this.registerBoolean("SelfGround", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting down = this.registerBoolean("Down", false, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting face = this.registerBoolean("Face", false, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting feet = this.registerBoolean("Feet", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting onlyAir = this.registerBoolean("OnlyAir", true, () -> ((String)this.page.getValue()).equals("Settings"));
    BooleanSetting air = this.registerBoolean("Air", true, () -> ((String)this.page.getValue()).equals("Settings"));
    DoubleSetting minTargetSpeed = this.registerDouble("MinTargetSpeed", 10.0, 0.0, 50.0, () -> ((String)this.page.getValue()).equals("Settings"));
    DoubleSetting range = this.registerDouble("Range", 5.0, 1.0, 6.0, () -> ((String)this.page.getValue()).equals("Settings"));
    IntegerSetting tickPredict = this.registerInteger("Tick Predict", 8, 0, 30, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting calculateYPredict = this.registerBoolean("Calculate Y Predict", true, () -> ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting startDecrease = this.registerInteger("Start Decrease", 39, 0, 200, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting exponentStartDecrease = this.registerInteger("Exponent Start", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting decreaseY = this.registerInteger("Decrease Y", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting exponentDecreaseY = this.registerInteger("Exponent Decrease Y", 1, 1, 3, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting splitXZ = this.registerBoolean("Split XZ", true, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting manualOutHole = this.registerBoolean("Manual Out Hole", false, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting aboveHoleManual = this.registerBoolean("Above Hole Manual", false, () -> (Boolean)this.manualOutHole.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting stairPredict = this.registerBoolean("Stair Predict", false, () -> ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting nStair = this.registerInteger("N Stair", 2, 1, 4, () -> (Boolean)this.stairPredict.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    DoubleSetting speedActivationStair = this.registerDouble("Speed Activation Stair", 0.3, 0.0, 1.0, () -> (Boolean)this.stairPredict.getValue() != false && ((String)this.page.getValue()).equals("Predict"));
    private final Timing timer = new Timing();
    private int progress = 0;

    @Override
    public void onTick() {
        if (!this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            return;
        }
        if (((Boolean)this.onlyGround.getValue()).booleanValue() && !AutoWeb.mc.field_71439_g.field_70122_E) {
            return;
        }
        this.progress = 0;
        PredictUtil.PredictSettings settings = new PredictUtil.PredictSettings((Integer)this.tickPredict.getValue(), (Boolean)this.calculateYPredict.getValue(), (Integer)this.startDecrease.getValue(), (Integer)this.exponentStartDecrease.getValue(), (Integer)this.decreaseY.getValue(), (Integer)this.exponentDecreaseY.getValue(), (Boolean)this.splitXZ.getValue(), (Boolean)this.manualOutHole.getValue(), (Boolean)this.aboveHoleManual.getValue(), (Boolean)this.stairPredict.getValue(), (Integer)this.nStair.getValue(), (Double)this.speedActivationStair.getValue());
        for (EntityPlayer player : AutoWeb.mc.field_71441_e.field_73010_i) {
            EntityPlayer target = PredictUtil.predictPlayer((EntityLivingBase)player, settings);
            if (EntityUtil.invalid((Entity)target, (Double)this.range.getValue() + 3.0) || AutoWeb.isInWeb(player) && ((Boolean)this.noInWeb.getValue()).booleanValue() || LemonClient.speedUtil.getPlayerSpeed(player) < (Double)this.minTargetSpeed.getValue() || ((Boolean)this.onlyAir.getValue()).booleanValue() && player.field_70122_E) continue;
            if (((Boolean)this.down.getValue()).booleanValue()) {
                this.placeWeb(new BlockPos(target.field_70165_t, target.field_70163_u - 0.3, target.field_70161_v));
                this.placeWeb(new BlockPos(target.field_70165_t + 0.1, target.field_70163_u - 0.3, target.field_70161_v + 0.1));
                this.placeWeb(new BlockPos(target.field_70165_t - 0.1, target.field_70163_u - 0.3, target.field_70161_v + 0.1));
                this.placeWeb(new BlockPos(target.field_70165_t - 0.1, target.field_70163_u - 0.3, target.field_70161_v - 0.1));
                this.placeWeb(new BlockPos(target.field_70165_t + 0.1, target.field_70163_u - 0.3, target.field_70161_v - 0.1));
            }
            if (((Boolean)this.face.getValue()).booleanValue()) {
                this.placeWeb(new BlockPos(target.field_70165_t + 0.2, target.field_70163_u + 1.5, target.field_70161_v + 0.2));
                this.placeWeb(new BlockPos(target.field_70165_t - 0.2, target.field_70163_u + 1.5, target.field_70161_v + 0.2));
                this.placeWeb(new BlockPos(target.field_70165_t - 0.2, target.field_70163_u + 1.5, target.field_70161_v - 0.2));
                this.placeWeb(new BlockPos(target.field_70165_t + 0.2, target.field_70163_u + 1.5, target.field_70161_v - 0.2));
            }
            if (!((Boolean)this.air.getValue()).booleanValue() || player.field_70122_E || !((Boolean)this.feet.getValue()).booleanValue() || HoleUtil.isHoleBlock(EntityUtil.getEntityPos((Entity)target), true, false, false)) continue;
            this.placeWeb(new BlockPos(target.field_70165_t + 0.2, target.field_70163_u + 0.5, target.field_70161_v + 0.2));
            this.placeWeb(new BlockPos(target.field_70165_t - 0.2, target.field_70163_u + 0.5, target.field_70161_v + 0.2));
            this.placeWeb(new BlockPos(target.field_70165_t - 0.2, target.field_70163_u + 0.5, target.field_70161_v - 0.2));
            this.placeWeb(new BlockPos(target.field_70165_t + 0.2, target.field_70163_u + 0.5, target.field_70161_v - 0.2));
        }
    }

    public static boolean isInWeb(EntityPlayer player) {
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u + 1.5, player.field_70161_v + 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u + 1.5, player.field_70161_v + 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u + 1.5, player.field_70161_v - 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u + 1.5, player.field_70161_v - 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u - 0.5, player.field_70161_v + 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u - 0.5, player.field_70161_v + 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u - 0.5, player.field_70161_v - 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u - 0.5, player.field_70161_v - 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u + 0.5, player.field_70161_v + 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u + 0.5, player.field_70161_v + 0.3))) {
            return true;
        }
        if (AutoWeb.isWeb(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u + 0.5, player.field_70161_v - 0.3))) {
            return true;
        }
        return AutoWeb.isWeb(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u + 0.5, player.field_70161_v - 0.3));
    }

    private static boolean isWeb(BlockPos pos) {
        return AutoWeb.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G && AutoWeb.checkEntity(pos);
    }

    private boolean isSelf(BlockPos pos) {
        if (!((Boolean)this.checkSelf.getValue()).booleanValue()) {
            return false;
        }
        for (Entity entity : AutoWeb.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (entity != AutoWeb.mc.field_71439_g) continue;
            return true;
        }
        return false;
    }

    private static boolean checkEntity(BlockPos pos) {
        for (Entity entity : AutoWeb.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer) || entity == AutoWeb.mc.field_71439_g) continue;
            return true;
        }
        return false;
    }

    private void placeWeb(BlockPos pos) {
        if (this.progress >= (Integer)this.multiPlace.getValue() || PlayerUtil.getDistance(pos) > (Double)this.range.getValue()) {
            return;
        }
        if (!AutoWeb.mc.field_71441_e.func_175623_d(pos.func_177984_a())) {
            return;
        }
        if (!this.canPlace(pos)) {
            return;
        }
        if (this.isSelf(pos)) {
            return;
        }
        if (BurrowUtil.findHotbarBlock(BlockWeb.class) == -1) {
            return;
        }
        InventoryUtil.run(BurrowUtil.findHotbarBlock(BlockWeb.class), (Boolean)this.packetSwitch.getValue(), () -> BlockUtil.placeBlock(pos, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue()));
        ++this.progress;
        this.timer.reset();
    }

    private boolean canPlace(BlockPos pos) {
        if (!BlockUtil.canBlockFacing(pos)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return this.strictPlaceCheck(pos);
    }

    private boolean strictPlaceCheck(BlockPos pos) {
        if (!((Boolean)this.strict.getValue()).booleanValue() && ((Boolean)this.raytrace.getValue()).booleanValue()) {
            return true;
        }
        for (EnumFacing side : BlockUtil.getPlacableFacings(pos, true, (Boolean)this.raytrace.getValue())) {
            if (!BlockUtil.canClick(pos.func_177972_a(side))) continue;
            return true;
        }
        return false;
    }
}
