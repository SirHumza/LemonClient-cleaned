/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketBlockBreakAnim
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoTrap", category=Category.Combat)
public class AutoTrap
extends Module {
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 20);
    IntegerSetting range = this.registerInteger("Range", 5, 0, 10);
    IntegerSetting bpt = this.registerInteger("BlocksPerTick", 4, 0, 20);
    BooleanSetting top = this.registerBoolean("Top+", false);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting packet = this.registerBoolean("Packet Place", false);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", false);
    BooleanSetting detect = this.registerBoolean("Detect Break", false);
    BooleanSetting self = this.registerBoolean("Self Break", false, () -> (Boolean)this.detect.getValue());
    BooleanSetting bed = this.registerBoolean("Bedrock", false, () -> (Boolean)this.detect.getValue());
    BooleanSetting pause = this.registerBoolean("BedrockHole", true);
    int ob;
    int waited;
    int placed;
    BlockPos trapPos;
    BlockPos player;
    List<BlockPos> posList = new ArrayList<BlockPos>();
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
    BlockPos[] blocks = new BlockPos[]{new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};
    BlockPos breakPos;
    private int place;
    @EventHandler
    private final Listener<PacketEvent.PostSend> listener = new Listener<PacketEvent.PostSend>(event -> {
        CPacketPlayerDigging packet;
        if (this.player == null || !((Boolean)this.self.getValue()).booleanValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            BlockPos ab = packet.func_179715_a();
            this.breakPos = packet.func_179715_a();
            if (ab.equals((Object)this.player.func_177982_a(0, 1, 0))) {
                this.place = 17;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 1, 0))) {
                this.place = 18;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 1, 0))) {
                this.place = 19;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 1, 1))) {
                this.place = 20;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 1, -1))) {
                this.place = 21;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 2, 0))) {
                this.place = 22;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 0, 0))) {
                this.place = 1;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 0, 0))) {
                this.place = 2;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, 1))) {
                this.place = 3;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, -1))) {
                this.place = 4;
            }
            if (ab.equals((Object)this.player.func_177982_a(2, 0, 0))) {
                this.place = 5;
            }
            if (ab.equals((Object)this.player.func_177982_a(-2, 0, 0))) {
                this.place = 6;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, 2))) {
                this.place = 7;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, -2))) {
                this.place = 8;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 1, 0))) {
                this.place = 9;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 1, 0))) {
                this.place = 10;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 1, 1))) {
                this.place = 11;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 1, -1))) {
                this.place = 12;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 0, 1))) {
                this.place = 13;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 0, -1))) {
                this.place = 14;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 0, 1))) {
                this.place = 15;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 0, -1))) {
                this.place = 16;
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (AutoTrap.mc.field_71441_e == null || AutoTrap.mc.field_71439_g == null || this.player == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            SPacketBlockBreakAnim packet = (SPacketBlockBreakAnim)event.getPacket();
            BlockPos ab = packet.func_179821_b();
            this.breakPos = packet.func_179821_b();
            if (ab.equals((Object)this.player.func_177982_a(0, 1, 0))) {
                this.place = 17;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 0, 0))) {
                this.place = 1;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 0, 0))) {
                this.place = 2;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, 1))) {
                this.place = 3;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, -1))) {
                this.place = 4;
            }
            if (ab.equals((Object)this.player.func_177982_a(2, 0, 0))) {
                this.place = 5;
            }
            if (ab.equals((Object)this.player.func_177982_a(-2, 0, 0))) {
                this.place = 6;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, 2))) {
                this.place = 7;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 0, -2))) {
                this.place = 8;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 1, 0))) {
                this.place = 9;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 1, 0))) {
                this.place = 10;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 1, 1))) {
                this.place = 11;
            }
            if (ab.equals((Object)this.player.func_177982_a(0, 1, -1))) {
                this.place = 12;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 0, 1))) {
                this.place = 13;
            }
            if (ab.equals((Object)this.player.func_177982_a(1, 0, -1))) {
                this.place = 14;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 0, 1))) {
                this.place = 15;
            }
            if (ab.equals((Object)this.player.func_177982_a(-1, 0, -1))) {
                this.place = 16;
            }
        }
    }, new Predicate[0]);

    public static boolean isPlayerInHole(EntityPlayer target) {
        BlockPos blockPos = AutoTrap.getLocalPlayerPosFloored(target);
        HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(blockPos, true, true, false);
        HoleUtil.HoleType holeType = holeInfo.getType();
        return holeType == HoleUtil.HoleType.SINGLE;
    }

    public static BlockPos getLocalPlayerPosFloored(EntityPlayer target) {
        return new BlockPos(target.func_174791_d());
    }

    @Override
    public void onUpdate() {
        if (AutoTrap.mc.field_71441_e == null || AutoTrap.mc.field_71439_g == null || AutoTrap.mc.field_71439_g.field_70128_L) {
            this.trapPos = null;
            this.posList.clear();
            return;
        }
        this.placed = 0;
        if ((Integer)this.delay.getValue() > 0) {
            if (this.waited++ < (Integer)this.delay.getValue()) {
                return;
            }
            this.waited = 0;
        }
        if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        EntityPlayer target = PlayerUtil.getNearestPlayer(((Integer)this.range.getValue()).intValue());
        if (target == null) {
            return;
        }
        if (AutoTrap.mc.field_71439_g.func_70032_d((Entity)target) > (float)((Integer)this.range.getValue()).intValue() || !AutoTrap.isPlayerInHole(target)) {
            this.posList.clear();
        } else {
            BlockPos pos2 = EntityUtil.getEntityPos((Entity)target);
            this.addBlock(pos2);
        }
        this.ob = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (this.ob == -1) {
            return;
        }
        this.posList.removeIf(pos -> {
            if (!BlockUtil.isAir(pos)) {
                return true;
            }
            return this.intersectsWithEntity((BlockPos)pos);
        });
        if (!this.posList.isEmpty()) {
            InventoryUtil.run(this.ob, (Boolean)this.packetSwitch.getValue(), () -> {
                for (BlockPos block : this.posList) {
                    if (this.placed > (Integer)this.bpt.getValue()) break;
                    BurrowUtil.placeBlock(block, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    ++this.placed;
                }
            });
        }
        this.player = EntityUtil.getEntityPos((Entity)target).func_177984_a();
        this.antiCity(this.player);
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoTrap.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private void addBlock(BlockPos pos) {
        if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        ArrayList<BlockPos> blocklist = new ArrayList<BlockPos>();
        blocklist.add(pos.func_177982_a(0, 2, 0));
        if (((Boolean)this.top.getValue()).booleanValue()) {
            blocklist.add(pos.func_177982_a(0, 3, 0));
        }
        int obby = 0;
        for (BlockPos side : this.sides) {
            if (AutoTrap.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)side)).func_177230_c() == Blocks.field_150357_h && !((Boolean)this.bed.getValue()).booleanValue()) continue;
            for (BlockPos blockPos : this.blocks) {
                blocklist.add(pos.func_177971_a((Vec3i)side).func_177971_a((Vec3i)blockPos));
            }
            ++obby;
        }
        if (obby == 0 && !((Boolean)this.pause.getValue()).booleanValue()) {
            return;
        }
        this.posList.addAll(blocklist);
    }

    private boolean noHard(Block block) {
        return block != Blocks.field_150357_h || (Boolean)this.bed.getValue() != false;
    }

    public void antiCity(BlockPos pos) {
        int obsidian = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (obsidian == -1) {
            return;
        }
        if (pos == null) {
            return;
        }
        pos = new BlockPos((double)pos.field_177962_a, (double)pos.field_177960_b + 0.2, (double)pos.field_177961_c);
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        if (this.breakPos != null) {
            if ((this.breakPos.equals((Object)pos.func_177982_a(1, 0, 0)) || this.breakPos.equals((Object)pos.func_177982_a(1, 1, 0))) && this.isAir(pos.func_177982_a(1, 0, 0)) && this.isAir(pos.func_177982_a(1, 1, 0))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(1, 0, 0))) {
                    list.add(pos.func_177982_a(1, 1, 0));
                } else {
                    list.add(pos.func_177982_a(1, 0, 0));
                }
            }
            if ((this.breakPos.equals((Object)pos.func_177982_a(-1, 0, 0)) || this.breakPos.equals((Object)pos.func_177982_a(-1, 1, 0))) && this.isAir(pos.func_177982_a(-1, 0, 0)) && this.isAir(pos.func_177982_a(-1, 1, 0))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(-1, 0, 0))) {
                    list.add(pos.func_177982_a(-1, 1, 0));
                } else {
                    list.add(pos.func_177982_a(-1, 0, 0));
                }
            }
            if ((this.breakPos.equals((Object)pos.func_177982_a(0, 0, 1)) || this.breakPos.equals((Object)pos.func_177982_a(0, 1, 1))) && this.isAir(pos.func_177982_a(0, 0, 1)) && this.isAir(pos.func_177982_a(0, 1, 1))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(0, 0, 1))) {
                    list.add(pos.func_177982_a(0, 1, 1));
                } else {
                    list.add(pos.func_177982_a(0, 0, 1));
                }
            }
            if ((this.breakPos.equals((Object)pos.func_177982_a(0, 0, -1)) || this.breakPos.equals((Object)pos.func_177982_a(0, 1, -1))) && this.isAir(pos.func_177982_a(0, 0, -1)) && this.isAir(pos.func_177982_a(0, 1, -1))) {
                if (this.breakPos.equals((Object)pos.func_177982_a(0, 0, -1))) {
                    list.add(pos.func_177982_a(0, 1, -1));
                } else {
                    list.add(pos.func_177982_a(0, 0, -1));
                }
            }
        }
        if (this.noHard(this.getBlock(pos.func_177982_a(1, 0, 0)).func_177230_c())) {
            if (this.place == 1) {
                list.add(pos.func_177982_a(2, 0, 0));
                list.add(pos.func_177982_a(1, 0, 1));
                list.add(pos.func_177982_a(1, 0, -1));
                list.add(pos.func_177982_a(1, 1, 0));
            }
            if (this.place == 5 || this.place == 9 || this.place == 13 || this.place == 14) {
                list.add(pos.func_177982_a(1, 0, 0));
            }
        }
        if (this.noHard(this.getBlock(pos.func_177982_a(-1, 0, 0)).func_177230_c())) {
            if (this.place == 2) {
                list.add(pos.func_177982_a(-2, 0, 0));
                list.add(pos.func_177982_a(-1, 0, 1));
                list.add(pos.func_177982_a(-1, 0, -1));
                list.add(pos.func_177982_a(-1, 1, 0));
            }
            if (this.place == 6 || this.place == 10 || this.place == 15 || this.place == 16) {
                list.add(pos.func_177982_a(-1, 0, 0));
            }
        }
        if (this.noHard(this.getBlock(pos.func_177982_a(0, 0, 1)).func_177230_c())) {
            if (this.place == 3) {
                list.add(pos.func_177982_a(0, 0, 2));
                list.add(pos.func_177982_a(1, 0, 1));
                list.add(pos.func_177982_a(-1, 0, 1));
                list.add(pos.func_177982_a(0, 1, 1));
            }
            if (this.place == 7 || this.place == 11 || this.place == 13 || this.place == 15) {
                list.add(pos.func_177982_a(0, 0, 1));
            }
        }
        if (this.noHard(this.getBlock(pos.func_177982_a(0, 0, -1)).func_177230_c())) {
            if (this.place == 4) {
                list.add(pos.func_177982_a(0, 0, -2));
                list.add(pos.func_177982_a(1, 0, -1));
                list.add(pos.func_177982_a(-1, 0, -1));
                list.add(pos.func_177982_a(0, 1, -1));
            }
            if (this.place == 8 || this.place == 12 || this.place == 14 || this.place == 16) {
                list.add(pos.func_177982_a(0, 0, -1));
            }
        }
        if (this.noHard(this.getBlock(pos.func_177982_a(0, 1, 0)).func_177230_c())) {
            if (this.place == 17) {
                list.add(pos.func_177982_a(0, 2, 0));
                list.add(pos.func_177982_a(0, 1, -1));
                list.add(pos.func_177982_a(0, 1, 1));
                list.add(pos.func_177982_a(1, 1, 0));
                list.add(pos.func_177982_a(-1, 1, 0));
            }
            if (this.place == 9 || this.place == 10 || this.place == 11 || this.place == 12 || this.place > 17) {
                list.add(pos.func_177982_a(0, 1, 0));
            }
        }
        this.place = 0;
        list.removeIf(p -> AutoTrap.PlayerCheck(p) || !this.CanPlace((BlockPos)p));
        if (!list.isEmpty()) {
            InventoryUtil.run(obsidian, (Boolean)this.packetSwitch.getValue(), () -> {
                for (BlockPos blockPos : list) {
                    if (this.placed >= (Integer)this.bpt.getValue()) break;
                    BurrowUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    ++this.placed;
                }
            });
        }
    }

    private IBlockState getBlock(BlockPos block) {
        if (block == null) {
            return null;
        }
        return AutoTrap.mc.field_71441_e.func_180495_p(block);
    }

    public boolean CanPlace(BlockPos block) {
        for (EnumFacing face : EnumFacing.field_82609_l) {
            if (!AutoTrap.isReplaceable(block) || BlockUtil.airBlocks.contains(this.getBlock(block.func_177972_a(face))) || !(AutoTrap.mc.field_71439_g.func_174818_b(block) <= MathUtil.square(5.0))) continue;
            return true;
        }
        return false;
    }

    public static boolean isReplaceable(BlockPos pos) {
        return BlockUtil.getState(pos).func_185904_a().func_76222_j();
    }

    private boolean isAir(BlockPos block) {
        return AutoTrap.mc.field_71441_e.func_180495_p(block).func_177230_c() == Blocks.field_150350_a;
    }

    public static boolean PlayerCheck(BlockPos pos) {
        for (Entity entity : AutoTrap.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer)) continue;
            return true;
        }
        return false;
    }
}
