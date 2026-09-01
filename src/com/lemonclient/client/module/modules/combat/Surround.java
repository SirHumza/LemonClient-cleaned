/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.MovementInputFromOptions
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.client.event.InputUpdateEvent
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.CrystalUtil;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Declaration(name="Surround", category=Category.Combat)
public class Surround
extends Module {
    ModeSetting time = this.registerMode("Time Mode", Arrays.asList("Tick", "onUpdate", "Fast"), "Tick");
    BooleanSetting once = this.registerBoolean("Once", true);
    BooleanSetting echest = this.registerBoolean("Ender Chest", true);
    BooleanSetting floor = this.registerBoolean("Floor", true);
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 20);
    IntegerSetting range = this.registerInteger("Range", 5, 0, 10);
    IntegerSetting bpt = this.registerInteger("BlocksPerTick", 4, 0, 20);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting packet = this.registerBoolean("Packet Place", false);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting forceBase = this.registerBoolean("Force Base", false);
    BooleanSetting breakCrystal = this.registerBoolean("Break Crystal", false);
    BooleanSetting packetBreak = this.registerBoolean("Packet Break", false, () -> (Boolean)this.breakCrystal.getValue());
    BooleanSetting antiWeakness = this.registerBoolean("Anti Weakness", false, () -> (Boolean)this.breakCrystal.getValue());
    BooleanSetting weakBypass = this.registerBoolean("Bypass Switch", false, () -> (Boolean)this.breakCrystal.getValue());
    BooleanSetting silent = this.registerBoolean("Silent Switch", false, () -> (Boolean)this.weakBypass.getValue() == false && (Boolean)this.breakCrystal.getValue() != false);
    List<EntityEnderCrystal> crystals = new ArrayList<EntityEnderCrystal>();
    List<BlockPos> surround = new ArrayList<BlockPos>();
    List<BlockPos> hasEntity = new ArrayList<BlockPos>();
    List<BlockPos> posList = new ArrayList<BlockPos>();
    List<BlockPos> floorPos = new ArrayList<BlockPos>();
    int placed;
    int waited;
    int slot;
    double y;
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
    BlockPos[] neighbour = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 1, 0)};
    @EventHandler
    private final Listener<InputUpdateEvent> inputUpdateEventListener = new Listener<InputUpdateEvent>(event -> {
        if (event.getMovementInput() instanceof MovementInputFromOptions) {
            double posY;
            if (event.getMovementInput().field_78901_c) {
                this.disable();
            }
            if ((event.getMovementInput().field_187255_c || event.getMovementInput().field_187256_d || event.getMovementInput().field_187257_e || event.getMovementInput().field_187258_f) && (posY = Surround.mc.field_71439_g.field_70163_u - this.y) * posY > 0.25) {
                this.disable();
            }
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        if (Surround.mc.field_71441_e == null || Surround.mc.field_71439_g == null || Surround.mc.field_71439_g.field_70128_L) {
            this.disable();
            return;
        }
        this.y = Surround.mc.field_71439_g.field_70163_u;
    }

    @Override
    public void onUpdate() {
        if (((String)this.time.getValue()).equals("onUpdate")) {
            this.doSurround();
        }
    }

    @Override
    public void onTick() {
        if (((String)this.time.getValue()).equals("Tick")) {
            this.doSurround();
        }
    }

    @Override
    public void fast() {
        if (((String)this.time.getValue()).equals("Fast")) {
            this.doSurround();
        }
    }

    private void doSurround() {
        if (Surround.mc.field_71441_e == null || Surround.mc.field_71439_g == null || Surround.mc.field_71439_g.field_70128_L) {
            this.disable();
            return;
        }
        this.slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (this.slot == -1 && ((Boolean)this.echest.getValue()).booleanValue()) {
            this.slot = BurrowUtil.findHotbarBlock(BlockEnderChest.class);
        }
        if (this.slot == -1) {
            return;
        }
        if (this.waited++ < (Integer)this.delay.getValue()) {
            return;
        }
        this.placed = 0;
        this.waited = 0;
        this.calc();
        if (((Boolean)this.breakCrystal.getValue()).booleanValue() && !this.crystals.isEmpty()) {
            EntityEnderCrystal crystal = null;
            Iterator<EntityEnderCrystal> iterator = this.crystals.iterator();
            if (iterator.hasNext()) {
                EntityEnderCrystal enderCrystal;
                crystal = enderCrystal = iterator.next();
            }
            if (crystal != null) {
                CrystalUtil.breakCrystal((Entity)crystal, (Boolean)this.packetBreak.getValue(), (Boolean)this.swing.getValue(), (Boolean)this.packetSwitch.getValue(), (Boolean)this.silent.getValue(), (Boolean)this.antiWeakness.getValue(), (Boolean)this.weakBypass.getValue());
            }
        }
        if (((Boolean)this.floor.getValue()).booleanValue()) {
            for (BlockPos pos : this.floorPos) {
                this.surround.add(pos.func_177977_b());
            }
        }
        if (this.surround.isEmpty()) {
            return;
        }
        for (BlockPos pos : this.surround) {
            if (this.placed >= (Integer)this.bpt.getValue()) break;
            if (!Surround.mc.field_71441_e.func_175623_d(pos) && Surround.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150480_ab && !(Surround.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
            EnumFacing face = BurrowUtil.getFirstFacing(pos);
            if (face == null || ((Boolean)this.forceBase.getValue()).booleanValue()) {
                boolean canPlace = false;
                for (BlockPos side : this.neighbour) {
                    BlockPos blockPos = pos.func_177971_a((Vec3i)side);
                    if (this.intersectsWithEntity(blockPos) || !BlockUtil.hasNeighbour(blockPos)) continue;
                    this.placeBlock(blockPos, BurrowUtil.getFirstFacing(blockPos));
                    canPlace = true;
                    break;
                }
                if (!canPlace) continue;
                face = BurrowUtil.getFirstFacing(pos);
            }
            this.placeBlock(pos, face);
        }
        if (((Boolean)this.once.getValue()).booleanValue()) {
            this.disable();
        }
    }

    private void placeBlock(BlockPos pos, EnumFacing side) {
        if (this.placed >= (Integer)this.bpt.getValue()) {
            return;
        }
        if (this.intersectsWithEntity(pos)) {
            return;
        }
        if (side == null) {
            return;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        if ((BlockUtil.blackList.contains(Surround.mc.field_71441_e.func_180495_p(neighbour).func_177230_c()) || BlockUtil.shulkerList.contains(Surround.mc.field_71441_e.func_180495_p(neighbour).func_177230_c())) && !Surround.mc.field_71439_g.func_70093_af()) {
            Surround.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Surround.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            Surround.mc.field_71439_g.func_70095_a(true);
        }
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            BurrowUtil.faceVector(hitVec, true);
        }
        InventoryUtil.run(this.slot, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.rightClickBlock(neighbour, hitVec, EnumHand.MAIN_HAND, opposite, (Boolean)this.packet.getValue(), (Boolean)this.swing.getValue()));
        ++this.placed;
    }

    private void calc() {
        this.crystals = new ArrayList<EntityEnderCrystal>();
        this.surround = new ArrayList<BlockPos>();
        this.hasEntity = new ArrayList<BlockPos>();
        this.posList = new ArrayList<BlockPos>();
        this.floorPos = new ArrayList<BlockPos>();
        BlockPos playerPos = PlayerUtil.getPlayerPos();
        this.addPos(playerPos);
        if (playerPos.field_177960_b != (int)Surround.mc.field_71439_g.field_70163_u) {
            this.addPos(PlayerUtil.getPlayerFloorPos());
        }
        if (!this.hasEntity.isEmpty()) {
            this.entityCalc();
        }
    }

    private void entityCalc() {
        this.posList = new ArrayList<BlockPos>();
        this.posList.addAll(this.hasEntity);
        this.hasEntity = new ArrayList<BlockPos>();
        for (BlockPos pos : this.posList) {
            this.addPos(pos);
        }
        this.hasEntity.removeIf(blockPos -> blockPos == null || this.floorPos.contains(blockPos) || Surround.mc.field_71439_g.func_174818_b(blockPos) > (double)((Integer)this.range.getValue() * (Integer)this.range.getValue()));
        this.surround.removeIf(blockPos -> blockPos == null || Surround.mc.field_71439_g.func_174818_b(blockPos) > (double)((Integer)this.range.getValue() * (Integer)this.range.getValue()));
        if (!this.hasEntity.isEmpty()) {
            this.entityCalc();
        }
    }

    private void addPos(BlockPos pos) {
        if (this.floorPos.contains(pos)) {
            return;
        }
        for (BlockPos side : this.sides) {
            BlockPos blockPos = pos.func_177971_a((Vec3i)side);
            if (this.intersectsWithEntity(blockPos)) {
                this.hasEntity.add(blockPos);
                continue;
            }
            this.surround.add(blockPos);
        }
        this.floorPos.add(pos);
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Surround.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            if (entity instanceof EntityEnderCrystal) {
                this.crystals.add((EntityEnderCrystal)entity);
                continue;
            }
            if (!(entity instanceof EntityPlayer)) continue;
            return true;
        }
        return false;
    }
}
