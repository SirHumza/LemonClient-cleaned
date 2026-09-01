/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.MovementInputFromOptions
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.InputUpdateEvent
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.api.event.events.StepEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.api.util.world.TimerUtils;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import java.lang.invoke.LambdaMetafactory;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Declaration(name="HoleSnap", category=Category.Movement, priority=120)
public class HoleSnap
extends Module {
    IntegerSetting downRange = this.registerInteger("Down Range", 5, 0, 8);
    IntegerSetting upRange = this.registerInteger("Up Range", 1, 0, 8);
    DoubleSetting hRange = this.registerDouble("H Range", 4.0, 1.0, 8.0);
    DoubleSetting timer = this.registerDouble("Timer", 2.0, 1.0, 50.0);
    DoubleSetting speed = this.registerDouble("Max Speed", 2.0, 0.0, 10.0);
    BooleanSetting step = this.registerBoolean("Step", true);
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("NCP", "Vanilla"), "NCP", () -> (Boolean)this.step.getValue());
    ModeSetting height = this.registerMode("NCP Height", Arrays.asList("1", "1.5", "2", "2.5", "3", "4"), "2.5", () -> ((String)this.mode.getValue()).equalsIgnoreCase("NCP") && (Boolean)this.step.getValue() != false);
    ModeSetting vHeight = this.registerMode("Vanilla Height", Arrays.asList("1", "1.5", "2", "2.5", "3", "4"), "2.5", () -> ((String)this.mode.getValue()).equalsIgnoreCase("Vanilla") && (Boolean)this.step.getValue() != false);
    BooleanSetting abnormal = this.registerBoolean("Abnormal", false, () -> !((String)this.mode.getValue()).equalsIgnoreCase("Vanilla") && (Boolean)this.step.getValue() != false);
    IntegerSetting centerSpeed = this.registerInteger("Center Speed", 2, 10, 1);
    IntegerSetting timeoutTicks = this.registerInteger("Timeout Ticks", 10, 0, 100);
    BooleanSetting only = this.registerBoolean("Only 1x1", true);
    BooleanSetting single = this.registerBoolean("Single Hole", true, () -> (Boolean)this.only.getValue() == false);
    BooleanSetting twoBlocks = this.registerBoolean("Double Hole", true, () -> (Boolean)this.only.getValue() == false);
    BooleanSetting custom = this.registerBoolean("Custom Hole", true, () -> (Boolean)this.only.getValue() == false);
    BooleanSetting four = this.registerBoolean("Four Blocks", true, () -> (Boolean)this.only.getValue() == false);
    BooleanSetting near = this.registerBoolean("Near Target", true);
    BooleanSetting bedrock = this.registerBoolean("Preferred Bedrock", true);
    BooleanSetting autoPhase = this.registerBoolean("AutoPhase OnDisable", true);
    private int stuckTicks = 0;
    private int enabledTicks = 0;
    BlockPos originPos;
    BlockPos startPos;
    boolean isActive;
    boolean slowDown;
    double playerSpeed;
    @EventHandler
    private final Listener<PacketEvent.Receive> listener = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.disable();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<InputUpdateEvent> inputUpdateEventListener = new Listener<InputUpdateEvent>(event -> {
        if (event.getMovementInput() instanceof MovementInputFromOptions && this.isActive) {
            event.getMovementInput().field_78901_c = false;
            event.getMovementInput().field_78899_d = false;
            event.getMovementInput().field_187255_c = false;
            event.getMovementInput().field_187256_d = false;
            event.getMovementInput().field_187257_e = false;
            event.getMovementInput().field_187258_f = false;
            event.getMovementInput().field_192832_b = 0.0f;
            event.getMovementInput().field_78902_a = 0.0f;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerMoveEvent> playerMoveListener = new Listener<PlayerMoveEvent>(event -> {
        this.isActive = false;
        if (++this.enabledTicks > (Integer)this.timeoutTicks.getValue()) {
            this.disable();
            return;
        }
        if (!HoleSnap.mc.field_71439_g.func_70089_S() || HoleSnap.mc.field_71439_g.func_184613_cA() || HoleSnap.mc.field_71439_g.field_71075_bZ.field_75100_b) {
            return;
        }
        double currentSpeed = Math.hypot(HoleSnap.mc.field_71439_g.field_70159_w, HoleSnap.mc.field_71439_g.field_70179_y);
        if (currentSpeed <= 0.05) {
            this.originPos = PlayerUtil.getPlayerPos();
        }
        if (this.shouldDisable(currentSpeed)) {
            this.disable();
            return;
        }
        BlockPos hole = this.findHoles();
        if (hole != null) {
            double x = (double)hole.func_177958_n() + 0.5;
            double y = hole.func_177956_o();
            double z = (double)hole.func_177952_p() + 0.5;
            this.enabledTicks = 0;
            if (this.checkYRange((int)HoleSnap.mc.field_71439_g.field_70163_u, this.originPos.field_177960_b) && HoleSnap.mc.field_71439_g.func_70011_f(x + 0.5, HoleSnap.mc.field_71439_g.field_70163_u, z + 0.5) <= (Double)this.hRange.getValue()) {
                this.isActive = true;
                TimerUtils.setTickLength((float)(50.0 / (Double)this.timer.getValue()));
                Vec3d playerPos = HoleSnap.mc.field_71439_g.func_174791_d();
                double yawRad = Math.toRadians(RotationUtil.getRotationTo((Vec3d)playerPos, (Vec3d)new Vec3d((double)x, (double)y, (double)z)).field_189982_i);
                double dist = Math.hypot(x - playerPos.field_72450_a, z - playerPos.field_72449_c);
                if (HoleSnap.mc.field_71439_g.field_70122_E) {
                    this.playerSpeed = MotionUtil.getBaseMoveSpeed() * (EntityUtil.isColliding(0.0, -0.5, 0.0) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.91 : (Double)this.speed.getValue());
                    this.slowDown = true;
                }
                double speed = Math.min(dist, this.playerSpeed);
                HoleSnap.mc.field_71439_g.field_70159_w = 0.0;
                HoleSnap.mc.field_71439_g.field_70179_y = 0.0;
                event.setX(-Math.sin(yawRad) * speed);
                event.setZ(Math.cos(yawRad) * speed);
            }
        }
        this.stuckTicks = HoleSnap.mc.field_71439_g.field_70123_F && hole == null ? ++this.stuckTicks : 0;
    }, new Predicate[0]);
    double[] pointFiveToOne = new double[]{0.41999998688698};
    double[] one = new double[]{0.41999998688698, 0.7531999805212};
    double[] oneFive = new double[]{0.42, 0.753, 1.001, 1.084, 1.006};
    double[] oneSixTwoFive = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372};
    double[] oneEightSevenFive = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652};
    double[] two = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
    double[] twoFive = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
    double[] threeStep = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43, 1.78, 1.63, 1.51, 1.9, 2.21, 2.45, 2.43};
    double[] fourStep = new double[]{0.42, 0.75, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43, 1.78, 1.63, 1.51, 1.9, 2.21, 2.45, 2.43, 2.78, 2.63, 2.51, 2.9, 3.21, 3.45, 3.43};
    @EventHandler
    private final Listener<StepEvent> stepEventListener = new Listener<StepEvent>(event -> {
        if (!this.canStep()) {
            return;
        }
        double step = event.getBB().field_72338_b - HoleSnap.mc.field_71439_g.field_70163_u;
        if (((String)this.mode.getValue()).equalsIgnoreCase("Vanilla")) {
            return;
        }
        if (((String)this.mode.getValue()).equalsIgnoreCase("NCP")) {
            if (step == 0.625 && ((Boolean)this.abnormal.getValue()).booleanValue()) {
                this.sendOffsets(this.pointFiveToOne);
            } else if (step == 1.0 || (step == 0.875 || step == 1.0625 || step == 0.9375) && ((Boolean)this.abnormal.getValue()).booleanValue()) {
                this.sendOffsets(this.one);
            } else if (step == 1.5) {
                this.sendOffsets(this.oneFive);
            } else if (step == 1.875 && ((Boolean)this.abnormal.getValue()).booleanValue()) {
                this.sendOffsets(this.oneEightSevenFive);
            } else if (step == 1.625 && ((Boolean)this.abnormal.getValue()).booleanValue()) {
                this.sendOffsets(this.oneSixTwoFive);
            } else if (step == 2.0) {
                this.sendOffsets(this.two);
            } else if (step == 2.5) {
                this.sendOffsets(this.twoFive);
            } else if (step == 3.0) {
                this.sendOffsets(this.threeStep);
            } else if (step == 4.0) {
                this.sendOffsets(this.fourStep);
            } else {
                event.cancel();
            }
        }
    }, new Predicate[0]);

    public static EntityPlayer getNearestPlayer() {
        return HoleSnap.mc.field_71441_e.field_73010_i.stream().filter(p -> HoleSnap.mc.field_71439_g.func_70032_d((Entity)p) <= 8.0f).filter(p -> HoleSnap.mc.field_71439_g.field_145783_c != p.field_145783_c).filter(p -> !EntityUtil.basicChecksEntity(p)).filter(p -> HoleUtil.isInHole((Entity)p, false, true, false)).min(Comparator.comparing(p -> Float.valueOf(HoleSnap.mc.field_71439_g.func_70032_d((Entity)p)))).orElse(null);
    }

    @Override
    public void onUpdate() {
        if (HoleSnap.mc.field_71441_e == null || HoleSnap.mc.field_71439_g == null || HoleSnap.mc.field_71439_g.field_70128_L || this.startPos == null) {
            this.disable();
            return;
        }
        if (((Boolean)this.step.getValue()).booleanValue()) {
            HoleSnap.mc.field_71439_g.field_70138_W = this.getHeight((String)this.mode.getValue());
        }
    }

    @Override
    public void onEnable() {
        this.startPos = this.originPos = PlayerUtil.getPlayerPos();
    }

    @Override
    public void onDisable() {
        this.isActive = false;
        this.enabledTicks = 0;
        this.stuckTicks = 0;
        TimerUtils.setTickLength(50.0f);
        if (HoleSnap.mc.field_71439_g != null) {
            if (HoleSnap.mc.field_71439_g.func_184187_bx() != null) {
                HoleSnap.mc.field_71439_g.func_184187_bx().field_70138_W = 1.0f;
            }
            HoleSnap.mc.field_71439_g.field_70138_W = 0.6f;
        }
        if (((Boolean)this.autoPhase.getValue()).booleanValue()) {
            ModuleManager.getModule("AutoPhase").enable();
        }
    }

    private BlockPos findHoles() {
        EntityPlayer target = HoleSnap.getNearestPlayer();
        boolean near = (Boolean)this.near.getValue() != false && target != null;
        NonNullList holes = NonNullList.func_191196_a();
        List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), (Double)this.hRange.getValue(), 8.0, false, true, 0);
        blockPosList.forEach(pos -> {
            if (!this.checkYRange((int)HoleSnap.mc.field_71439_g.field_70163_u, pos.field_177960_b)) {
                return;
            }
            if (!HoleSnap.mc.field_71441_e.func_175623_d(PlayerUtil.getPlayerPos().func_177981_b(2)) && (int)HoleSnap.mc.field_71439_g.field_70163_u < pos.field_177960_b) {
                return;
            }
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, (Boolean)this.only.getValue(), false, false);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType != HoleUtil.HoleType.NONE) {
                if (((Boolean)this.only.getValue()).booleanValue()) {
                    if (holeType != HoleUtil.HoleType.SINGLE) {
                        return;
                    }
                } else {
                    if (!((Boolean)this.single.getValue()).booleanValue() && holeType == HoleUtil.HoleType.SINGLE) {
                        return;
                    }
                    if (!((Boolean)this.twoBlocks.getValue()).booleanValue() && holeType == HoleUtil.HoleType.DOUBLE) {
                        return;
                    }
                    if (!((Boolean)this.custom.getValue()).booleanValue() && holeType == HoleUtil.HoleType.CUSTOM) {
                        return;
                    }
                    if (!((Boolean)this.four.getValue()).booleanValue() && holeType == HoleUtil.HoleType.FOUR) {
                        return;
                    }
                }
                if (HoleSnap.mc.field_71441_e.func_175623_d(pos) && HoleSnap.mc.field_71441_e.func_175623_d(pos.func_177982_a(0, 1, 0)) && HoleSnap.mc.field_71441_e.func_175623_d(pos.func_177982_a(0, 2, 0))) {
                    int high = 0;
                    while ((double)high < HoleSnap.mc.field_71439_g.field_70163_u - (double)pos.field_177960_b) {
                        if (high != 0) {
                            BlockPos newPos;
                            if (HoleSnap.mc.field_71439_g.field_70163_u > (double)pos.field_177960_b && !HoleSnap.mc.field_71441_e.func_175623_d(new BlockPos(pos.field_177962_a, pos.field_177960_b + high, pos.field_177961_c))) {
                                return;
                            }
                            if (HoleSnap.mc.field_71439_g.field_70163_u < (double)pos.field_177960_b && HoleSnap.mc.field_71441_e.func_175623_d(newPos = new BlockPos(pos.field_177962_a, pos.field_177960_b + high, pos.field_177961_c)) && (HoleSnap.mc.field_71441_e.func_175623_d(newPos.func_177977_b()) || HoleSnap.mc.field_71441_e.func_175623_d(newPos.func_177984_a()))) {
                                return;
                            }
                        }
                        ++high;
                    }
                    holes.add((Object)new HoleBlock((BlockPos)pos, (near ? target.func_70011_f((double)pos.field_177962_a + 0.5, (double)pos.field_177960_b, (double)pos.field_177961_c + 0.5) : HoleSnap.mc.field_71439_g.func_70011_f((double)pos.field_177962_a + 0.5, (double)pos.field_177960_b, (double)pos.field_177961_c + 0.5)) + (double)(((Boolean)this.bedrock.getValue()).booleanValue() ? (holeInfo.getSafety() == HoleUtil.BlockSafety.UNBREAKABLE ? -100 : 0) : 0)));
                }
            }
        });
        if (holes.isEmpty()) {
            return null;
        }
        return ((HoleBlock)holes.stream().min(Comparator.comparing((Function<HoleBlock, Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$findHoles$17(com.lemonclient.client.module.modules.movement.HoleSnap$HoleBlock ), (Lcom/lemonclient/client/module/modules/movement/HoleSnap$HoleBlock;)Ljava/lang/Double;)())).orElse(null)).pos;
    }

    private boolean shouldDisable(Double currentSpeed) {
        if (!HoleSnap.mc.field_71439_g.field_70122_E) {
            return false;
        }
        if (this.stuckTicks > 5 && currentSpeed < 0.05) {
            return true;
        }
        HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(new BlockPos((double)PlayerUtil.getPlayerPos().field_177962_a, (double)PlayerUtil.getPlayerPos().field_177960_b + 0.5, (double)PlayerUtil.getPlayerPos().field_177961_c), false, false, false);
        HoleUtil.HoleType holeType = holeInfo.getType();
        if (holeType != HoleUtil.HoleType.NONE) {
            if (((Boolean)this.only.getValue()).booleanValue()) {
                if (holeType != HoleUtil.HoleType.SINGLE) {
                    return false;
                }
            } else {
                if (!((Boolean)this.single.getValue()).booleanValue() && holeType == HoleUtil.HoleType.SINGLE) {
                    return false;
                }
                if (!((Boolean)this.twoBlocks.getValue()).booleanValue() && holeType == HoleUtil.HoleType.DOUBLE) {
                    return false;
                }
                if (!((Boolean)this.custom.getValue()).booleanValue() && holeType == HoleUtil.HoleType.CUSTOM) {
                    return false;
                }
                if (!((Boolean)this.four.getValue()).booleanValue() && holeType == HoleUtil.HoleType.FOUR) {
                    return false;
                }
            }
            Vec3d center = this.getCenter(holeInfo.getCentre());
            double XDiff = Math.abs(center.field_72450_a - HoleSnap.mc.field_71439_g.field_70165_t);
            double ZDiff = Math.abs(center.field_72449_c - HoleSnap.mc.field_71439_g.field_70161_v);
            if (!(XDiff <= 0.3) || !(ZDiff <= 0.3)) {
                double MotionX = center.field_72450_a - HoleSnap.mc.field_71439_g.field_70165_t;
                double MotionZ = center.field_72449_c - HoleSnap.mc.field_71439_g.field_70161_v;
                HoleSnap.mc.field_71439_g.field_70159_w = MotionX / (double)((Integer)this.centerSpeed.getValue()).intValue();
                HoleSnap.mc.field_71439_g.field_70179_y = MotionZ / (double)((Integer)this.centerSpeed.getValue()).intValue();
            }
            return true;
        }
        return false;
    }

    public Vec3d getCenter(AxisAlignedBB box) {
        boolean air = HoleSnap.mc.field_71441_e.func_175623_d(new BlockPos(box.field_72340_a, box.field_72338_b + 1.0, box.field_72339_c));
        return air ? new Vec3d(box.field_72340_a + (box.field_72336_d - box.field_72340_a) / 2.0, box.field_72338_b, box.field_72339_c + (box.field_72334_f - box.field_72339_c) / 2.0) : new Vec3d(box.field_72336_d - 0.5, box.field_72338_b, box.field_72334_f - 0.5);
    }

    private boolean checkYRange(int playerY, int holeY) {
        if (playerY >= holeY) {
            return playerY - holeY <= (Integer)this.downRange.getValue();
        }
        return holeY - playerY <= -((Integer)this.upRange.getValue()).intValue();
    }

    float getHeight(String mode) {
        return Float.parseFloat(mode.equals("Vanilla") ? (String)this.vHeight.getValue() : (String)this.height.getValue());
    }

    protected boolean canStep() {
        return !HoleSnap.mc.field_71439_g.func_70090_H() && HoleSnap.mc.field_71439_g.field_70122_E && !HoleSnap.mc.field_71439_g.func_70617_f_() && !HoleSnap.mc.field_71439_g.field_71158_b.field_78901_c && HoleSnap.mc.field_71439_g.field_70124_G && (double)HoleSnap.mc.field_71439_g.field_70143_R < 0.1 && (Boolean)this.step.getValue() != false;
    }

    void sendOffsets(double[] offsets) {
        for (double i : offsets) {
            HoleSnap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleSnap.mc.field_71439_g.field_70165_t, HoleSnap.mc.field_71439_g.field_70163_u + i + 0.0, HoleSnap.mc.field_71439_g.field_70161_v, false));
        }
    }

    private static /* synthetic */ Double lambda$findHoles$17(HoleBlock p) {
        return p.value;
    }

    static class HoleBlock {
        public final BlockPos pos;
        public final double value;

        public HoleBlock(BlockPos pos, double value) {
            this.pos = pos;
            this.value = value;
        }
    }
}
