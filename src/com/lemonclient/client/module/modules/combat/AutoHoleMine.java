/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockConcretePowder
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AntiBurrow;
import com.lemonclient.client.module.modules.combat.AntiRegear;
import com.lemonclient.client.module.modules.combat.CevBreaker;
import com.lemonclient.client.module.modules.dev.BedCevBreaker;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoHoleMine", category=Category.Combat)
public final class AutoHoleMine
extends Module {
    public static AutoHoleMine INSTANCE;
    BooleanSetting breakTrap = this.registerBoolean("Break Trap", false);
    BooleanSetting doubleMine = this.registerBoolean("Double Mine", true);
    BooleanSetting ignore = this.registerBoolean("Ignore Bed", false);
    BooleanSetting ignorePiston = this.registerBoolean("Ignore Piston", false);
    BooleanSetting ignoreWeb = this.registerBoolean("Ignore Web", false);
    BooleanSetting fire = this.registerBoolean("Fire", false);
    BooleanSetting sand = this.registerBoolean("Falling Blocks", false);
    public boolean working;
    BlockPos[] side = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0)};

    public AutoHoleMine() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        BlockPos surroundPos;
        BlockPos crystalPos;
        BlockPos surroundPos2;
        EntityPlayer target;
        if (AutoHoleMine.mc.field_71441_e == null || AutoHoleMine.mc.field_71439_g == null || AutoHoleMine.mc.field_71439_g.field_70128_L) {
            return;
        }
        this.working = false;
        if (AntiBurrow.INSTANCE.mining || AntiRegear.INSTANCE.working || CevBreaker.INSTANCE.working || BedCevBreaker.INSTANCE.working) {
            return;
        }
        BlockPos instantPos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            instantPos = PacketMine.INSTANCE.packetPos;
        }
        if (instantPos != null) {
            if (instantPos.equals((Object)new BlockPos(AutoHoleMine.mc.field_71439_g.field_70165_t, AutoHoleMine.mc.field_71439_g.field_70163_u + 2.0, AutoHoleMine.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (instantPos.equals((Object)new BlockPos(AutoHoleMine.mc.field_71439_g.field_70165_t, AutoHoleMine.mc.field_71439_g.field_70163_u - 1.0, AutoHoleMine.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (AutoHoleMine.mc.field_71441_e.func_180495_p(instantPos).func_177230_c() == Blocks.field_150321_G) {
                return;
            }
        }
        if ((target = PlayerUtil.getNearestPlayer(8.0)) == null) {
            return;
        }
        BlockPos feet = new BlockPos(target.field_70165_t, target.field_70163_u + 0.2, target.field_70161_v);
        double breakRange = 0.0;
        BlockPos doublePos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            doublePos = PacketMine.INSTANCE.doublePos;
            breakRange = (Double)PacketMine.INSTANCE.breakRange.getValue();
        }
        BlockPos pos = null;
        for (BlockPos side : this.side) {
            BlockPos surroundPos3 = feet.func_177971_a((Vec3i)side);
            BlockPos crystalPos2 = surroundPos3.func_177971_a((Vec3i)side);
            if (!BlockUtil.isAir(surroundPos3)) continue;
            if (BlockUtil.isAir(surroundPos3.func_177984_a())) {
                return;
            }
            if (!BlockUtil.isAirBlock(crystalPos2) || !BlockUtil.isAirBlock(crystalPos2.func_177984_a())) continue;
            if (((Boolean)this.breakTrap.getValue()).booleanValue()) {
                pos = surroundPos3.func_177984_a();
                continue;
            }
            return;
        }
        if (pos != null) {
            this.surroundMine(pos);
            return;
        }
        ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
        for (Object side : this.side) {
            surroundPos2 = feet.func_177971_a((Vec3i)side);
            crystalPos = surroundPos2.func_177971_a((Vec3i)side);
            if (BlockUtil.isAirBlock(crystalPos) && BlockUtil.isAirBlock(crystalPos.func_177984_a())) {
                if (!this.checkMine(surroundPos2, breakRange)) continue;
                posList.add(surroundPos2);
                continue;
            }
            if (BlockUtil.isAir(surroundPos2) && BlockUtil.isAirBlock(crystalPos.func_177984_a())) {
                if (!this.checkMine(crystalPos, breakRange)) continue;
                posList.add(crystalPos);
                continue;
            }
            if (!BlockUtil.isAir(surroundPos2) || !BlockUtil.isAirBlock(crystalPos) || !this.checkMine(crystalPos.func_177984_a(), breakRange)) continue;
            posList.add(crystalPos.func_177984_a());
        }
        if (!posList.isEmpty()) {
            this.surroundMine(posList.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)AutoHoleMine.mc.field_71439_g).func_174818_b(arg_0))).orElse(null));
            return;
        }
        if (((Boolean)this.doubleMine.getValue()).booleanValue()) {
            BlockPos crystalPos3;
            ArrayList breakList = new ArrayList();
            for (BlockPos side : this.side) {
                surroundPos = feet.func_177971_a((Vec3i)side);
                crystalPos3 = surroundPos.func_177971_a((Vec3i)side);
                if (!BlockUtil.isAir(surroundPos) && !BlockUtil.isAirBlock(crystalPos3) && BlockUtil.isAirBlock(crystalPos3.func_177984_a())) {
                    if (!this.checkMine(surroundPos, breakRange) || !this.checkMine(crystalPos3, breakRange)) continue;
                    breakList.add(new DoubleBreak(surroundPos, crystalPos3));
                    continue;
                }
                if (!BlockUtil.isAir(surroundPos) && !BlockUtil.isAirBlock(crystalPos3.func_177984_a()) && BlockUtil.isAirBlock(crystalPos3)) {
                    if (!this.checkMine(surroundPos, breakRange) || !this.checkMine(crystalPos3.func_177984_a(), breakRange)) continue;
                    breakList.add(new DoubleBreak(surroundPos, crystalPos3.func_177984_a()));
                    continue;
                }
                if (!BlockUtil.isAir(surroundPos) || BlockUtil.isAirBlock(crystalPos3) || BlockUtil.isAirBlock(crystalPos3.func_177984_a()) || !this.checkMine(crystalPos3, breakRange) || !this.checkMine(crystalPos3.func_177984_a(), breakRange)) continue;
                breakList.add(new DoubleBreak(crystalPos3, crystalPos3.func_177984_a()));
            }
            if (breakList.isEmpty()) {
                for (BlockPos side : this.side) {
                    surroundPos = feet.func_177971_a((Vec3i)side);
                    crystalPos3 = surroundPos.func_177971_a((Vec3i)side);
                    if (!this.checkMine(surroundPos, breakRange) || !this.checkMine(crystalPos3, breakRange) || !this.checkMine(crystalPos3.func_177984_a(), breakRange)) continue;
                    breakList.add(new DoubleBreak(crystalPos3, crystalPos3.func_177984_a()));
                }
            }
            if (breakList.isEmpty()) {
                for (BlockPos side : this.side) {
                    surroundPos = feet.func_177971_a((Vec3i)side);
                    crystalPos3 = surroundPos.func_177971_a((Vec3i)side);
                    if (BlockUtil.isAirBlock(crystalPos3) || BlockUtil.isAirBlock(crystalPos3.func_177984_a()) || this.checkMine(crystalPos3) || this.checkMine(crystalPos3.func_177984_a()) || !this.checkMine(surroundPos, breakRange) || !this.checkMine(surroundPos.func_177984_a(), breakRange)) continue;
                    breakList.add(new DoubleBreak(surroundPos, surroundPos.func_177984_a()));
                }
            }
            if (!breakList.isEmpty()) {
                DoubleBreak doubleBreak = breakList.stream().min(Comparator.comparing(DoubleBreak::maxRange)).orElse(null);
                this.surroundMine(doubleBreak.doublePos);
                if (doublePos == null) {
                    this.surroundMine(doubleBreak.packetPos);
                }
                return;
            }
        } else {
            for (Object side : this.side) {
                surroundPos2 = feet.func_177971_a((Vec3i)side);
                crystalPos = surroundPos2.func_177971_a((Vec3i)side);
                if (!BlockUtil.isAir(surroundPos2) && this.checkMine(surroundPos2, breakRange)) {
                    if ((!BlockUtil.isAirBlock(crystalPos) || !this.checkMine(crystalPos, breakRange)) && (!BlockUtil.isAirBlock(crystalPos.func_177984_a()) || !this.checkMine(crystalPos.func_177984_a(), breakRange))) continue;
                    posList.add(surroundPos2);
                    continue;
                }
                if (!BlockUtil.isAirBlock(crystalPos) && this.checkMine(crystalPos, breakRange)) {
                    if ((!BlockUtil.isAir(surroundPos2) || !this.checkMine(surroundPos2, breakRange)) && (!BlockUtil.isAirBlock(crystalPos.func_177984_a()) || !this.checkMine(crystalPos.func_177984_a(), breakRange))) continue;
                    posList.add(crystalPos);
                    continue;
                }
                if (BlockUtil.isAirBlock(crystalPos.func_177984_a()) || !this.checkMine(crystalPos.func_177984_a(), breakRange) || (!BlockUtil.isAir(surroundPos2) || !this.checkMine(surroundPos2, breakRange)) && (!BlockUtil.isAirBlock(crystalPos) || !this.checkMine(crystalPos, breakRange))) continue;
                posList.add(crystalPos.func_177984_a());
            }
            if (posList.isEmpty()) {
                for (Object side : this.side) {
                    surroundPos2 = feet.func_177971_a((Vec3i)side);
                    crystalPos = surroundPos2.func_177971_a((Vec3i)side);
                    if (!this.checkMine(surroundPos2, breakRange) || !this.checkMine(crystalPos, breakRange) || !this.checkMine(crystalPos.func_177984_a(), breakRange)) continue;
                    posList.add(crystalPos.func_177984_a());
                }
            }
            if (!posList.isEmpty()) {
                this.surroundMine(posList.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)AutoHoleMine.mc.field_71439_g).func_174818_b(arg_0))).orElse(null));
                return;
            }
        }
        boolean hole = true;
        for (BlockPos offset : this.side) {
            if (!BlockUtil.isAir(feet.func_177971_a((Vec3i)offset)) || !BlockUtil.isAir(feet.func_177971_a((Vec3i)offset).func_177984_a())) continue;
            hole = false;
        }
        if (!hole) {
            return;
        }
        for (BlockPos side : this.side) {
            surroundPos = feet.func_177971_a((Vec3i)side);
            if (!this.checkMine(surroundPos, breakRange)) continue;
            posList.add(surroundPos);
        }
        if (!posList.isEmpty()) {
            this.surroundMine(posList.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)AutoHoleMine.mc.field_71439_g).func_174818_b(arg_0))).orElse(null));
        }
    }

    private boolean checkMine(BlockPos pos) {
        return !BlockUtil.isAir(pos) && BlockUtil.getBlock((BlockPos)pos).field_149782_v >= 0.0f && this.can(pos);
    }

    private boolean checkMine(BlockPos pos, double range) {
        return !BlockUtil.isAir(pos) && BlockUtil.getBlock((BlockPos)pos).field_149782_v >= 0.0f && this.can(pos) && this.getDistance(pos) <= range;
    }

    private boolean can(BlockPos pos) {
        return !((Boolean)this.ignore.getValue() != false && AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C || (Boolean)this.ignorePiston.getValue() != false && AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150332_K || (Boolean)this.ignoreWeb.getValue() != false && AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G || (Boolean)this.fire.getValue() == false && AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150480_ab || (Boolean)this.sand.getValue() == false && (AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150354_m || AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150351_n || AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150467_bQ || AutoHoleMine.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockConcretePowder));
    }

    private void surroundMine(BlockPos pos) {
        if (pos == null || !this.checkMine(pos)) {
            return;
        }
        this.working = true;
        BlockPos doublePos = null;
        BlockPos instantPos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            instantPos = PacketMine.INSTANCE.packetPos;
            doublePos = PacketMine.INSTANCE.doublePos;
        }
        if (instantPos != null && instantPos.equals((Object)pos)) {
            return;
        }
        if (doublePos != null && doublePos.equals((Object)pos)) {
            return;
        }
        AutoHoleMine.mc.field_71442_b.func_180512_c(pos, BlockUtil.getRayTraceFacing(pos));
    }

    private double getDistance(BlockPos pos) {
        return AutoHoleMine.mc.field_71439_g.func_70011_f((double)pos.field_177962_a + 0.5, (double)pos.field_177960_b + 0.5, (double)pos.field_177961_c + 0.5);
    }

    class DoubleBreak {
        BlockPos packetPos;
        BlockPos doublePos;

        public DoubleBreak(BlockPos packetPos, BlockPos doublePos) {
            this.packetPos = packetPos;
            this.doublePos = doublePos;
        }

        public double maxRange() {
            double packetRange = AutoHoleMine.this.getDistance(this.packetPos);
            double doubleRange = AutoHoleMine.this.getDistance(this.doublePos);
            return Math.max(packetRange, doubleRange);
        }
    }
}
