/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
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

import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.CrystalUtil;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="BurrowBypass", category=Category.Combat)
public class BurrowBypass
extends Module {
    BooleanSetting multiPlace = this.registerBoolean("MultiPlace", false);
    BooleanSetting tpCenter = this.registerBoolean("TPCenter", false);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packet = this.registerBoolean("PacketPlace", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting strict = this.registerBoolean("Strict", true);
    BooleanSetting raytrace = this.registerBoolean("RayTrace", true);
    ModeSetting jumpMode = this.registerMode("JumpMode", Arrays.asList("Normal", "Future", "Strict"), "Normal");
    ModeSetting bypassMode = this.registerMode("Bypass", Arrays.asList("Normal", "Middle", "Test"), "Normal");
    ModeSetting rubberBand = this.registerMode("RubberBand", Arrays.asList("Cn", "Strict", "Future", "FutureStrict", "Troll", "Void", "Auto", "Test", "Custom"), "Cn");
    DoubleSetting offsetX = this.registerDouble("OffsetX", -7.0, -10.0, 10.0, () -> ((String)this.rubberBand.getValue()).equals("Custom"));
    DoubleSetting offsetY = this.registerDouble("OffsetY", -7.0, -10.0, 10.0, () -> ((String)this.rubberBand.getValue()).equals("Custom"));
    DoubleSetting offsetZ = this.registerDouble("OffsetZ", -7.0, -10.0, 10.0, () -> ((String)this.rubberBand.getValue()).equals("Custom"));
    BooleanSetting head = this.registerBoolean("Head", true);
    BooleanSetting onlyOnGround = this.registerBoolean("OnGroundOnly", true);
    BooleanSetting air = this.registerBoolean("NotAir", true);
    ModeSetting mode = this.registerMode("BlockMode", Arrays.asList("Obsidian", "EChest", "ObbyEChest", "EChestObby"), "ObbyEChest");
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", false);
    BooleanSetting breakCrystal = this.registerBoolean("BreakCrystal", true);
    BooleanSetting packetBreak = this.registerBoolean("PacketBreak", true, () -> (Boolean)this.breakCrystal.getValue());
    BooleanSetting antiWk = this.registerBoolean("AntiWeak", true, () -> (Boolean)this.breakCrystal.getValue());
    BooleanSetting weakBypass = this.registerBoolean("BypassSwitch", true, () -> (Boolean)this.breakCrystal.getValue() != false && (Boolean)this.antiWk.getValue() != false);
    BooleanSetting testMode = this.registerBoolean("TestMode", true);
    BooleanSetting move = this.registerBoolean("Move", true, () -> (Boolean)this.testMode.getValue());
    boolean moved;
    Vec3d[] offsets = new Vec3d[]{new Vec3d(0.3, 0.0, 0.3), new Vec3d(-0.3, 0.0, 0.3), new Vec3d(0.3, 0.0, -0.3), new Vec3d(-0.3, 0.0, -0.3)};
    @EventHandler
    private final Listener<PlayerMoveEvent> playerMoveListener = new Listener<PlayerMoveEvent>(event -> {
        if (!BurrowBypass.mc.field_71439_g.func_70089_S() || BurrowBypass.mc.field_71439_g.func_184613_cA() || BurrowBypass.mc.field_71439_g.field_71075_bZ.field_75100_b) {
            return;
        }
        if (!this.moved) {
            BlockPos blockPos = PlayerUtil.getPlayerPos();
            for (Vec3d vec3d : new Vec3d[]{new Vec3d(0.4, 0.0, 0.4), new Vec3d(0.4, 0.0, -0.4), new Vec3d(-0.4, 0.0, 0.4), new Vec3d(-0.4, 0.0, -0.4)}) {
                BlockPos pos = new BlockPos(BurrowBypass.mc.field_71439_g.field_70165_t + vec3d.field_72450_a, BurrowBypass.mc.field_71439_g.field_70163_u, BurrowBypass.mc.field_71439_g.field_70161_v + vec3d.field_72449_c);
                if (!BlockUtil.isAir(pos.func_177977_b()) || !BurrowBypass.mc.field_71441_e.func_175623_d(pos) || !BurrowBypass.mc.field_71441_e.func_175623_d(pos.func_177984_a()) || !BurrowBypass.mc.field_71441_e.func_175623_d(pos.func_177981_b(2))) continue;
                blockPos = pos;
                break;
            }
            double x = this.roundToClosest(BurrowBypass.mc.field_71439_g.field_70165_t, (double)blockPos.field_177962_a + 0.02, (double)blockPos.field_177962_a + 0.98);
            double y = BurrowBypass.mc.field_71439_g.field_70163_u;
            double z = this.roundToClosest(BurrowBypass.mc.field_71439_g.field_70161_v, (double)blockPos.field_177961_c + 0.02, (double)blockPos.field_177961_c + 0.98);
            Vec3d playerPos = BurrowBypass.mc.field_71439_g.func_174791_d();
            double yawRad = Math.toRadians(RotationUtil.getRotationTo((Vec3d)playerPos, (Vec3d)new Vec3d((double)x, (double)y, (double)z)).field_189982_i);
            double dist = Math.hypot(x - playerPos.field_72450_a, z - playerPos.field_72449_c);
            if (x - playerPos.field_72450_a == 0.0 && z - playerPos.field_72449_c == 0.0) {
                this.moved = true;
            }
            double playerSpeed = MotionUtil.getBaseMoveSpeed() * (EntityUtil.isColliding(0.0, -0.5, 0.0) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.91 : 1.0);
            double speed = Math.min(dist, playerSpeed);
            event.setX(-Math.sin(yawRad) * speed);
            event.setZ(Math.cos(yawRad) * speed);
            if (LemonClient.speedUtil.getPlayerSpeed((EntityPlayer)BurrowBypass.mc.field_71439_g) == 0.0) {
                this.moved = true;
            }
        }
    }, new Predicate[0]);

    public void breakCrystal() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(BurrowBypass.getFlooredPosition((Entity)BurrowBypass.mc.field_71439_g));
        List l = BurrowBypass.mc.field_71441_e.func_72839_b(null, axisAlignedBB);
        for (Entity entity : l) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            CrystalUtil.breakCrystal(entity, (Boolean)this.packetBreak.getValue(), (Boolean)this.swing.getValue(), (Boolean)this.packetSwitch.getValue(), true, (Boolean)this.antiWk.getValue(), (Boolean)this.weakBypass.getValue());
            break;
        }
    }

    public static void back() {
        for (Entity crystal : BurrowBypass.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityEnderCrystal && !e.field_70128_L).sorted(Comparator.comparing(e -> Float.valueOf(BurrowBypass.mc.field_71439_g.func_70032_d(e)))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal)) continue;
            BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(crystal));
            BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
        }
    }

    private double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        if (d2 > d1) {
            return low;
        }
        return high;
    }

    private boolean canGoTo(BlockPos pos) {
        return BurrowBypass.isAir(pos) && BurrowBypass.isAir(pos.func_177984_a());
    }

    @Override
    public void onEnable() {
        boolean bl = this.moved = (Boolean)this.move.getValue() == false;
        if (((Boolean)this.onlyOnGround.getValue()).booleanValue() && !BurrowBypass.mc.field_71439_g.field_70122_E) {
            this.disable();
            return;
        }
        if (((Boolean)this.air.getValue()).booleanValue() && BurrowBypass.mc.field_71441_e.func_180495_p(BurrowBypass.getFlooredPosition((Entity)BurrowBypass.mc.field_71439_g).func_177972_a(EnumFacing.DOWN)).func_177230_c().equals(Blocks.field_150350_a)) {
            this.disable();
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onUpdate() {
        boolean bypassed;
        BlockPos pos3;
        BlockPos playerPos = new BlockPos(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.5, BurrowBypass.mc.field_71439_g.field_70161_v);
        Vec3d vecPos = new Vec3d(BurrowBypass.mc.field_71439_g.field_70165_t, (double)((int)(BurrowBypass.mc.field_71439_g.field_70163_u + 0.5)), BurrowBypass.mc.field_71439_g.field_70161_v);
        int a = BurrowBypass.mc.field_71439_g.field_71071_by.field_70461_c;
        int slot = -1;
        switch ((String)this.mode.getValue()) {
            case "Obsidian": {
                slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
                break;
            }
            case "EChest": {
                slot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
                break;
            }
            case "EChestObby": {
                slot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
                if (slot != -1) break;
                slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
                break;
            }
            case "ObbyEChest": {
                slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
                if (slot != -1) break;
                slot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
            }
        }
        if (slot == -1) {
            this.disable();
            return;
        }
        if (((Boolean)this.testMode.getValue()).booleanValue()) {
            if (!this.moved) {
                return;
            }
            boolean burrow = false;
            for (Vec3d vec3d : this.offsets) {
                if (this.isPos2(new BlockPos(vecPos.func_178787_e(vec3d)), playerPos)) continue;
                burrow = true;
                break;
            }
            if (!burrow) {
                this.disable();
                return;
            }
        }
        if (((Boolean)this.breakCrystal.getValue()).booleanValue()) {
            BurrowBypass.back();
        }
        if (!BurrowBypass.mc.field_71441_e.func_175667_e(BurrowBypass.mc.field_71439_g.func_180425_c()) || BurrowBypass.mc.field_71439_g.func_180799_ab() || BurrowBypass.mc.field_71439_g.func_70090_H() || BurrowBypass.mc.field_71439_g.field_70134_J) {
            this.disable();
            return;
        }
        if (((Boolean)this.tpCenter.getValue()).booleanValue()) {
            PlayerUtil.centerPlayer();
        }
        if (!this.fakeBBoxCheck()) {
            if (((Boolean)this.testMode.getValue()).booleanValue() && !this.bypassBurrowed() || (!BlockUtil.canReplace(playerPos) || !BlockUtil.canReplace(playerPos.func_177984_a())) && this.intersect(playerPos.func_177984_a())) {
                this.gotoPos(playerPos);
            } else {
                ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
                ArrayList<BlockPos> airList = new ArrayList<BlockPos>();
                if (((Boolean)this.testMode.getValue()).booleanValue()) {
                    airList.add(playerPos);
                    for (Vec3d vec3d : this.offsets) {
                        pos3 = new BlockPos(vecPos.func_178787_e(vec3d));
                        if (!BlockUtil.isAir(pos3)) continue;
                        posList.add(pos3);
                    }
                } else {
                    for (Vec3d vec3d : this.offsets) {
                        boolean air = true;
                        BlockPos pos2 = new BlockPos(vecPos.func_178787_e(vec3d));
                        for (int i = 0; i < 2; ++i) {
                            BlockPos blockPos = pos2.func_177981_b(i);
                            if (BurrowBypass.isAir(blockPos)) continue;
                            air = false;
                        }
                        if (this.intersect(pos2) && !air) {
                            posList.add(pos2);
                            continue;
                        }
                        airList.add(pos2);
                    }
                }
                BlockPos movePos = posList.isEmpty() ? (BlockPos)airList.stream().min(Comparator.comparing(p -> BurrowBypass.mc.field_71439_g.func_70011_f((double)p.field_177962_a + 0.5, BurrowBypass.mc.field_71439_g.field_70163_u, (double)p.field_177961_c + 0.5))).orElse(null) : (BlockPos)posList.stream().min(Comparator.comparing(p -> BurrowBypass.mc.field_71439_g.func_70011_f((double)p.field_177962_a + 0.5, BurrowBypass.mc.field_71439_g.field_70163_u, (double)p.field_177961_c + 0.5))).orElse(null);
                this.gotoPos(movePos);
            }
            bypassed = true;
        } else {
            bypassed = false;
            switch ((String)this.jumpMode.getValue()) {
                case "Normal": {
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.419999986886978, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.7531999805212015, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.001335979112147, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.166109260938214, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    break;
                }
                case "Future": {
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.419997486886978, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.7500025, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.999995, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.170005001788139, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.2426050013947485, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    break;
                }
                case "Strict": {
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.419998586886978, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.7500014, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.9999972, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.170002801788139, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.170009801788139, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                }
            }
        }
        InventoryUtil.run(slot, (Boolean)this.packetSwitch.getValue(), () -> {
            if (!((Boolean)this.multiPlace.getValue()).booleanValue()) {
                this.placeBlock(new BlockPos((Vec3i)this.getPlayerPosFixY((EntityPlayer)BurrowBypass.mc.field_71439_g)));
            } else {
                for (Vec3d vec3d : this.offsets) {
                    this.placeBlock(vecPos.func_178787_e(vec3d));
                }
                if (((Boolean)this.head.getValue()).booleanValue() && bypassed) {
                    for (Vec3d vec3d : this.offsets) {
                        this.placeBlock(vecPos.func_178787_e(vec3d).func_72441_c(0.0, 1.0, 0.0));
                    }
                }
            }
        });
        block32 : switch ((String)this.rubberBand.getValue()) {
            case "Cn": {
                double distance = 0.0;
                BlockPos bestPos = null;
                for (BlockPos pos3 : BlockUtil.getBox(6.0f)) {
                    if (!this.canGoTo(pos3) || BurrowBypass.mc.field_71439_g.func_70011_f((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5) <= 3.0 || bestPos != null && BurrowBypass.mc.field_71439_g.func_70011_f((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5) >= distance) continue;
                    bestPos = pos3;
                    distance = BurrowBypass.mc.field_71439_g.func_70011_f((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5);
                }
                if (bestPos != null) {
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)bestPos.func_177958_n() + 0.5, (double)bestPos.func_177956_o(), (double)bestPos.func_177952_p() + 0.5, false));
                    break;
                }
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, -7.0, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Future": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.242609801394749, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 2.340028003576279, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "FutureStrict": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.315205001001358, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 1.315205001001358, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 2.485225002789497, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Troll": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 3.3400880035762786, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u - 1.0, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Strict": {
                void var11_27;
                double distance = 0.0;
                BlockPos bestPos = null;
                boolean bl = false;
                while (var11_27 < 20) {
                    pos3 = new BlockPos(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.5 + (double)var11_27, BurrowBypass.mc.field_71439_g.field_70161_v);
                    if (this.canGoTo(pos3) && BurrowBypass.mc.field_71439_g.func_70011_f((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5) > 5.0 && (bestPos == null || BurrowBypass.mc.field_71439_g.func_70011_f((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5) < distance)) {
                        bestPos = pos3;
                        distance = BurrowBypass.mc.field_71439_g.func_70011_f((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5);
                    }
                    ++var11_27;
                }
                if (bestPos != null) {
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)bestPos.func_177958_n() + 0.5, (double)bestPos.func_177956_o(), (double)bestPos.func_177952_p() + 0.5, false));
                    break;
                }
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, -7.0, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Void": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, -7.0, BurrowBypass.mc.field_71439_g.field_70161_v, false));
                break;
            }
            case "Auto": {
                for (int i = -10; i < 10; ++i) {
                    if (i == -1) {
                        i = 4;
                    }
                    if (!BurrowBypass.mc.field_71441_e.func_180495_p(BurrowBypass.getFlooredPosition((Entity)BurrowBypass.mc.field_71439_g).func_177982_a(0, i, 0)).func_177230_c().equals(Blocks.field_150350_a) || !BurrowBypass.mc.field_71441_e.func_180495_p(BurrowBypass.getFlooredPosition((Entity)BurrowBypass.mc.field_71439_g).func_177982_a(0, i + 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) continue;
                    BlockPos pos4 = BurrowBypass.getFlooredPosition((Entity)BurrowBypass.mc.field_71439_g).func_177982_a(0, i, 0);
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)pos4.func_177958_n() + 0.3, (double)pos4.func_177956_o(), (double)pos4.func_177952_p() + 0.3, false));
                    break block32;
                }
                break;
            }
            case "Custom": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t + (Double)this.offsetX.getValue(), BurrowBypass.mc.field_71439_g.field_70163_u + (Double)this.offsetY.getValue(), BurrowBypass.mc.field_71439_g.field_70161_v + (Double)this.offsetZ.getValue(), false));
            }
        }
        this.disable();
    }

    private void gotoPos(BlockPos pos) {
        switch ((String)this.bypassMode.getValue()) {
            case "Normal": {
                if (Math.abs((double)pos.func_177958_n() + 0.5 - BurrowBypass.mc.field_71439_g.field_70165_t) < Math.abs((double)pos.func_177952_p() + 0.5 - BurrowBypass.mc.field_71439_g.field_70161_v)) {
                    BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + 0.2, (double)pos.func_177952_p() + 0.5, true));
                    break;
                }
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)pos.func_177958_n() + 0.5, BurrowBypass.mc.field_71439_g.field_70163_u + 0.2, BurrowBypass.mc.field_71439_g.field_70161_v, true));
                break;
            }
            case "Middle": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position((double)pos.func_177958_n() + 0.5, BurrowBypass.mc.field_71439_g.field_70163_u + 0.2, (double)pos.func_177952_p() + 0.5, true));
                break;
            }
            case "Test": {
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t + ((double)pos.func_177958_n() + 0.5 - BurrowBypass.mc.field_71439_g.field_70165_t) * 0.42132, BurrowBypass.mc.field_71439_g.field_70163_u + 0.12160004615784, BurrowBypass.mc.field_71439_g.field_70161_v + ((double)pos.func_177952_p() + 0.5 - BurrowBypass.mc.field_71439_g.field_70161_v) * 0.42132, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t + ((double)pos.func_177958_n() + 0.5 - BurrowBypass.mc.field_71439_g.field_70165_t) * 0.95, BurrowBypass.mc.field_71439_g.field_70163_u + 0.200000047683716, BurrowBypass.mc.field_71439_g.field_70161_v + ((double)pos.func_177952_p() + 0.5 - BurrowBypass.mc.field_71439_g.field_70161_v) * 0.95, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t + ((double)pos.func_177958_n() + 0.5 - BurrowBypass.mc.field_71439_g.field_70165_t) * 1.03, BurrowBypass.mc.field_71439_g.field_70163_u + 0.200000047683716, BurrowBypass.mc.field_71439_g.field_70161_v + ((double)pos.func_177952_p() + 0.5 - BurrowBypass.mc.field_71439_g.field_70161_v) * 1.03, false));
                BurrowBypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(BurrowBypass.mc.field_71439_g.field_70165_t + ((double)pos.func_177958_n() + 0.5 - BurrowBypass.mc.field_71439_g.field_70165_t) * 1.0933, BurrowBypass.mc.field_71439_g.field_70163_u + 0.12160004615784, BurrowBypass.mc.field_71439_g.field_70161_v + ((double)pos.func_177952_p() + 0.5 - BurrowBypass.mc.field_71439_g.field_70161_v) * 1.0933, false));
            }
        }
    }

    private boolean intersect(BlockPos pos) {
        AxisAlignedBB box = BlockUtil.getBoundingBox(pos);
        if (box == null) {
            return false;
        }
        return BurrowBypass.mc.field_71439_g.field_70121_D.func_72326_a(box);
    }

    public static BlockPos getFlooredPosition(Entity entity) {
        return new BlockPos(Math.floor(entity.field_70165_t), (double)Math.round(entity.field_70163_u), Math.floor(entity.field_70161_v));
    }

    private boolean fakeBBoxCheck() {
        Vec3d playerPos = BurrowBypass.mc.field_71439_g.func_174791_d();
        playerPos = new Vec3d(playerPos.field_72450_a, (double)((int)(playerPos.field_72448_b + 0.5)), playerPos.field_72449_c);
        for (Vec3d vec : this.offsets) {
            for (int i = 0; i < 3; ++i) {
                BlockPos pos = new BlockPos(playerPos.func_178787_e(vec).func_72441_c(0.0, (double)i, 0.0));
                if (i < 2 && !this.intersect(pos) || BurrowBypass.isAir(pos)) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean isAir(Vec3d vec3d) {
        return BurrowBypass.isAir(new BlockPos(vec3d));
    }

    public static boolean isAir(BlockPos pos) {
        return BlockUtil.canReplace(pos);
    }

    private void placeBlock(BlockPos pos) {
        BlockUtil.placeBlockBoolean(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue());
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BurrowBypass.mc.field_71439_g.field_70165_t, BurrowBypass.mc.field_71439_g.field_70163_u + (double)BurrowBypass.mc.field_71439_g.func_70047_e(), BurrowBypass.mc.field_71439_g.field_70161_v);
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    private void placeBlock(Vec3d vec3d) {
        BlockPos pos = new BlockPos(vec3d);
        if (((Boolean)this.testMode.getValue()).booleanValue() && (!this.bypassBurrowed() || !((Boolean)this.head.getValue()).booleanValue()) && this.isPos2(pos, PlayerUtil.getPlayerPos())) {
            return;
        }
        this.placeBlock(pos);
    }

    private BlockPos getPlayerPosFixY(EntityPlayer player) {
        return new BlockPos(Math.floor(player.field_70165_t), (double)Math.round(player.field_70163_u), Math.floor(player.field_70161_v));
    }

    private boolean bypassBurrowed() {
        Vec3d pos = new Vec3d(BurrowBypass.mc.field_71439_g.field_70165_t, (double)((int)(BurrowBypass.mc.field_71439_g.field_70163_u + 0.5)), BurrowBypass.mc.field_71439_g.field_70161_v);
        for (Vec3d vec3d : this.offsets) {
            if (BlockUtil.isAir(new BlockPos(pos.func_178787_e(vec3d)).func_177984_a())) continue;
            return true;
        }
        return false;
    }
}
