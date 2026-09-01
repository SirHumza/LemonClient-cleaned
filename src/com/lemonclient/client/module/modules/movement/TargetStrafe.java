/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.event.events.MotionUpdateEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.movement.HoleSnap;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;

@Module.Declaration(name="TargetStrafe", category=Category.Movement)
public class TargetStrafe
extends Module {
    IntegerSetting range = this.registerInteger("TargetRange", 20, 0, 256);
    BooleanSetting jump = this.registerBoolean("Jump", true);
    BooleanSetting antiStuck = this.registerBoolean("AntiStuck", true);
    DoubleSetting distanceSetting = this.registerDouble("PreferredDistance", 1.0, 0.0, 10.0);
    DoubleSetting maxDistance = this.registerDouble("MaxDistance", 10.0, 1.0, 32.0);
    DoubleSetting turnAmount = this.registerDouble("TurnAmount", 5.0, 1.0, 90.0);
    String pattern = "%.1f";
    Timing lagBackCoolDown = new Timing();
    Timing boostTimer = new Timing();
    long detectionTime;
    boolean checkCoolDown = false;
    double boostSpeed;
    double boostSpeed2;
    double lastDist;
    int level = 1;
    double moveSpeed;
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (TargetStrafe.mc.field_71441_e == null || TargetStrafe.mc.field_71439_g == null || TargetStrafe.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.lastDist = 0.0;
            this.moveSpeed = Math.min(this.getBaseMoveSpeed(), this.getBaseMoveSpeed());
            this.detectionTime = System.currentTimeMillis();
            if (!this.checkCoolDown) {
                this.lagBackCoolDown.reset();
                this.checkCoolDown = true;
            }
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).func_149412_c() == TargetStrafe.mc.field_71439_g.func_145782_y()) {
            this.boostSpeed2 = this.boostSpeed = Math.hypot((float)((SPacketEntityVelocity)event.getPacket()).field_149415_b / 8000.0f, (float)((SPacketEntityVelocity)event.getPacket()).field_149414_d / 8000.0f);
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<MotionUpdateEvent> motionUpdateEventListener = new Listener<MotionUpdateEvent>(event -> {
        if (TargetStrafe.mc.field_71441_e == null || TargetStrafe.mc.field_71439_g == null || TargetStrafe.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (ModuleManager.getModule(HoleSnap.class).isEnabled()) {
            return;
        }
        try {
            if (this.lagBackCoolDown.passedMs((long)Double.parseDouble(String.format(this.pattern, 1000.0)))) {
                this.checkCoolDown = false;
                this.lagBackCoolDown.reset();
            }
            if (event.stage == 1) {
                this.lastDist = Math.sqrt((TargetStrafe.mc.field_71439_g.field_70165_t - TargetStrafe.mc.field_71439_g.field_70169_q) * (TargetStrafe.mc.field_71439_g.field_70165_t - TargetStrafe.mc.field_71439_g.field_70169_q) + (TargetStrafe.mc.field_71439_g.field_70161_v - TargetStrafe.mc.field_71439_g.field_70166_s) * (TargetStrafe.mc.field_71439_g.field_70161_v - TargetStrafe.mc.field_71439_g.field_70166_s));
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerMoveEvent> playerMoveEventListener = new Listener<PlayerMoveEvent>(event -> {
        if (TargetStrafe.mc.field_71441_e == null || TargetStrafe.mc.field_71439_g == null || TargetStrafe.mc.field_71439_g.field_70128_L) {
            return;
        }
        EntityPlayer target = PlayerUtil.getNearestPlayer(((Integer)this.range.getValue()).intValue());
        if (target != null) {
            if (TargetStrafe.mc.field_71439_g.func_180799_ab() || TargetStrafe.mc.field_71439_g.func_70090_H() || TargetStrafe.mc.field_71439_g.field_70134_J) {
                return;
            }
            if (TargetStrafe.mc.field_71439_g.field_70122_E) {
                this.level = 2;
            }
            if (TargetStrafe.round(TargetStrafe.mc.field_71439_g.field_70163_u - (double)((int)TargetStrafe.mc.field_71439_g.field_70163_u), 3) == TargetStrafe.round(0.138, 3) && ((Boolean)this.jump.getValue()).booleanValue()) {
                EntityPlayerSP player = TargetStrafe.mc.field_71439_g;
                player.field_70181_x -= 0.07;
                event.setY(event.getY() - 0.08316090325960147);
                EntityPlayerSP player2 = TargetStrafe.mc.field_71439_g;
                player2.field_70163_u -= 0.08316090325960147;
            }
            if (this.level != 1 || TargetStrafe.mc.field_71439_g.field_191988_bg == 0.0f && TargetStrafe.mc.field_71439_g.field_70702_br == 0.0f) {
                if (this.level == 2) {
                    this.level = 3;
                    if (MotionUtil.moving((EntityLivingBase)TargetStrafe.mc.field_71439_g)) {
                        if (!TargetStrafe.mc.field_71439_g.func_180799_ab() && TargetStrafe.mc.field_71439_g.field_70122_E && ((Boolean)this.jump.getValue()).booleanValue()) {
                            TargetStrafe.mc.field_71439_g.field_70181_x = 0.4;
                            event.setY(0.4);
                        }
                        this.moveSpeed *= 1.433;
                    }
                } else if (this.level == 3) {
                    this.level = 4;
                    this.moveSpeed = this.lastDist - 0.6553 * (this.lastDist - this.getBaseMoveSpeed() + 0.04);
                } else {
                    if (TargetStrafe.mc.field_71439_g.field_70122_E && (TargetStrafe.mc.field_71441_e.func_184144_a((Entity)TargetStrafe.mc.field_71439_g, TargetStrafe.mc.field_71439_g.field_70121_D.func_72317_d(0.0, TargetStrafe.mc.field_71439_g.field_70181_x, 0.0)).size() > 0 || TargetStrafe.mc.field_71439_g.field_70124_G)) {
                        this.level = 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 201.0;
                }
            } else {
                this.level = 2;
                this.moveSpeed = 1.418 * this.getBaseMoveSpeed();
            }
            if (MotionUtil.moving((EntityLivingBase)TargetStrafe.mc.field_71439_g) && this.boostSpeed2 != 0.0) {
                if (this.boostTimer.passedMs(1L)) {
                    this.moveSpeed = this.boostSpeed2;
                    this.boostTimer.reset();
                }
                this.boostSpeed2 = 0.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            if (TargetStrafe.mc.field_71439_g.field_70123_F && ((Boolean)this.antiStuck.getValue()).booleanValue()) {
                this.switchDirection();
            }
            this.doStrafeAtSpeed((PlayerMoveEvent)event, RotationUtil.getRotationTo((Vec3d)target.func_174791_d()).field_189982_i, target.func_174791_d());
        }
    }, -100, new Predicate[0]);
    int direction = 1;

    public static double round(double n, int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(n).setScale(n2, RoundingMode.HALF_UP).doubleValue();
    }

    private void switchDirection() {
        this.direction = -this.direction;
    }

    private void doStrafeAtSpeed(PlayerMoveEvent event, float rotation, Vec3d target) {
        float rotationYaw = rotation + 90.0f * (float)this.direction;
        double disX = TargetStrafe.mc.field_71439_g.field_70165_t - target.field_72450_a;
        double disZ = TargetStrafe.mc.field_71439_g.field_70161_v - target.field_72449_c;
        double distance = Math.sqrt(disX * disX + disZ * disZ);
        if (distance < (Double)this.maxDistance.getValue()) {
            if (distance > (Double)this.distanceSetting.getValue()) {
                rotationYaw = (float)((double)rotationYaw - (Double)this.turnAmount.getValue() * (double)this.direction);
            } else if (distance < (Double)this.distanceSetting.getValue()) {
                rotationYaw = (float)((double)rotationYaw + (Double)this.turnAmount.getValue() * (double)this.direction);
            }
        } else {
            rotationYaw = rotation;
        }
        if (((Boolean)this.jump.getValue()).booleanValue() && TargetStrafe.mc.field_71439_g.field_70122_E) {
            TargetStrafe.mc.field_71439_g.func_70664_aZ();
        }
        event.setX(this.moveSpeed * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
        event.setZ(this.moveSpeed * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
    }

    public double getBaseMoveSpeed() {
        double n = 0.2873;
        if (TargetStrafe.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            n *= 1.0 + 0.2 * (double)(Objects.requireNonNull(TargetStrafe.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76458_c() + 1);
        }
        return n;
    }
}
