/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Declaration(name="BetterTrap", category=Category.Dev)
public class AutoTrap
extends Module {
    DoubleSetting range = this.registerDouble("Range", 5.0, 0.0, 10.0);
    IntegerSetting delay = this.registerInteger("Delay", 50, 0, 500);
    IntegerSetting retryDelay = this.registerInteger("RetryDelay", 50, 0, 500);
    IntegerSetting blocksPerPlace = this.registerInteger("BlocksPerTick", 8, 1, 30);
    BooleanSetting chest = this.registerBoolean("EnderChest", true);
    BooleanSetting helpBlocks = this.registerBoolean("HelpBlocks", false);
    BooleanSetting only = this.registerBoolean("OnlyUntrapped", true);
    BooleanSetting strict = this.registerBoolean("Strict", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting raytrace = this.registerBoolean("Raytrace", false);
    BooleanSetting antiScaffold = this.registerBoolean("AntiScaffold", false);
    BooleanSetting antiStep = this.registerBoolean("AntiStep", false);
    BooleanSetting noGhost = this.registerBoolean("Packet", false);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting check = this.registerBoolean("SwitchCheck", false);
    BooleanSetting packet = this.registerBoolean("PacketSwitch", false);
    private final Timing timer = new Timing();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timing retryTimer = new Timing();
    public EntityPlayer target;
    private boolean didPlace = false;
    private int lastHotbarSlot;
    private int placements = 0;
    List<BlockPos> posList;

    @Override
    public void onEnable() {
        if (AutoTrap.mc.field_71441_e == null || AutoTrap.mc.field_71439_g == null || AutoTrap.mc.field_71439_g.field_70128_L) {
            return;
        }
        this.lastHotbarSlot = AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        this.retries.clear();
    }

    @Override
    public void onTick() {
        if (AutoTrap.mc.field_71441_e == null || AutoTrap.mc.field_71439_g == null || AutoTrap.mc.field_71439_g.field_70128_L) {
            return;
        }
        this.doTrap();
    }

    private void doTrap() {
        if (this.check()) {
            return;
        }
        this.doStaticTrap();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void doStaticTrap() {
        int slot;
        int obbySlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
        int n = (Boolean)this.chest.getValue() != false ? eChestSlot : (slot = obbySlot == -1 ? eChestSlot : obbySlot);
        if (slot == -1) {
            return;
        }
        int originalSlot = AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        Vec3d[] sides = new Vec3d[]{new Vec3d(0.3, 0.5, 0.3), new Vec3d(-0.3, 0.5, 0.3), new Vec3d(0.3, 0.5, -0.3), new Vec3d(-0.3, 0.5, -0.3)};
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        for (Vec3d vec3d : sides) {
            placeTargets.addAll(EntityUtil.targets(this.target.func_174791_d().func_178787_e(vec3d), (Boolean)this.antiScaffold.getValue(), (Boolean)this.antiStep.getValue(), false, false, false, (Boolean)this.raytrace.getValue()));
        }
        this.posList = this.placeList(placeTargets, this.target);
        if (!this.posList.isEmpty()) {
            this.switchTo(slot);
            for (BlockPos pos : this.posList) {
                this.placeBlock(pos);
            }
            this.switchTo(originalSlot);
        }
    }

    private List<BlockPos> placeList(List<Vec3d> list, EntityPlayer target) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoTrap.mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), AutoTrap.mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
        ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            if (this.intersectsWithEntity(position) || !BlockUtil.isAir(position)) continue;
            int placeability = BlockUtil.isPositionPlaceable(position, (Boolean)this.raytrace.getValue());
            if (placeability == 1 && (this.retries.get(position) == null || this.retries.get(position) < 4)) {
                posList.add(position);
                this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                this.retryTimer.reset();
                continue;
            }
            if (placeability != 3 && ((Boolean)this.helpBlocks.getValue()).booleanValue() && (long)position.func_177956_o() == Math.round(target.field_70163_u) + 1L) {
                posList.add(position.func_177977_b());
            }
            posList.add(position);
        }
        posList.sort(Comparator.comparingDouble(pos -> pos.field_177960_b));
        return posList;
    }

    private void switchTo(int slot) {
        if (!(slot <= -1 || slot >= 9 || ((Boolean)this.check.getValue()).booleanValue() && AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c == slot)) {
            if (((Boolean)this.packet.getValue()).booleanValue()) {
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                AutoTrap.mc.field_71442_b.func_78765_e();
            }
        }
    }

    private boolean check() {
        int slot;
        this.didPlace = false;
        this.placements = 0;
        int obbySlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
        int n = (Boolean)this.chest.getValue() != false ? eChestSlot : (slot = obbySlot == -1 ? eChestSlot : obbySlot);
        if (this.retryTimer.passedMs(((Integer)this.retryDelay.getValue()).intValue())) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (slot == -1) {
            return true;
        }
        if (AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
            this.lastHotbarSlot = AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        this.target = this.getTarget((Double)this.range.getValue(), (Boolean)this.only.getValue());
        return this.target == null || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue());
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.field_71441_e.field_73010_i) {
            if (!EntityUtil.isPlayerValid(player, (float)range) || trapped && EntityUtil.isTrapped(player, (Boolean)this.antiScaffold.getValue(), (Boolean)this.antiStep.getValue(), false, false, false) || LemonClient.speedUtil.getPlayerSpeed(player) > 15.0) continue;
            if (target == null) {
                target = player;
                distance = AutoTrap.mc.field_71439_g.func_70068_e((Entity)player);
                continue;
            }
            if (!(AutoTrap.mc.field_71439_g.func_70068_e((Entity)player) < distance)) continue;
            target = player;
            distance = AutoTrap.mc.field_71439_g.func_70068_e((Entity)player);
        }
        return target;
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoTrap.mc.field_71441_e.field_72996_f) {
            if (entity.field_70128_L || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityArrow || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < (Integer)this.blocksPerPlace.getValue()) {
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.noGhost.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue());
            this.didPlace = true;
            ++this.placements;
        }
    }
}
