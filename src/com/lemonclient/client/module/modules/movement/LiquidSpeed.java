/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.api.util.world.TimerUtils;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Declaration(name="LiquidSpeed", category=Category.Movement)
public class LiquidSpeed
extends Module {
    DoubleSetting timerVal = this.registerDouble("Timer Speed", 1.0, 1.0, 2.0);
    DoubleSetting XZWater = this.registerDouble("XZ Water", 5.75, 0.01, 8.0);
    DoubleSetting upWater = this.registerDouble("Y+ Water", 2.69, 0.01, 8.0);
    DoubleSetting downWater = this.registerDouble("Y- Water", 0.8, 0.01, 8.0);
    DoubleSetting XZBoostWater = this.registerDouble("XZ Boost Water", 6.0, 1.0, 8.0);
    DoubleSetting yBoostWater = this.registerDouble("Y Boost Water", 2.9, 0.1, 8.0);
    DoubleSetting XZLava = this.registerDouble("XZ Lava", 3.8, 0.01, 8.0);
    DoubleSetting upLava = this.registerDouble("Y+ Lava", 2.69, 0.01, 8.0);
    DoubleSetting downLava = this.registerDouble("Y- Lava", 4.22, 0.01, 8.0);
    DoubleSetting XZBoostLava = this.registerDouble("XZ Boost Lava", 4.0, 1.0, 8.0);
    DoubleSetting yBoostLava = this.registerDouble("Y Boost Lava", 2.0, 0.1, 8.0);
    DoubleSetting jitter = this.registerDouble("Jitter", 1.0, 1.0, 20.0);
    BooleanSetting groundIgnore = this.registerBoolean("Ground Ignore", true);
    Vec3d[] sides = new Vec3d[]{new Vec3d(0.3, 0.0, 0.3), new Vec3d(0.3, 0.0, -0.3), new Vec3d(-0.3, 0.0, 0.3), new Vec3d(-0.3, 0.0, -0.3)};
    double moveSpeed = 0.0;
    double motionY = 0.0;
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.reset();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerMoveEvent> playerMoveEventListener = new Listener<PlayerMoveEvent>(event -> {
        if (LiquidSpeed.mc.field_71439_g == null || LiquidSpeed.mc.field_71441_e == null) {
            return;
        }
        if (!LiquidSpeed.mc.field_71439_g.func_70090_H() && !LiquidSpeed.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        if (((Boolean)this.groundIgnore.getValue()).booleanValue() || !LiquidSpeed.mc.field_71439_g.field_70122_E) {
            if (LiquidSpeed.mc.field_71439_g.func_70090_H()) {
                this.waterSwim((PlayerMoveEvent)event);
            } else if (LiquidSpeed.mc.field_71439_g.func_180799_ab()) {
                this.lavaSwim((PlayerMoveEvent)event);
            } else {
                this.reset();
            }
        } else {
            this.stopMotion((PlayerMoveEvent)event);
            this.reset();
        }
    }, new Predicate[0]);

    @Override
    public void onDisable() {
        this.reset();
    }

    private boolean intersect(BlockPos pos) {
        AxisAlignedBB box = BlockUtil.getBoundingBox(pos);
        if (box == null) {
            return false;
        }
        return LiquidSpeed.mc.field_71439_g.field_70121_D.func_72326_a(box);
    }

    private boolean inLiquid(Material material) {
        Vec3d vec = LiquidSpeed.mc.field_71439_g.func_174791_d();
        for (Vec3d side : this.sides) {
            BlockPos blockPos = new BlockPos(vec.func_178787_e(side));
            if (!this.intersect(blockPos)) continue;
            IBlockState blockState = BlockUtil.getState(blockPos);
            if (!(blockState instanceof BlockLiquid)) {
                return false;
            }
            if (((BlockLiquid)blockState).field_149764_J == material) continue;
            return false;
        }
        return true;
    }

    private void lavaSwim(PlayerMoveEvent moveEvent) {
        this.ySwim(moveEvent, (Double)this.yBoostLava.getValue(), (Double)this.upLava.getValue(), (Double)this.downLava.getValue());
        boolean jump = LiquidSpeed.mc.field_71439_g.field_71158_b.field_78901_c;
        boolean sneak = LiquidSpeed.mc.field_71439_g.field_71158_b.field_78899_d;
        if (!(jump && sneak || !jump && !sneak)) {
            TimerUtils.setTimerSpeed(((Double)this.timerVal.getValue()).floatValue());
        } else {
            TimerUtils.setTimerSpeed(1.0f);
        }
        if (LiquidSpeed.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || LiquidSpeed.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f) {
            double yaw = MotionUtil.calcMoveYaw();
            this.moveSpeed = Math.min(Math.max(this.moveSpeed * (Double)this.XZBoostLava.getValue(), 0.05), (Double)this.XZLava.getValue() / 20.0);
            moveEvent.setX(-Math.sin(yaw) * this.moveSpeed);
            moveEvent.setZ(Math.cos(yaw) * this.moveSpeed);
        } else {
            this.stopMotion(moveEvent);
        }
    }

    private void waterSwim(PlayerMoveEvent moveEvent) {
        this.ySwim(moveEvent, (Double)this.yBoostWater.getValue(), (Double)this.upWater.getValue(), (Double)this.downWater.getValue() * 20.0);
        boolean jump = LiquidSpeed.mc.field_71439_g.field_71158_b.field_78901_c;
        boolean sneak = LiquidSpeed.mc.field_71439_g.field_71158_b.field_78899_d;
        if (!(jump && sneak || !jump && !sneak)) {
            TimerUtils.setTimerSpeed(((Double)this.timerVal.getValue()).floatValue());
        } else {
            TimerUtils.setTimerSpeed(1.0f);
        }
        if (LiquidSpeed.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || LiquidSpeed.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f) {
            double yaw = MotionUtil.calcMoveYaw();
            double multiplier = this.applySpeedPotionEffects();
            this.moveSpeed = Math.min(Math.max(this.moveSpeed * (Double)this.XZBoostWater.getValue(), 0.075), (Double)this.XZWater.getValue() / 20.0);
            if (LiquidSpeed.mc.field_71439_g.field_71158_b.field_78899_d && !LiquidSpeed.mc.field_71439_g.field_71158_b.field_78901_c) {
                double downMotion = LiquidSpeed.mc.field_71439_g.field_70181_x * 0.25;
                this.moveSpeed = Math.min(this.moveSpeed, Math.max(this.moveSpeed + downMotion, 0.0));
            }
            this.moveSpeed *= multiplier;
            moveEvent.setX(-Math.sin(yaw) * this.moveSpeed);
            moveEvent.setZ(Math.cos(yaw) * this.moveSpeed);
        } else {
            this.stopMotion(moveEvent);
        }
    }

    private double applySpeedPotionEffects() {
        double result = 1.0;
        if (LiquidSpeed.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c) != null) {
            result += ((double)LiquidSpeed.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c() + 1.0) * 0.2;
        }
        if (LiquidSpeed.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d) != null) {
            result -= ((double)LiquidSpeed.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c() + 1.0) * 0.15;
        }
        return result;
    }

    private void ySwim(PlayerMoveEvent moveEvent, double vBoost, double upSpeed, double downSpeed) {
        boolean jump = LiquidSpeed.mc.field_71439_g.field_71158_b.field_78901_c;
        boolean sneak = LiquidSpeed.mc.field_71439_g.field_71158_b.field_78899_d;
        this.motionY = Math.pow(0.1, (Double)this.jitter.getValue());
        if (!jump || !sneak) {
            if (jump) {
                this.motionY = Math.min(this.motionY + vBoost / 20.0, upSpeed / 20.0);
            }
            if (sneak) {
                this.motionY = Math.max(this.motionY - vBoost / 20.0, -downSpeed / 20.0);
            }
        }
        moveEvent.setY(this.motionY);
    }

    private void stopMotion(PlayerMoveEvent event) {
        event.setX(0.0);
        event.setZ(0.0);
        this.moveSpeed = 0.0;
    }

    private void reset() {
        this.moveSpeed = 0.0;
        this.motionY = 0.0;
    }
}
