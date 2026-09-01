/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockTrapDoor
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PhaseUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoPhase", category=Category.Combat)
public class AutoPhase
extends Module {
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("5b", "Jp"), "5b");
    ModeSetting bound = this.registerMode("Bounds", PhaseUtil.bound, "Min", () -> ((String)this.mode.getValue()).equals("5b"));
    BooleanSetting twoBeePvP = this.registerBoolean("2b2tpvp", false, () -> ((String)this.mode.getValue()).equals("5b"));
    BooleanSetting update = this.registerBoolean("Update Pos", false, () -> ((String)this.mode.getValue()).equals("5b"));
    BooleanSetting packet = this.registerBoolean("Packet Place", true, () -> ((String)this.mode.getValue()).equals("Jp"));
    BooleanSetting swing = this.registerBoolean("Swing", true, () -> ((String)this.mode.getValue()).equals("Jp"));
    BooleanSetting mine = this.registerBoolean("Mine", true, () -> ((String)this.mode.getValue()).equals("Jp"));
    BooleanSetting burrow = this.registerBoolean("Try Burrow", true, () -> ((String)this.mode.getValue()).equals("Jp"));
    BooleanSetting doubleBurrow = this.registerBoolean("Double", true, () -> ((String)this.mode.getValue()).equals("Jp") && (Boolean)this.burrow.getValue() != false);
    IntegerSetting entity = this.registerInteger("Entity Time", 5, 0, 10, () -> ((String)this.mode.getValue()).equals("Jp"));
    BooleanSetting ignoreCrystal = this.registerBoolean("Ignore Crystal", true, () -> ((String)this.mode.getValue()).equals("Jp"));
    IntegerSetting checkDelay = this.registerInteger("Check Time", 50, 0, 500, () -> ((String)this.mode.getValue()).equals("Jp"));
    BlockPos originalPos;
    boolean down;
    Timing timing = new Timing();
    Timing timer = new Timing();
    int tpid = 0;
    List<Block> blockList = Arrays.asList(Blocks.field_150357_h, Blocks.field_150343_Z, Blocks.field_150477_bB, Blocks.field_150467_bQ);
    BlockPos[] sides = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0)};
    BlockPos[] height = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 1, 0)};
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.tpid = ((SPacketPlayerPosLook)event.getPacket()).field_186966_g;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketPlayer.PositionRotation || event.getPacket() instanceof CPacketPlayer.Position) {
            ++this.tpid;
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        if (((String)this.mode.getValue()).equals("Jp")) {
            this.down = true;
            this.originalPos = PlayerUtil.getPlayerPos();
            this.originalPos = new BlockPos((double)this.originalPos.field_177962_a, (double)this.originalPos.field_177960_b + 0.2, (double)this.originalPos.field_177961_c);
            if (BurrowUtil.findHotbarBlock(BlockTrapDoor.class) == -1 || !AutoPhase.mc.field_71441_e.func_175623_d(this.originalPos)) {
                this.disable();
                return;
            }
            AutoPhase.mc.field_71439_g.func_70107_b(AutoPhase.mc.field_71439_g.field_70165_t, (double)((int)AutoPhase.mc.field_71439_g.field_70163_u), AutoPhase.mc.field_71439_g.field_70161_v);
            this.timing.reset();
            this.timer.reset();
            this.down = false;
        }
    }

    @Override
    public void onDisable() {
        if (((String)this.mode.getValue()).equals("Jp") && ModuleManager.isModuleEnabled(PacketMine.class)) {
            PacketMine.INSTANCE.lastBlock = null;
        }
    }

    @Override
    public void onUpdate() {
        if (((String)this.mode.getValue()).equals("Jp")) {
            this.trapdoor();
        } else {
            this.packetFly();
        }
    }

    void packetFly() {
        double[] clip = MotionUtil.forward(0.0624);
        if (AutoPhase.mc.field_71439_g.field_70122_E) {
            this.tp(0.0, -0.0624, 0.0, false);
        } else {
            this.tp(clip[0], 0.0, clip[1], true);
        }
        this.disable();
    }

    void tp(double x, double y, double z, boolean onGround) {
        double[] dir = MotionUtil.forward(-0.0312);
        if (((Boolean)this.twoBeePvP.getValue()).booleanValue()) {
            AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoPhase.mc.field_71439_g.field_70165_t + dir[0], AutoPhase.mc.field_71439_g.field_70163_u, AutoPhase.mc.field_71439_g.field_70161_v + dir[1], onGround));
        }
        AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(((Boolean)this.twoBeePvP.getValue() != false ? x / 2.0 : x) + AutoPhase.mc.field_71439_g.field_70165_t, y + AutoPhase.mc.field_71439_g.field_70163_u, ((Boolean)this.twoBeePvP.getValue() != false ? z / 2.0 : z) + AutoPhase.mc.field_71439_g.field_70161_v, onGround));
        AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.tpid - 1));
        AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.tpid));
        AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.tpid + 1));
        PhaseUtil.doBounds((String)this.bound.getValue(), true);
        if (((Boolean)this.update.getValue()).booleanValue()) {
            AutoPhase.mc.field_71439_g.func_70107_b(x, y, z);
        }
    }

    private void trapdoor() {
        if (AutoPhase.mc.field_71441_e == null || AutoPhase.mc.field_71439_g == null || AutoPhase.mc.field_71439_g.field_70128_L || this.originalPos == null) {
            this.disable();
            return;
        }
        if (!this.down) {
            if (BurrowUtil.findHotbarBlock(BlockTrapDoor.class) == -1) {
                this.disable();
                return;
            }
            if (this.intersectsWithEntity(this.originalPos) && this.timer.passedS(((Integer)this.entity.getValue()).intValue())) {
                this.disable();
                return;
            }
            EnumFacing facing = BurrowUtil.getTrapdoorFacing(this.originalPos);
            BlockPos burrowPos = null;
            for (BlockPos side : this.sides) {
                BlockPos blockPos = PlayerUtil.getPlayerPos().func_177971_a((Vec3i)side);
                if (BlockUtil.getBlock(blockPos) != Blocks.field_150357_h && BlockUtil.getBlock(blockPos) != Blocks.field_150343_Z) continue;
                burrowPos = blockPos;
                break;
            }
            int obsidian = BurrowUtil.findHotbarBlock(BlockObsidian.class);
            if (facing == null || burrowPos == null && ((Boolean)this.burrow.getValue()).booleanValue()) {
                if (((Boolean)this.burrow.getValue()).booleanValue()) {
                    boolean placed = false;
                    if (obsidian != -1) {
                        for (BlockPos side : this.sides) {
                            BlockPos blockPos = PlayerUtil.getPlayerPos().func_177971_a((Vec3i)side);
                            if (this.intersectsWithEntity(blockPos) || !BlockUtil.hasNeighbour(blockPos)) continue;
                            AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(obsidian));
                            BurrowUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, false, false, false, false);
                            if (((Boolean)this.doubleBurrow.getValue()).booleanValue()) {
                                BurrowUtil.placeBlock(blockPos.func_177984_a(), EnumHand.MAIN_HAND, false, false, false, false);
                            }
                            placed = true;
                            break;
                        }
                    }
                    if (!placed) {
                        this.disable();
                        return;
                    }
                } else {
                    this.disable();
                }
                return;
            }
            if (((Boolean)this.burrow.getValue()).booleanValue() && ((Boolean)this.doubleBurrow.getValue()).booleanValue() && BlockUtil.isAir(burrowPos.func_177984_a())) {
                AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(obsidian));
                BurrowUtil.placeBlock(burrowPos.func_177984_a(), EnumHand.MAIN_HAND, false, false, false, false);
            }
            BlockPos neighbour = this.originalPos.func_177972_a(facing);
            EnumFacing opposite = facing.func_176734_d();
            double x = AutoPhase.mc.field_71439_g.field_70165_t;
            double y = AutoPhase.mc.field_71439_g.field_70163_u;
            double z = AutoPhase.mc.field_71439_g.field_70161_v;
            AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y + (double)0.2f, z, AutoPhase.mc.field_71439_g.field_70122_E));
            AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(BurrowUtil.findHotbarBlock(BlockTrapDoor.class)));
            boolean sneak = false;
            if ((BlockUtil.blackList.contains(AutoPhase.mc.field_71441_e.func_180495_p(neighbour).func_177230_c()) || BlockUtil.shulkerList.contains(AutoPhase.mc.field_71441_e.func_180495_p(neighbour).func_177230_c())) && !AutoPhase.mc.field_71439_g.func_70093_af()) {
                AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoPhase.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                AutoPhase.mc.field_71439_g.func_70095_a(true);
                sneak = true;
            }
            AutoPhase.rightClickBlock(neighbour, opposite, new Vec3d(0.5, 0.8, 0.5), (Boolean)this.packet.getValue(), (Boolean)this.swing.getValue());
            AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(AutoPhase.mc.field_71439_g.field_71071_by.field_70461_c));
            AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, AutoPhase.mc.field_71439_g.field_70122_E));
            if (sneak) {
                AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoPhase.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                AutoPhase.mc.field_71439_g.func_70095_a(false);
            }
            if (((Boolean)this.burrow.getValue()).booleanValue()) {
                if (burrowPos == null) {
                    return;
                }
                AutoPhase.mc.field_71439_g.func_70107_b((double)burrowPos.field_177962_a + 0.5, (double)burrowPos.field_177960_b, (double)burrowPos.field_177961_c + 0.5);
                this.disable();
            } else {
                int bedrocks = 0;
                int blocks = 0;
                double xAdd = 0.0;
                double zAdd = 0.0;
                for (BlockPos side : this.sides) {
                    for (BlockPos add : this.sides) {
                        if (this.isPos2(this.originalPos, this.originalPos.func_177971_a((Vec3i)side).func_177971_a((Vec3i)add)) || this.isPos2(side, add)) continue;
                        int bedrock = 0;
                        int block = 0;
                        BlockPos sidePos = this.originalPos.func_177971_a((Vec3i)side);
                        BlockPos addPos = this.originalPos.func_177971_a((Vec3i)add);
                        BlockPos addSide = this.originalPos.func_177971_a((Vec3i)side).func_177971_a((Vec3i)add);
                        for (BlockPos high : this.height) {
                            Block sideState = AutoPhase.mc.field_71441_e.func_180495_p(sidePos.func_177971_a((Vec3i)high)).func_177230_c();
                            Block addState = AutoPhase.mc.field_71441_e.func_180495_p(addPos.func_177971_a((Vec3i)high)).func_177230_c();
                            Block addSideState = AutoPhase.mc.field_71441_e.func_180495_p(addSide.func_177971_a((Vec3i)high)).func_177230_c();
                            if (this.blockList.contains(sideState)) {
                                block += 3;
                            }
                            if (sideState == Blocks.field_150357_h) {
                                bedrock += 3;
                            }
                            if (this.blockList.contains(addState)) {
                                block += 3;
                            }
                            if (addState == Blocks.field_150357_h) {
                                bedrock += 3;
                            }
                            if (this.blockList.contains(addSideState)) {
                                ++block;
                            }
                            if (addSideState != Blocks.field_150357_h) continue;
                            ++bedrock;
                        }
                        boolean shouldSet = false;
                        if (block > blocks) {
                            shouldSet = true;
                        } else if (block == blocks && bedrock > bedrocks) {
                            shouldSet = true;
                        }
                        if (!shouldSet) continue;
                        bedrocks = bedrock;
                        blocks = block;
                        xAdd = this.getAdd(side.field_177962_a + add.field_177962_a);
                        zAdd = this.getAdd(side.field_177961_c + add.field_177961_c);
                    }
                }
                AutoPhase.mc.field_71439_g.func_70107_b((double)this.originalPos.func_177958_n() + xAdd, (double)this.originalPos.func_177956_o(), (double)this.originalPos.func_177952_p() + zAdd);
                AutoPhase.mc.field_71439_g.field_70159_w = 0.0;
                AutoPhase.mc.field_71439_g.field_70179_y = 0.0;
                if (AutoPhase.mc.field_71439_g.field_70165_t == (double)this.originalPos.func_177958_n() + xAdd && AutoPhase.mc.field_71439_g.field_70161_v == (double)this.originalPos.func_177952_p() + zAdd && !AutoPhase.mc.field_71441_e.func_175623_d(this.originalPos) && this.timing.passedMs(((Integer)this.checkDelay.getValue()).intValue())) {
                    this.down = true;
                }
            }
        }
        if (this.down) {
            this.timing.reset();
            AutoPhase.mc.field_71439_g.field_70159_w = 0.0;
            AutoPhase.mc.field_71439_g.field_70179_y = 0.0;
            if (((Boolean)this.mine.getValue()).booleanValue()) {
                AutoPhase.mc.field_71442_b.func_180512_c(this.originalPos, EnumFacing.UP);
            } else {
                this.disable();
            }
            if (AutoPhase.mc.field_71441_e.func_175623_d(this.originalPos)) {
                this.disable();
            }
        }
    }

    private double getAdd(int pos) {
        if (pos == 1) {
            return 0.99999999;
        }
        return 0.0;
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    public static void rightClickBlock(BlockPos pos, EnumFacing facing, Vec3d hVec, boolean packet, boolean swing) {
        Vec3d hitVec = new Vec3d((Vec3i)pos).func_178787_e(hVec).func_178787_e(new Vec3d(facing.func_176730_m()).func_186678_a(0.5));
        if (packet) {
            AutoPhase.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
        } else {
            AutoPhase.mc.field_71442_b.func_187099_a(AutoPhase.mc.field_71439_g, AutoPhase.mc.field_71441_e, pos, facing, hitVec, EnumHand.MAIN_HAND);
        }
        if (swing) {
            AutoPhase.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction) {
        float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
        float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
        float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
        AutoPhase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoPhase.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity instanceof EntityEnderCrystal && ((Boolean)this.ignoreCrystal.getValue()).booleanValue() || entity == AutoPhase.mc.field_71439_g || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }
}
