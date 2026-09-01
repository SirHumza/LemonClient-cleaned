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
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.DeathEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.Declaration(name="AutoSelfFill", category=Category.Combat)
public class AutoSelfFill
extends Module {
    IntegerSetting delay = this.registerInteger("Delay", 10, 0, 50);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packet = this.registerBoolean("Packet Place", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting obsidian = this.registerBoolean("Obsidian", true);
    BooleanSetting echest = this.registerBoolean("Ender Chest", true);
    BooleanSetting web = this.registerBoolean("Web", true);
    BooleanSetting skull = this.registerBoolean("Skull", true);
    BooleanSetting plate = this.registerBoolean("Slab", true);
    BooleanSetting upPlate = this.registerBoolean("Up Slab", true);
    BooleanSetting trapdoor = this.registerBoolean("Trapdoor", true);
    int new_slot = -1;
    int waited;
    boolean door;
    boolean block;
    @EventHandler
    private final Listener<DeathEvent> deathEventListener = new Listener<DeathEvent>(event -> {
        if (event.player == AutoSelfFill.mc.field_71439_g) {
            this.disable();
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        if (this.waited++ < (Integer)this.delay.getValue()) {
            return;
        }
        this.waited = 0;
        if (BlockUtil.isAir(PlayerUtil.getPlayerPos()) && AutoSelfFill.mc.field_71439_g.field_70122_E && this.intersectsWithEntity(PlayerUtil.getPlayerPos())) {
            this.placeBlock();
        }
    }

    public void placeBlock() {
        this.new_slot = this.find_in_hotbar();
        if (this.new_slot == -1) {
            return;
        }
        InventoryUtil.run(this.new_slot, (Boolean)this.packetSwitch.getValue(), () -> {
            if (this.door) {
                this.placeTrapdoor();
            } else if (((Boolean)this.upPlate.getValue()).booleanValue() && this.new_slot == BurrowUtil.findHotbarBlock(BlockSlab.class)) {
                this.burrowUp();
            } else if (this.block) {
                this.burrow();
            } else {
                BurrowUtil.placeBlock(PlayerUtil.getPlayerPos(), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
            }
        });
    }

    private int find_in_hotbar() {
        this.block = false;
        this.door = false;
        int newHand = -1;
        if (((Boolean)this.trapdoor.getValue()).booleanValue() && (newHand = BurrowUtil.findHotbarBlock(BlockTrapDoor.class)) != -1) {
            this.door = true;
        }
        if (newHand == -1 && ((Boolean)this.skull.getValue()).booleanValue()) {
            newHand = InventoryUtil.findSkullSlot();
        }
        if (newHand == -1 && ((Boolean)this.web.getValue()).booleanValue()) {
            newHand = BurrowUtil.findHotbarBlock(BlockWeb.class);
        }
        if (newHand == -1 && ((Boolean)this.plate.getValue()).booleanValue()) {
            newHand = BurrowUtil.findHotbarBlock(BlockSlab.class);
        }
        if (newHand == -1 && ((Boolean)this.obsidian.getValue()).booleanValue() && (newHand = BurrowUtil.findHotbarBlock(BlockObsidian.class)) != -1) {
            this.block = true;
        }
        if (newHand == -1 && ((Boolean)this.echest.getValue()).booleanValue() && (newHand = BurrowUtil.findHotbarBlock(BlockEnderChest.class)) != -1) {
            this.block = true;
        }
        return newHand;
    }

    private void placeTrapdoor() {
        BlockPos originalPos = PlayerUtil.getPlayerPos();
        EnumFacing facing = BurrowUtil.getTrapdoorFacing(originalPos);
        if (facing == null) {
            return;
        }
        BlockPos neighbour = originalPos.func_177972_a(facing);
        EnumFacing opposite = facing.func_176734_d();
        double x = AutoSelfFill.mc.field_71439_g.field_70165_t;
        double y = (int)AutoSelfFill.mc.field_71439_g.field_70163_u;
        double z = AutoSelfFill.mc.field_71439_g.field_70161_v;
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y + (double)0.2f, z, AutoSelfFill.mc.field_71439_g.field_70122_E));
        BurrowUtil.rightClickBlock(neighbour, opposite, new Vec3d(0.5, 0.8, 0.5), (Boolean)this.packet.getValue(), (boolean)((Boolean)this.swing.getValue()));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, AutoSelfFill.mc.field_71439_g.field_70122_E));
    }

    private void burrow() {
        BlockPos originalPos = new BlockPos(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u, AutoSelfFill.mc.field_71439_g.field_70161_v);
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 0.42, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 0.75, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 1.01, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 1.16, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 1.01, AutoSelfFill.mc.field_71439_g.field_70161_v, false));
    }

    private void burrowUp() {
        EnumFacing opposite;
        BlockPos neighbour;
        BlockPos originalPos = PlayerUtil.getPlayerPos();
        if (!AutoSelfFill.mc.field_71441_e.func_175623_d(originalPos.func_177968_d())) {
            neighbour = originalPos.func_177972_a(EnumFacing.SOUTH);
            opposite = EnumFacing.SOUTH.func_176734_d();
        } else if (!AutoSelfFill.mc.field_71441_e.func_175623_d(originalPos.func_177978_c())) {
            neighbour = originalPos.func_177972_a(EnumFacing.NORTH);
            opposite = EnumFacing.NORTH.func_176734_d();
        } else if (!AutoSelfFill.mc.field_71441_e.func_175623_d(originalPos.func_177974_f())) {
            neighbour = originalPos.func_177972_a(EnumFacing.EAST);
            opposite = EnumFacing.EAST.func_176734_d();
        } else if (!AutoSelfFill.mc.field_71441_e.func_175623_d(originalPos.func_177976_e())) {
            neighbour = originalPos.func_177972_a(EnumFacing.WEST);
            opposite = EnumFacing.WEST.func_176734_d();
        } else {
            return;
        }
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 0.42, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 0.75, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 1.01, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 1.16, AutoSelfFill.mc.field_71439_g.field_70161_v, true));
        BurrowUtil.rightClickBlock(neighbour, opposite, new Vec3d(0.5, 0.8, 0.5), (Boolean)this.packet.getValue(), (boolean)((Boolean)this.swing.getValue()));
        AutoSelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoSelfFill.mc.field_71439_g.field_70165_t, AutoSelfFill.mc.field_71439_g.field_70163_u + 1.01, AutoSelfFill.mc.field_71439_g.field_70161_v, false));
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoSelfFill.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity == AutoSelfFill.mc.field_71439_g || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }
}
