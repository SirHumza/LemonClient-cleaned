/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
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
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AntiBurrow;
import com.lemonclient.client.module.modules.combat.AntiRegear;
import com.lemonclient.client.module.modules.combat.CevBreaker;
import com.lemonclient.client.module.modules.dev.BedCevBreaker;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoCity", category=Category.Combat)
public class AutoCityDev
extends Module {
    public static AutoCityDev INSTANCE;
    ModeSetting breakBlock = this.registerMode("Break Block", Arrays.asList("Normal", "Packet"), "Packet");
    IntegerSetting range = this.registerInteger("Range", 6, 0, 10);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting ignore = this.registerBoolean("Ignore Bed", false);
    public boolean working;
    float pitch;
    float yaw;
    BlockPos blockMine;
    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (!((Boolean)this.rotate.getValue()).booleanValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.field_149476_e = this.yaw;
            packet.field_149473_f = this.pitch;
        }
    }, new Predicate[0]);

    public AutoCityDev() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        EntityPlayer aimTarget;
        if (AutoCityDev.mc.field_71441_e == null || AutoCityDev.mc.field_71439_g == null || AutoCityDev.mc.field_71439_g.field_70128_L) {
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
            if (instantPos.equals((Object)new BlockPos(AutoCityDev.mc.field_71439_g.field_70165_t, AutoCityDev.mc.field_71439_g.field_70163_u + 2.0, AutoCityDev.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (instantPos.equals((Object)new BlockPos(AutoCityDev.mc.field_71439_g.field_70165_t, AutoCityDev.mc.field_71439_g.field_70163_u - 1.0, AutoCityDev.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (AutoCityDev.mc.field_71441_e.func_180495_p(instantPos).func_177230_c() == Blocks.field_150321_G) {
                return;
            }
            if (this.blockMine != null && !AutoCityDev.isPos2(this.blockMine, instantPos)) {
                this.blockMine = null;
            }
        }
        if ((aimTarget = PlayerUtil.getNearestPlayer((Integer)this.range.getValue() + 2)) == null) {
            return;
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
        BlockPos playerPos = EntityUtil.getEntityPos((Entity)aimTarget);
        if (this.blockMine != null) {
            if (AutoCityDev.mc.field_71439_g.func_70011_f((double)this.blockMine.field_177962_a + 0.5, (double)this.blockMine.field_177960_b + 0.5, (double)this.blockMine.field_177961_c + 0.5) > (double)((Integer)this.range.getValue()).intValue()) {
                this.blockMine = null;
            } else {
                boolean same = false;
                for (BlockPos offset : offsets) {
                    if (!AutoCityDev.isPos2(playerPos.func_177971_a((Vec3i)offset), this.blockMine)) continue;
                    same = true;
                }
                if (!same) {
                    this.blockMine = null;
                }
            }
        }
        boolean hole = true;
        for (BlockPos offset : offsets) {
            BlockPos pos = playerPos.func_177971_a((Vec3i)offset);
            IBlockState blockState = BlockUtil.getState(pos);
            if (!BlockUtil.isAir(pos) && (!((Boolean)this.ignore.getValue()).booleanValue() || blockState != Blocks.field_150324_C)) continue;
            hole = false;
        }
        if (!hole) {
            return;
        }
        if (this.blockMine != null) {
            this.working = true;
            return;
        }
        EnumFacing facing = RotationUtil.getFacing(PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i);
        this.blockMine = playerPos.func_177967_a(facing, -1);
        if (AutoCityDev.mc.field_71439_g.func_70011_f((double)this.blockMine.field_177962_a + 0.5, (double)this.blockMine.field_177960_b + 0.5, (double)this.blockMine.field_177961_c + 0.5) > (double)((Integer)this.range.getValue()).intValue() || (Boolean)this.ignore.getValue() != false && BlockUtil.getBlock(this.blockMine) == Blocks.field_150324_C || BlockUtil.getBlock((BlockPos)this.blockMine).field_149782_v < 0.0f) {
            ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
            for (BlockPos offset : offsets) {
                BlockPos pos = playerPos.func_177971_a((Vec3i)offset);
                if (AutoCityDev.mc.field_71439_g.func_174818_b(pos) > (double)((Integer)this.range.getValue() * (Integer)this.range.getValue()) || BlockUtil.getBlock(pos) == Blocks.field_150357_h) continue;
                if (((Boolean)this.ignore.getValue()).booleanValue() && BlockUtil.getBlock(pos) == Blocks.field_150324_C) {
                    return;
                }
                if (AutoCityDev.mc.field_71439_g.func_70011_f((double)pos.field_177962_a + 0.5, (double)pos.field_177960_b + 0.5, (double)pos.field_177961_c + 0.5) > (double)((Integer)this.range.getValue()).intValue()) continue;
                posList.add(pos);
            }
            this.blockMine = posList.stream().min(Comparator.comparing(p -> AutoCityDev.mc.field_71439_g.func_70011_f((double)p.field_177962_a + 0.5, (double)p.field_177960_b + 0.5, (double)p.field_177961_c + 0.5))).orElse(null);
        }
        if (this.blockMine == null) {
            return;
        }
        this.working = true;
        if (((Boolean)this.swing.getValue()).booleanValue()) {
            AutoCityDev.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        if (((String)this.breakBlock.getValue()).equalsIgnoreCase("Packet")) {
            AutoCityDev.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockMine, EnumFacing.UP));
            AutoCityDev.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockMine, EnumFacing.UP));
        } else {
            AutoCityDev.mc.field_71442_b.func_180512_c(this.blockMine, EnumFacing.UP);
        }
    }

    private static boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }
}
