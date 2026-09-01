/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockBed
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemBed
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBed;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

@Module.Declaration(name="SelfBed", category=Category.Dev, priority=999)
public class SelfBed
extends Module {
    ModeSetting page = this.registerMode("Page", Arrays.asList("General", "Calc"), "General");
    BooleanSetting packetPlace = this.registerBoolean("Packet Place", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting placeSwing = this.registerBoolean("Place Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting breakSwing = this.registerBoolean("Break Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetSwing = this.registerBoolean("Packet Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting highVersion = this.registerBoolean("1.13", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting autoSwitch = this.registerBoolean("Auto Switch", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting update = this.registerBoolean("Update", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting silentSwitch = this.registerBoolean("Switch Back", true, () -> ((String)this.page.getValue()).equals("General") && (Boolean)this.autoSwitch.getValue() != false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("General"));
    IntegerSetting calcDelay = this.registerInteger("Calc Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting placeDelay = this.registerInteger("Place Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting breakDelay = this.registerInteger("Break Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting range = this.registerDouble("Place Range", 5.0, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting yRange = this.registerDouble("Y Range", 2.5, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Calc"));
    ModeSetting handMode = this.registerMode("Hand", Arrays.asList("Main", "Off", "Auto"), "Auto", () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting maxDmg = this.registerDouble("Max Self Dmg", 10.0, 0.0, 20.0, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting antiSuicide = this.registerBoolean("Anti Suicide", true, () -> ((String)this.page.getValue()).equals("Calc"));
    BlockPos headPos;
    BlockPos basePos;
    float damage;
    float selfDamage;
    String face;
    Timing basetiming = new Timing();
    Timing calctiming = new Timing();
    Timing placetiming = new Timing();
    Timing breaktiming = new Timing();
    EnumHand hand;
    int slot;
    Vec2f rotation;
    int nowSlot;
    @EventHandler
    private final Listener<PacketEvent.Send> postSendListener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.nowSlot = ((CPacketHeldItemChange)event.getPacket()).func_149614_c();
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        if (SelfBed.mc.field_71439_g == null || SelfBed.mc.field_71441_e == null || EntityUtil.isDead((Entity)SelfBed.mc.field_71439_g) || this.inNether()) {
            this.basePos = null;
            this.headPos = null;
            this.selfDamage = 0.0f;
            this.damage = 0.0f;
            this.rotation = null;
            return;
        }
        this.calc();
    }

    @Override
    public void fast() {
        if (SelfBed.mc.field_71439_g == null || SelfBed.mc.field_71441_e == null || EntityUtil.isDead((Entity)SelfBed.mc.field_71439_g) || this.inNether()) {
            return;
        }
        if (SelfBed.mc.field_71439_g.field_71158_b.field_192832_b == 0.0f && SelfBed.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) {
            return;
        }
        this.bedaura();
    }

    private void bedaura() {
        if (this.headPos == null || this.basePos == null) {
            return;
        }
        if (this.isBed(this.headPos) || this.isBed(this.basePos)) {
            this.breakBed();
        }
        this.place();
        this.breakBed();
    }

    private void calc() {
        if (this.calctiming.passedMs(((Integer)this.calcDelay.getValue()).intValue())) {
            boolean offhand;
            this.calctiming.reset();
            this.basePos = null;
            this.headPos = null;
            this.selfDamage = 0.0f;
            this.damage = 0.0f;
            this.rotation = null;
            if (SelfBed.mc.field_71439_g.field_71158_b.field_192832_b == 0.0f && SelfBed.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) {
                return;
            }
            boolean bl = offhand = !((String)this.handMode.getValue()).equals("Main") && SelfBed.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151104_aV;
            if (!offhand && !((String)this.handMode.getValue()).equals("Off")) {
                this.slot = BurrowUtil.findHotbarBlock(ItemBed.class);
                if (this.slot == -1) {
                    return;
                }
            }
            this.hand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            BlockPos bedPos = this.findBlocksExcluding();
            if (bedPos == null) {
                return;
            }
            this.headPos = bedPos;
            if (SelfBed.mc.field_71439_g.func_174811_aO().equals((Object)EnumFacing.SOUTH)) {
                this.face = "SOUTH";
                this.rotation = new Vec2f(0.0f, 90.0f);
                bedPos = new BlockPos(this.headPos.field_177962_a, this.headPos.field_177960_b, this.headPos.field_177961_c - 1);
            } else if (SelfBed.mc.field_71439_g.func_174811_aO().equals((Object)EnumFacing.WEST)) {
                this.face = "WEST";
                this.rotation = new Vec2f(90.0f, 90.0f);
                bedPos = new BlockPos(this.headPos.field_177962_a + 1, this.headPos.field_177960_b, this.headPos.field_177961_c);
            } else if (SelfBed.mc.field_71439_g.func_174811_aO().equals((Object)EnumFacing.NORTH)) {
                this.face = "NORTH";
                this.rotation = new Vec2f(180.0f, 90.0f);
                bedPos = new BlockPos(this.headPos.field_177962_a, this.headPos.field_177960_b, this.headPos.field_177961_c + 1);
            } else {
                this.face = "EAST";
                this.rotation = new Vec2f(-90.0f, 90.0f);
                bedPos = new BlockPos(this.headPos.field_177962_a - 1, this.headPos.field_177960_b, this.headPos.field_177961_c);
            }
            if (!this.block(bedPos, true)) {
                this.basePos = null;
                this.headPos = null;
                this.selfDamage = 0.0f;
                this.damage = 0.0f;
                this.rotation = null;
                return;
            }
            this.headPos = this.headPos.func_177984_a();
            this.basePos = bedPos.func_177984_a();
        }
    }

    private void place() {
        if (this.placetiming.passedMs(((Integer)this.placeDelay.getValue()).intValue())) {
            BlockPos neighbour = this.basePos.func_177977_b();
            EnumFacing opposite = EnumFacing.DOWN.func_176734_d();
            Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
            boolean sneak = false;
            if (BlockUtil.blackList.contains(SelfBed.mc.field_71441_e.func_180495_p(neighbour).func_177230_c()) && !SelfBed.mc.field_71439_g.func_70093_af()) {
                SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfBed.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                sneak = true;
            }
            this.run(() -> BurrowUtil.rightClickBlock(neighbour, hitVec, this.hand, opposite, (boolean)((Boolean)this.packetPlace.getValue())), this.slot);
            if (((Boolean)this.placeSwing.getValue()).booleanValue()) {
                this.swing(this.hand);
            }
            if (sneak) {
                SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfBed.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.placetiming.reset();
        }
    }

    private void run(Runnable runnable, int slot) {
        if (this.hand == EnumHand.OFF_HAND) {
            runnable.run();
            return;
        }
        int oldSlot = SelfBed.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot != oldSlot) {
            if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
                this.switchTo(slot);
                if (this.nowSlot == slot || SelfBed.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151104_aV) {
                    runnable.run();
                }
                if (((Boolean)this.silentSwitch.getValue()).booleanValue()) {
                    this.switchTo(oldSlot);
                }
            }
        } else {
            runnable.run();
        }
    }

    private void breakBed() {
        if (this.breaktiming.passedMs(((Integer)this.breakDelay.getValue()).intValue())) {
            EnumFacing side = EnumFacing.UP;
            if (ModuleManager.getModule(ColorMain.class).sneaking) {
                SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfBed.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            Vec3d facing = this.getHitVecOffset(side);
            if (this.isBed(this.headPos) && !this.isBed(this.basePos)) {
                SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.headPos, side, this.hand, (float)facing.field_72450_a, (float)facing.field_72448_b, (float)facing.field_72449_c));
            } else {
                SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.basePos, side, this.hand, (float)facing.field_72450_a, (float)facing.field_72448_b, (float)facing.field_72449_c));
            }
            if (((Boolean)this.breakSwing.getValue()).booleanValue()) {
                this.swing(this.hand);
            }
            this.breaktiming.reset();
        }
    }

    private BlockPos findBlocksExcluding() {
        double x = SelfBed.mc.field_71439_g.field_70169_q;
        double z = SelfBed.mc.field_71439_g.field_70166_s;
        double dX = SelfBed.mc.field_71439_g.field_70165_t - x;
        double dZ = SelfBed.mc.field_71439_g.field_70161_v - z;
        ArrayList posList = new ArrayList();
        for (int y : new int[]{-3, -2, -1, 0, 1, 2}) {
            posList.addAll(EntityUtil.getSphere(PlayerUtil.getEyesPos(), (Double)this.range.getValue() + 1.0, 1.0, false, false, y).stream().filter(p -> (SelfBed.mc.field_71439_g.field_70165_t - x) * (SelfBed.mc.field_71439_g.field_70165_t - (double)p.field_177962_a) > 0.0 && (SelfBed.mc.field_71439_g.field_70161_v - z) * (SelfBed.mc.field_71439_g.field_70161_v - (double)p.field_177961_c) > 0.0).filter(this::canPlaceBed).filter(p -> (x - (double)p.field_177962_a) * dX >= 0.0 && (z - (double)p.field_177961_c) * dZ >= 0.0).filter(p -> {
                double dmg = DamageUtil.calculateDamage((EntityLivingBase)SelfBed.mc.field_71439_g, SelfBed.mc.field_71439_g.func_174791_d(), SelfBed.mc.field_71439_g.func_174813_aQ(), (double)p.field_177962_a + 0.5, (double)p.field_177960_b + 1.5625, (double)p.field_177961_c + 0.5, 5.0f, "Bed");
                return dmg <= (Double)this.maxDmg.getValue() && ((Boolean)this.antiSuicide.getValue() == false || dmg <= (double)(EntityUtil.getHealth((Entity)SelfBed.mc.field_71439_g) + 1.0f));
            }).collect(Collectors.toList()));
        }
        BlockPos pos = posList.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)SelfBed.mc.field_71439_g).func_174818_b(arg_0))).orElse(null);
        return pos;
    }

    private boolean canPlaceBed(BlockPos blockPos) {
        if (!this.block(blockPos, false)) {
            return false;
        }
        BlockPos pos = blockPos.func_177967_a(SelfBed.mc.field_71439_g.func_174811_aO(), -1);
        return this.block(pos, true) && this.inRange(pos.func_177984_a());
    }

    private boolean block(BlockPos pos, boolean rangeCheck) {
        if (!this.space(pos.func_177984_a())) {
            return false;
        }
        if (BlockUtil.canReplace(pos)) {
            return false;
        }
        if (!((Boolean)this.highVersion.getValue()).booleanValue() && !this.solid(pos)) {
            return false;
        }
        return !rangeCheck || this.inRange(pos.func_177984_a());
    }

    private boolean isBed(BlockPos pos) {
        Block block = SelfBed.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return block == Blocks.field_150324_C || block instanceof BlockBed;
    }

    private boolean space(BlockPos pos) {
        return SelfBed.mc.field_71441_e.func_175623_d(pos) || SelfBed.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C;
    }

    private boolean solid(BlockPos pos) {
        return !BlockUtil.isBlockUnSolid(pos) && !(SelfBed.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockBed) && SelfBed.mc.field_71441_e.func_180495_p(pos).isSideSolid((IBlockAccess)SelfBed.mc.field_71441_e, pos, EnumFacing.UP);
    }

    private boolean inRange(BlockPos pos) {
        double x = (double)pos.field_177962_a - SelfBed.mc.field_71439_g.field_70165_t;
        double z = (double)pos.field_177961_c - SelfBed.mc.field_71439_g.field_70161_v;
        double y = pos.field_177960_b - PlayerUtil.getEyesPos().field_177960_b;
        double add = Math.sqrt(y * y) / 2.0;
        return x * x + z * z <= ((Double)this.range.getValue() - add) * ((Double)this.range.getValue() - add) && y * y <= (Double)this.yRange.getValue() * (Double)this.yRange.getValue();
    }

    private Vec3d getHitVecOffset(EnumFacing face) {
        Vec3i vec = face.func_176730_m();
        return new Vec3d((double)((float)vec.field_177962_a * 0.5f + 0.5f), (double)((float)vec.field_177960_b * 0.5f + 0.5f), (double)((float)vec.field_177961_c * 0.5f + 0.5f));
    }

    private void switchTo(int slot) {
        if (slot > -1 && slot < 9) {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                SelfBed.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            if (((Boolean)this.update.getValue()).booleanValue()) {
                SelfBed.mc.field_71442_b.func_78765_e();
            }
        }
    }

    private void swing(EnumHand hand) {
        if (((Boolean)this.packetSwing.getValue()).booleanValue()) {
            SelfBed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(hand));
        } else {
            SelfBed.mc.field_71439_g.func_184609_a(hand);
        }
    }

    private boolean inNether() {
        return SelfBed.mc.field_71439_g.field_71093_bK == 0;
    }

    @Override
    public void onEnable() {
        this.calctiming.reset();
        this.basetiming.reset();
        this.placetiming.reset();
        this.breaktiming.reset();
    }
}
