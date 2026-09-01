/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
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
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.CrystalUtil;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lemonclient.client.module.modules.qwq.AutoEz;
import com.lemonclient.mixin.mixins.accessor.AccessorCPacketVehicleMove;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="PistonAura", category=Category.Dev)
public class PistonAura
extends Module {
    public static PistonAura INSTANCE;
    public boolean autoCrystal;
    ModeSetting page = this.registerMode("Page", Arrays.asList("Calc", "General", "Render"), "Calc");
    IntegerSetting maxTarget = this.registerInteger("Max Target", 1, 1, 10, () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting range = this.registerDouble("Range", 6.0, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting maxY = this.registerInteger("MaxY", 3, 1, 5, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting delay = this.registerInteger("Delay", 20, 0, 100, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting baseDelay = this.registerInteger("Base Delay", 0, 0, 100, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting startBreakDelay = this.registerInteger("Start Break Delay", 0, 0, 100, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting breakDelay = this.registerInteger("Break Delay", 0, 0, 100, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting floor = this.registerBoolean("Floor", false, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting alwaysCalc = this.registerBoolean("Loop Calc", false, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting pistonCheck = this.registerBoolean("Piston Check", false, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting entityCheck = this.registerBoolean("Crystal Check", false, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting base = this.registerBoolean("Base", true, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting push = this.registerBoolean("Push To Block", false, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting crystal = this.registerBoolean("Crystal Detect", false, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting fire = this.registerBoolean("Fire", true, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting different = this.registerBoolean("Different Pos", false, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting maxPos = this.registerInteger("Max Pos", 10, 1, 25, () -> (Boolean)this.different.getValue() != false && ((String)this.page.getValue()).equals("Calc"));
    ModeSetting redstone = this.registerMode("Redstone", Arrays.asList("Block", "Torch", "Both"), "Block", () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetPlace = this.registerBoolean("Packet Place", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packet = this.registerBoolean("Packet Crystal", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetBreak = this.registerBoolean("Packet Break", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting antiWeakness = this.registerBoolean("Anti Weakness", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting swingArm = this.registerBoolean("Swing Arm", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting silentSwitch = this.registerBoolean("Switch Back", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting crystalBypass = this.registerBoolean("Crystal Bypass", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting force = this.registerBoolean("Force Bypass", false, () -> (Boolean)this.crystalBypass.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting strict = this.registerBoolean("Strict", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting forceRotate = this.registerBoolean("Piston ForceRotate", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting rotate = this.registerBoolean("Rotate", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting pistonRotate = this.registerBoolean("Piston Rotate", true, () -> (Boolean)this.rotate.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting raytrace = this.registerBoolean("RayTrace", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting baseRaytrace = this.registerBoolean("Base RayTrace", true, () -> (Boolean)this.base.getValue() != false && (Boolean)this.raytrace.getValue() != false && ((String)this.page.getValue()).equals("General"));
    DoubleSetting forceRange = this.registerDouble("Force Range", 3.0, 0.0, 6.0, () -> (Boolean)this.raytrace.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting pauseEat = this.registerBoolean("Pause When Eating", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting pause1 = this.registerBoolean("Pause When Burrow", true, () -> ((String)this.page.getValue()).equals("General"));
    DoubleSetting maxSelfSpeed = this.registerDouble("Max Self Speed", 10.0, 0.0, 50.0, () -> ((String)this.page.getValue()).equals("General"));
    DoubleSetting maxTargetSpeed = this.registerDouble("Max Target Speed", 10.0, 0.0, 50.0, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting render = this.registerBoolean("Render", false, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting fireRender = this.registerBoolean("Fire Render", false, () -> (Boolean)this.render.getValue() != false && ((String)this.page.getValue()).equals("Render"));
    BooleanSetting box = this.registerBoolean("Box", false, () -> (Boolean)this.render.getValue() != false && ((String)this.page.getValue()).equals("Render"));
    BooleanSetting outline = this.registerBoolean("Outline", false, () -> (Boolean)this.render.getValue() != false && ((String)this.page.getValue()).equals("Render"));
    BooleanSetting hud = this.registerBoolean("HUD", false, () -> ((String)this.page.getValue()).equals("Render"));
    public static EntityPlayer target;
    public BlockPos targetPos;
    public BlockPos pistonPos;
    public BlockPos crystalPos;
    public BlockPos redStonePos;
    public BlockPos firePos;
    public BlockPos lastTargetPos;
    public int pistonSlot;
    public int crystalSlot;
    public int redStoneSlot;
    public int obbySlot;
    public int flintSlot;
    public Timing timer = new Timing();
    public Timing baseTimer = new Timing();
    public Timing startBreakTimer = new Timing();
    public Timing breakTimer = new Timing();
    public boolean preparedSpace;
    public boolean placedPiston;
    public boolean placedCrystal;
    public boolean placedFire;
    public boolean placedRedstone;
    public boolean brokeCrystal;
    int oldSlot;
    boolean useBlock;
    boolean boom;
    boolean burrowed;
    boolean moving;
    boolean first;
    Vec2f rotation;
    BlockPos[] saveArray = new BlockPos[25];
    Vec3d[] sides = new Vec3d[]{new Vec3d(0.24, 0.0, 0.24), new Vec3d(-0.24, 0.0, 0.24), new Vec3d(0.24, 0.0, -0.24), new Vec3d(-0.24, 0.0, -0.24)};
    BlockPos[] offsets = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
    BlockPos[] fireList = new BlockPos[]{new BlockPos(0, 1, 0), new BlockPos(1, 1, 1), new BlockPos(1, 1, 0), new BlockPos(1, 1, -1), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1), new BlockPos(-1, 1, 1), new BlockPos(-1, 1, 0), new BlockPos(-1, 1, -1), new BlockPos(1, 0, 1), new BlockPos(1, 0, 0), new BlockPos(1, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, -1), new BlockPos(0, 0, 0)};
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (this.rotation != null) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                ((CPacketPlayer.Rotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
                ((CPacketPlayer.Rotation)event.getPacket()).field_149473_f = 0.0f;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149473_f = 0.0f;
            }
            if (event.getPacket() instanceof CPacketVehicleMove) {
                ((AccessorCPacketVehicleMove)event.getPacket()).setYaw(this.rotation.field_189982_i);
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (this.rotation == null || event.getPhase() != Phase.PRE) {
            return;
        }
        PlayerPacket packet = new PlayerPacket((Module)this, new Vec2f(this.rotation.field_189982_i, 0.0f));
        PlayerPacketManager.INSTANCE.addPacket(packet);
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        SPacketSoundEffect packet;
        if (PistonAura.mc.field_71441_e == null || PistonAura.mc.field_71439_g == null || this.crystalPos == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB && this.crystalPos.func_177954_c(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f()) <= 9.0) {
            this.boom = true;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> listener = new Listener<PacketEvent.Receive>(event -> {
        SPacketSoundEffect packet;
        if (PistonAura.mc.field_71441_e == null || PistonAura.mc.field_71439_g == null || PistonAura.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB) {
            for (Entity crystal : new ArrayList(PistonAura.mc.field_71441_e.field_72996_f)) {
                if (!(crystal instanceof EntityEnderCrystal) || !(crystal.func_70011_f(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f()) <= (Double)this.range.getValue() + 5.0)) continue;
                crystal.func_70106_y();
            }
        }
    }, new Predicate[0]);

    public PistonAura() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.saveArray = new BlockPos[25];
        this.first = true;
        this.reset();
    }

    @Override
    public void onTick() {
        if (this.autoCrystal) {
            this.reset();
            return;
        }
        this.doPA();
    }

    public void doPA() {
        this.moving = false;
        this.burrowed = false;
        BlockPos originalPos = PlayerUtil.getPlayerPos();
        Block block = BlockUtil.getBlock(originalPos);
        if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z || block == Blocks.field_150477_bB) {
            this.burrowed = true;
        }
        if (((Boolean)this.pause1.getValue()).booleanValue() && this.burrowed) {
            return;
        }
        if (((Boolean)this.pauseEat.getValue()).booleanValue() && EntityUtil.isEating()) {
            return;
        }
        if (LemonClient.speedUtil.getPlayerSpeed((EntityPlayer)PistonAura.mc.field_71439_g) > (Double)this.maxSelfSpeed.getValue()) {
            return;
        }
        this._doPA();
    }

    public void _doPA() {
        if (!((Boolean)this.forceRotate.getValue()).booleanValue()) {
            this.rotation = null;
        }
        if (PistonAura.mc.field_71441_e == null || PistonAura.mc.field_71439_g == null || PistonAura.mc.field_71439_g.field_70128_L) {
            this.reset();
            return;
        }
        try {
            boolean finish;
            if (!this.findMaterials()) {
                this.reset();
                return;
            }
            if (((Boolean)this.alwaysCalc.getValue()).booleanValue() || this.boom || target == null || !EntityUtil.isAlive((Entity)target)) {
                PistonAuraPos pos = this.findSpace();
                if (pos == null) {
                    this.first = true;
                    target = null;
                    this.crystalPos = null;
                    this.firePos = null;
                    this.redStonePos = null;
                    this.pistonPos = null;
                    this.targetPos = null;
                    this.rotation = null;
                    return;
                }
                target = pos.target;
                this.targetPos = pos.targetPos;
                this.pistonPos = pos.piston;
                this.redStonePos = pos.redstone;
                this.crystalPos = pos.crystal;
                this.firePos = pos.fire;
            }
            if (this.targetPos == null || this.pistonPos == null || this.redStonePos == null || this.crystalPos == null) {
                if (this.breakTimer.passedDms(((Integer)this.breakDelay.getValue()).intValue()) && this.lastTargetPos != null) {
                    if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
                        CrystalUtil.breakCrystalPacket(this.lastTargetPos, (boolean)((Boolean)this.swingArm.getValue()));
                    } else {
                        CrystalUtil.breakCrystal(this.lastTargetPos, (boolean)((Boolean)this.swingArm.getValue()));
                    }
                    this.breakTimer.reset();
                }
                this.reset();
                return;
            }
            if (PlayerUtil.getDistanceI(this.pistonPos) > (Double)this.range.getValue() || PlayerUtil.getDistanceI(this.redStonePos) > (Double)this.range.getValue() || PlayerUtil.getDistanceI(this.crystalPos) > (Double)this.range.getValue()) {
                this.lastTargetPos = null;
                this.reset();
                return;
            }
            AutoEz.INSTANCE.addTargetedPlayer(target.func_70005_c_());
            this.lastTargetPos = new BlockPos(this.targetPos.func_177958_n(), this.crystalPos.func_177956_o() + 2, this.targetPos.func_177952_p());
            this.oldSlot = PistonAura.mc.field_71439_g.field_71071_by.field_70461_c;
            BlockPos offset = new BlockPos(this.crystalPos.func_177958_n() - this.targetPos.func_177958_n(), 0, this.crystalPos.func_177952_p() - this.targetPos.func_177952_p());
            BlockPos headPos = this.pistonPos.func_177982_a(offset.func_177958_n() * -1, 0, offset.func_177952_p() * -1);
            Block block = BlockUtil.getBlock(headPos);
            if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || this.checkPos(headPos)) {
                this.reset();
                return;
            }
            this.placedPiston = BlockUtil.getBlock(this.pistonPos) instanceof BlockPistonBase;
            this.placedRedstone = this.hasRedstone(this.pistonPos);
            this.placedCrystal = this.getCrystal(this.crystalPos.func_177984_a()) != null && this.getCrystal(new BlockPos(this.targetPos.func_177958_n(), this.crystalPos.func_177956_o() + 2, this.targetPos.func_177952_p())) != null;
            this.placedFire = BlockUtil.getBlock(this.firePos) instanceof BlockFire;
            if (this.flintSlot == -1 || BlockUtil.isBlockUnSolid(this.firePos.func_177977_b()) || BlockUtil.canOpen(this.firePos.func_177977_b())) {
                this.placedFire = true;
            }
            if (!(this.placedCrystal || BlockUtil.isAirBlock(this.crystalPos.func_177984_a()) && BlockUtil.isAirBlock(this.crystalPos.func_177981_b(2)))) {
                this.reset();
                return;
            }
            if (!(this.placedPiston || !this.intersectsWithEntity(this.pistonPos) && BlockUtil.canReplace(this.pistonPos))) {
                this.reset();
                return;
            }
            if (!(this.placedRedstone || !this.intersectsWithEntity(this.redStonePos) && BlockUtil.canReplace(this.redStonePos))) {
                this.reset();
                return;
            }
            float[] angle = MathUtil.calcAngle(new Vec3d((Vec3i)this.crystalPos), new Vec3d((Vec3i)this.targetPos));
            this.rotation = new Vec2f(angle[0] + 180.0f, angle[1]);
            if (!this.preparedSpace) {
                boolean bl = this.preparedSpace = this.canPlace(this.pistonPos) || this.canPlace(this.redStonePos);
                if (!this.preparedSpace) {
                    if (!((Boolean)this.base.getValue()).booleanValue()) {
                        this.preparedSpace = true;
                    } else if (this.baseTimer.passedDms(((Integer)this.baseDelay.getValue()).intValue())) {
                        this.baseTimer.reset();
                        this.preparedSpace = this.prepareSpace();
                    }
                }
                this.timer.reset();
            }
            if (this.preparedSpace && this.first) {
                if (!((Boolean)this.forceRotate.getValue()).booleanValue()) {
                    this.timer.setMs(1000000000L);
                }
                this.first = false;
            }
            boolean bl = finish = this.placedPiston && this.placedCrystal && this.placedRedstone;
            if (this.timer.passedDms(((Integer)this.delay.getValue()).intValue()) && !finish) {
                this.timer.reset();
                if (!this.placedPiston && !this.canPlace(this.pistonPos) && this.canPlace(this.redStonePos)) {
                    this.placeRedstone(this.preparedSpace && !this.placedRedstone);
                }
                this.placePiston(this.preparedSpace && !this.placedPiston);
                this.placeCrystal(this.placedPiston && !this.placedCrystal);
                if (this.placedCrystal && !this.placedFire) {
                    this.setItem(this.flintSlot);
                    if (this.placeBlock(this.firePos, (Boolean)this.packetPlace.getValue())) {
                        this.placedFire = true;
                    }
                }
                this.placeRedstone(this.placedFire && !this.placedRedstone);
                boolean bl2 = finish = this.placedPiston && this.placedCrystal && this.placedFire && this.placedRedstone;
            }
            if (finish) {
                Entity crystal = PistonAura.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.crystalPos.func_177981_b(2))).stream().filter(e -> e instanceof EntityEnderCrystal).min(Comparator.comparing(e -> this.getDistance(target, (Entity)e))).orElse(null);
                if (this.startBreakTimer.passedDms(((Integer)this.startBreakDelay.getValue()).intValue()) && this.breakTimer.passedDms(((Integer)this.breakDelay.getValue()).intValue()) && crystal != null) {
                    this.breakTimer.reset();
                    if (((Boolean)this.antiWeakness.getValue()).booleanValue() && PistonAura.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
                        int newSlot = -1;
                        for (int i = 0; i < 9; ++i) {
                            ItemStack stack = PistonAura.mc.field_71439_g.field_71071_by.func_70301_a(i);
                            if (stack == ItemStack.field_190927_a) continue;
                            if (stack.func_77973_b() instanceof ItemSword) {
                                newSlot = i;
                                break;
                            }
                            if (!(stack.func_77973_b() instanceof ItemTool)) continue;
                            newSlot = i;
                        }
                        if (newSlot != -1) {
                            this.setItem(newSlot);
                        }
                    }
                    if (((Boolean)this.packetBreak.getValue()).booleanValue()) {
                        PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(crystal));
                    } else {
                        PistonAura.mc.field_71442_b.func_78764_a((EntityPlayer)PistonAura.mc.field_71439_g, crystal);
                    }
                    if (((Boolean)this.swingArm.getValue()).booleanValue()) {
                        PistonAura.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                    }
                }
            }
            this.restoreItem();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void placePiston(boolean work) {
        if (!work) {
            return;
        }
        this.setItem(this.pistonSlot);
        PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(this.rotation.field_189982_i, this.rotation.field_189983_j, true));
        this.placedPiston = this.placeBlock(this.pistonPos, (Boolean)this.packetPlace.getValue());
        this.startBreakTimer.reset();
        this.breakTimer.reset();
    }

    private void placeCrystal(boolean work) {
        EnumHand hand;
        if (!work) {
            return;
        }
        EnumHand enumHand = hand = this.crystalSlot != 999 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        if (((Boolean)this.crystalBypass.getValue()).booleanValue() && (this.crystalSlot >= 9 || ((Boolean)this.force.getValue()).booleanValue()) && hand == EnumHand.MAIN_HAND) {
            int slot = this.crystalSlot;
            if (slot < 9) {
                slot += 36;
            }
            PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, slot, PistonAura.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, ItemStack.field_190927_a, PistonAura.mc.field_71439_g.field_71069_bz.func_75136_a(PistonAura.mc.field_71439_g.field_71071_by)));
            this.placedCrystal = CrystalUtil.placeCrystal(this.crystalPos, hand, (Boolean)this.packet.getValue(), (Boolean)this.rotate.getValue(), (Boolean)this.swingArm.getValue());
            PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, slot, PistonAura.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, Items.field_185158_cP.func_190903_i(), PistonAura.mc.field_71439_g.field_71069_bz.func_75136_a(PistonAura.mc.field_71439_g.field_71071_by)));
        } else {
            this.setItem(this.crystalSlot);
            this.placedCrystal = CrystalUtil.placeCrystal(this.crystalPos, hand, (Boolean)this.packet.getValue(), (Boolean)this.rotate.getValue(), (Boolean)this.swingArm.getValue());
        }
        this.startBreakTimer.reset();
        this.breakTimer.reset();
    }

    private void placeRedstone(boolean work) {
        if (!work) {
            return;
        }
        this.setItem(this.redStoneSlot);
        this.placedRedstone = BlockUtil.placeBlockBoolean(this.redStonePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packetPlace.getValue(), (Boolean)this.strict.getValue(), this.needRaytrace(this.redStonePos), (Boolean)this.swingArm.getValue());
        this.startBreakTimer.reset();
        this.breakTimer.reset();
    }

    public boolean prepareSpace() {
        BlockPos piston = this.pistonPos.func_177982_a(0, -1, 0);
        if (this.isPos2(piston, this.redStonePos)) {
            piston = piston.func_177977_b();
        }
        BlockPos redstone = this.redStonePos.func_177982_a(0, -1, 0);
        if (!this.canPlace(this.pistonPos)) {
            if (this.intersectsWithEntity(this.pistonPos)) {
                this.reset();
            } else {
                BlockPos offset = new BlockPos(this.crystalPos.func_177958_n() - this.targetPos.func_177958_n(), 0, this.crystalPos.func_177952_p() - this.targetPos.func_177952_p());
                BlockPos crystalOffset = this.crystalPos.func_177971_a((Vec3i)offset);
                BlockPos crystalOffset1 = crystalOffset.func_177971_a((Vec3i)offset);
                this.setItem(this.obbySlot);
                if (this.canPlace(piston) && BlockUtil.canReplace(piston) && !this.isPos2(piston, this.redStonePos)) {
                    if (this.intersectsWithEntity(piston)) {
                        this.reset();
                    } else {
                        BlockUtil.placeBlock(piston, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packetPlace.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue() != false && (Boolean)this.baseRaytrace.getValue() != false, (Boolean)this.swingArm.getValue());
                    }
                } else if (this.canPlace(crystalOffset1) && BlockUtil.canReplace(crystalOffset1) && !this.isPos2(crystalOffset1, this.redStonePos)) {
                    if (this.intersectsWithEntity(crystalOffset1)) {
                        this.reset();
                    } else {
                        BlockUtil.placeBlock(crystalOffset1, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packetPlace.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue() != false && (Boolean)this.baseRaytrace.getValue() != false, (Boolean)this.swingArm.getValue());
                    }
                } else if (this.canPlace(crystalOffset) && BlockUtil.canReplace(crystalOffset) && !this.isPos2(crystalOffset, this.redStonePos)) {
                    if (this.intersectsWithEntity(crystalOffset)) {
                        this.reset();
                    } else {
                        BlockUtil.placeBlock(crystalOffset, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packetPlace.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue() != false && (Boolean)this.baseRaytrace.getValue() != false, (Boolean)this.swingArm.getValue());
                    }
                } else {
                    this.reset();
                }
            }
            return false;
        }
        if ((!this.canPlace(this.redStonePos) || !this.useBlock && this.redStonePos.func_177956_o() == this.pistonPos.func_177956_o()) && this.canPlace(redstone) && !this.isPos2(redstone, this.pistonPos)) {
            if (this.intersectsWithEntity(redstone)) {
                this.reset();
            } else {
                this.setItem(this.obbySlot);
                BlockUtil.placeBlock(redstone, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packetPlace.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue() != false && (Boolean)this.baseRaytrace.getValue() != false, (Boolean)this.swingArm.getValue());
            }
            return false;
        }
        return true;
    }

    public PistonAuraPos findSpace() {
        ArrayList list = new ArrayList();
        for (EntityPlayer target : PlayerUtil.getNearPlayers((Double)this.range.getValue() + 4.0, (Integer)this.maxTarget.getValue())) {
            if (LemonClient.speedUtil.getPlayerSpeed(target) > (Double)this.maxTargetSpeed.getValue() || (int)(target.field_70163_u + 0.5) >= 255) continue;
            ArrayList<PistonAuraPos> sideList = new ArrayList<PistonAuraPos>();
            for (Vec3d vec3d : this.sides) {
                PistonAuraPos best;
                int y;
                BlockPos targetPos = new BlockPos(target.field_70165_t + vec3d.field_72450_a, target.field_70163_u + 0.5, target.field_70161_v + vec3d.field_72449_c);
                BlockPos cPos = null;
                for (Entity entity : PistonAura.mc.field_71441_e.field_72996_f) {
                    if (!(entity instanceof EntityEnderCrystal)) continue;
                    cPos = new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v);
                    int x = Math.abs(cPos.func_177958_n() - targetPos.func_177958_n());
                    y = cPos.field_177960_b - targetPos.field_177960_b;
                    int z = Math.abs(cPos.func_177952_p() - targetPos.func_177952_p());
                    if (x <= 1 && y <= 5 && y >= 0 && z <= 1) break;
                    cPos = null;
                }
                BlockPos[] offsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
                boolean calc = false;
                ArrayList<PistonAuraPos> posList = new ArrayList<PistonAuraPos>();
                int n = y = (Boolean)this.floor.getValue() != false ? -1 : 0;
                while (y <= (Integer)this.maxY.getValue()) {
                    boolean cantPlace = false;
                    boolean block = false;
                    for (int high = y + 1; high >= 0; --high) {
                        BlockPos pos = targetPos.func_177981_b(high);
                        if (!PistonAura.isResistant(BlockUtil.getState(pos))) continue;
                        if (high < y + 1) {
                            cantPlace = true;
                            continue;
                        }
                        if (!((Boolean)this.push.getValue()).booleanValue()) {
                            cantPlace = true;
                            continue;
                        }
                        block = true;
                    }
                    if (!cantPlace) {
                        for (BlockPos side : offsets) {
                            BlockPos crystalPos;
                            BlockPos offset;
                            if (!((Boolean)this.crystal.getValue()).booleanValue()) {
                                cPos = null;
                            }
                            BlockPos blockPos = offset = cPos == null ? side : new BlockPos(cPos.func_177958_n() - targetPos.func_177958_n(), 0, cPos.func_177952_p() - targetPos.func_177952_p());
                            if (cPos != null && this.isPos2(new BlockPos(-offset.func_177958_n(), 0, -offset.func_177952_p()), side)) {
                                cPos = null;
                            }
                            if (cPos == null) {
                                offset = side;
                            } else if (calc) continue;
                            BlockPos blockPos2 = crystalPos = cPos == null ? targetPos.func_177982_a(offset.func_177958_n(), y, offset.func_177952_p()) : cPos;
                            if (crystalPos.field_177960_b >= 255 || cPos == null && (BlockUtil.getBlock(crystalPos) != Blocks.field_150343_Z && BlockUtil.getBlock(crystalPos) != Blocks.field_150357_h || !PistonAura.mc.field_71441_e.func_175623_d(crystalPos.func_177984_a()) || !PistonAura.mc.field_71441_e.func_175623_d(crystalPos.func_177981_b(2)) || PlayerUtil.getDistanceI(crystalPos) > (Double)this.range.getValue() || !PistonAura.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(crystalPos.func_177984_a())).isEmpty() || !PistonAura.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(crystalPos.func_177981_b(2))).isEmpty())) continue;
                            BlockPos normal = crystalPos.func_177971_a((Vec3i)offset);
                            BlockPos oneBlock = normal.func_177971_a((Vec3i)offset);
                            BlockPos side0 = normal.func_177982_a(offset.func_177952_p(), 0, offset.func_177958_n());
                            BlockPos side1 = normal.func_177982_a(offset.func_177952_p() * -1, 0, offset.func_177958_n() * -1);
                            BlockPos side2 = oneBlock.func_177982_a(offset.func_177952_p(), 0, offset.func_177958_n());
                            BlockPos side3 = oneBlock.func_177982_a(offset.func_177952_p() * -1, 0, offset.func_177958_n() * -1);
                            BlockPos side4 = crystalPos.func_177982_a(offset.func_177952_p(), 0, offset.func_177958_n());
                            BlockPos side5 = crystalPos.func_177982_a(offset.func_177952_p() * -1, 0, offset.func_177958_n() * -1);
                            ArrayList<BlockPos> pistons = new ArrayList<BlockPos>();
                            this.add(pistons, normal);
                            this.add(pistons, oneBlock);
                            this.add(pistons, side0);
                            this.add(pistons, side1);
                            this.add(pistons, side2);
                            this.add(pistons, side3);
                            this.add(pistons, side4);
                            this.add(pistons, side5);
                            BlockPos finalOffset = offset;
                            pistons.removeIf(p -> {
                                if (this.flintSlot != -1 && !this.getFirePos(targetPos, crystalPos, finalOffset)) {
                                    return true;
                                }
                                if (!((Boolean)this.different.getValue()).booleanValue()) {
                                    return false;
                                }
                                boolean same = false;
                                for (BlockPos savePos : this.saveArray) {
                                    if (!this.isPos2(savePos, (BlockPos)p)) continue;
                                    same = true;
                                    break;
                                }
                                return same;
                            });
                            if (pistons.isEmpty()) continue;
                            List pistonList = pistons.stream().filter(pistonPos -> {
                                BlockPos redstonePos;
                                BlockPos headPos = pistonPos.func_177982_a(finalOffset.func_177958_n() * -1, 0, finalOffset.func_177952_p() * -1);
                                Block headBlock = BlockUtil.getBlock(headPos);
                                if (headBlock == Blocks.field_150357_h || headBlock == Blocks.field_150343_Z || headBlock == Blocks.field_150477_bB || headBlock == Blocks.field_150332_K || this.checkPos(headPos)) {
                                    return false;
                                }
                                boolean isPiston = BlockUtil.getBlock(pistonPos) instanceof BlockPistonBase;
                                if (!isPiston) {
                                    if (!this.canPlace((BlockPos)pistonPos)) {
                                        return false;
                                    }
                                    if (PistonAura.mc.field_71439_g.func_70011_f((double)pistonPos.func_177958_n() + 0.5, (double)pistonPos.func_177956_o() + 0.5, (double)pistonPos.func_177952_p() + 0.5) > (Double)this.range.getValue()) {
                                        return false;
                                    }
                                    double feetY = PistonAura.mc.field_71439_g.field_70163_u;
                                    if (PlayerUtil.getDistanceI(pistonPos) < 0.8 + (double)pistonPos.func_177956_o() - feetY && (double)pistonPos.func_177956_o() > feetY + 1.0 || PlayerUtil.getDistanceI(pistonPos) < 1.8 + feetY - (double)pistonPos.func_177956_o() && (double)pistonPos.func_177956_o() < feetY) {
                                        return false;
                                    }
                                } else if (((Boolean)this.pistonCheck.getValue()).booleanValue() && (this.hasRedstone((BlockPos)pistonPos) || !this.isFacing((BlockPos)pistonPos, headPos))) {
                                    return false;
                                }
                                if ((redstonePos = this.getRedStonePos(crystalPos, (BlockPos)pistonPos, finalOffset)) == null) {
                                    return false;
                                }
                                if (this.flintSlot != -1 && this.getFirePos(targetPos, crystalPos, (BlockPos)pistonPos, redstonePos, finalOffset) == null) {
                                    return false;
                                }
                                return isPiston || BlockUtil.canPlaceWithoutBase(pistonPos, (Boolean)this.strict.getValue(), this.needRaytrace((BlockPos)pistonPos), this.canPlace(redstonePos) || this.obbySlot != -1 && ((Boolean)this.base.getValue() != false || this.canPlace(redstonePos.func_177977_b())));
                            }).collect(Collectors.toList());
                            if (pistonList.isEmpty()) {
                                pistonList.addAll(pistons);
                            }
                            BlockPos piston = pistonList.stream().min(Comparator.comparing(this::blockLevel)).orElse(null);
                            PistonAuraPos pos = new PistonAuraPos(crystalPos, piston, this.getRedStonePos(crystalPos, piston, offset), offset, target, targetPos, block);
                            posList.add(pos);
                            if (cPos == null) continue;
                            calc = true;
                        }
                    }
                    ++y;
                }
                List paList = posList.stream().filter(p -> !p.block || p.offset.field_177961_c == 1).collect(Collectors.toList());
                if (paList.isEmpty()) {
                    paList.addAll(posList);
                }
                if ((best = (PistonAuraPos)paList.stream().min(Comparator.comparing(PistonAuraPos::range)).orElse(null)) == null) continue;
                sideList.add(best);
            }
            if (sideList.isEmpty()) continue;
            list.add(sideList.stream().min(Comparator.comparing(PistonAuraPos::range)).orElse(null));
        }
        PistonAuraPos best = list.stream().min(Comparator.comparing(PistonAuraPos::range)).orElse(null);
        if (best == null) {
            this.saveArray = new BlockPos[25];
            return null;
        }
        return best;
    }

    public boolean isFacing(BlockPos pos, BlockPos facingPos) {
        ImmutableMap properties = PistonAura.mc.field_71441_e.func_180495_p(pos).func_177228_b();
        for (IProperty prop : properties.keySet()) {
            if (prop.func_177699_b() != EnumFacing.class || !prop.func_177701_a().equals("facing") && !prop.func_177701_a().equals("rotation")) continue;
            BlockPos pushPos = pos.func_177972_a((EnumFacing)properties.get((Object)prop));
            return this.isPos2(facingPos, pushPos);
        }
        return false;
    }

    public static boolean isResistant(IBlockState blockState) {
        return blockState.func_185904_a() != Material.field_151579_a && !BlockUtil.isAir(blockState.func_177230_c()) && !(blockState instanceof BlockLiquid) && (double)blockState.func_177230_c().field_149781_w >= 19.7;
    }

    public boolean getFirePos(BlockPos targetPos, BlockPos crystalPos, BlockPos offset) {
        BlockPos origin = new BlockPos(targetPos.func_177958_n(), crystalPos.func_177956_o() + 1, targetPos.func_177952_p());
        BlockPos pos1 = targetPos.func_177982_a(-offset.field_177962_a, 0, -offset.field_177961_c);
        BlockPos pos2 = pos1.func_177982_a(offset.field_177961_c, 0, offset.field_177962_a);
        BlockPos pos3 = pos1.func_177982_a(-offset.field_177961_c, 0, -offset.field_177962_a);
        BlockPos pos4 = targetPos.func_177982_a(-offset.field_177962_a, 1, -offset.field_177961_c);
        BlockPos pos5 = pos4.func_177982_a(offset.field_177961_c, 0, offset.field_177962_a);
        BlockPos pos6 = pos4.func_177982_a(-offset.field_177961_c, 0, -offset.field_177962_a);
        BlockPos[] posList = new BlockPos[]{pos1, pos2, pos3, pos4, pos5, pos6};
        boolean air = true;
        for (BlockPos pos : posList) {
            AxisAlignedBB box;
            if (BlockUtil.isAir(pos) || (box = BlockUtil.getBoundingBox(pos)) == null) continue;
            double x = box.field_72336_d - box.field_72340_a;
            double z = box.field_72334_f - box.field_72339_c;
            if (x * x != (double)(offset.field_177962_a * offset.field_177962_a) && z * z != (double)(offset.field_177961_c * offset.field_177961_c)) continue;
            air = false;
            break;
        }
        for (BlockPos firePos : this.fireList) {
            BlockPos pos = origin.func_177971_a((Vec3i)firePos);
            if (!air && (targetPos.func_177982_a(-offset.field_177962_a, 0, 0).func_177958_n() == pos.func_177958_n() || targetPos.func_177982_a(0, 0, -offset.field_177961_c).func_177952_p() == pos.func_177952_p())) continue;
            if (BlockUtil.getBlock(pos) == Blocks.field_150480_ab) {
                return true;
            }
            if (!this.canPlaceFire(pos) || !PistonAura.mc.field_71441_e.func_175623_d(pos) || BlockUtil.isBlockUnSolid(pos.func_177977_b()) || BlockUtil.canOpen(pos.func_177977_b())) continue;
            return true;
        }
        return false;
    }

    public BlockPos getFirePos(BlockPos targetPos, BlockPos crystalPos, BlockPos pistonPos, BlockPos redstonePos, BlockPos offset) {
        BlockPos origin = new BlockPos(targetPos.func_177958_n(), crystalPos.func_177956_o() + 1, targetPos.func_177952_p());
        BlockPos pos1 = targetPos.func_177982_a(-offset.field_177962_a, 0, -offset.field_177961_c);
        BlockPos pos2 = pos1.func_177982_a(offset.field_177961_c, 0, offset.field_177962_a);
        BlockPos pos3 = pos1.func_177982_a(-offset.field_177961_c, 0, -offset.field_177962_a);
        BlockPos pos4 = targetPos.func_177982_a(-offset.field_177962_a, 1, -offset.field_177961_c);
        BlockPos pos5 = pos4.func_177982_a(offset.field_177961_c, 0, offset.field_177962_a);
        BlockPos pos6 = pos4.func_177982_a(-offset.field_177961_c, 0, -offset.field_177962_a);
        BlockPos[] posList = new BlockPos[]{pos1, pos2, pos3, pos4, pos5, pos6};
        boolean air = true;
        for (BlockPos pos : posList) {
            AxisAlignedBB box;
            if (BlockUtil.isAir(pos) || (box = BlockUtil.getBoundingBox(pos)) == null) continue;
            double x = box.field_72336_d - box.field_72340_a;
            double z = box.field_72334_f - box.field_72339_c;
            if (x * x != (double)(offset.field_177962_a * offset.field_177962_a) && z * z != (double)(offset.field_177961_c * offset.field_177961_c)) continue;
            air = false;
            break;
        }
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        for (BlockPos firePos : this.fireList) {
            BlockPos pos = origin.func_177971_a((Vec3i)firePos);
            if (this.isPos2(pistonPos, pos) || this.isPos2(pistonPos.func_177984_a(), pos) || this.isPos2(redstonePos, pos) || this.isPos2(pistonPos, pos.func_177971_a((Vec3i)offset)) || this.isPos2(pistonPos.func_177984_a(), pos.func_177971_a((Vec3i)offset)) || !air && (targetPos.func_177982_a(-offset.field_177962_a, 0, 0).func_177958_n() == pos.func_177958_n() || targetPos.func_177982_a(0, 0, -offset.field_177961_c).func_177952_p() == pos.func_177952_p())) continue;
            if (BlockUtil.getBlock(pos) == Blocks.field_150480_ab) {
                return pos;
            }
            if (!this.canPlaceFire(pos) || !PistonAura.mc.field_71441_e.func_175623_d(pos) || BlockUtil.isBlockUnSolid(pos.func_177977_b()) || BlockUtil.canOpen(pos.func_177977_b())) continue;
            list.add(pos);
        }
        return list.stream().sorted(this::fireLevel).min(Comparator.comparing(PlayerUtil::getDistanceI)).orElse(null);
    }

    public BlockPos getRedStonePos(BlockPos crystalPos, BlockPos pistonPos, BlockPos offset) {
        BlockPos pos = this.hasRedstoneBlock(pistonPos);
        if (pos != null) {
            return pos;
        }
        ArrayList<BlockPos> redstone = new ArrayList<BlockPos>();
        if (this.useBlock) {
            for (EnumFacing facing : EnumFacing.field_82609_l) {
                BlockPos redstonePos = pistonPos.func_177972_a(facing);
                if (!BlockUtil.canReplace(redstonePos) || PistonAura.mc.field_71439_g.func_70011_f((double)redstonePos.func_177958_n() + 0.5, (double)redstonePos.func_177956_o() + 0.5, (double)redstonePos.func_177952_p() + 0.5) > (Double)this.range.getValue()) continue;
                boolean can = true;
                for (BlockPos blockPos : this.fireList) {
                    if (!this.isPos2(crystalPos.func_177984_a().func_177971_a((Vec3i)blockPos), redstonePos)) continue;
                    can = false;
                    break;
                }
                if (!can || !BlockUtil.canPlaceWithoutBase(redstonePos, (Boolean)this.strict.getValue(), this.needRaytrace(redstonePos), (Boolean)this.base.getValue())) continue;
                redstone.add(redstonePos);
            }
        } else {
            BlockPos pistonPush = pistonPos.func_177982_a(offset.func_177958_n() * -1, 0, offset.func_177952_p() * -1);
            for (BlockPos add : this.offsets) {
                for (int i = 0; i < 2; ++i) {
                    BlockPos torch = pistonPos.func_177979_c(i).func_177971_a((Vec3i)add);
                    if (!BlockUtil.canReplace(torch) || torch.func_177958_n() == crystalPos.func_177958_n() && torch.func_177952_p() == crystalPos.func_177952_p() || torch.func_177958_n() == pistonPush.func_177958_n() && torch.func_177952_p() == pistonPush.func_177952_p() || i == 1 && BlockUtil.isBlockUnSolid(torch.func_177984_a()) || PistonAura.mc.field_71439_g.func_70011_f((double)torch.func_177958_n() + 0.5, (double)torch.func_177956_o() + 0.5, (double)torch.func_177952_p() + 0.5) > (Double)this.range.getValue() || !BlockUtil.isAir(torch.func_177977_b()) && BlockUtil.isBlockUnSolid(torch.func_177977_b()) || !BlockUtil.canPlaceWithoutBase(torch, (Boolean)this.strict.getValue(), this.needRaytrace(torch), (Boolean)this.base.getValue(), pistonPos)) continue;
                    redstone.add(torch);
                }
            }
        }
        redstone.removeIf(p -> p.field_177960_b > 255);
        return redstone.stream().min(Comparator.comparing(this::blockLevel)).orElse(null);
    }

    public boolean hasRedstone(BlockPos pos) {
        return this.hasRedstoneBlock(pos) != null;
    }

    public BlockPos hasRedstoneBlock(BlockPos pos) {
        if (BlockUtil.getBlock(pos.func_177984_a()) == Blocks.field_150451_bX) {
            return pos.func_177984_a();
        }
        for (BlockPos offset : this.offsets) {
            for (int i = 0; i < 2; ++i) {
                BlockPos blockPos = pos.func_177971_a((Vec3i)offset).func_177979_c(i);
                Block block = BlockUtil.getBlock(blockPos);
                if (!(i == 0 ? block == Blocks.field_150429_aA || block == Blocks.field_150451_bX : block == Blocks.field_150429_aA && !BlockUtil.isBlockUnSolid(blockPos.func_177984_a()))) continue;
                return blockPos;
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

    private double getDistance(EntityPlayer player, Entity entity) {
        double x = player.field_70165_t - entity.field_70165_t;
        double z = player.field_70161_v - entity.field_70161_v;
        return Math.sqrt(x * x + z * z);
    }

    private boolean canPlace(BlockPos pos) {
        return BlockUtil.getFirstFacing(pos, (Boolean)this.strict.getValue(), this.needRaytrace(pos)) != null && !this.intersectsWithEntity(pos);
    }

    private boolean canPlaceFire(BlockPos pos) {
        return BlockUtil.getFirstFacing(pos, (Boolean)this.strict.getValue(), this.needRaytrace(pos)) != null;
    }

    private Entity getCrystal(BlockPos pos) {
        for (Entity entity : PistonAura.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityEnderCrystal) || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return entity;
        }
        return null;
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : PistonAura.mc.field_71441_e.field_72996_f) {
            if (entity.field_70128_L || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityArrow || !((Boolean)this.entityCheck.getValue()).booleanValue() && entity instanceof EntityEnderCrystal || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public void add(List<BlockPos> pistons, BlockPos pos) {
        pistons.add(pos.func_177982_a(0, 1, 0));
        pistons.add(pos.func_177982_a(0, 2, 0));
    }

    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = PistonAura.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || ((ItemBlock)stack.func_77973_b()).func_179223_d() != blockIn) continue;
            return i;
        }
        return -1;
    }

    private int getItemHotbar() {
        for (int i = 0; i < ((Boolean)this.crystalBypass.getValue() != false ? 36 : 9); ++i) {
            Item item = PistonAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)item) != Item.func_150891_b((Item)Items.field_185158_cP)) continue;
            return i;
        }
        return -1;
    }

    public boolean findMaterials() {
        this.pistonSlot = PistonAura.findHotbarBlock((Block)Blocks.field_150331_J);
        this.obbySlot = PistonAura.findHotbarBlock(Blocks.field_150343_Z);
        this.crystalSlot = this.getItemHotbar();
        int n = this.flintSlot = (Boolean)this.fire.getValue() != false ? BurrowUtil.findHotbarBlock(Items.field_151033_d.getClass()) : -1;
        if (this.pistonSlot == -1) {
            this.pistonSlot = PistonAura.findHotbarBlock((Block)Blocks.field_150320_F);
        }
        if (PistonAura.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
            this.crystalSlot = 999;
        }
        int block = PistonAura.findHotbarBlock(Blocks.field_150451_bX);
        int torch = PistonAura.findHotbarBlock(Blocks.field_150429_aA);
        if (((String)this.redstone.getValue()).equals("Block")) {
            this.redStoneSlot = block;
        }
        if (((String)this.redstone.getValue()).equals("Torch")) {
            this.redStoneSlot = torch;
        }
        if (((String)this.redstone.getValue()).equals("Both")) {
            this.redStoneSlot = block != -1 ? block : torch;
        }
        this.useBlock = this.redStoneSlot == block;
        return this.pistonSlot != -1 && this.crystalSlot != -1 && this.redStoneSlot != -1;
    }

    private void reset() {
        int i;
        for (i = this.saveArray.length - 1; i > 0; --i) {
            this.saveArray[i] = this.saveArray[i - 1];
        }
        if (this.pistonPos != null) {
            this.saveArray[0] = this.pistonPos;
        }
        for (i = 0; i < this.saveArray.length; ++i) {
            if (i < (Integer)this.maxPos.getValue()) continue;
            this.saveArray[i] = null;
        }
        if (!((Boolean)this.different.getValue()).booleanValue()) {
            this.saveArray = new BlockPos[25];
        }
        target = null;
        this.targetPos = null;
        this.rotation = null;
        this.pistonPos = null;
        this.crystalPos = null;
        this.redStonePos = null;
        this.firePos = null;
        this.pistonSlot = -1;
        this.crystalSlot = -1;
        this.redStoneSlot = -1;
        this.obbySlot = -1;
        this.baseTimer = new Timing();
        this.timer = new Timing();
        this.startBreakTimer = new Timing();
        this.breakTimer = new Timing();
        this.preparedSpace = false;
        this.placedPiston = false;
        this.placedCrystal = false;
        this.placedRedstone = false;
        this.brokeCrystal = false;
        this.boom = false;
    }

    public boolean checkPos(BlockPos pos) {
        BlockPos myPos = PlayerUtil.getPlayerPos();
        return pos.func_177958_n() == myPos.func_177958_n() && pos.func_177952_p() == myPos.func_177952_p() && (myPos.func_177956_o() == pos.func_177956_o() || myPos.func_177956_o() + 1 == pos.func_177956_o());
    }

    public void setItem(int slot) {
        if (slot == 999) {
            return;
        }
        this.normalSwitch(slot);
    }

    private void normalSwitch(int slot) {
        if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
            PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
        } else {
            PistonAura.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            PistonAura.mc.field_71442_b.func_78765_e();
        }
    }

    public void restoreItem() {
        if (((Boolean)this.silentSwitch.getValue()).booleanValue()) {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.oldSlot));
            } else {
                PistonAura.mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
                PistonAura.mc.field_71442_b.func_78765_e();
            }
        }
    }

    private boolean placeBlock(BlockPos pos, boolean packet) {
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        EnumFacing side = BlockUtil.getFirstFacing(pos, (Boolean)this.strict.getValue(), this.needRaytrace(pos));
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        boolean sneak = false;
        if (!ColorMain.INSTANCE.sneaking && BlockUtil.blackList.contains(BlockUtil.getBlock(neighbour))) {
            PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PistonAura.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            PistonAura.mc.field_71439_g.func_70095_a(true);
            sneak = true;
        }
        if (packet) {
            PistonAura.rightClickBlock(neighbour, hitVec, EnumHand.MAIN_HAND, opposite);
        } else {
            PistonAura.mc.field_71442_b.func_187099_a(PistonAura.mc.field_71439_g, PistonAura.mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        }
        if (((Boolean)this.swingArm.getValue()).booleanValue()) {
            PistonAura.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        if (((Boolean)this.rotate.getValue()).booleanValue() && ((Boolean)this.pistonRotate.getValue()).booleanValue()) {
            BlockUtil.faceVector(hitVec);
        }
        if (sneak) {
            PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PistonAura.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            PistonAura.mc.field_71439_g.func_70095_a(false);
        }
        return true;
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction) {
        float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
        float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
        float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
        PistonAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
    }

    private int fireLevel(BlockPos pos, BlockPos targetPos) {
        return pos.func_177958_n() == targetPos.func_177958_n() || pos.func_177952_p() == targetPos.func_177952_p() ? 1 : 0;
    }

    private int blockLevel(BlockPos pos) {
        return pos.func_177956_o() * 10000;
    }

    private boolean needRaytrace(BlockPos pos) {
        return PistonAura.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) > (Double)this.forceRange.getValue() && (Boolean)this.raytrace.getValue() != false;
    }

    public boolean canPistonCrystal(BlockPos crystalPos, BlockPos targetPos) {
        BlockPos offset = new BlockPos(targetPos.field_177962_a - crystalPos.field_177962_a, crystalPos.field_177960_b, targetPos.field_177961_c - crystalPos.field_177961_c);
        BlockPos normal = crystalPos.func_177971_a((Vec3i)offset);
        BlockPos oneBlock = normal.func_177971_a((Vec3i)offset);
        BlockPos side0 = normal.func_177982_a(offset.func_177952_p(), 0, offset.func_177958_n());
        BlockPos side1 = normal.func_177982_a(offset.func_177952_p() * -1, 0, offset.func_177958_n() * -1);
        BlockPos side2 = oneBlock.func_177982_a(offset.func_177952_p(), 0, offset.func_177958_n());
        BlockPos side3 = oneBlock.func_177982_a(offset.func_177952_p() * -1, 0, offset.func_177958_n() * -1);
        BlockPos side4 = crystalPos.func_177982_a(offset.func_177952_p(), 0, offset.func_177958_n());
        BlockPos side5 = crystalPos.func_177982_a(offset.func_177952_p() * -1, 0, offset.func_177958_n() * -1);
        ArrayList<BlockPos> pistons = new ArrayList<BlockPos>();
        this.add(pistons, normal);
        this.add(pistons, oneBlock);
        this.add(pistons, side0);
        this.add(pistons, side1);
        this.add(pistons, side2);
        this.add(pistons, side3);
        this.add(pistons, side4);
        this.add(pistons, side5);
        pistons.removeIf(p -> {
            if (!((Boolean)this.different.getValue()).booleanValue()) {
                return false;
            }
            boolean same = false;
            for (BlockPos savePos : this.saveArray) {
                if (!this.isPos2(savePos, (BlockPos)p)) continue;
                same = true;
                break;
            }
            return same;
        });
        if (pistons.isEmpty()) {
            return false;
        }
        List pistonList = pistons.stream().filter(pistonPos -> {
            BlockPos redstonePos;
            BlockPos headPos = pistonPos.func_177982_a(offset.func_177958_n() * -1, 0, offset.func_177952_p() * -1);
            Block headBlock = BlockUtil.getBlock(headPos);
            if (headBlock == Blocks.field_150357_h || headBlock == Blocks.field_150343_Z || headBlock == Blocks.field_150477_bB || headBlock == Blocks.field_150332_K || this.checkPos(headPos)) {
                return false;
            }
            boolean isPiston = BlockUtil.getBlock(pistonPos) instanceof BlockPistonBase;
            if (!isPiston) {
                if (!this.canPlace((BlockPos)pistonPos)) {
                    return false;
                }
                if (PistonAura.mc.field_71439_g.func_70011_f((double)pistonPos.func_177958_n() + 0.5, (double)pistonPos.func_177956_o() + 0.5, (double)pistonPos.func_177952_p() + 0.5) > (Double)this.range.getValue()) {
                    return false;
                }
                double feetY = PistonAura.mc.field_71439_g.field_70163_u;
                if (PlayerUtil.getDistanceI(pistonPos) < 0.8 + (double)pistonPos.func_177956_o() - feetY && (double)pistonPos.func_177956_o() > feetY + 1.0 || PlayerUtil.getDistanceI(pistonPos) < 1.8 + feetY - (double)pistonPos.func_177956_o() && (double)pistonPos.func_177956_o() < feetY) {
                    return false;
                }
            } else if (((Boolean)this.pistonCheck.getValue()).booleanValue() && (this.hasRedstone((BlockPos)pistonPos) || !this.isFacing((BlockPos)pistonPos, headPos))) {
                return false;
            }
            if ((redstonePos = this.getRedStonePos(crystalPos, (BlockPos)pistonPos, offset)) == null) {
                return false;
            }
            if (this.flintSlot != -1 && this.getFirePos(targetPos, crystalPos, (BlockPos)pistonPos, redstonePos, offset) == null) {
                return false;
            }
            return isPiston || BlockUtil.canPlaceWithoutBase(pistonPos, (Boolean)this.strict.getValue(), this.needRaytrace((BlockPos)pistonPos), this.canPlace(redstonePos) || this.obbySlot != -1 && ((Boolean)this.base.getValue() != false || this.canPlace(redstonePos.func_177977_b())));
        }).collect(Collectors.toList());
        return !pistonList.isEmpty();
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (PistonAura.mc.field_71441_e == null || PistonAura.mc.field_71439_g == null) {
            return;
        }
        if (((Boolean)this.render.getValue()).booleanValue()) {
            if (this.firePos != null && ((Boolean)this.fireRender.getValue()).booleanValue()) {
                this.drawBoxMain(this.firePos.field_177962_a, this.firePos.field_177960_b, this.firePos.field_177961_c, 255, 160, 0);
            }
            if (this.pistonPos != null && this.crystalPos != null && this.redStonePos != null) {
                this.drawBoxMain(this.pistonPos.field_177962_a, this.pistonPos.field_177960_b, this.pistonPos.field_177961_c, 255, 255, 150);
                this.drawBoxMain(this.crystalPos.field_177962_a, this.crystalPos.field_177960_b, this.crystalPos.field_177961_c, 255, 255, 255);
                this.drawBoxMain(this.redStonePos.field_177962_a, this.redStonePos.field_177960_b, this.redStonePos.field_177961_c, 225, 50, 50);
            }
        }
    }

    void drawBoxMain(double x, double y, double z, int r, int g, int b) {
        AxisAlignedBB box = this.getBox(x, y, z);
        if (((Boolean)this.box.getValue()).booleanValue()) {
            RenderUtil.drawBox(box, false, 1.0, new GSColor(r, g, b, 25), 63);
        }
        if (((Boolean)this.outline.getValue()).booleanValue()) {
            RenderUtil.drawBoundingBox(box, 1.0, new GSColor(r, g, b, 255));
        }
    }

    AxisAlignedBB getBox(double x, double y, double z) {
        double maxX = x + 1.0;
        double maxZ = z + 1.0;
        return new AxisAlignedBB(x, y, z, maxX, y + 1.0, maxZ);
    }

    @Override
    public String getHudInfo() {
        return (Boolean)this.hud.getValue() != false && target != null ? "[" + ChatFormatting.WHITE + target.func_70005_c_() + ChatFormatting.GRAY + "]" : "";
    }

    static {
        target = null;
    }

    public class PistonAuraPos {
        public BlockPos targetPos;
        public BlockPos crystal;
        public BlockPos piston;
        public BlockPos redstone;
        public BlockPos fire;
        public BlockPos offset;
        EntityPlayer target;
        boolean block;

        public PistonAuraPos(BlockPos crystal, BlockPos piston, BlockPos redstone, BlockPos offset, EntityPlayer target, BlockPos targetPos, boolean block) {
            this.crystal = crystal;
            this.piston = piston;
            this.redstone = redstone;
            this.offset = offset;
            this.targetPos = targetPos;
            this.target = target;
            this.block = block;
            this.fire = PistonAura.this.getFirePos(targetPos, crystal, piston, redstone, offset);
        }

        public double range() {
            double crystalRange = PlayerUtil.getDistanceL(this.crystal);
            double pistonRange = PlayerUtil.getDistanceL(this.piston);
            return Math.max(pistonRange, crystalRange);
        }
    }
}
