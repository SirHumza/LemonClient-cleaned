/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketBlockBreakAnim
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="AntiCity", category=Category.Combat)
public class AntiCity
extends Module {
    ModeSetting time = this.registerMode("Time Mode", Arrays.asList("Tick", "onUpdate", "Fast"), "Tick");
    IntegerSetting bpt = this.registerInteger("Blocks Per Tick", 4, 0, 20);
    BooleanSetting self = this.registerBoolean("Self", false);
    BooleanSetting smart = this.registerBoolean("Smart", false);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packet = this.registerBoolean("Packet", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BlockPos breakPos;
    private int placeID;
    @EventHandler
    private final Listener<PacketEvent.PostSend> sendListener = new Listener<PacketEvent.PostSend>(event -> {
        if (AntiCity.mc.field_71441_e == null || AntiCity.mc.field_71439_g == null) {
            return;
        }
        if (!((Boolean)this.self.getValue()).booleanValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && ((CPacketPlayerDigging)event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            BlockPos ab = packet.func_179715_a();
            this.breakPos = packet.func_179715_a();
            BlockPos player = EntityUtil.getPlayerPos((EntityPlayer)AntiCity.mc.field_71439_g);
            if (ab.equals((Object)player.func_177982_a(1, 0, 0))) {
                this.placeID = 1;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 0, 0))) {
                this.placeID = 2;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, 1))) {
                this.placeID = 3;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, -1))) {
                this.placeID = 4;
            }
            if (ab.equals((Object)player.func_177982_a(2, 0, 0))) {
                this.placeID = 5;
            }
            if (ab.equals((Object)player.func_177982_a(-2, 0, 0))) {
                this.placeID = 6;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, 2))) {
                this.placeID = 7;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, -2))) {
                this.placeID = 8;
            }
            if (ab.equals((Object)player.func_177982_a(1, 1, 0))) {
                this.placeID = 9;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 1, 0))) {
                this.placeID = 10;
            }
            if (ab.equals((Object)player.func_177982_a(0, 1, 1))) {
                this.placeID = 11;
            }
            if (ab.equals((Object)player.func_177982_a(0, 1, -1))) {
                this.placeID = 12;
            }
            if (ab.equals((Object)player.func_177982_a(1, 0, 1))) {
                this.placeID = 13;
            }
            if (ab.equals((Object)player.func_177982_a(1, 0, -1))) {
                this.placeID = 14;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 0, 1))) {
                this.placeID = 15;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 0, -1))) {
                this.placeID = 16;
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (AntiCity.mc.field_71441_e == null || AntiCity.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            SPacketBlockBreakAnim packet = (SPacketBlockBreakAnim)event.getPacket();
            BlockPos ab = packet.func_179821_b();
            this.breakPos = packet.func_179821_b();
            BlockPos player = EntityUtil.getPlayerPos((EntityPlayer)AntiCity.mc.field_71439_g);
            if (ab.equals((Object)player.func_177982_a(1, 0, 0))) {
                this.placeID = 1;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 0, 0))) {
                this.placeID = 2;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, 1))) {
                this.placeID = 3;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, -1))) {
                this.placeID = 4;
            }
            if (ab.equals((Object)player.func_177982_a(2, 0, 0))) {
                this.placeID = 5;
            }
            if (ab.equals((Object)player.func_177982_a(-2, 0, 0))) {
                this.placeID = 6;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, 2))) {
                this.placeID = 7;
            }
            if (ab.equals((Object)player.func_177982_a(0, 0, -2))) {
                this.placeID = 8;
            }
            if (ab.equals((Object)player.func_177982_a(1, 1, 0))) {
                this.placeID = 9;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 1, 0))) {
                this.placeID = 10;
            }
            if (ab.equals((Object)player.func_177982_a(0, 1, 1))) {
                this.placeID = 11;
            }
            if (ab.equals((Object)player.func_177982_a(0, 1, -1))) {
                this.placeID = 12;
            }
            if (ab.equals((Object)player.func_177982_a(1, 0, 1))) {
                this.placeID = 13;
            }
            if (ab.equals((Object)player.func_177982_a(1, 0, -1))) {
                this.placeID = 14;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 0, 1))) {
                this.placeID = 15;
            }
            if (ab.equals((Object)player.func_177982_a(-1, 0, -1))) {
                this.placeID = 16;
            }
        }
    }, new Predicate[0]);
    int placed;

    public static boolean noHard(Block block) {
        return block != Blocks.field_150357_h;
    }

    @Override
    public void onUpdate() {
        if (((String)this.time.getValue()).equals("onUpdate")) {
            this.antiCity();
        }
        this.placed = 0;
    }

    @Override
    public void onTick() {
        if (((String)this.time.getValue()).equals("Tick")) {
            this.antiCity();
        }
    }

    @Override
    public void fast() {
        if (((String)this.time.getValue()).equals("Fast")) {
            this.antiCity();
        }
    }

    public void antiCity() {
        if (AntiCity.mc.field_71441_e == null || AntiCity.mc.field_71439_g == null || AntiCity.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (LemonClient.speedUtil.getPlayerSpeed((EntityPlayer)AntiCity.mc.field_71439_g) >= 15.0) {
            return;
        }
        int obsidian = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (obsidian == -1) {
            return;
        }
        BlockPos pos = EntityUtil.getPlayerPos((EntityPlayer)AntiCity.mc.field_71439_g);
        if (pos == null) {
            return;
        }
        pos = new BlockPos((double)pos.field_177962_a, (double)pos.field_177960_b + 0.2, (double)pos.field_177961_c);
        ArrayList<BlockPos> placeList = new ArrayList<BlockPos>();
        if (this.breakPos != null) {
            if ((this.breakPos.equals((Object)pos.func_177982_a(1, 0, 0)) || this.breakPos.equals((Object)pos.func_177982_a(1, 1, 0))) && this.isAir(pos.func_177982_a(1, 0, 0)) && this.isAir(pos.func_177982_a(1, 1, 0))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(1, 0, 0))) {
                    placeList.add(pos.func_177982_a(1, 1, 0));
                } else {
                    placeList.add(pos.func_177982_a(1, 0, 0));
                }
            }
            if ((this.breakPos.equals((Object)pos.func_177982_a(-1, 0, 0)) || this.breakPos.equals((Object)pos.func_177982_a(-1, 1, 0))) && this.isAir(pos.func_177982_a(-1, 0, 0)) && this.isAir(pos.func_177982_a(-1, 1, 0))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(-1, 0, 0))) {
                    placeList.add(pos.func_177982_a(-1, 1, 0));
                } else {
                    placeList.add(pos.func_177982_a(-1, 0, 0));
                }
            }
            if ((this.breakPos.equals((Object)pos.func_177982_a(0, 0, 1)) || this.breakPos.equals((Object)pos.func_177982_a(0, 1, 1))) && this.isAir(pos.func_177982_a(0, 0, 1)) && this.isAir(pos.func_177982_a(0, 1, 1))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(0, 0, 1))) {
                    placeList.add(pos.func_177982_a(0, 1, 1));
                } else {
                    placeList.add(pos.func_177982_a(0, 0, 1));
                }
            }
            if ((this.breakPos.equals((Object)pos.func_177982_a(0, 0, -1)) || this.breakPos.equals((Object)pos.func_177982_a(0, 1, -1))) && this.isAir(pos.func_177982_a(0, 0, -1)) && this.isAir(pos.func_177982_a(0, 1, -1))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(0, 0, -1))) {
                    placeList.add(pos.func_177982_a(0, 1, -1));
                } else {
                    placeList.add(pos.func_177982_a(0, 0, -1));
                }
            }
        }
        if (AntiCity.noHard(this.getBlock(pos.func_177982_a(1, 0, 0)).func_177230_c())) {
            if (this.placeID == 1) {
                placeList.add(pos.func_177982_a(2, 0, 0));
                placeList.add(pos.func_177982_a(1, 0, 1));
                placeList.add(pos.func_177982_a(1, 0, -1));
                placeList.add(pos.func_177982_a(1, 1, 0));
                if (AntiCity.EntityCheck(pos.func_177982_a(2, 0, 0))) {
                    placeList.add(pos.func_177982_a(3, 0, 0));
                    placeList.add(pos.func_177982_a(3, 1, 0));
                }
            }
            if (this.placeID == 5) {
                placeList.add(pos.func_177982_a(1, 0, 0));
                placeList.add(pos.func_177982_a(2, 1, 0));
                placeList.add(pos.func_177982_a(3, 0, 0));
            }
            if (this.placeID == 9) {
                placeList.add(pos.func_177982_a(1, 0, 0));
                placeList.add(pos.func_177982_a(2, 1, 0));
            }
            if (this.placeID == 13 || this.placeID == 14) {
                placeList.add(pos.func_177982_a(1, 0, 0));
            }
        }
        if (AntiCity.noHard(this.getBlock(pos.func_177982_a(-1, 0, 0)).func_177230_c())) {
            if (this.placeID == 2) {
                placeList.add(pos.func_177982_a(-2, 0, 0));
                placeList.add(pos.func_177982_a(-1, 0, 1));
                placeList.add(pos.func_177982_a(-1, 0, -1));
                placeList.add(pos.func_177982_a(-1, 1, 0));
                if (AntiCity.EntityCheck(pos.func_177982_a(-2, 0, 0))) {
                    placeList.add(pos.func_177982_a(-3, 0, 0));
                    placeList.add(pos.func_177982_a(-3, 1, 0));
                }
            }
            if (this.placeID == 6) {
                placeList.add(pos.func_177982_a(-1, 0, 0));
                placeList.add(pos.func_177982_a(-2, 1, 0));
                placeList.add(pos.func_177982_a(-3, 0, 0));
            }
            if (this.placeID == 10) {
                placeList.add(pos.func_177982_a(-1, 0, 0));
                placeList.add(pos.func_177982_a(-2, 1, 0));
            }
            if (this.placeID == 15 || this.placeID == 16) {
                placeList.add(pos.func_177982_a(-1, 0, 0));
            }
        }
        if (AntiCity.noHard(this.getBlock(pos.func_177982_a(0, 0, 1)).func_177230_c())) {
            if (this.placeID == 3) {
                placeList.add(pos.func_177982_a(0, 0, 2));
                placeList.add(pos.func_177982_a(1, 0, 1));
                placeList.add(pos.func_177982_a(-1, 0, 1));
                placeList.add(pos.func_177982_a(0, 1, 1));
                if (AntiCity.EntityCheck(pos.func_177982_a(0, 0, 2))) {
                    placeList.add(pos.func_177982_a(0, 0, 3));
                    placeList.add(pos.func_177982_a(0, 1, 3));
                }
            }
            if (this.placeID == 7) {
                placeList.add(pos.func_177982_a(0, 0, 1));
                placeList.add(pos.func_177982_a(0, 1, 2));
                placeList.add(pos.func_177982_a(0, 0, 3));
            }
            if (this.placeID == 11) {
                placeList.add(pos.func_177982_a(0, 0, 1));
                placeList.add(pos.func_177982_a(0, 1, 2));
            }
            if (this.placeID == 13 || this.placeID == 15) {
                placeList.add(pos.func_177982_a(0, 0, 1));
            }
        }
        if (AntiCity.noHard(this.getBlock(pos.func_177982_a(0, 0, -1)).func_177230_c())) {
            if (this.placeID == 4) {
                placeList.add(pos.func_177982_a(0, 0, -2));
                placeList.add(pos.func_177982_a(1, 0, -1));
                placeList.add(pos.func_177982_a(-1, 0, -1));
                placeList.add(pos.func_177982_a(0, 1, -1));
                if (AntiCity.EntityCheck(pos.func_177982_a(0, 0, -2))) {
                    placeList.add(pos.func_177982_a(0, 0, -3));
                    placeList.add(pos.func_177982_a(0, 1, -3));
                }
            }
            if (this.placeID == 8) {
                placeList.add(pos.func_177982_a(0, 0, -1));
                placeList.add(pos.func_177982_a(0, 1, -2));
                placeList.add(pos.func_177982_a(0, 0, -3));
            }
            if (this.placeID == 12) {
                placeList.add(pos.func_177982_a(0, 0, -1));
                placeList.add(pos.func_177982_a(0, 1, -2));
            }
            if (this.placeID == 14 || this.placeID == 16) {
                placeList.add(pos.func_177982_a(0, 0, -1));
            }
        }
        this.placeID = 0;
        BlockPos instantPos = ModuleManager.isModuleEnabled(PacketMine.class) ? PacketMine.INSTANCE.packetPos : null;
        placeList.removeIf(blockPos -> AntiCity.PlayerCheck(blockPos) || !this.CanPlace((BlockPos)blockPos) || (Boolean)this.smart.getValue() != false && this.isPos2((BlockPos)blockPos, instantPos) || AntiCity.EntityCheck(blockPos));
        if (!placeList.isEmpty()) {
            InventoryUtil.run(obsidian, (Boolean)this.packetSwitch.getValue(), () -> {
                for (BlockPos blockPos : placeList) {
                    if (this.placed >= (Integer)this.bpt.getValue()) break;
                    BurrowUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    ++this.placed;
                }
            });
        }
    }

    public static boolean EntityCheck(BlockPos pos) {
        for (Entity entity : AntiCity.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity == null) continue;
            return true;
        }
        return false;
    }

    private IBlockState getBlock(BlockPos block) {
        if (block == null) {
            return null;
        }
        return AntiCity.mc.field_71441_e.func_180495_p(block);
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    public boolean CanPlace(BlockPos block) {
        for (EnumFacing face : EnumFacing.field_82609_l) {
            if (!AntiCity.isReplaceable(block) || BlockUtil.airBlocks.contains(this.getBlock(block.func_177972_a(face))) || !(AntiCity.mc.field_71439_g.func_174818_b(block) <= MathUtil.square(5.0))) continue;
            return true;
        }
        return false;
    }

    public static boolean isReplaceable(BlockPos pos) {
        return BlockUtil.getState(pos).func_185904_a().func_76222_j();
    }

    private boolean isAir(BlockPos block) {
        return AntiCity.mc.field_71441_e.func_180495_p(block).func_177230_c() == Blocks.field_150350_a;
    }

    public static boolean PlayerCheck(BlockPos pos) {
        for (Entity entity : AntiCity.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer)) continue;
            return true;
        }
        return false;
    }
}
