/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockAnvil
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.block.BlockRedstoneTorch
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.google.common.collect.ImmutableMap;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlacementUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.SpoofRotationUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.CrystalUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="Blocker", category=Category.Combat)
public class Blocker
extends Module {
    ModeSetting time = this.registerMode("Time Mode", Arrays.asList("Tick", "onUpdate", "Both", "Fast"), "Tick");
    ModeSetting breakType = this.registerMode("Type", Arrays.asList("Vanilla", "Packet"), "Vanilla");
    BooleanSetting packet = this.registerBoolean("Packet Place", false);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting anvilBlocker = this.registerBoolean("Anvil", true);
    BooleanSetting fallingBlocks = this.registerBoolean("Block FallingBlocks", true);
    BooleanSetting trap = this.registerBoolean("Trap", true, () -> (Boolean)this.fallingBlocks.getValue());
    ModeSetting fallingMode = this.registerMode("Block Mode", Arrays.asList("Break", "Torch", "Skull"), "Break", () -> (Boolean)this.fallingBlocks.getValue());
    BooleanSetting pistonBlocker = this.registerBoolean("Break Piston", true);
    BooleanSetting pistonBlockerNew = this.registerBoolean("Block Piston", true);
    BooleanSetting antiFacePlace = this.registerBoolean("Shift AntiFacePlace", true);
    ModeSetting blockPlaced = this.registerMode("Block Place", Arrays.asList("Pressure", "String"), "String", () -> (Boolean)this.antiFacePlace.getValue());
    IntegerSetting BlocksPerTick = this.registerInteger("Blocks Per Tick", 4, 0, 10);
    IntegerSetting tickDelay = this.registerInteger("Tick Delay", 5, 0, 10);
    DoubleSetting range = this.registerDouble("Range", 5.0, 0.0, 10.0);
    DoubleSetting yrange = this.registerDouble("YRange", 5.0, 0.0, 10.0);
    List<BlockPos> pistonList = new ArrayList<BlockPos>();
    private int delayTimeTicks = 0;
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};

    @Override
    public void onEnable() {
        this.pistonList = new ArrayList<BlockPos>();
        SpoofRotationUtil.ROTATION_UTIL.onEnable();
        PlacementUtil.onEnable();
    }

    @Override
    public void onDisable() {
        SpoofRotationUtil.ROTATION_UTIL.onDisable();
        PlacementUtil.onDisable();
    }

    @Override
    public void onUpdate() {
        if (((String)this.time.getValue()).equals("onUpdate") || ((String)this.time.getValue()).equals("Both")) {
            this.block();
        }
    }

    @Override
    public void onTick() {
        if (((String)this.time.getValue()).equals("Tick") || ((String)this.time.getValue()).equals("Both")) {
            this.block();
        }
    }

    @Override
    public void fast() {
        if (((String)this.time.getValue()).equals("Fast")) {
            this.block();
        }
    }

    private void block() {
        if (Blocker.mc.field_71439_g == null || Blocker.mc.field_71441_e == null || Blocker.mc.field_71439_g.field_70128_L) {
            this.pistonList.clear();
            return;
        }
        if (this.delayTimeTicks < (Integer)this.tickDelay.getValue()) {
            ++this.delayTimeTicks;
        } else {
            SpoofRotationUtil.ROTATION_UTIL.shouldSpoofAngles(true);
            this.delayTimeTicks = 0;
            if (((Boolean)this.anvilBlocker.getValue()).booleanValue()) {
                this.blockAnvil();
            }
            if (((Boolean)this.fallingBlocks.getValue()).booleanValue()) {
                this.blockFallingBlocks();
            }
            if (((Boolean)this.pistonBlocker.getValue()).booleanValue()) {
                this.blockPiston();
            }
            if (((Boolean)this.pistonBlockerNew.getValue()).booleanValue()) {
                this.blockPA();
            }
            if (((Boolean)this.antiFacePlace.getValue()).booleanValue() && Blocker.mc.field_71474_y.field_74311_E.func_151468_f()) {
                this.antiFacePlace();
            }
        }
    }

    private List<BlockPos> posList() {
        return EntityUtil.getSphere(PlayerUtil.getPlayerPos(), (Double)this.range.getValue(), (Double)this.yrange.getValue(), false, false, 0);
    }

    private void antiFacePlace() {
        int blocksPlaced = 0;
        for (Vec3d surround : new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0)}) {
            BlockPos pos = new BlockPos(Blocker.mc.field_71439_g.field_70165_t + surround.field_72450_a, Blocker.mc.field_71439_g.field_70163_u, Blocker.mc.field_71439_g.field_70161_v + surround.field_72449_c);
            Block temp = BlockUtil.getBlock(pos);
            if (!(temp instanceof BlockObsidian) && temp != Blocks.field_150357_h) continue;
            if (blocksPlaced++ == 0) {
                InventoryUtil.getHotBarPressure((String)this.blockPlaced.getValue());
            }
            PlacementUtil.placeItem(new BlockPos((double)pos.func_177958_n(), (double)pos.func_177956_o() + surround.field_72448_b, (double)pos.func_177952_p()), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), Items.field_151007_F.getClass());
            if (blocksPlaced != (Integer)this.BlocksPerTick.getValue()) continue;
            return;
        }
    }

    private void blockPA() {
        int slot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (slot == -1) {
            return;
        }
        for (BlockPos pos : this.posList()) {
            if (this.pistonList.contains(pos) || !(Blocker.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockPistonBase) && Blocker.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150331_J && Blocker.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150320_F) continue;
            this.pistonList.add(pos);
        }
        this.pistonList.removeIf(blockPos -> Blocker.mc.field_71439_g.func_174818_b(blockPos) > (Double)this.range.getValue() * (Double)this.range.getValue());
        if (!this.pistonList.isEmpty()) {
            InventoryUtil.run(slot, (Boolean)this.packetSwitch.getValue(), () -> {
                for (BlockPos pos : this.pistonList) {
                    BlockPos head = this.getHeadPos(pos);
                    if (!BlockUtil.canReplace(pos) && !BlockUtil.canReplace(head)) continue;
                    BurrowUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    BurrowUtil.placeBlock(head, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                }
            });
        }
        this.pistonList.removeIf(blockPos -> Blocker.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z);
    }

    public BlockPos getHeadPos(BlockPos pos) {
        ImmutableMap properties = Blocker.mc.field_71441_e.func_180495_p(pos).func_177228_b();
        for (IProperty prop : properties.keySet()) {
            if (prop.func_177699_b() != EnumFacing.class || !prop.func_177701_a().equals("facing") && !prop.func_177701_a().equals("rotation")) continue;
            BlockPos pushPos = pos.func_177972_a((EnumFacing)properties.get((Object)prop));
            for (BlockPos side : this.sides) {
                if (!this.isPos2(pos.func_177971_a((Vec3i)side), pushPos)) continue;
                return pos.func_177971_a((Vec3i)side);
            }
        }
        return null;
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    private void blockAnvil() {
        for (Entity t : Blocker.mc.field_71441_e.field_72996_f) {
            Block ex;
            if (!(t instanceof EntityFallingBlock) || !((ex = ((EntityFallingBlock)t).field_175132_d.func_177230_c()) instanceof BlockAnvil) || (int)t.field_70165_t != (int)Blocker.mc.field_71439_g.field_70165_t || (int)t.field_70161_v != (int)Blocker.mc.field_71439_g.field_70161_v || !(BlockUtil.getBlock(Blocker.mc.field_71439_g.field_70165_t, Blocker.mc.field_71439_g.field_70163_u + 2.0, Blocker.mc.field_71439_g.field_70161_v) instanceof BlockAir)) continue;
            this.placeBlock(new BlockPos(Blocker.mc.field_71439_g.field_70165_t, Blocker.mc.field_71439_g.field_70163_u + 2.0, Blocker.mc.field_71439_g.field_70161_v));
        }
    }

    private void blockFallingBlocks() {
        for (Entity t : Blocker.mc.field_71441_e.field_72996_f) {
            Block ex;
            if (!(t instanceof EntityFallingBlock) || (ex = ((EntityFallingBlock)t).field_175132_d.func_177230_c()) instanceof BlockAnvil || (int)t.field_70165_t != (int)Blocker.mc.field_71439_g.field_70165_t || (int)t.field_70161_v != (int)Blocker.mc.field_71439_g.field_70161_v || (int)t.field_70163_u <= (int)Blocker.mc.field_71439_g.field_70163_u) continue;
            if (((Boolean)this.trap.getValue()).booleanValue()) {
                this.placeBlock(new BlockPos(Blocker.mc.field_71439_g.field_70165_t, Blocker.mc.field_71439_g.field_70163_u + 2.0, Blocker.mc.field_71439_g.field_70161_v));
            }
            int slot = -1;
            switch ((String)this.fallingMode.getValue()) {
                case "Torch": {
                    slot = BurrowUtil.findHotbarBlock(BlockRedstoneTorch.class);
                    break;
                }
                case "Skull": {
                    slot = InventoryUtil.findSkullSlot();
                }
            }
            if (slot != -1) {
                InventoryUtil.run(slot, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.placeBlock(PlayerUtil.getPlayerPos(), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
                continue;
            }
            Blocker.mc.field_71442_b.func_180512_c(PlayerUtil.getPlayerPos(), EnumFacing.UP);
        }
    }

    private void blockPiston() {
        for (Entity t : Blocker.mc.field_71441_e.field_72996_f) {
            if (!(t instanceof EntityEnderCrystal) || !(t.field_70165_t >= Blocker.mc.field_71439_g.field_70165_t - 1.5) || !(t.field_70165_t <= Blocker.mc.field_71439_g.field_70165_t + 1.5) || !(t.field_70161_v >= Blocker.mc.field_71439_g.field_70161_v - 1.5) || !(t.field_70161_v <= Blocker.mc.field_71439_g.field_70161_v + 1.5)) continue;
            for (int i = -2; i < 3; ++i) {
                for (int j = -2; j < 3; ++j) {
                    if (i != 0 && j != 0 || !(BlockUtil.getBlock(t.field_70165_t + (double)i, t.field_70163_u, t.field_70161_v + (double)j) instanceof BlockPistonBase)) continue;
                    this.breakCrystalPiston(t);
                }
            }
        }
    }

    private void placeBlock(BlockPos pos) {
        if (!Blocker.mc.field_71441_e.func_175623_d(pos)) {
            return;
        }
        int obsidianSlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (obsidianSlot == -1) {
            return;
        }
        InventoryUtil.run(obsidianSlot, (Boolean)this.packetSwitch.getValue(), () -> {
            boolean isNull = true;
            if (BurrowUtil.getFirstFacing(pos) == null) {
                for (BlockPos side : this.sides) {
                    BlockPos added = pos.func_177971_a((Vec3i)side);
                    if (this.intersectsWithEntity(added) || BurrowUtil.getFirstFacing(added) == null) continue;
                    BurrowUtil.placeBlock(added, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    isNull = false;
                    break;
                }
            } else {
                isNull = false;
            }
            if (!isNull) {
                BurrowUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
            }
        });
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Blocker.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private void breakCrystalPiston(Entity crystal) {
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            SpoofRotationUtil.ROTATION_UTIL.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, (EntityPlayer)Blocker.mc.field_71439_g);
        }
        if (((String)this.breakType.getValue()).equals("Vanilla")) {
            CrystalUtil.breakCrystal(crystal, (boolean)((Boolean)this.swing.getValue()));
        } else {
            CrystalUtil.breakCrystalPacket(crystal, (Boolean)this.swing.getValue());
        }
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            SpoofRotationUtil.ROTATION_UTIL.resetRotation();
        }
    }
}
