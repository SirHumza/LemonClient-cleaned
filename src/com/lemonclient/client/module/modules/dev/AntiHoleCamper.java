/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemPiston
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.dev;

import com.google.common.collect.ImmutableMap;
import com.lemonclient.api.event.Phase;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPiston;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AntiHoleCamper", category=Category.Dev, priority=1000)
public class AntiHoleCamper
extends Module {
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 20);
    BooleanSetting pause = this.registerBoolean("Pause When Move", true);
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("Block", "Torch", "Both"), "Block");
    IntegerSetting range = this.registerInteger("Range", 6, 0, 10);
    BooleanSetting look = this.registerBoolean("Looking Target", false);
    BooleanSetting ground = this.registerBoolean("OnGround Check", true);
    BooleanSetting box = this.registerBoolean("Entity Box", true);
    BooleanSetting hole = this.registerBoolean("Double Hole Check", false);
    BooleanSetting pushCheck = this.registerBoolean("Push Check", false);
    BooleanSetting headCheck = this.registerBoolean("Head Check", false);
    BooleanSetting breakRedstone = this.registerBoolean("Break Redstone", false);
    BooleanSetting pushedCheck = this.registerBoolean("Pushed Check", true, () -> (Boolean)this.breakRedstone.getValue());
    ModeSetting breakBlock = this.registerMode("Break Block", Arrays.asList("Normal", "Packet"), "Packet", () -> (Boolean)this.breakRedstone.getValue());
    BooleanSetting packetPiston = this.registerBoolean("Packet Place Piston", true);
    BooleanSetting packetRedstone = this.registerBoolean("Packet Place Redstone", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting block = this.registerBoolean("Place Block", true);
    BooleanSetting packet = this.registerBoolean("Packet Place", true, () -> (Boolean)this.block.getValue());
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting update = this.registerBoolean("Update Controller", true);
    BooleanSetting force = this.registerBoolean("Force Rotate", true);
    BooleanSetting strict = this.registerBoolean("Strict", true);
    BooleanSetting raytrace = this.registerBoolean("RayTrace", true);
    DoubleSetting maxSpeed = this.registerDouble("Max Target Speed", 5.0, 0.0, 50.0);
    BooleanSetting debug = this.registerBoolean("Debug Msg", true);
    ModeSetting disable = this.registerMode("Disable Mode", Arrays.asList("NoDisable", "Check", "AutoDisable"), "AutoDisable");
    IntegerSetting disableDelay = this.registerInteger("Disable Delay", 0, 0, 50);
    private final Timing timer = new Timing();
    BlockPos beforePlayerPos;
    BlockPos pistonPos;
    BlockPos redstonePos;
    PistonPos pos = null;
    boolean useBlock;
    boolean disabling;
    int redstoneSlot;
    int pistonSlot;
    int obsiSlot;
    int waited;
    int wait;
    int[] enemyCoordsInt;
    EntityPlayer aimTarget = null;
    Vec2f rotation;
    Vec3d[] sides = new Vec3d[]{new Vec3d(0.25, 0.0, 0.25), new Vec3d(-0.25, 0.0, 0.25), new Vec3d(0.25, 0.0, -0.25), new Vec3d(-0.25, 0.0, -0.25)};
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (event.getPhase() != Phase.PRE || this.rotation == null) {
            return;
        }
        PlayerPacket packet = new PlayerPacket((Module)this, new Vec2f(this.rotation.field_189982_i, PlayerPacketManager.INSTANCE.getServerSideRotation().field_189983_j));
        PlayerPacketManager.INSTANCE.addPacket(packet);
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (this.rotation != null && ((Boolean)this.force.getValue()).booleanValue()) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                ((CPacketPlayer.Rotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
            }
        }
    }, new Predicate[0]);

    private void switchTo(int slot, Runnable runnable) {
        int oldslot = AntiHoleCamper.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (slot < 9) {
            boolean packetSwitch = (Boolean)this.packetSwitch.getValue();
            if (packetSwitch) {
                AntiHoleCamper.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AntiHoleCamper.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            if (((Boolean)this.update.getValue()).booleanValue()) {
                AntiHoleCamper.mc.field_71442_b.func_78765_e();
            }
            runnable.run();
            if (packetSwitch) {
                AntiHoleCamper.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                AntiHoleCamper.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
            }
        }
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AntiHoleCamper.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private boolean airBlock(BlockPos pos) {
        return BlockUtil.canReplace(pos);
    }

    private boolean canPlacePiston(BlockPos pos, EnumFacing facing) {
        BlockPos p = pos.func_177972_a(facing);
        BlockPos push = pos.func_177967_a(facing, -1);
        double feetY = AntiHoleCamper.mc.field_71439_g.field_70163_u;
        return !((this.intersectsWithEntity(p) || !this.airBlock(p) || PlayerUtil.getDistanceI(p) < 1.4 + (double)p.func_177956_o() - feetY && (double)p.func_177956_o() > feetY + 1.0 || PlayerUtil.getDistanceI(p) < 2.4 + feetY - (double)p.func_177956_o() && (double)p.func_177956_o() < feetY || !BlockUtil.canPlaceWithoutBase(p, (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), true)) && (!this.isFacing(p, pos) || !(AntiHoleCamper.mc.field_71441_e.func_180495_p(p).func_177230_c() instanceof BlockPistonBase) && AntiHoleCamper.mc.field_71441_e.func_180495_p(p).func_177230_c() != Blocks.field_150331_J && AntiHoleCamper.mc.field_71441_e.func_180495_p(p).func_177230_c() != Blocks.field_150320_F) || (Boolean)this.hole.getValue() != false && !this.airBlock(push) || (Boolean)this.pushCheck.getValue() != false && (!this.airBlock(push.func_177984_a()) || !this.airBlock(push.func_177981_b(2)) && !this.airBlock(push)));
    }

    public BlockPos getRedstonePos(BlockPos pistonPos) {
        BlockPos pos = this.hasRedstoneBlock(pistonPos);
        if (pos != null) {
            return pos;
        }
        List<Object> redstone = new ArrayList<BlockPos>();
        if (this.useBlock) {
            for (EnumFacing facing : EnumFacing.field_82609_l) {
                redstone.add(pistonPos.func_177972_a(facing));
            }
        } else {
            BlockPos[] offsets;
            for (BlockPos offs : offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)}) {
                for (int i = 0; i < 2; ++i) {
                    BlockPos torch = pistonPos.func_177979_c(i).func_177971_a((Vec3i)offs);
                    if (i == 1 && BlockUtil.isBlockUnSolid(torch.func_177984_a())) continue;
                    redstone.add(torch);
                }
            }
        }
        if ((redstone = redstone.stream().filter(p -> !ColorMain.INSTANCE.breakList.contains(p) && !this.intersectsWithEntity((BlockPos)p) && AntiHoleCamper.mc.field_71439_g.func_70011_f((double)p.func_177958_n() + 0.5, (double)p.func_177956_o() + 0.5, (double)p.func_177952_p() + 0.5) <= (double)((Integer)this.range.getValue()).intValue()).collect(Collectors.toList())).isEmpty()) {
            return null;
        }
        List hasBase = redstone.stream().filter(p -> BlockUtil.canPlaceWithoutBase(p, (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), false)).collect(Collectors.toList());
        if (hasBase.isEmpty()) {
            hasBase.addAll(redstone);
        }
        return hasBase.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)AntiHoleCamper.mc.field_71439_g).func_174818_b(arg_0))).orElse(null);
    }

    @Override
    public void onDisable() {
        if (!(!((Boolean)this.breakRedstone.getValue()).booleanValue() || this.redstonePos == null || this.airBlock(this.redstonePos) || ((Boolean)this.pushedCheck.getValue()).booleanValue() && AntiHoleCamper.mc.field_71441_e.func_180495_p(this.beforePlayerPos).func_177230_c() != Blocks.field_150332_K && AntiHoleCamper.mc.field_71441_e.func_180495_p(this.beforePlayerPos.func_177984_a()).func_177230_c() != Blocks.field_150332_K)) {
            this.doBreak(this.redstonePos);
        }
    }

    @Override
    public void onEnable() {
        this.disabling = false;
    }

    @Override
    public void onUpdate() {
        if (AntiHoleCamper.mc.field_71441_e == null || AntiHoleCamper.mc.field_71439_g == null || AntiHoleCamper.mc.field_71439_g.field_70128_L) {
            this.disable();
            return;
        }
        this.rotation = null;
        this.aimTarget = null;
        if (!(!((Boolean)this.breakRedstone.getValue()).booleanValue() || this.redstonePos == null || this.airBlock(this.redstonePos) || ((Boolean)this.pushedCheck.getValue()).booleanValue() && AntiHoleCamper.mc.field_71441_e.func_180495_p(this.beforePlayerPos).func_177230_c() != Blocks.field_150332_K && AntiHoleCamper.mc.field_71441_e.func_180495_p(this.beforePlayerPos.func_177984_a()).func_177230_c() != Blocks.field_150332_K)) {
            this.doBreak(this.redstonePos);
        }
        if (this.disabling && !((String)this.disable.getValue()).equals("NoDisable")) {
            if (this.wait++ >= (Integer)this.disableDelay.getValue()) {
                boolean placed = true;
                if (((Boolean)this.block.getValue()).booleanValue()) {
                    if (this.timer.passedMs(1000L)) {
                        this.switchTo(this.obsiSlot, () -> BlockUtil.placeBlock(this.beforePlayerPos, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue()));
                        this.timer.reset();
                        if (((String)this.disable.getValue()).equals("AutoDisable")) {
                            this.disable();
                            return;
                        }
                    }
                    placed = AntiHoleCamper.mc.field_71441_e.func_180495_p(this.beforePlayerPos).func_177230_c() == Blocks.field_150343_Z;
                } else if (((String)this.disable.getValue()).equals("AutoDisable")) {
                    this.disable();
                }
                if (((String)this.disable.getValue()).equals("Check") && placed) {
                    this.disable();
                }
                this.wait = 0;
                return;
            }
        } else {
            this.wait = 0;
        }
        if (this.waited++ < (Integer)this.delay.getValue() || MotionUtil.isMoving((EntityLivingBase)AntiHoleCamper.mc.field_71439_g) && ((Boolean)this.pause.getValue()).booleanValue()) {
            return;
        }
        this.waited = 0;
        this.redstoneSlot = this.pistonSlot = this.obsiSlot - 1;
        if (!this.ready()) {
            if (!((String)this.disable.getValue()).equals("NoDisable")) {
                this.disable();
            }
            return;
        }
        this.aimTarget = (Boolean)this.look.getValue() == false ? PlayerUtil.getNearestPlayer((double)((Integer)this.range.getValue()).intValue() + 1.5) : PlayerUtil.findLookingPlayer((double)((Integer)this.range.getValue()).intValue() + 1.5);
        this.pos = null;
        if (this.aimTarget != null) {
            PistonPos piston;
            BlockPos blockPos;
            Vec3d vec3d;
            ArrayList<PistonPos> list;
            if (LemonClient.speedUtil.getPlayerSpeed(this.aimTarget) > (Double)this.maxSpeed.getValue()) {
                return;
            }
            if (!this.aimTarget.field_70122_E && ((Boolean)this.ground.getValue()).booleanValue()) {
                return;
            }
            this.beforePlayerPos = new BlockPos(this.aimTarget.field_70165_t, this.aimTarget.field_70163_u, this.aimTarget.field_70161_v);
            this.enemyCoordsInt = new int[]{(int)this.aimTarget.field_70165_t, (int)this.aimTarget.field_70163_u, (int)this.aimTarget.field_70161_v};
            if (((Boolean)this.box.getValue()).booleanValue()) {
                list = new ArrayList<PistonPos>();
                for (Vec3d side : this.sides) {
                    vec3d = new Vec3d(this.aimTarget.field_70165_t + side.field_72450_a, this.aimTarget.field_70163_u, this.aimTarget.field_70161_v + side.field_72449_c);
                    blockPos = AntiHoleCamper.vec3toBlockPos(vec3d);
                    piston = this.getPos(blockPos, blockPos);
                    if (piston == null) continue;
                    list.add(piston);
                }
                this.pos = list.stream().filter(p -> p.getMaxRange() <= (double)((Integer)this.range.getValue()).intValue()).min(Comparator.comparing(PistonPos::getMaxRange)).orElse(null);
            } else {
                this.pos = this.getPos(this.beforePlayerPos, this.beforePlayerPos);
            }
            if (this.pos == null) {
                if (((Boolean)this.box.getValue()).booleanValue()) {
                    list = new ArrayList();
                    for (Vec3d side : this.sides) {
                        vec3d = new Vec3d(this.aimTarget.field_70165_t + side.field_72450_a, this.aimTarget.field_70163_u, this.aimTarget.field_70161_v + side.field_72449_c);
                        blockPos = AntiHoleCamper.vec3toBlockPos(vec3d);
                        piston = this.getPos(blockPos.func_177984_a(), blockPos);
                        if (piston == null) continue;
                        list.add(piston);
                    }
                    this.pos = list.stream().filter(p -> p.getMaxRange() <= (double)((Integer)this.range.getValue()).intValue()).min(Comparator.comparing(PistonPos::getMaxRange)).orElse(null);
                } else {
                    this.pos = this.getPos(this.beforePlayerPos.func_177984_a(), this.beforePlayerPos);
                }
            }
        } else {
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                MessageBus.sendClientDeleteMessage("Cant find target", Notification.Type.ERROR, "AntiCamp", 7);
            }
            if (!((String)this.disable.getValue()).equals("NoDisable")) {
                this.disable();
            }
        }
        if (this.pos != null) {
            if (this.redstonePos != null && !this.useBlock && AntiHoleCamper.mc.field_71441_e.func_180495_p(this.redstonePos.func_177977_b()).func_177230_c() == Blocks.field_150350_a) {
                BlockPos obsiPos = new BlockPos(this.redstonePos.field_177962_a, this.redstonePos.field_177960_b - 1, this.redstonePos.field_177961_c);
                this.switchTo(this.obsiSlot, () -> BlockUtil.placeBlock(obsiPos, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue()));
            }
            this.pistonPos = this.pos.piston;
            this.redstonePos = this.pos.redstone;
            this.beforePlayerPos = this.pos.calcPos;
            if (BurrowUtil.getFirstFacing(this.redstonePos) == null) {
                this.placePiston(this.pistonPos, this.beforePlayerPos);
                this.placeRedstone(this.redstonePos);
            } else {
                this.placeRedstone(this.redstonePos);
                this.placePiston(this.pistonPos, this.beforePlayerPos);
            }
            this.disabling = true;
        }
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    private void placePiston(BlockPos pistonPos, BlockPos targetPos) {
        if (!BlockUtil.isAir(pistonPos)) {
            return;
        }
        float[] angle = MathUtil.calcAngle(new Vec3d((double)pistonPos.field_177962_a, 0.0, (double)pistonPos.field_177961_c), new Vec3d((double)targetPos.field_177962_a, 0.0, (double)targetPos.field_177961_c));
        this.rotation = new Vec2f(angle[0] + 180.0f, angle[1]);
        AntiHoleCamper.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0] + 180.0f, angle[1], true));
        this.switchTo(this.pistonSlot, () -> {
            BlockUtil.placeBlock(pistonPos, false, (Boolean)this.packetPiston.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue());
            if (((Boolean)this.rotate.getValue()).booleanValue()) {
                EntityUtil.facePlacePos(pistonPos, (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue());
            }
        });
    }

    private void placeRedstone(BlockPos redstonePos) {
        this.switchTo(this.redstoneSlot, () -> BlockUtil.placeBlock(redstonePos, (Boolean)this.rotate.getValue(), (Boolean)this.packetRedstone.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue()));
    }

    private PistonPos getPos(BlockPos calcPos, BlockPos playerPos) {
        if (AntiHoleCamper.mc.field_71441_e.func_180495_p(calcPos).func_177230_c() == Blocks.field_150357_h || AntiHoleCamper.mc.field_71441_e.func_180495_p(calcPos).func_177230_c() == Blocks.field_150343_Z) {
            return null;
        }
        ArrayList<PistonPos> posList = new ArrayList<PistonPos>();
        if (((Boolean)this.headCheck.getValue()).booleanValue() && !this.airBlock(playerPos.func_177981_b(2))) {
            return null;
        }
        for (EnumFacing facing : EnumFacing.field_82609_l) {
            BlockPos pistonPos;
            BlockPos redstonePos;
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN || !this.canPlacePiston(calcPos, facing) || (redstonePos = this.getRedstonePos(pistonPos = calcPos.func_177972_a(facing))) == null || !BlockUtil.hasNeighbour(redstonePos) && !BlockUtil.hasNeighbour(pistonPos)) continue;
            posList.add(new PistonPos(pistonPos, redstonePos, calcPos));
        }
        return posList.stream().filter(p -> p.getMaxRange() <= (double)((Integer)this.range.getValue()).intValue()).min(Comparator.comparing(PistonPos::getMaxRange)).orElse(null);
    }

    public static BlockPos vec3toBlockPos(Vec3d vec3d) {
        return new BlockPos(Math.floor(vec3d.field_72450_a), (double)Math.round(vec3d.field_72448_b), Math.floor(vec3d.field_72449_c));
    }

    private boolean ready() {
        this.pistonSlot = AntiHoleCamper.findHotbarBlock((Block)Blocks.field_150331_J);
        if (this.pistonSlot == -1) {
            this.pistonSlot = AntiHoleCamper.findHotbarBlock((Block)Blocks.field_150320_F);
        }
        int n = this.redstoneSlot = !((String)this.mode.getValue()).equals("Torch") ? AntiHoleCamper.findHotbarBlock(Blocks.field_150451_bX) : AntiHoleCamper.findHotbarBlock(Blocks.field_150429_aA);
        if (((String)this.mode.getValue()).equals("Both") && this.redstoneSlot == -1) {
            this.redstoneSlot = AntiHoleCamper.findHotbarBlock(Blocks.field_150429_aA);
        }
        this.obsiSlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (this.redstoneSlot == -1) {
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                MessageBus.sendClientDeleteMessage("Cant find Redstone", Notification.Type.ERROR, "AntiCamp", 7);
            }
            return false;
        }
        boolean bl = this.useBlock = this.redstoneSlot == AntiHoleCamper.findHotbarBlock(Blocks.field_150451_bX);
        if ((!this.useBlock || ((Boolean)this.block.getValue()).booleanValue()) && this.obsiSlot == -1) {
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                MessageBus.sendClientDeleteMessage("Cant find Obsidian", Notification.Type.ERROR, "AntiCamp", 7);
            }
            return false;
        }
        if (BurrowUtil.findHotbarBlock(ItemPiston.class) == -1) {
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                MessageBus.sendClientDeleteMessage("Cant find Piston", Notification.Type.ERROR, "AntiCamp", 7);
            }
            return false;
        }
        return true;
    }

    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AntiHoleCamper.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || ((ItemBlock)stack.func_77973_b()).func_179223_d() != blockIn) continue;
            return i;
        }
        return -1;
    }

    private void doBreak(BlockPos pos) {
        if (((Boolean)this.swing.getValue()).booleanValue()) {
            AntiHoleCamper.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        if (((String)this.breakBlock.getValue()).equals("Packet")) {
            AntiHoleCamper.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
            AntiHoleCamper.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
        } else {
            AntiHoleCamper.mc.field_71442_b.func_180512_c(pos, EnumFacing.UP);
        }
    }

    public boolean isFacing(BlockPos pos, BlockPos facingPos) {
        ImmutableMap properties = AntiHoleCamper.mc.field_71441_e.func_180495_p(pos).func_177228_b();
        for (IProperty prop : properties.keySet()) {
            BlockPos pushPos;
            if (prop.func_177699_b() != EnumFacing.class || !prop.func_177701_a().equals("facing") && !prop.func_177701_a().equals("rotation") || !this.isPos2(facingPos, pushPos = pos.func_177972_a((EnumFacing)properties.get((Object)prop)))) continue;
            return true;
        }
        return false;
    }

    public BlockPos hasRedstoneBlock(BlockPos pos) {
        BlockPos[] offsets;
        ArrayList<BlockPos> redstone = new ArrayList<BlockPos>();
        for (BlockPos redstonePos : offsets = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)}) {
            redstone.add(pos.func_177971_a((Vec3i)redstonePos));
        }
        if (this.useBlock) {
            redstone.add(pos.func_177982_a(0, 1, 0));
        }
        return redstone.stream().filter(p -> BlockUtil.getBlock(p) == Blocks.field_150429_aA || BlockUtil.getBlock(p) == Blocks.field_150451_bX).min(Comparator.comparing(PlayerUtil::getDistanceI)).orElse(null);
    }

    static class PistonPos {
        public BlockPos piston;
        public BlockPos redstone;
        public BlockPos calcPos;

        public PistonPos(BlockPos pistonPos, BlockPos redstonePos, BlockPos pos) {
            this.piston = pistonPos;
            this.redstone = redstonePos;
            this.calcPos = pos;
        }

        public double getMaxRange() {
            if (this.piston == null || this.redstone == null) {
                return 999999.0;
            }
            return Math.max(PlayerUtil.getDistance(this.piston), PlayerUtil.getDistance(this.redstone));
        }
    }
}
