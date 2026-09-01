/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockTrapDoor
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.CrystalUtil;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="HoleFill", category=Category.Combat, priority=999)
public class HoleFill
extends Module {
    BooleanSetting test = this.registerBoolean("Test", false);
    ModeSetting page = this.registerMode("Page", Arrays.asList("Target", "Place", "HoleFill", "SelfFill", "Render"), "Target");
    IntegerSetting maxTarget = this.registerInteger("Max Target", 10, 1, 50, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting tickAdd = this.registerInteger("Tick Add", 8, 1, 30, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting maxTick = this.registerInteger("Max Tick", 8, 0, 30, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting calculateYPredict = this.registerBoolean("Calculate Y Predict", true, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting startDecrease = this.registerInteger("Start Decrease", 39, 0, 200, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting exponentStartDecrease = this.registerInteger("Exponent Start", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting decreaseY = this.registerInteger("Decrease Y", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting exponentDecreaseY = this.registerInteger("Exponent Decrease Y", 1, 1, 3, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    BooleanSetting splitXZ = this.registerBoolean("Split XZ", true, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting manualOutHole = this.registerBoolean("Manual Out Hole", false, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting aboveHoleManual = this.registerBoolean("Above Hole Manual", false, () -> (Boolean)this.manualOutHole.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    BooleanSetting stairPredict = this.registerBoolean("Stair Predict", false, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting nStair = this.registerInteger("N Stair", 2, 1, 4, () -> (Boolean)this.stairPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    DoubleSetting speedActivationStair = this.registerDouble("Speed Activation Stair", 0.3, 0.0, 1.0, () -> (Boolean)this.stairPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting delay = this.registerInteger("Calc Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting upPlate = this.registerBoolean("Up Slab", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting selfFill = this.registerBoolean("Self Fill", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting mine = this.registerBoolean("Mine SelfFill", true, () -> ((String)this.page.getValue()).equals("Place") && (Boolean)this.selfFill.getValue() != false);
    BooleanSetting selfTrap = this.registerBoolean("Self Trap", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting yCheck = this.registerBoolean("Y Check", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting web = this.registerBoolean("Web", true, () -> ((String)this.page.getValue()).equals("Place") && (Boolean)this.yCheck.getValue() != false);
    BooleanSetting above = this.registerBoolean("Above", true, () -> ((String)this.page.getValue()).equals("Place") && (Boolean)this.yCheck.getValue() != false);
    BooleanSetting raytraceCheck = this.registerBoolean("Raytrace Check", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting holeCheck = this.registerBoolean("InHole Check", true, () -> ((String)this.page.getValue()).equals("Place"));
    IntegerSetting placeDelay = this.registerInteger("Place Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Place"));
    IntegerSetting bpc = this.registerInteger("Block pre Tick", 6, 1, 20, () -> ((String)this.page.getValue()).equals("Place"));
    DoubleSetting range = this.registerDouble("Range", 6.0, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Place"));
    DoubleSetting yRange = this.registerDouble("Y Range", 2.5, 0.0, 6.0, () -> ((String)this.page.getValue()).equals("Place"));
    DoubleSetting fillRange = this.registerDouble("Fill Range", 3.0, 0.0, 6.0, () -> ((String)this.page.getValue()).equals("Place"));
    DoubleSetting fillYRange = this.registerDouble("Fill YRange", 3.0, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Place"));
    DoubleSetting safety = this.registerDouble("Safety Range", 3.0, 0.0, 6.0, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting rotate = this.registerBoolean("Rotate", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting strict = this.registerBoolean("Strict", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting raytrace = this.registerBoolean("RayTrace", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting onGround = this.registerBoolean("OnGround", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting packet = this.registerBoolean("Packet Place", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting swing = this.registerBoolean("Swing", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting render = this.registerBoolean("Render", false, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting box = this.registerBoolean("Box", true, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false);
    BooleanSetting outline = this.registerBoolean("Outline", true, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false);
    IntegerSetting width = this.registerInteger("Width", 1, 1, 5, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false && (Boolean)this.outline.getValue() != false);
    ColorSetting color = this.registerColor("Color", new GSColor(255, 0, 0), () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false);
    IntegerSetting alpha = this.registerInteger("Alpha", 75, 0, 255, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false && (Boolean)this.box.getValue() != false);
    IntegerSetting outAlpha = this.registerInteger("Outline Alpha", 125, 0, 255, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false && (Boolean)this.outline.getValue() != false);
    BooleanSetting animate = this.registerBoolean("Animate", true, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false);
    IntegerSetting time = this.registerInteger("Life Time", 500, 0, 1000, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.render.getValue() != false);
    BooleanSetting hObby = this.registerBoolean("H-Obby", true, () -> ((String)this.page.getValue()).equals("HoleFill"));
    BooleanSetting hEChest = this.registerBoolean("H-EChest", true, () -> ((String)this.page.getValue()).equals("HoleFill"));
    BooleanSetting hWeb = this.registerBoolean("H-Web", true, () -> ((String)this.page.getValue()).equals("HoleFill"));
    BooleanSetting hSlab = this.registerBoolean("H-Slab", true, () -> ((String)this.page.getValue()).equals("HoleFill"));
    BooleanSetting hSkull = this.registerBoolean("H-Skull", true, () -> ((String)this.page.getValue()).equals("HoleFill"));
    BooleanSetting hTrap = this.registerBoolean("H-Trapdoor", true, () -> ((String)this.page.getValue()).equals("HoleFill"));
    BooleanSetting sObby = this.registerBoolean("S-Obby", true, () -> ((String)this.page.getValue()).equals("SelfFill"));
    BooleanSetting sEChest = this.registerBoolean("S-EChest", true, () -> ((String)this.page.getValue()).equals("SelfFill"));
    BooleanSetting sWeb = this.registerBoolean("S-Web", true, () -> ((String)this.page.getValue()).equals("SelfFill"));
    BooleanSetting sSlab = this.registerBoolean("S-Slab", true, () -> ((String)this.page.getValue()).equals("SelfFill"));
    BooleanSetting sSkull = this.registerBoolean("S-Skull", true, () -> ((String)this.page.getValue()).equals("SelfFill"));
    BooleanSetting sTrap = this.registerBoolean("S-Trapdoor", true, () -> ((String)this.page.getValue()).equals("SelfFill"));
    ModeSetting jumpMode = this.registerMode("JumpMode", Arrays.asList("Normal", "Future", "Strict"), "Normal", () -> ((String)this.page.getValue()).equals("SelfFill"));
    ModeSetting rubberBand = this.registerMode("RubberBand", Arrays.asList("Cn", "Strict", "Future", "FutureStrict", "Troll", "Void", "Auto", "Test", "Custom"), "Cn", () -> ((String)this.page.getValue()).equals("SelfFill"));
    managerClassRenderBlocks managerRenderBlocks = new managerClassRenderBlocks();
    List<BlockPos> posList = new ArrayList<BlockPos>();
    Timing timer = new Timing();
    Timing placeTimer = new Timing();
    boolean trapdoor;
    boolean mined;
    boolean self;
    boolean placedSelf;
    int placed;
    int slot;
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
    Vec3d[] add = new Vec3d[]{new Vec3d(0.1, 0.0, 0.1), new Vec3d(-0.1, 0.0, 0.1), new Vec3d(-0.1, 0.0, -0.1), new Vec3d(0.1, 0.0, -0.1)};

    @Override
    public void onTick() {
        if (HoleFill.mc.field_71441_e == null || HoleFill.mc.field_71439_g == null) {
            return;
        }
        if (this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            this.posList = this.calc();
            this.timer.reset();
        }
    }

    @Override
    public void fast() {
        if (HoleFill.mc.field_71441_e == null || HoleFill.mc.field_71439_g == null || !HoleFill.mc.field_71439_g.field_70122_E && ((Boolean)this.onGround.getValue()).booleanValue()) {
            return;
        }
        if (this.placeTimer.passedMs(((Integer)this.placeDelay.getValue()).intValue()) && !this.posList.isEmpty()) {
            this.slot = this.findRightBlock(false);
            InventoryUtil.run(this.slot, (Boolean)this.packetSwitch.getValue(), () -> {
                for (BlockPos pos : this.posList) {
                    if (this.placed >= (Integer)this.bpc.getValue()) break;
                    this.placeBlock(pos);
                }
            });
            this.placeTimer.reset();
        }
        if (((Boolean)this.mine.getValue()).booleanValue() && !this.self && this.placedSelf) {
            boolean air = BlockUtil.isAir(PlayerUtil.getPlayerPos());
            if (this.mined) {
                if (air) {
                    if (ModuleManager.isModuleEnabled(PacketMine.class)) {
                        PacketMine.INSTANCE.lastBlock = null;
                    }
                    this.mined = false;
                    this.placedSelf = false;
                }
            } else if (!air) {
                HoleFill.mc.field_71442_b.func_180512_c(PlayerUtil.getPlayerPos(), EnumFacing.UP);
                this.mined = true;
            }
        }
    }

    private List<BlockPos> calc() {
        if (HoleFill.mc.field_71441_e == null || HoleFill.mc.field_71439_g == null || HoleFill.mc.field_71439_g.field_70128_L) {
            return new ArrayList<BlockPos>();
        }
        this.placed = 0;
        ArrayList<BlockPos> check = new ArrayList<BlockPos>();
        ArrayList<HoleInfo> holeList = new ArrayList<HoleInfo>();
        for (BlockPos pos2 : EntityUtil.getSphere(PlayerUtil.getEyesPos(), (Double)this.range.getValue() + 1.0, (Double)this.yRange.getValue() + 1.0, false, false, 0)) {
            HoleUtil.HoleInfo holeInfo;
            Object holeType;
            if (check.contains(pos2) || !BlockUtil.canReplace(pos2) || DamageUtil.isResistantMine(pos2.func_177984_a()) || DamageUtil.isResistantMine(pos2.func_177981_b(2)) || (holeType = (holeInfo = HoleUtil.isHole(pos2, false, true, false)).getType()) == HoleUtil.HoleType.NONE) continue;
            AxisAlignedBB box = holeInfo.getCentre();
            Vec3d center = box.func_189972_c();
            ArrayList<BlockPos> holePos = new ArrayList<BlockPos>();
            for (Vec3d add : this.add) {
                BlockPos hole = new BlockPos(center.field_72450_a + add.field_72450_a, center.field_72448_b, center.field_72449_c + add.field_72449_c);
                if (holePos.contains(hole)) continue;
                holePos.add(hole);
            }
            check.addAll(holePos);
            boolean recall = false;
            for (BlockPos block : holePos) {
                boolean selfFilling = this.isPlayer(block);
                if (selfFilling) {
                    if (((Boolean)this.selfTrap.getValue()).booleanValue()) break;
                    if (!((Boolean)this.selfFill.getValue()).booleanValue() || this.findRightBlock(true) == -1) {
                        recall = true;
                        break;
                    }
                }
                if (!ColorMain.INSTANCE.breakList.contains(block)) continue;
                recall = true;
                break;
            }
            if (recall) continue;
            holeList.add(new HoleInfo(holePos, box));
        }
        ArrayList<BlockPos> holePos = new ArrayList<BlockPos>();
        List targets = PlayerUtil.getNearPlayers((Double)this.range.getValue() + (Double)this.fillRange.getValue(), this.maxTarget.getMax()).stream().filter(player -> (Boolean)this.holeCheck.getValue() == false || !HoleUtil.isInHole((Entity)player, false, false, false)).collect(Collectors.toList());
        if (((Boolean)this.test.getValue()).booleanValue()) {
            targets.add(HoleFill.mc.field_71439_g);
        }
        ArrayList<EntityPlayer> listPlayer = new ArrayList<EntityPlayer>();
        block3: for (EntityPlayer player2 : targets) {
            for (int tick = 0; tick <= (Integer)this.maxTick.getValue() + (Integer)this.tickAdd.getValue(); tick += ((Integer)this.tickAdd.getValue()).intValue()) {
                if (tick >= (Integer)this.maxTick.getValue()) {
                    tick = (Integer)this.maxTick.getValue();
                }
                listPlayer.add(PredictUtil.predictPlayer((EntityLivingBase)player2, new PredictUtil.PredictSettings(tick, (Boolean)this.calculateYPredict.getValue(), (Integer)this.startDecrease.getValue(), (Integer)this.exponentStartDecrease.getValue(), (Integer)this.decreaseY.getValue(), (Integer)this.exponentDecreaseY.getValue(), (Boolean)this.splitXZ.getValue(), (Boolean)this.manualOutHole.getValue(), (Boolean)this.aboveHoleManual.getValue(), (Boolean)this.stairPredict.getValue(), (Integer)this.nStair.getValue(), (Double)this.speedActivationStair.getValue())));
                if (tick == (Integer)this.maxTick.getValue()) continue block3;
            }
        }
        boolean fill = false;
        AxisAlignedBB selfBox = HoleFill.mc.field_71439_g.func_174813_aQ();
        block5: for (HoleInfo hole : holeList) {
            for (EntityPlayer target : listPlayer) {
                AxisAlignedBB targetBox = target.field_70121_D;
                if (!targetBox.func_72326_a(hole.checkBox)) continue;
                if (hole.box.func_72326_a(targetBox)) continue block5;
                double y = hole.box.field_72338_b + 1.0;
                if (((Boolean)this.yCheck.getValue()).booleanValue() && (double)((int)(target.field_70163_u + 0.5)) != y) {
                    BlockPos pos3;
                    boolean recall;
                    int value;
                    boolean cancel;
                    if (target.field_70163_u < y) {
                        if (((Boolean)this.web.getValue()).booleanValue() && target.field_70134_J) continue;
                        cancel = false;
                        for (value = (int)y - 1 - (int)target.field_70163_u; value > 0; --value) {
                            recall = false;
                            for (BlockPos blockPos : hole.posList) {
                                pos3 = blockPos.func_177979_c(value);
                                if (!DamageUtil.isResistantMine(pos3)) continue;
                                cancel = true;
                                recall = true;
                                break;
                            }
                            if (recall) break;
                        }
                        if (cancel) {
                            continue;
                        }
                    } else if (((Boolean)this.above.getValue()).booleanValue()) {
                        cancel = false;
                        for (value = (int)target.field_70163_u - (int)y; value > 0; --value) {
                            recall = false;
                            for (BlockPos blockPos : hole.posList) {
                                pos3 = blockPos.func_177981_b(value);
                                if (!DamageUtil.isResistantMine(pos3)) continue;
                                cancel = true;
                                recall = true;
                                break;
                            }
                            if (recall) break;
                        }
                        if (cancel) continue;
                    }
                }
                if (((Boolean)this.raytraceCheck.getValue()).booleanValue() && !CrystalUtil.calculateRaytrace(target, hole.box.func_189972_c())) continue;
                if (!fill && selfBox.func_72326_a(hole.box)) {
                    fill = true;
                }
                holePos.addAll(hole.posList);
                continue block5;
            }
        }
        this.self = fill;
        boolean inHole = HoleUtil.isInHole((Entity)HoleFill.mc.field_71439_g, false, true, false);
        holePos.sort(Comparator.comparing(p -> p.field_177960_b));
        holePos.removeIf(pos -> {
            if (!this.checkPlaceRange((BlockPos)pos) || DamageUtil.isResistantMine(pos.func_177984_a()) || DamageUtil.isResistantMine(pos.func_177981_b(2))) {
                return true;
            }
            if (!inHole && MathUtil.isIntersect(selfBox.func_186662_g(((Double)this.safety.getValue()).doubleValue()), new AxisAlignedBB(pos))) {
                return true;
            }
            return holePos.contains(pos.func_177984_a());
        });
        return holePos;
    }

    private boolean checkPlaceRange(BlockPos pos) {
        BlockPos playerPos = new BlockPos(Math.floor(HoleFill.mc.field_71439_g.field_70165_t), Math.floor(HoleFill.mc.field_71439_g.field_70163_u), Math.floor(HoleFill.mc.field_71439_g.field_70161_v));
        double x = (double)playerPos.field_177962_a - ((double)pos.field_177962_a + 0.5);
        double y = (double)playerPos.field_177960_b - ((double)pos.field_177960_b + 0.5);
        double z = (double)playerPos.field_177961_c - ((double)pos.field_177961_c + 0.5);
        return x * x <= (Double)this.range.getValue() * (Double)this.range.getValue() && y * y <= (Double)this.yRange.getValue() * (Double)this.yRange.getValue() && z * z <= (Double)this.range.getValue() * (Double)this.range.getValue();
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : HoleFill.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || !MathUtil.isIntersect(new AxisAlignedBB(pos), entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private boolean isPlayer(BlockPos pos) {
        for (EntityPlayer entity : HoleFill.mc.field_71441_e.field_73010_i) {
            if (entity != HoleFill.mc.field_71439_g || !MathUtil.isIntersect(new AxisAlignedBB(pos), entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private void placeBlock(BlockPos pos) {
        EnumFacing side;
        int obby;
        if (pos == null) {
            return;
        }
        boolean selfFilling = this.isPlayer(pos);
        if (selfFilling && ((Boolean)this.selfTrap.getValue()).booleanValue() && (obby = BurrowUtil.findHotbarBlock(BlockObsidian.class)) != -1) {
            InventoryUtil.run(obby, (Boolean)this.packetSwitch.getValue(), () -> {
                BlockPos ori = pos.func_177984_a();
                if (BurrowUtil.getFirstFacing(pos.func_177981_b(2)) == null) {
                    BlockPos added;
                    BlockPos e = null;
                    boolean isNull = true;
                    for (BlockPos side : this.sides) {
                        added = ori.func_177984_a().func_177971_a((Vec3i)side);
                        if (this.intersectsWithEntity(added) || BurrowUtil.getFirstFacing(added) == null) continue;
                        e = added;
                        isNull = false;
                        break;
                    }
                    if (isNull) {
                        for (BlockPos side : this.sides) {
                            added = ori.func_177971_a((Vec3i)side);
                            if (this.intersectsWithEntity(added) || this.intersectsWithEntity(added.func_177984_a())) continue;
                            this.placeTrapBlock(added);
                            e = added.func_177984_a();
                            break;
                        }
                    }
                    this.placeTrapBlock(e);
                }
                this.placeTrapBlock(pos.func_177981_b(2));
            });
            return;
        }
        int fillSlot = -1;
        if (selfFilling) {
            if (!((Boolean)this.selfFill.getValue()).booleanValue()) {
                return;
            }
            fillSlot = this.findRightBlock(true);
            if (fillSlot == -1) {
                return;
            }
        } else if (this.intersectsWithEntity(pos)) {
            return;
        }
        this.trapdoor = fillSlot == InventoryUtil.findFirstBlockSlot(BlockTrapDoor.class, 0, 8) || (Boolean)this.upPlate.getValue() != false && fillSlot == BurrowUtil.findHotbarBlock(BlockSlab.class);
        boolean jump = fillSlot == BurrowUtil.findHotbarBlock(BlockEnderChest.class) || fillSlot == BurrowUtil.findHotbarBlock(BlockObsidian.class);
        EnumFacing enumFacing = side = this.trapdoor ? BurrowUtil.getTrapdoorFacing(pos) : BlockUtil.getFirstFacing(pos, (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue());
        if (side == null) {
            return;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, this.trapdoor ? 0.8 : 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        if ((BlockUtil.blackList.contains(HoleFill.mc.field_71441_e.func_180495_p(neighbour).func_177230_c()) || BlockUtil.shulkerList.contains(HoleFill.mc.field_71441_e.func_180495_p(neighbour).func_177230_c())) && !HoleFill.mc.field_71439_g.func_70093_af()) {
            HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)HoleFill.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            HoleFill.mc.field_71439_g.func_70095_a(true);
        }
        if (selfFilling) {
            this.placedSelf = true;
            if (this.trapdoor) {
                double x = HoleFill.mc.field_71439_g.field_70165_t;
                double y = (int)HoleFill.mc.field_71439_g.field_70163_u;
                double z = HoleFill.mc.field_71439_g.field_70161_v;
                if (fillSlot == InventoryUtil.findFirstBlockSlot(BlockTrapDoor.class, 0, 8)) {
                    HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y + (double)0.2f, z, HoleFill.mc.field_71439_g.field_70122_E));
                } else {
                    this.jump();
                }
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(fillSlot));
                BurrowUtil.rightClickBlock(neighbour, opposite, new Vec3d(0.5, 0.8, 0.5), true, (boolean)((Boolean)this.swing.getValue()));
                if (fillSlot == InventoryUtil.findFirstBlockSlot(BlockTrapDoor.class, 0, 8)) {
                    HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, HoleFill.mc.field_71439_g.field_70122_E));
                } else {
                    this.rubberBand();
                }
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.slot));
                return;
            }
            if (jump) {
                this.jump();
            }
        }
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            BurrowUtil.faceVector(hitVec, true);
        }
        InventoryUtil.run(jump ? fillSlot : this.slot, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.rightClickBlock(neighbour, hitVec, EnumHand.MAIN_HAND, opposite, (Boolean)this.packet.getValue(), (Boolean)this.swing.getValue()));
        if (selfFilling) {
            this.rubberBand();
        }
        this.managerRenderBlocks.addRender(pos);
        ++this.placed;
    }

    public static BlockPos getFlooredPosition(Entity entity) {
        return new BlockPos(Math.floor(entity.field_70165_t), (double)Math.round(entity.field_70163_u), Math.floor(entity.field_70161_v));
    }

    private void placeTrapBlock(BlockPos pos) {
        if (ColorMain.INSTANCE.breakList.contains(pos)) {
            return;
        }
        BurrowUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
    }

    private int findRightBlock(boolean selfFill) {
        int slot = -1;
        if (selfFill) {
            if (((Boolean)this.sTrap.getValue()).booleanValue()) {
                slot = InventoryUtil.findFirstBlockSlot(BlockTrapDoor.class, 0, 8);
            }
            if (((Boolean)this.sSkull.getValue()).booleanValue() && slot == -1) {
                slot = InventoryUtil.findSkullSlot();
            }
            if (((Boolean)this.sWeb.getValue()).booleanValue() && slot == -1) {
                slot = InventoryUtil.findFirstBlockSlot(BlockWeb.class, 0, 8);
            }
            if (((Boolean)this.sSlab.getValue()).booleanValue() && slot == -1) {
                slot = BurrowUtil.findHotbarBlock(BlockSlab.class);
            }
            if (((Boolean)this.sEChest.getValue()).booleanValue() && slot == -1) {
                slot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
            }
            if (((Boolean)this.sObby.getValue()).booleanValue() && slot == -1) {
                slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
            }
        } else {
            if (((Boolean)this.hObby.getValue()).booleanValue()) {
                slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
            }
            if (((Boolean)this.hEChest.getValue()).booleanValue() && slot == -1) {
                slot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
            }
            if (((Boolean)this.hSlab.getValue()).booleanValue() && slot == -1) {
                slot = BurrowUtil.findHotbarBlock(BlockSlab.class);
            }
            if (((Boolean)this.hWeb.getValue()).booleanValue() && slot == -1) {
                slot = InventoryUtil.findFirstBlockSlot(BlockWeb.class, 0, 8);
            }
            if (((Boolean)this.hSkull.getValue()).booleanValue() && slot == -1) {
                slot = InventoryUtil.findSkullSlot();
            }
            if (((Boolean)this.hTrap.getValue()).booleanValue()) {
                slot = InventoryUtil.findFirstBlockSlot(BlockTrapDoor.class, 0, 8);
            }
        }
        return slot;
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        this.managerRenderBlocks.update((Integer)this.time.getValue());
        this.managerRenderBlocks.render();
    }

    boolean sameBlockPos(BlockPos first, BlockPos second) {
        if (first == null || second == null) {
            return false;
        }
        return first.func_177958_n() == second.func_177958_n() && first.func_177956_o() == second.func_177956_o() && first.func_177952_p() == second.func_177952_p();
    }

    public static void back() {
        for (Entity crystal : HoleFill.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityEnderCrystal && !e.field_70128_L).sorted(Comparator.comparing(e -> Float.valueOf(HoleFill.mc.field_71439_g.func_70032_d(e)))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal)) continue;
            HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(crystal));
            HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
        }
    }

    private boolean canGoTo(BlockPos pos) {
        return HoleFill.isAir(pos) && HoleFill.isAir(pos.func_177984_a());
    }

    public static boolean isAir(Vec3d vec3d) {
        return HoleFill.isAir(new BlockPos(vec3d));
    }

    public static boolean isAir(BlockPos pos) {
        return BlockUtil.canReplace(pos);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + (double)HoleFill.mc.field_71439_g.func_70047_e(), HoleFill.mc.field_71439_g.field_70161_v);
    }

    private void jump() {
        switch ((String)this.jumpMode.getValue()) {
            case "Normal": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.419999986886978, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.7531999805212015, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.001335979112147, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.166109260938214, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Future": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.419997486886978, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.7500025, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.999995, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.170005001788139, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.2426050013947485, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Strict": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.419998586886978, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.7500014, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.9999972, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.170002801788139, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.170009801788139, HoleFill.mc.field_71439_g.field_70161_v, false));
            }
        }
    }

    private void rubberBand() {
        block9 : switch ((String)this.rubberBand.getValue()) {
            case "Cn": {
                double distance = 0.0;
                BlockPos bestPos = null;
                for (BlockPos pos : BlockUtil.getBox(6.0f)) {
                    if (!this.canGoTo(pos) || HoleFill.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) <= 3.0 || bestPos != null && HoleFill.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) >= distance) continue;
                    bestPos = pos;
                    distance = HoleFill.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5);
                }
                if (bestPos != null) {
                    HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)bestPos.func_177958_n() + 0.5, (double)bestPos.func_177956_o(), (double)bestPos.func_177952_p() + 0.5, false));
                    break;
                }
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, -7.0, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Future": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.242609801394749, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 2.340028003576279, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "FutureStrict": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.315205001001358, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 1.315205001001358, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 2.485225002789497, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Troll": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 3.3400880035762786, HoleFill.mc.field_71439_g.field_70161_v, false));
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u - 1.0, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Strict": {
                double distance = 0.0;
                BlockPos bestPos = null;
                for (int i = 0; i < 20; ++i) {
                    BlockPos pos = new BlockPos(HoleFill.mc.field_71439_g.field_70165_t, HoleFill.mc.field_71439_g.field_70163_u + 0.5 + (double)i, HoleFill.mc.field_71439_g.field_70161_v);
                    if (!this.canGoTo(pos) || !(HoleFill.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) > 5.0) || bestPos != null && !(HoleFill.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) < distance)) continue;
                    bestPos = pos;
                    distance = HoleFill.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5);
                }
                if (bestPos != null) {
                    HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)bestPos.func_177958_n() + 0.5, (double)bestPos.func_177956_o(), (double)bestPos.func_177952_p() + 0.5, false));
                    break;
                }
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, -7.0, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Void": {
                HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleFill.mc.field_71439_g.field_70165_t, -7.0, HoleFill.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Auto": {
                for (int i = -10; i < 10; ++i) {
                    if (i == -1) {
                        i = 4;
                    }
                    if (!HoleFill.mc.field_71441_e.func_180495_p(HoleFill.getFlooredPosition((Entity)HoleFill.mc.field_71439_g).func_177982_a(0, i, 0)).func_177230_c().equals(Blocks.field_150350_a) || !HoleFill.mc.field_71441_e.func_180495_p(HoleFill.getFlooredPosition((Entity)HoleFill.mc.field_71439_g).func_177982_a(0, i + 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) continue;
                    BlockPos pos = HoleFill.getFlooredPosition((Entity)HoleFill.mc.field_71439_g).func_177982_a(0, i, 0);
                    HoleFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)pos.func_177958_n() + 0.3, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.3, false));
                    break block9;
                }
                break;
            }
        }
    }

    class HoleInfo {
        List<BlockPos> posList;
        AxisAlignedBB checkBox;
        AxisAlignedBB box;

        public HoleInfo(List<BlockPos> posList, AxisAlignedBB box) {
            this.posList = posList;
            this.box = box;
            this.checkBox = new AxisAlignedBB(box.field_72340_a - (Double)HoleFill.this.fillRange.getValue(), box.field_72338_b, box.field_72339_c - (Double)HoleFill.this.fillRange.getValue(), box.field_72336_d + (Double)HoleFill.this.fillRange.getValue(), box.field_72337_e + (Double)HoleFill.this.fillYRange.getValue(), box.field_72334_f + (Double)HoleFill.this.fillRange.getValue());
        }
    }

    class renderBlock {
        private final BlockPos pos;
        private long start = System.currentTimeMillis();
        boolean placed;

        public renderBlock(BlockPos pos) {
            this.pos = pos;
            this.placed = false;
        }

        void resetTime() {
            this.start = System.currentTimeMillis();
        }

        void render() {
            if (!this.placed) {
                if (DamageUtil.isResistantMine(this.pos)) {
                    this.resetTime();
                    this.placed = true;
                } else {
                    return;
                }
            }
            AxisAlignedBB alignedBB = new AxisAlignedBB(this.pos);
            if (((Boolean)HoleFill.this.animate.getValue()).booleanValue()) {
                alignedBB = alignedBB.func_186662_g(this.delta() * this.delta() / 2.0 - 1.0);
            }
            if (((Boolean)HoleFill.this.box.getValue()).booleanValue()) {
                RenderUtil.drawBox(alignedBB, true, 1.0, new GSColor(HoleFill.this.color.getColor(), this.returnGradient()), 63);
            }
            if (((Boolean)HoleFill.this.outline.getValue()).booleanValue()) {
                RenderUtil.drawBoundingBox(alignedBB, (double)((Integer)HoleFill.this.width.getValue()).intValue(), new GSColor(HoleFill.this.color.getColor(), this.returnOutGradient()));
            }
        }

        public double delta() {
            long end = this.start + (long)((Integer)HoleFill.this.time.getValue()).intValue();
            double result = (double)(end - System.currentTimeMillis()) / (double)(end - this.start);
            if (result < 0.0) {
                result = 0.0;
            }
            if (result > 1.0) {
                result = 1.0;
            }
            return 1.0 - result;
        }

        public int returnGradient() {
            return (int)((double)((Integer)HoleFill.this.alpha.getValue()).intValue() * (1.0 - this.delta()));
        }

        public int returnOutGradient() {
            return (int)((double)((Integer)HoleFill.this.outAlpha.getValue()).intValue() * (1.0 - this.delta()));
        }
    }

    class managerClassRenderBlocks {
        ArrayList<renderBlock> blocks = new ArrayList();

        managerClassRenderBlocks() {
        }

        void update(int time) {
            this.blocks.removeIf(e -> System.currentTimeMillis() - ((renderBlock)e).start > (long)time);
        }

        void render() {
            this.blocks.forEach(renderBlock::render);
        }

        void addRender(BlockPos pos) {
            boolean render = true;
            for (renderBlock block : this.blocks) {
                if (!HoleFill.this.sameBlockPos(block.pos, pos)) continue;
                render = false;
                block.resetTime();
                break;
            }
            if (render) {
                this.blocks.add(new renderBlock(pos));
            }
        }
    }
}
