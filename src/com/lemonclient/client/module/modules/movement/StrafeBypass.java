/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.event.LemonClientEvent;
import com.lemonclient.api.event.events.MotionUpdateEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.PlayerJumpEvent;
import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.movement.SpeedPlus;
import java.util.Arrays;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@Module.Declaration(name="StrafeBypass", category=Category.Movement, priority=999)
public class StrafeBypass
extends Module {
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("Strict", "Normal"), "Normal");
    BooleanSetting boost = this.registerBoolean("DamageBoost", false);
    BooleanSetting randomBoost = this.registerBoolean("RandomBoost", false);
    BooleanSetting debug = this.registerBoolean("Debug", false);
    public Timing rdBoostTimer = new Timing();
    public float boostFactor = 4.0f;
    public long detectionTime;
    public boolean lagDetected;
    public double boostSpeed;
    public int stage = 1;
    private double lastDist;
    private double moveSpeed;
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (StrafeBypass.mc.field_71441_e == null || StrafeBypass.mc.field_71439_g == null || StrafeBypass.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).func_149412_c() == StrafeBypass.mc.field_71439_g.func_145782_y() && !ModuleManager.getModule(SpeedPlus.class).isEnabled()) {
            this.boostSpeed = Math.max(Math.hypot((float)((SPacketEntityVelocity)event.getPacket()).field_149415_b / 8000.0f, (float)((SPacketEntityVelocity)event.getPacket()).field_149414_d / 8000.0f), this.boostSpeed);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.detectionTime = System.currentTimeMillis();
            this.lagDetected = true;
            this.rdBoostTimer.reset();
            this.boostFactor = 6.0f;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<MotionUpdateEvent> motionUpdateEventListener = new Listener<MotionUpdateEvent>(event -> {
        if (StrafeBypass.mc.field_71441_e == null || StrafeBypass.mc.field_71439_g == null || StrafeBypass.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getEra() != LemonClientEvent.Era.PRE) {
            return;
        }
        if (System.currentTimeMillis() - this.detectionTime > 3182L) {
            this.lagDetected = false;
        }
        if (event.stage == 1) {
            this.lastDist = Math.sqrt((StrafeBypass.mc.field_71439_g.field_70165_t - StrafeBypass.mc.field_71439_g.field_70169_q) * (StrafeBypass.mc.field_71439_g.field_70165_t - StrafeBypass.mc.field_71439_g.field_70169_q) + (StrafeBypass.mc.field_71439_g.field_70161_v - StrafeBypass.mc.field_71439_g.field_70166_s) * (StrafeBypass.mc.field_71439_g.field_70161_v - StrafeBypass.mc.field_71439_g.field_70166_s));
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerJumpEvent> jumpEventListener = new Listener<PlayerJumpEvent>(event -> {
        if (StrafeBypass.mc.field_71441_e == null || StrafeBypass.mc.field_71439_g == null || StrafeBypass.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (!StrafeBypass.mc.field_71439_g.func_70090_H() && !StrafeBypass.mc.field_71439_g.func_180799_ab()) {
            event.cancel();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerMoveEvent> moveEventListener = new Listener<PlayerMoveEvent>(event -> {
        if (StrafeBypass.mc.field_71441_e == null || StrafeBypass.mc.field_71439_g == null || StrafeBypass.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (!StrafeBypass.mc.field_71439_g.func_70090_H() && !StrafeBypass.mc.field_71439_g.func_180799_ab()) {
            if ((double)StrafeBypass.mc.field_71439_g.field_71158_b.field_192832_b == 0.0 && (double)StrafeBypass.mc.field_71439_g.field_71158_b.field_78902_a == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
                event.setSpeed(0.0);
                return;
            }
            if (StrafeBypass.mc.field_71439_g.field_70122_E) {
                this.stage = 2;
            }
            switch (this.stage) {
                case 0: {
                    ++this.stage;
                    this.lastDist = 0.0;
                    break;
                }
                case 3: {
                    this.moveSpeed = this.lastDist - (((String)this.mode.getValue()).equals("Normal") ? 0.6896 : 0.795) * (this.lastDist - this.getBaseMoveSpeed());
                    break;
                }
                default: {
                    if ((!StrafeBypass.mc.field_71441_e.func_184144_a((Entity)StrafeBypass.mc.field_71439_g, StrafeBypass.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, StrafeBypass.mc.field_71439_g.field_70181_x, 0.0)).isEmpty() || StrafeBypass.mc.field_71439_g.field_70124_G) && this.stage > 0) {
                        this.stage = StrafeBypass.mc.field_71439_g.field_191988_bg != 0.0f || StrafeBypass.mc.field_71439_g.field_70702_br != 0.0f ? 1 : 0;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
            }
            if (((Boolean)this.boost.getValue()).booleanValue() && this.boostSpeed != 0.0 && MotionUtil.moving((EntityLivingBase)StrafeBypass.mc.field_71439_g)) {
                this.moveSpeed += this.boostSpeed;
                this.boostSpeed = 0.0;
            }
            if (((Boolean)this.randomBoost.getValue()).booleanValue() && this.rdBoostTimer.passedMs(3500L) && !this.lagDetected && MotionUtil.moving((EntityLivingBase)StrafeBypass.mc.field_71439_g) && StrafeBypass.mc.field_71439_g.field_70122_E) {
                this.moveSpeed += this.moveSpeed / (double)this.boostFactor;
                if (((Boolean)this.debug.getValue()).booleanValue()) {
                    MessageBus.sendClientPrefixMessage("RandomBoost", Notification.Type.INFO);
                }
                this.boostFactor = 4.0f;
                this.rdBoostTimer.reset();
            }
            this.moveSpeed = !StrafeBypass.mc.field_71474_y.field_74314_A.func_151470_d() && StrafeBypass.mc.field_71439_g.field_70122_E ? this.getBaseMoveSpeed() : Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            if ((double)StrafeBypass.mc.field_71439_g.field_71158_b.field_192832_b != 0.0 && (double)StrafeBypass.mc.field_71439_g.field_71158_b.field_78902_a != 0.0) {
                StrafeBypass.mc.field_71439_g.field_71158_b.field_192832_b *= (float)Math.sin(0.7853981633974483);
                StrafeBypass.mc.field_71439_g.field_71158_b.field_78902_a *= (float)Math.cos(0.7853981633974483);
            }
            event.setX(((double)StrafeBypass.mc.field_71439_g.field_71158_b.field_192832_b * this.moveSpeed * -Math.sin(Math.toRadians(StrafeBypass.mc.field_71439_g.field_70177_z)) + (double)StrafeBypass.mc.field_71439_g.field_71158_b.field_78902_a * this.moveSpeed * Math.cos(Math.toRadians(StrafeBypass.mc.field_71439_g.field_70177_z))) * (((String)this.mode.getValue()).equals("Normal") ? 0.993 : 0.99));
            event.setZ(((double)StrafeBypass.mc.field_71439_g.field_71158_b.field_192832_b * this.moveSpeed * Math.cos(Math.toRadians(StrafeBypass.mc.field_71439_g.field_70177_z)) - (double)StrafeBypass.mc.field_71439_g.field_71158_b.field_78902_a * this.moveSpeed * -Math.sin(Math.toRadians(StrafeBypass.mc.field_71439_g.field_70177_z))) * (((String)this.mode.getValue()).equals("Normal") ? 0.993 : 0.99));
            ++this.stage;
        }
    }, new Predicate[0]);

    public double getBaseMoveSpeed() {
        double result = 0.2873;
        if (StrafeBypass.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c) != null) {
            result += 0.2873 * ((double)StrafeBypass.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c() + 1.0) * 0.2;
        }
        if (StrafeBypass.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d) != null) {
            result -= 0.2873 * ((double)StrafeBypass.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c() + 1.0) * 0.15;
        }
        return result;
    }
}
