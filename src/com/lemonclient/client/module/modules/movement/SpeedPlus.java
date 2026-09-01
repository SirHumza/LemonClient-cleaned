/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  org.lwjgl.input.Keyboard
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.event.events.MotionUpdateEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.api.event.events.StepEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.api.util.misc.KeyBoardClass;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.api.util.world.TimerUtils;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.input.Keyboard;

@Module.Declaration(name="Speed+", category=Category.Movement, priority=999)
public class SpeedPlus
extends Module {
    public static SpeedPlus INSTANCE = new SpeedPlus();
    BooleanSetting damageBoost = this.registerBoolean("DamageBoost", true);
    public BooleanSetting sum = this.registerBoolean("Sum", false, () -> (Boolean)this.damageBoost.getValue());
    BooleanSetting longJump = this.registerBoolean("TryLongJump", false);
    IntegerSetting lagCoolDown = this.registerInteger("LagCoolDown", 2200, 0, 8000, () -> (Boolean)this.longJump.getValue());
    IntegerSetting jumpStage = this.registerInteger("JumpStage", 6, 1, 20, () -> (Boolean)this.longJump.getValue());
    BooleanSetting motionJump = this.registerBoolean("MotionJump", false, () -> (Boolean)this.longJump.getValue());
    BooleanSetting randomBoost = this.registerBoolean("RandomBoost", false);
    BooleanSetting lavaBoost = this.registerBoolean("LavaBoost", true);
    BooleanSetting SpeedInWater = this.registerBoolean("SpeedInWater", true);
    BooleanSetting strict = this.registerBoolean("Strict", false);
    BooleanSetting strictBoost = this.registerBoolean("StrictBoost", false, () -> (Boolean)this.damageBoost.getValue());
    BooleanSetting useTimer = this.registerBoolean("UseTimer", true);
    BooleanSetting jump = this.registerBoolean("Jump", true);
    BooleanSetting stepCheck = this.registerBoolean("Step Check", true);
    BooleanSetting bindCheck = this.registerBoolean("Use Bind", false, () -> (Boolean)this.stepCheck.getValue());
    StringSetting bind = this.registerString("Step Check Bind", "", () -> (Boolean)this.stepCheck.getValue() != false && (Boolean)this.bindCheck.getValue() != false);
    DoubleSetting minStepHeight = this.registerDouble("Min Step Height", 1.0, 0.0, 10.0, () -> (Boolean)this.stepCheck.getValue());
    DoubleSetting maxStepHeight = this.registerDouble("Max Step Height", 2.5, 0.0, 10.0, () -> (Boolean)this.stepCheck.getValue());
    BooleanSetting test = this.registerBoolean("Test Mode", false, () -> (Boolean)this.stepCheck.getValue());
    Timing lagBackCoolDown = new Timing();
    Timing rdBoostTimer = new Timing();
    boolean lagDetected;
    boolean inCoolDown;
    boolean checkCoolDown;
    boolean warn;
    boolean checkStep;
    int readyStage;
    int stage = 1;
    int level = 1;
    double boostSpeed;
    double lastDist;
    double moveSpeed;
    double stepHigh;
    float boostFactor = 6.0f;
    long detectionTime;
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (SpeedPlus.mc.field_71441_e == null || SpeedPlus.mc.field_71439_g == null || SpeedPlus.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.lastDist = 0.0;
            this.moveSpeed = this.applySpeedPotionEffects();
            this.stage = 2;
            this.detectionTime = System.currentTimeMillis();
            this.lagDetected = true;
            this.rdBoostTimer.reset();
            this.boostFactor = 8.0f;
            if (((Boolean)this.longJump.getValue()).booleanValue()) {
                this.readyStage = 0;
                this.inCoolDown = true;
                if (!this.checkCoolDown) {
                    this.lagBackCoolDown.reset();
                    this.checkCoolDown = true;
                }
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<MotionUpdateEvent> motionUpdateEventListener = new Listener<MotionUpdateEvent>(event -> {
        if (SpeedPlus.mc.field_71441_e == null || SpeedPlus.mc.field_71439_g == null || SpeedPlus.mc.field_71439_g.field_70128_L) {
            return;
        }
        try {
            if (this.lagBackCoolDown.passedMs(((Integer)this.lagCoolDown.getValue()).intValue())) {
                this.checkCoolDown = false;
                this.inCoolDown = false;
                this.lagBackCoolDown.reset();
            }
            if (System.currentTimeMillis() - this.detectionTime > 3182L) {
                this.lagDetected = false;
            }
            if (((Boolean)this.useTimer.getValue()).booleanValue()) {
                TimerUtils.setTickLength(45.955883f);
            }
            if (event.stage == 1) {
                this.lastDist = Math.sqrt((SpeedPlus.mc.field_71439_g.field_70165_t - SpeedPlus.mc.field_71439_g.field_70169_q) * (SpeedPlus.mc.field_71439_g.field_70165_t - SpeedPlus.mc.field_71439_g.field_70169_q) + (SpeedPlus.mc.field_71439_g.field_70161_v - SpeedPlus.mc.field_71439_g.field_70166_s) * (SpeedPlus.mc.field_71439_g.field_70161_v - SpeedPlus.mc.field_71439_g.field_70166_s));
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerMoveEvent> playerMoveEventListener = new Listener<PlayerMoveEvent>(event -> {
        if (SpeedPlus.mc.field_71441_e == null || SpeedPlus.mc.field_71439_g == null || SpeedPlus.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (SpeedPlus.mc.field_71439_g.field_71158_b.field_192832_b == 0.0f && SpeedPlus.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
            event.setSpeed(0.0);
            return;
        }
        if (this.checkStep && ((Boolean)this.test.getValue()).booleanValue()) {
            double yaw = this.calcMoveYaw(SpeedPlus.mc.field_71439_g.field_70177_z, SpeedPlus.mc.field_71439_g.field_71158_b.field_192832_b, SpeedPlus.mc.field_71439_g.field_71158_b.field_78902_a);
            double dirX = -Math.sin(yaw);
            double dirZ = Math.cos(yaw);
            double dist = this.calcBlockDistAhead(dirX * 6.0, dirZ * 6.0);
            double stepHeight = (Boolean)this.test.getValue() != false ? this.calcStepHeight(dist, dirX, dirZ) : this.stepHigh;
            double multiplier = this.applySpeedPotionEffects();
            if (stepHeight <= (Double)this.maxStepHeight.getValue()) {
                if (dist < 3.0 * multiplier && stepHeight > (Double)this.minStepHeight.getValue() * 2.0) {
                    return;
                }
                if (dist < 1.4 * multiplier && stepHeight > (Double)this.minStepHeight.getValue()) {
                    return;
                }
            }
        }
        if (!((Boolean)this.SpeedInWater.getValue()).booleanValue() && this.shouldReturn()) {
            return;
        }
        if (SpeedPlus.mc.field_71439_g.field_70122_E) {
            this.level = 2;
        }
        if (SpeedPlus.round(SpeedPlus.mc.field_71439_g.field_70163_u - (double)((int)SpeedPlus.mc.field_71439_g.field_70163_u), 3) == SpeedPlus.round(0.138, 3) && ((Boolean)this.jump.getValue()).booleanValue()) {
            SpeedPlus.mc.field_71439_g.field_70181_x -= 0.07;
            event.setY(event.getY() - 0.08316090325960147);
            SpeedPlus.mc.field_71439_g.field_70163_u -= 0.08316090325960147;
        }
        if (this.level != 1) {
            if (this.level == 2) {
                this.level = 3;
                if (!SpeedPlus.mc.field_71439_g.func_180799_ab() && SpeedPlus.mc.field_71439_g.field_70122_E && ((Boolean)this.jump.getValue()).booleanValue()) {
                    SpeedPlus.mc.field_71439_g.field_70181_x = this.applyJumpBoostPotionEffects();
                    event.setY(SpeedPlus.mc.field_71439_g.field_70181_x);
                }
                this.moveSpeed = ((Boolean)this.strict.getValue()).booleanValue() || SpeedPlus.mc.field_71439_g.func_70093_af() ? (this.moveSpeed *= 1.433) : (this.moveSpeed *= 1.64847275);
            } else if (this.level == 3) {
                this.level = 4;
                this.moveSpeed = this.lastDist - 0.6553 * (this.lastDist - this.applySpeedPotionEffects() + 0.04);
            } else {
                if (SpeedPlus.mc.field_71439_g.field_70122_E && (!SpeedPlus.mc.field_71441_e.func_184144_a((Entity)SpeedPlus.mc.field_71439_g, SpeedPlus.mc.field_71439_g.field_70121_D.func_72317_d(0.0, SpeedPlus.mc.field_71439_g.field_70181_x, 0.0)).isEmpty() || SpeedPlus.mc.field_71439_g.field_70124_G)) {
                    this.level = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 201.0;
            }
        } else {
            this.level = 2;
            this.moveSpeed = 1.418 * this.applySpeedPotionEffects();
        }
        if (((Boolean)this.damageBoost.getValue()).booleanValue() && ColorMain.INSTANCE.velocityBoost != 0.0) {
            if (((Boolean)this.longJump.getValue()).booleanValue()) {
                ++this.readyStage;
            }
            this.boostSpeed = ColorMain.INSTANCE.velocityBoost;
            this.moveSpeed += this.boostSpeed;
            if (((Boolean)this.strictBoost.getValue()).booleanValue()) {
                this.moveSpeed = Math.max((this.moveSpeed + (double)0.1f) / 1.5, this.applySpeedPotionEffects());
            }
            ColorMain.INSTANCE.velocityBoost = 0.0;
        }
        if (((Boolean)this.randomBoost.getValue()).booleanValue() && this.rdBoostTimer.passedMs(3500L) && !this.lagDetected && MotionUtil.moving((EntityLivingBase)SpeedPlus.mc.field_71439_g) && SpeedPlus.mc.field_71439_g.field_70122_E) {
            this.moveSpeed += this.moveSpeed / (double)this.boostFactor;
            this.boostFactor = 6.0f;
            this.rdBoostTimer.reset();
        }
        if (((Boolean)this.longJump.getValue()).booleanValue() && this.readyStage >= (Integer)this.jumpStage.getValue() && !this.inCoolDown) {
            if (!((Boolean)this.motionJump.getValue()).booleanValue()) {
                this.moveSpeed *= (double)((float)((Integer)this.jumpStage.getValue()).intValue() / 10.0f);
            } else {
                SpeedPlus.motionJump();
                SpeedPlus.mc.field_71439_g.field_70181_x *= 1.02;
                SpeedPlus.mc.field_71439_g.field_70181_x *= 1.13;
                SpeedPlus.mc.field_71439_g.field_70181_x *= 1.27;
                this.moveSpeed += Math.abs(this.moveSpeed - this.boostSpeed);
            }
            this.readyStage = 0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.applySpeedPotionEffects());
        if (!this.shouldReturn()) {
            event.setSpeed(this.moveSpeed);
        } else if (((Boolean)this.lavaBoost.getValue()).booleanValue() && SpeedPlus.mc.field_71439_g.func_180799_ab()) {
            event.setX(event.getX() * 3.1);
            event.setZ(event.getZ() * 3.1);
            if (SpeedPlus.mc.field_71474_y.field_74314_A.func_151470_d()) {
                event.setY(event.getY() * 3.0);
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<StepEvent> stepEventListener = new Listener<StepEvent>(event -> {
        this.stepHigh = event.getBB().field_72338_b - SpeedPlus.mc.field_71439_g.field_70163_u;
    }, new Predicate[0]);

    @Override
    public void onTick() {
        this.checkStep = false;
        if (((Boolean)this.stepCheck.getValue()).booleanValue()) {
            if (((Boolean)this.bindCheck.getValue()).booleanValue()) {
                if (this.bind.getText().isEmpty() || !Keyboard.isKeyDown((int)KeyBoardClass.getKeyFromChar(this.bind.getText().charAt(0)))) {
                    this.checkStep = !this.checkStep;
                }
            } else {
                this.checkStep = true;
            }
        }
    }

    public static double round(double n, int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(n).setScale(n2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public void onEnable() {
        if (SpeedPlus.mc.field_71439_g == null) {
            this.disable();
            return;
        }
        this.boostSpeed = 0.0;
        this.lagBackCoolDown.reset();
        this.readyStage = 0;
        this.warn = false;
        this.moveSpeed = this.applySpeedPotionEffects();
    }

    public static void motionJump() {
        if (!SpeedPlus.mc.field_71439_g.field_70124_G) {
            if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.07190068807140403) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.35f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.10306193759436909) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.55f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.13395038817442878) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.67f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.16635183030382) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.69f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.19088711097794803) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.71f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.21121925191528862) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.2f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.11979897632390576) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.93f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.18758479151225355) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.72f;
            } else if (SpeedPlus.mc.field_71439_g.field_70181_x == -0.21075983825251726) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= (double)0.76f;
            }
            if (SpeedPlus.mc.field_71439_g.field_70181_x < -0.2 && SpeedPlus.mc.field_71439_g.field_70181_x > -0.24) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= 0.7;
            }
            if (SpeedPlus.mc.field_71439_g.field_70181_x < -0.25 && SpeedPlus.mc.field_71439_g.field_70181_x > -0.32) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= 0.8;
            }
            if (SpeedPlus.mc.field_71439_g.field_70181_x < -0.35 && SpeedPlus.mc.field_71439_g.field_70181_x > -0.8) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= 0.98;
            }
            if (SpeedPlus.mc.field_71439_g.field_70181_x < -0.8 && SpeedPlus.mc.field_71439_g.field_70181_x > -1.6) {
                SpeedPlus.mc.field_71439_g.field_70181_x *= 0.99;
            }
        }
    }

    public boolean shouldReturn() {
        return SpeedPlus.mc.field_71439_g.func_180799_ab() || SpeedPlus.mc.field_71439_g.func_70090_H() || SpeedPlus.mc.field_71439_g.field_70134_J;
    }

    @Override
    public void onDisable() {
        this.moveSpeed = 0.0;
        this.stage = 2;
        if (SpeedPlus.mc.field_71439_g != null) {
            SpeedPlus.mc.field_71439_g.field_70138_W = 0.6f;
            TimerUtils.setTickLength(50.0f);
        }
    }

    private double calcBlockDistAhead(double offsetX, double offsetZ) {
        if (SpeedPlus.mc.field_71439_g.field_70123_F) {
            return 0.0;
        }
        AxisAlignedBB box = SpeedPlus.mc.field_71439_g.field_70121_D;
        double x = offsetX > 0.0 ? box.field_72336_d : box.field_72340_a;
        double z = offsetX > 0.0 ? box.field_72334_f : box.field_72339_c;
        return Math.min(this.rayTraceDist(new Vec3d(x, box.field_72338_b + 0.6, z), offsetX, offsetZ), this.rayTraceDist(new Vec3d(x, box.field_72337_e + 0.6, z), offsetX, offsetZ));
    }

    private double rayTraceDist(Vec3d start, double offsetX, double offsetZ) {
        RayTraceResult result = SpeedPlus.mc.field_71441_e.func_147447_a(start, start.func_72441_c(offsetX, 0.0, offsetZ), false, true, false);
        if (result != null && result.field_72307_f != null) {
            double x = start.field_72450_a - result.field_72307_f.field_72450_a;
            double z = start.field_72449_c - result.field_72307_f.field_72449_c;
            return Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        }
        return 999.0;
    }

    private double calcMoveYaw(float yaw, float moveForward, float moveStrafe) {
        double moveYaw = moveForward == 0.0f && moveStrafe == 0.0f ? 0.0 : Math.toDegrees(Math.atan2(moveForward, moveStrafe)) - 90.0;
        return Math.toRadians(RotationUtil.normalizeAngle((double)yaw + moveYaw));
    }

    private double calcStepHeight(double dist, double motionX, double motionZ) {
        BlockPos pos = PlayerUtil.getPlayerPos();
        if (SpeedPlus.mc.field_71441_e.func_180495_p(pos).func_185890_d((IBlockAccess)SpeedPlus.mc.field_71441_e, pos) != null) {
            return 0.0;
        }
        double i = Math.max(Math.round(dist), 1L);
        double minStepHeight = Double.MAX_VALUE;
        double x = motionX * i;
        double z = motionZ * i;
        minStepHeight = this.checkBox(minStepHeight, x, 0.0);
        return (minStepHeight = this.checkBox(minStepHeight, 0.0, z)) == Double.MAX_VALUE ? 0.0 : minStepHeight;
    }

    private double checkBox(double minStepHeight, double offsetX, double offsetZ) {
        AxisAlignedBB box = SpeedPlus.mc.field_71439_g.field_70121_D.func_72317_d(offsetX, 0.0, offsetZ);
        if (!SpeedPlus.mc.field_71441_e.func_184143_b(box)) {
            return minStepHeight;
        }
        double stepHeight = minStepHeight;
        for (double y : new double[]{0.605, 1.005, 1.505, 2.005, 2.505}) {
            double maxStepHeight;
            if (y > minStepHeight) break;
            AxisAlignedBB stepBox = new AxisAlignedBB(box.field_72340_a, box.field_72338_b + y - 0.5, box.field_72339_c, box.field_72336_d, box.field_72338_b + y, box.field_72334_f);
            List boxList = SpeedPlus.mc.field_71441_e.func_184144_a(null, stepBox);
            AxisAlignedBB maxHeight = boxList.stream().max(Comparator.comparing(bb -> bb.field_72337_e)).orElse(null);
            if (maxHeight == null || SpeedPlus.mc.field_71441_e.func_184143_b(box.func_72317_d(0.0, maxStepHeight = maxHeight.field_72337_e - SpeedPlus.mc.field_71439_g.field_70163_u, 0.0))) continue;
            stepHeight = maxStepHeight;
            break;
        }
        return stepHeight;
    }

    private double applySpeedPotionEffects() {
        double result = 0.2873;
        if (SpeedPlus.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c) != null) {
            result += 0.2873 * ((double)SpeedPlus.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c() + 1.0) * 0.2;
        }
        if (SpeedPlus.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d) != null) {
            result -= 0.2873 * ((double)SpeedPlus.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c() + 1.0) * 0.15;
        }
        return result;
    }

    private double applyJumpBoostPotionEffects() {
        double result = 0.4;
        if (SpeedPlus.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j) != null) {
            result += (double)(SpeedPlus.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1;
        }
        return result;
    }
}
