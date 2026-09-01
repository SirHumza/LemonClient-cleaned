/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockConcretePowder
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AntiRegear;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Module.Declaration(name="AutoMineBurrow", category=Category.Combat)
public class AntiBurrow
extends Module {
    public static AntiBurrow INSTANCE;
    ModeSetting breakBlock = this.registerMode("Break Block", Arrays.asList("Normal", "Packet"), "Packet");
    DoubleSetting balance = this.registerDouble("Reduce", 0.24, 0.0, 0.5);
    BooleanSetting up = this.registerBoolean("Head", true);
    BooleanSetting down = this.registerBoolean("Feet", true);
    BooleanSetting first = this.registerBoolean("Head First", false, () -> (Boolean)this.up.getValue());
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting ignore = this.registerBoolean("Ignore Bed", false);
    BooleanSetting ignorePiston = this.registerBoolean("Ignore Piston", false);
    BooleanSetting ignoreWeb = this.registerBoolean("Ignore Web", false);
    BooleanSetting fire = this.registerBoolean("Fire", false);
    BooleanSetting sand = this.registerBoolean("Falling Blocks", false);
    BooleanSetting rail = this.registerBoolean("Rail", false);
    IntegerSetting range = this.registerInteger("Range", 5, 0, 10);
    BooleanSetting doubleMine = this.registerBoolean("Double Mine", false);
    public double yaw;
    public double pitch;
    public boolean mining;
    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (!((Boolean)this.rotate.getValue()).booleanValue() || !this.mining) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.field_149476_e = (float)this.yaw;
            packet.field_149473_f = (float)this.pitch;
        }
    }, new Predicate[0]);
    public static final List<Block> airBlocks;

    public AntiBurrow() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.mining = false;
    }

    @Override
    public void onUpdate() {
        BlockPos pos;
        if (AntiBurrow.mc.field_71441_e == null || AntiBurrow.mc.field_71439_g == null || AntiBurrow.mc.field_71439_g.field_70128_L) {
            return;
        }
        this.mining = false;
        if (AntiRegear.INSTANCE.working) {
            return;
        }
        BlockPos instantPos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            instantPos = PacketMine.INSTANCE.packetPos;
        }
        if (instantPos != null) {
            if (instantPos.equals((Object)new BlockPos(AntiBurrow.mc.field_71439_g.field_70165_t, AntiBurrow.mc.field_71439_g.field_70163_u + 2.0, AntiBurrow.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (instantPos.equals((Object)new BlockPos(AntiBurrow.mc.field_71439_g.field_70165_t, AntiBurrow.mc.field_71439_g.field_70163_u - 1.0, AntiBurrow.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (AntiBurrow.mc.field_71441_e.func_180495_p(instantPos).func_177230_c() == Blocks.field_150321_G) {
                return;
            }
        }
        if ((pos = this.getCityPos(null)) == null) {
            return;
        }
        this.mining = true;
        if (((Boolean)this.doubleMine.getValue()).booleanValue()) {
            BlockPos doublePos = null;
            if (ModuleManager.isModuleEnabled(PacketMine.class)) {
                doublePos = PacketMine.INSTANCE.doublePos;
            }
            if (doublePos == null) {
                this.doBreak(this.getCityPos(pos));
            }
        }
        double[] rotate = EntityUtil.calculateLookAt((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5, (Entity)AntiBurrow.mc.field_71439_g);
        this.yaw = rotate[0];
        this.pitch = rotate[1];
        this.doBreak(pos);
    }

    public BlockPos getCityPos(BlockPos pos) {
        EntityPlayer player = PlayerUtil.getNearestPlayer(((Integer)this.range.getValue()).intValue());
        if (player == null) {
            return null;
        }
        Vec3d[] sides = new Vec3d[]{new Vec3d(((Double)this.balance.getValue()).doubleValue(), 0.0, ((Double)this.balance.getValue()).doubleValue()), new Vec3d(-((Double)this.balance.getValue()).doubleValue(), 0.0, ((Double)this.balance.getValue()).doubleValue()), new Vec3d(((Double)this.balance.getValue()).doubleValue(), 0.0, -((Double)this.balance.getValue()).doubleValue()), new Vec3d(-((Double)this.balance.getValue()).doubleValue(), 0.0, -((Double)this.balance.getValue()).doubleValue())};
        if (((Boolean)this.first.getValue()).booleanValue() && ((Boolean)this.up.getValue()).booleanValue()) {
            for (int x = 1; x > -1 && (((Boolean)this.down.getValue()).booleanValue() || x != 0); --x) {
                for (Vec3d side : sides) {
                    BlockPos burrowPos = new BlockPos(player.field_70165_t + side.field_72450_a, player.field_70163_u + (double)x, player.field_70161_v + side.field_72449_c);
                    if (!this.intersect(player, burrowPos) || this.isPos2(burrowPos, pos) || !this.burrow(burrowPos)) continue;
                    return burrowPos;
                }
            }
        } else {
            int x;
            int n = x = (Boolean)this.down.getValue() != false ? 0 : 1;
            while (x < 2 && (((Boolean)this.up.getValue()).booleanValue() || x != 1)) {
                for (Vec3d side : sides) {
                    BlockPos burrowPos = new BlockPos(player.field_70165_t + side.field_72450_a, player.field_70163_u + (double)x, player.field_70161_v + side.field_72449_c);
                    if (!this.intersect(player, burrowPos) || this.isPos2(burrowPos, pos) || !this.burrow(burrowPos)) continue;
                    return burrowPos;
                }
                ++x;
            }
        }
        return null;
    }

    private boolean burrow(BlockPos pos) {
        return !(airBlocks.contains(AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c()) || !(BlockUtil.getBlock((BlockPos)pos).field_149782_v >= 0.0f) || (Boolean)this.ignore.getValue() != false && AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C || (Boolean)this.ignorePiston.getValue() != false && AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150332_K || (Boolean)this.ignoreWeb.getValue() != false && AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G || (Boolean)this.fire.getValue() == false && AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150480_ab || (Boolean)this.rail.getValue() == false && (AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150448_aq || AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150408_cc || AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150319_E || AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150318_D) || (Boolean)this.sand.getValue() == false && (AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150354_m || AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150351_n || AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150467_bQ || AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockConcretePowder));
    }

    private void doBreak(BlockPos pos) {
        if (pos == null) {
            this.mining = false;
            return;
        }
        BlockPos doublePos = null;
        BlockPos instantPos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            instantPos = PacketMine.INSTANCE.packetPos;
            doublePos = PacketMine.INSTANCE.doublePos;
        }
        if (this.isPos2(instantPos, pos) || this.isPos2(doublePos, pos)) {
            return;
        }
        if (((Boolean)this.swing.getValue()).booleanValue()) {
            AntiBurrow.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        if (((String)this.breakBlock.getValue()).equals("Packet")) {
            AntiBurrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
            AntiBurrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
        } else {
            AntiBurrow.mc.field_71442_b.func_180512_c(pos, EnumFacing.UP);
        }
    }

    private boolean intersect(EntityPlayer player, BlockPos pos) {
        return player.field_70121_D.func_72326_a(AntiBurrow.mc.field_71441_e.func_180495_p(pos).func_185918_c((World)AntiBurrow.mc.field_71441_e, pos));
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    static {
        airBlocks = Arrays.asList(Blocks.field_150350_a, Blocks.field_150353_l, Blocks.field_150356_k, Blocks.field_150355_j, Blocks.field_150358_i, Blocks.field_150349_c);
    }
}
