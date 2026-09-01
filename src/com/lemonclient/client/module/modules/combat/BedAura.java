/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockBed
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemBed
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.Phase;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lemonclient.client.module.modules.qwq.AutoEz;
import com.lemonclient.mixin.mixins.accessor.AccessorCPacketVehicleMove;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

@Module.Declaration(name="BedAura", category=Category.Combat, priority=999)
public class BedAura
extends Module {
    ModeSetting page = this.registerMode("Page", Arrays.asList("Target", "General", "Delay", "Base", "Calc", "SlowFacePlace", "Switch", "Render"), "General");
    BooleanSetting predict = this.registerBoolean("Predict", true, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting selfPredict = this.registerBoolean("Predict Self", true, () -> ((String)this.page.getValue()).equals("Target"));
    DoubleSetting resetRotate = this.registerDouble("Reset Yaw Difference", 15.0, 0.0, 180.0, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting detect = this.registerBoolean("Detect Ping", false, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting startTick = this.registerInteger("Start Tick", 2, 0, 30, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting addTick = this.registerInteger("Add Tick", 4, 0, 10, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting tickPredict = this.registerInteger("Max Predict Ticks", 10, 0, 30, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting calculateYPredict = this.registerBoolean("Calculate Y Predict", true, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting startDecrease = this.registerInteger("Start Decrease", 39, 0, 200, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting exponentStartDecrease = this.registerInteger("Exponent Start", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting decreaseY = this.registerInteger("Decrease Y", 2, 1, 5, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    IntegerSetting exponentDecreaseY = this.registerInteger("Exponent Decrease Y", 1, 1, 3, () -> (Boolean)this.calculateYPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    BooleanSetting splitXZ = this.registerBoolean("Split XZ", true, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting manualOutHole = this.registerBoolean("Manual Out Hole", false, () -> ((String)this.page.getValue()).equals("Target"));
    BooleanSetting aboveHoleManual = this.registerBoolean("Above Hole Manual", false, () -> (Boolean)this.manualOutHole.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    BooleanSetting stairPredict = this.registerBoolean("Stair Predict", false, () -> ((String)this.page.getValue()).equals("Target"));
    IntegerSetting nStair = this.registerInteger("N Stair", 2, 1, 4, () -> (Boolean)this.stairPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    DoubleSetting speedActivationStair = this.registerDouble("Speed Activation Stair", 0.3, 0.0, 1.0, () -> (Boolean)this.stairPredict.getValue() != false && ((String)this.page.getValue()).equals("Target"));
    ModeSetting targetMode = this.registerMode("Target", Arrays.asList("Nearest", "Damage", "Health", "Smart"), "Nearest", () -> ((String)this.page.getValue()).equals("General"));
    DoubleSetting smartHealth = this.registerDouble("Smart Health", 16.0, 0.0, 36.0, () -> ((String)this.page.getValue()).equals("General") && ((String)this.targetMode.getValue()).equals("Smart"));
    BooleanSetting monster = this.registerBoolean("Monsters", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting neutral = this.registerBoolean("Neutrals", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting animal = this.registerBoolean("Animals", true, () -> ((String)this.page.getValue()).equals("General"));
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("PlaceBreak", "BreakPlace", "Switch", "Stuck", "Test"), "PlaceBreak", () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetPlace = this.registerBoolean("Packet Place", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting placeSwing = this.registerBoolean("Place Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting breakSwing = this.registerBoolean("Break Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetSwing = this.registerBoolean("Packet Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting checkBed = this.registerBoolean("Placed Check", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting highVersion = this.registerBoolean("1.13", true, () -> ((String)this.page.getValue()).equals("Base"));
    BooleanSetting placeInAir = this.registerBoolean("Place In Air", true, () -> ((String)this.page.getValue()).equals("Base"));
    BooleanSetting base = this.registerBoolean("Place Base", true, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.highVersion.getValue() == false);
    BooleanSetting allPossible = this.registerBoolean("Calc All Possible", true, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    BooleanSetting detectBreak = this.registerBoolean("Detect Break", true, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    BooleanSetting packetBase = this.registerBoolean("Packet Base Place", true, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    BooleanSetting baseSwing = this.registerBoolean("Base Swing", true, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    DoubleSetting toggleDmg = this.registerDouble("Toggle Damage", 8.0, 0.0, 36.0, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    IntegerSetting baseDelay = this.registerInteger("Base Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    DoubleSetting baseMinDmg = this.registerDouble("Base MinDmg", 8.0, 0.0, 36.0, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    DoubleSetting maxY = this.registerDouble("Max Y", 1.0, 0.0, 3.0, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    DoubleSetting maxSpeed = this.registerDouble("Max Target Speed", 10.0, 0.0, 50.0, () -> ((String)this.page.getValue()).equals("Base") && (Boolean)this.base.getValue() != false && (Boolean)this.highVersion.getValue() == false);
    IntegerSetting calcDelay = this.registerInteger("Calc Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay"));
    IntegerSetting updateDelay = this.registerInteger("Update Delay", 0, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay"));
    IntegerSetting placeDelay = this.registerInteger("Place Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay"));
    IntegerSetting breakDelay = this.registerInteger("Break Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay"));
    IntegerSetting switchPlaceDelay = this.registerInteger("Switch Place Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay") && (((String)this.mode.getValue()).equals("Switch") || ((String)this.mode.getValue()).equals("Test")));
    IntegerSetting switchBreakDelay = this.registerInteger("Switch Break Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay") && (((String)this.mode.getValue()).equals("Switch") || ((String)this.mode.getValue()).equals("Test")));
    IntegerSetting stuckPlaceDelay = this.registerInteger("Stuck Place Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay") && (((String)this.mode.getValue()).equals("Stuck") || ((String)this.mode.getValue()).equals("Test")));
    IntegerSetting stuckBreakDelay = this.registerInteger("Stuck Break Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Delay") && (((String)this.mode.getValue()).equals("Stuck") || ((String)this.mode.getValue()).equals("Test")));
    DoubleSetting range = this.registerDouble("Place Range", 5.0, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting yRange = this.registerDouble("Y Range", 2.5, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting enemyRange = this.registerInteger("Enemy Range", 10, 0, 16, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting maxEnemies = this.registerInteger("Max Calc Enemies", 5, 0, 25, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting autorotate = this.registerBoolean("Auto Rotate", true, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting pause = this.registerBoolean("Pause While Burrow", false, () -> ((String)this.page.getValue()).equals("Calc") && (Boolean)this.autorotate.getValue() != false);
    BooleanSetting pitch = this.registerBoolean("Pitch Down", true, () -> ((String)this.page.getValue()).equals("Calc") && (Boolean)this.autorotate.getValue() != false);
    DoubleSetting minDmg = this.registerDouble("Min Damage", 8.0, 0.0, 36.0, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting ignore = this.registerBoolean("Ignore Self Dmg", false, () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting maxSelfDmg = this.registerDouble("Max Self Dmg", 10.0, 1.0, 36.0, () -> ((String)this.page.getValue()).equals("Calc") && (Boolean)this.ignore.getValue() == false);
    BooleanSetting suicide = this.registerBoolean("Anti Suicide", true, () -> ((String)this.page.getValue()).equals("Calc"));
    DoubleSetting balance = this.registerDouble("Health Balance", 2.5, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting facePlaceValue = this.registerInteger("FacePlace HP", 8, 0, 36, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting armorCount = this.registerInteger("ArmorCount", 1, 0, 64, () -> ((String)this.page.getValue()).equals("Calc"));
    IntegerSetting armorRate = this.registerInteger("ArmorDamage", 15, 0, 100, () -> ((String)this.page.getValue()).equals("Calc") && (Integer)this.armorCount.getValue() > 0);
    DoubleSetting fpMinDmg = this.registerDouble("FP Min Damage", 1.0, 0.0, 36.0, () -> ((String)this.page.getValue()).equals("Calc"));
    BooleanSetting forcePlace = this.registerBoolean("Force Place", false, () -> ((String)this.page.getValue()).equals("Calc"));
    ModeSetting handMode = this.registerMode("Hand", Arrays.asList("Main", "Off", "Auto"), "Auto", () -> ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting autoSwitch = this.registerBoolean("Auto Switch", true, () -> ((String)this.page.getValue()).equals("Switch") && !((String)this.handMode.getValue()).equals("OFff"));
    BooleanSetting silentSwitch = this.registerBoolean("Switch Back", true, () -> ((String)this.page.getValue()).equals("Switch") && (Boolean)this.autoSwitch.getValue() != false && this.autoSwitch.isVisible());
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("Switch") && this.autoSwitch.isVisible());
    BooleanSetting refill = this.registerBoolean("Refill Beds", true, () -> ((String)this.page.getValue()).equals("Switch") && this.autoSwitch.isVisible());
    ModeSetting clickMode = this.registerMode("Click Mode", Arrays.asList("Quick", "Swap", "Pickup"), "Quick", () -> ((String)this.page.getValue()).equals("Switch") && (Boolean)this.refill.getValue() != false && this.autoSwitch.isVisible());
    ModeSetting refillMode = this.registerMode("Refill Mode", Arrays.asList("All", "Only"), "All", () -> ((String)this.page.getValue()).equals("Switch") && (Boolean)this.refill.getValue() != false && this.autoSwitch.isVisible());
    IntegerSetting slotS = this.registerInteger("Slot", 1, 1, 9, () -> ((String)this.page.getValue()).equals("Switch") && (Boolean)this.refill.getValue() != false && this.autoSwitch.isVisible());
    BooleanSetting force = this.registerBoolean("Force Refill", false, () -> ((String)this.page.getValue()).equals("Switch") && (Boolean)this.refill.getValue() != false && this.autoSwitch.isVisible());
    BooleanSetting slowFP = this.registerBoolean("Slow Face Place", true, () -> ((String)this.page.getValue()).equals("SlowFacePlace"));
    IntegerSetting slowPlaceDelay = this.registerInteger("SlowFP Place Delay", 500, 0, 1000, () -> (Boolean)this.slowFP.getValue() != false && ((String)this.page.getValue()).equals("SlowFacePlace"));
    IntegerSetting slowBreakDelay = this.registerInteger("SlowFP Break Delay", 500, 0, 1000, () -> (Boolean)this.slowFP.getValue() != false && ((String)this.page.getValue()).equals("SlowFacePlace"));
    DoubleSetting slowMinDmg = this.registerDouble("SlowFP Min Dmg", 0.05, 0.0, 36.0, () -> (Boolean)this.slowFP.getValue() != false && ((String)this.page.getValue()).equals("SlowFacePlace"));
    BooleanSetting showDamage = this.registerBoolean("Render Dmg", true, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting showSelfDamage = this.registerBoolean("Self Dmg", true, () -> ((String)this.page.getValue()).equals("Render") && (Boolean)this.showDamage.getValue() != false);
    ColorSetting color = this.registerColor("Hand Color", new GSColor(255, 0, 0, 50), () -> ((String)this.page.getValue()).equals("Render"));
    ColorSetting color2 = this.registerColor("Base Color", new GSColor(0, 255, 0, 50), () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting alpha = this.registerInteger("Alpha", 60, 0, 255, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting outAlpha = this.registerInteger("Outline Alpha", 120, 0, 255, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting gradient = this.registerBoolean("Gradient", true, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting outGradient = this.registerBoolean("Outline Gradient", true, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting width = this.registerInteger("Width", 1, 1, 10, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting movingTime = this.registerInteger("MovingTime", 0, 0, 500, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting lifeTime = this.registerInteger("FadeTime", 100, 0, 500, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting renderTest = this.registerBoolean("Render Test", false, () -> ((String)this.page.getValue()).equals("Render"));
    ModeSetting hudDisplay = this.registerMode("HUD", Arrays.asList("Target", "Damage", "Both", "None"), "None", () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting hudSelfDamage = this.registerBoolean("Show Self Damage", false, () -> ((String)this.page.getValue()).equals("Render") && (((String)this.hudDisplay.getValue()).equals("Damage") || ((String)this.hudDisplay.getValue()).equals("Both")));
    HashMap<EntityPlayer, MoveRotation> playerSpeed = new HashMap();
    EntityInfo target = null;
    BlockPos headPos;
    BlockPos basePos;
    BlockPos continuE;
    boolean canBasePlace;
    boolean burrow;
    float damage;
    float selfDamage;
    String face;
    Vec3d movingBaseNow = new Vec3d(-1.0, -1.0, -1.0);
    Vec3d movingHeadNow = new Vec3d(-1.0, -1.0, -1.0);
    BlockPos lastBestBase = null;
    BlockPos lastBestHead = null;
    Timing basetiming = new Timing();
    Timing calctiming = new Timing();
    Timing placetiming = new Timing();
    Timing breaktiming = new Timing();
    Timing updatetiming = new Timing();
    EnumHand hand;
    int slot;
    int maxPredict;
    long updateTimeBase;
    long updateTimeHead;
    long startTime;
    Vec2f rotation;
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (this.rotation != null) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                ((CPacketPlayer.Rotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
                if (((Boolean)this.pitch.getValue()).booleanValue()) {
                    ((CPacketPlayer.Rotation)event.getPacket()).field_149473_f = 90.0f;
                }
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
                if (((Boolean)this.pitch.getValue()).booleanValue()) {
                    ((CPacketPlayer.PositionRotation)event.getPacket()).field_149473_f = 90.0f;
                }
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
        PlayerPacket packet = new PlayerPacket((Module)this, new Vec2f(this.rotation.field_189982_i, (Boolean)this.pitch.getValue() != false ? 90.0f : PlayerPacketManager.INSTANCE.getServerSideRotation().field_189983_j));
        PlayerPacketManager.INSTANCE.addPacket(packet);
    }, new Predicate[0]);
    boolean switching = true;

    @Override
    public void onUpdate() {
        if (BedAura.mc.field_71439_g == null || BedAura.mc.field_71441_e == null || EntityUtil.isDead((Entity)BedAura.mc.field_71439_g) || this.inNether()) {
            this.target = null;
            this.basePos = null;
            this.headPos = null;
            this.selfDamage = 0.0f;
            this.damage = 0.0f;
            this.rotation = null;
            return;
        }
        for (EntityPlayer player : BedAura.mc.field_71441_e.field_73010_i) {
            if (BedAura.mc.field_71439_g.func_70068_e((Entity)player) > (double)((Integer)this.enemyRange.getValue() * (Integer)this.enemyRange.getValue())) continue;
            double lastYaw = 512.0;
            int tick = (Integer)this.startTick.getValue();
            if (this.playerSpeed.get(player) != null) {
                MoveRotation info = this.playerSpeed.get(player);
                lastYaw = info.yaw;
                tick = info.tick + (Integer)this.addTick.getValue();
            }
            if (tick > this.maxPredict) {
                tick = this.maxPredict;
            }
            this.playerSpeed.put(player, new MoveRotation(player, lastYaw, tick));
        }
        this.calc();
    }

    @Override
    public void fast() {
        if (BedAura.mc.field_71439_g == null || BedAura.mc.field_71441_e == null || EntityUtil.isDead((Entity)BedAura.mc.field_71439_g) || this.inNether()) {
            return;
        }
        if (this.updatetiming.passedMs(((Integer)this.updateDelay.getValue()).intValue())) {
            NetworkPlayerInfo info;
            BlockPos pos;
            this.updatetiming.reset();
            this.burrow = (Boolean)this.pause.getValue() != false ? this.isBurrow(pos = PlayerUtil.getPlayerPos()) && !this.isBurrow(pos.func_177984_a()) : false;
            this.maxPredict = (Integer)this.tickPredict.getValue();
            NetHandlerPlayClient connection = mc.func_147114_u();
            if (((Boolean)this.detect.getValue()).booleanValue() && connection != null && (info = connection.func_175102_a(mc.func_147114_u().func_175105_e().getId())) != null) {
                this.maxPredict = info.func_178853_c() * 2 / 50;
            }
            if (((Boolean)this.base.getValue()).booleanValue() && this.basetiming.passedMs(((Integer)this.baseDelay.getValue()).intValue())) {
                this.canBasePlace = true;
                this.basetiming.reset();
            }
        }
        if (this.continuE != null && !BedAura.isPos2(this.continuE, this.basePos) && this.isBed(this.continuE)) {
            this.switching = true;
        }
        this.bedaura();
    }

    private boolean isBurrow(BlockPos pos) {
        AxisAlignedBB box = BlockUtil.getBoundingBox(pos);
        if (box == null) {
            return false;
        }
        if (!BedAura.mc.field_71439_g.field_70121_D.func_72326_a(box)) {
            return false;
        }
        Block block = BlockUtil.getBlock(pos);
        return block == Blocks.field_150343_Z || block == Blocks.field_150357_h || block == Blocks.field_150477_bB;
    }

    /*
     * Unable to fully structure code
     */
    private void bedaura() {
        if (((Boolean)this.renderTest.getValue()).booleanValue() || this.headPos == null || this.basePos == null) {
            return;
        }
        if (this.target.defaultPlayer == null || ColorMain.INSTANCE.breakList.contains(this.basePos) || ColorMain.INSTANCE.breakList.contains(this.headPos)) {
            this.place((Integer)this.placeDelay.getValue());
            this.breakBed((Integer)this.breakDelay.getValue());
            return;
        }
        var1_1 = (String)this.mode.getValue();
        var2_2 = -1;
        switch (var1_1.hashCode()) {
            case 1700657240: {
                if (!var1_1.equals("PlaceBreak")) break;
                var2_2 = 0;
                break;
            }
            case -1012124824: {
                if (!var1_1.equals("BreakPlace")) break;
                var2_2 = 1;
                break;
            }
            case -1805606060: {
                if (!var1_1.equals("Switch")) break;
                var2_2 = 2;
                break;
            }
            case 80223612: {
                if (!var1_1.equals("Stuck")) break;
                var2_2 = 3;
                break;
            }
            case 2603186: {
                if (!var1_1.equals("Test")) break;
                var2_2 = 4;
            }
        }
        switch (var2_2) {
            case 0: {
                this.place((Integer)this.placeDelay.getValue());
                this.breakBed((Integer)this.breakDelay.getValue());
                break;
            }
            case 1: {
                this.breakBed((Integer)this.breakDelay.getValue());
                this.place((Integer)this.placeDelay.getValue());
                break;
            }
            case 2: {
                if (!this.switching) ** GOTO lbl49
                if (this.place((Integer)this.placeDelay.getValue()) || this.breakBed((Integer)this.breakDelay.getValue())) {
                    this.switching = false;
                }
                ** GOTO lbl51
lbl49:
                // 1 sources

                if (this.breakBed((Integer)this.switchBreakDelay.getValue()) || this.place((Integer)this.switchPlaceDelay.getValue())) {
                    this.switching = true;
                }
            }
lbl51:
            // 5 sources

            case 3: {
                if (this.stuck(this.target)) {
                    this.breakBed((Integer)this.stuckBreakDelay.getValue());
                    this.place((Integer)this.stuckPlaceDelay.getValue());
                    break;
                }
                this.place((Integer)this.placeDelay.getValue());
                this.breakBed((Integer)this.breakDelay.getValue());
                break;
            }
            case 4: {
                if (this.stuck(this.target)) {
                    this.breakBed((Integer)this.stuckBreakDelay.getValue());
                    this.place((Integer)this.stuckPlaceDelay.getValue());
                    break;
                }
                if (this.switching) {
                    if (!this.place((Integer)this.placeDelay.getValue()) && !this.breakBed((Integer)this.breakDelay.getValue())) break;
                    this.switching = false;
                    break;
                }
                if (!this.breakBed((Integer)this.switchBreakDelay.getValue()) && !this.place((Integer)this.switchPlaceDelay.getValue())) break;
                this.switching = true;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void calc() {
        block23: {
            BlockPos bedPos;
            block22: {
                block24: {
                    block25: {
                        boolean offhand;
                        if (!this.calctiming.passedMs(((Integer)this.calcDelay.getValue()).intValue())) break block23;
                        this.calctiming.reset();
                        this.target = null;
                        this.basePos = null;
                        this.headPos = null;
                        this.selfDamage = 0.0f;
                        this.damage = 0.0f;
                        this.rotation = null;
                        boolean bl = offhand = !((String)this.handMode.getValue()).equals("Main") && BedAura.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151104_aV;
                        if (!offhand && !((String)this.handMode.getValue()).equals("Off")) {
                            if (((Boolean)this.refill.getValue()).booleanValue()) {
                                this.refill_bed();
                            }
                            this.slot = BurrowUtil.findHotbarBlock(ItemBed.class);
                            if (this.slot == -1) {
                                return;
                            }
                        }
                        this.hand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                        EntityInfo self = new EntityInfo((EntityPlayer)BedAura.mc.field_71439_g, (Boolean)this.selfPredict.getValue());
                        PlaceInfo placeInfo = this.getPlaceInfo(self, this.findBlocksExcluding((Boolean)this.base.getValue() != false && this.canBasePlace));
                        if (placeInfo != null) {
                            this.target = placeInfo.target;
                            if (ModuleManager.isModuleEnabled("AutoEz")) {
                                AutoEz.INSTANCE.addTargetedPlayer(this.target.defaultPlayer.func_70005_c_());
                            }
                            if (((Boolean)this.base.getValue()).booleanValue() && placeInfo.basePos != null) {
                                this.canBasePlace = false;
                                int obbySlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
                                BlockPos pos = placeInfo.basePos;
                                InventoryUtil.run(obbySlot, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, (Boolean)this.packetBase.getValue(), false, (Boolean)this.baseSwing.getValue()));
                            }
                        } else {
                            ArrayList<Entity> entityList = new ArrayList<Entity>();
                            for (Entity entity : BedAura.mc.field_71441_e.field_72996_f) {
                                if (BedAura.mc.field_71439_g.func_70032_d(entity) > (float)((Integer)this.enemyRange.getValue()).intValue() || EntityUtil.isDead(entity)) continue;
                                if (((Boolean)this.monster.getValue()).booleanValue() && EntityUtil.isMobAggressive(entity)) {
                                    entityList.add(entity);
                                }
                                if (((Boolean)this.neutral.getValue()).booleanValue() && EntityUtil.isNeutralMob(entity)) {
                                    entityList.add(entity);
                                }
                                if (!((Boolean)this.animal.getValue()).booleanValue() || !EntityUtil.isPassive(entity)) continue;
                                entityList.add(entity);
                            }
                            placeInfo = this.calculatePlacement(this.getNearestEntity(entityList), self, this.findBlocksExcluding(true));
                            this.target = placeInfo.target;
                        }
                        if ((bedPos = placeInfo.placePos) == null) {
                            return;
                        }
                        this.damage = placeInfo.damage;
                        this.selfDamage = placeInfo.selfDamage;
                        this.headPos = bedPos;
                        switch (RotationUtil.getFacing(PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i)) {
                            case SOUTH: {
                                this.face = "SOUTH";
                                this.rotation = new Vec2f(0.0f, 90.0f);
                                bedPos = new BlockPos(this.headPos.field_177962_a, this.headPos.field_177960_b, this.headPos.field_177961_c - 1);
                                break;
                            }
                            case WEST: {
                                this.face = "WEST";
                                this.rotation = new Vec2f(90.0f, 90.0f);
                                bedPos = new BlockPos(this.headPos.field_177962_a + 1, this.headPos.field_177960_b, this.headPos.field_177961_c);
                                break;
                            }
                            case NORTH: {
                                this.face = "NORTH";
                                this.rotation = new Vec2f(180.0f, 90.0f);
                                bedPos = new BlockPos(this.headPos.field_177962_a, this.headPos.field_177960_b, this.headPos.field_177961_c + 1);
                                break;
                            }
                            case EAST: {
                                this.face = "EAST";
                                this.rotation = new Vec2f(-90.0f, 90.0f);
                                bedPos = new BlockPos(this.headPos.field_177962_a - 1, this.headPos.field_177960_b, this.headPos.field_177961_c);
                                break;
                            }
                        }
                        if (this.block(bedPos, true, true)) break block22;
                        if (!((Boolean)this.autorotate.getValue()).booleanValue() || this.burrow) break block24;
                        if (!this.block(this.headPos.func_177974_f(), true, true)) break block25;
                        this.face = "WEST";
                        this.rotation = new Vec2f(90.0f, 90.0f);
                        bedPos = new BlockPos(this.headPos.field_177962_a + 1, this.headPos.field_177960_b, this.headPos.field_177961_c);
                        break block22;
                    }
                    if (this.block(this.headPos.func_177978_c(), true, true)) {
                        this.face = "SOUTH";
                        this.rotation = new Vec2f(0.0f, 90.0f);
                        bedPos = new BlockPos(this.headPos.field_177962_a, this.headPos.field_177960_b, this.headPos.field_177961_c - 1);
                        break block22;
                    } else if (this.block(this.headPos.func_177976_e(), true, true)) {
                        this.face = "EAST";
                        this.rotation = new Vec2f(-90.0f, 90.0f);
                        bedPos = new BlockPos(this.headPos.field_177962_a - 1, this.headPos.field_177960_b, this.headPos.field_177961_c);
                        break block22;
                    } else {
                        if (!this.block(this.headPos.func_177968_d(), true, true)) {
                            this.target = null;
                            this.basePos = null;
                            this.headPos = null;
                            this.selfDamage = 0.0f;
                            this.damage = 0.0f;
                            this.rotation = null;
                            return;
                        }
                        this.face = "NORTH";
                        this.rotation = new Vec2f(180.0f, 90.0f);
                        bedPos = new BlockPos(this.headPos.field_177962_a, this.headPos.field_177960_b, this.headPos.field_177961_c + 1);
                    }
                    break block22;
                }
                this.target = null;
                this.basePos = null;
                this.headPos = null;
                this.selfDamage = 0.0f;
                this.damage = 0.0f;
                this.rotation = null;
                return;
            }
            this.headPos = this.headPos.func_177984_a();
            this.basePos = bedPos.func_177984_a();
        }
    }

    private boolean place(int delay) {
        if (((Boolean)this.checkBed.getValue()).booleanValue() && (this.isBed(this.headPos) || this.isBed(this.basePos))) {
            return true;
        }
        if (this.placetiming.passedMs(this.getPlaceDelay(delay))) {
            if (this.continuE == null || this.continuE.func_177951_i((Vec3i)this.basePos) > 14.0 || BlockUtil.getBlock(this.continuE) != Blocks.field_150324_C) {
                this.continuE = this.basePos;
            }
            BlockPos neighbour = this.basePos.func_177977_b();
            EnumFacing opposite = EnumFacing.DOWN.func_176734_d();
            Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
            if (BlockUtil.blackList.contains(BedAura.mc.field_71441_e.func_180495_p(neighbour).func_177230_c()) && !ColorMain.INSTANCE.sneaking) {
                BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedAura.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            }
            this.run(() -> {
                if (((Boolean)this.packetPlace.getValue()).booleanValue()) {
                    BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(neighbour, EnumFacing.UP, this.hand, 0.5f, 1.0f, 0.5f));
                } else {
                    BedAura.mc.field_71442_b.func_187099_a(BedAura.mc.field_71439_g, BedAura.mc.field_71441_e, neighbour, EnumFacing.UP, hitVec, this.hand);
                }
            }, this.slot);
            if (((Boolean)this.placeSwing.getValue()).booleanValue()) {
                this.swing(this.hand);
            }
            this.placetiming.reset();
            return true;
        }
        return false;
    }

    private void run(Runnable runnable, int slot) {
        if (this.hand == EnumHand.OFF_HAND) {
            runnable.run();
            return;
        }
        int oldslot = BedAura.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
        } else {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                InventoryUtil.packetSwitch(slot);
            } else {
                InventoryUtil.switchSlot(slot);
            }
            runnable.run();
            if (((Boolean)this.silentSwitch.getValue()).booleanValue()) {
                if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                    InventoryUtil.packetSwitch(oldslot);
                } else {
                    InventoryUtil.switchSlot(oldslot);
                }
            }
            BedAura.mc.field_71439_g.field_71070_bA.func_75142_b();
        }
    }

    private boolean breakBed(int delay) {
        if (this.breaktiming.passedMs(this.getBreakDelay(delay))) {
            EnumFacing side = EnumFacing.UP;
            Vec3d facing = this.getHitVecOffset(side);
            if (ModuleManager.getModule(ColorMain.class).sneaking) {
                BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedAura.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.basePos, side, this.hand, (float)facing.field_72450_a, (float)facing.field_72448_b, (float)facing.field_72449_c));
            if (this.isBed(this.headPos) && !this.isBed(this.basePos)) {
                BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.headPos, side, this.hand, (float)facing.field_72450_a, (float)facing.field_72448_b, (float)facing.field_72449_c));
            }
            if (((Boolean)this.breakSwing.getValue()).booleanValue()) {
                this.swing(this.hand);
            }
            this.breaktiming.reset();
            return true;
        }
        return false;
    }

    private PlaceInfo getPlaceInfo(EntityInfo self, List<BlockPos> posList) {
        PlaceInfo placeInfo = null;
        List<EntityPlayer> playerList = PlayerUtil.getNearPlayers(((Integer)this.enemyRange.getValue()).intValue(), (Integer)this.maxEnemies.getValue());
        switch ((String)this.targetMode.getValue()) {
            case "Nearest": {
                EntityPlayer entityPlayer = playerList.stream().min(Comparator.comparing(p -> Float.valueOf(BedAura.mc.field_71439_g.func_70032_d((Entity)p)))).orElse(null);
                if (entityPlayer == null) break;
                EntityInfo player = new EntityInfo(entityPlayer, (Boolean)this.predict.getValue());
                placeInfo = this.calculateBestPlacement(player, self, posList);
                break;
            }
            case "Damage": {
                PlaceInfo best = null;
                for (EntityPlayer entityPlayer : playerList) {
                    if (entityPlayer == null) continue;
                    EntityInfo player = new EntityInfo(entityPlayer, (Boolean)this.predict.getValue());
                    PlaceInfo info = this.calculateBestPlacement(player, self, posList);
                    if (best != null && !(info.damage > best.damage)) continue;
                    best = info;
                }
                placeInfo = best;
                break;
            }
            case "Health": {
                double health = 37.0;
                EntityPlayer player = null;
                for (EntityPlayer entityPlayer : playerList) {
                    if (player != null && !(health > (double)(entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj()))) continue;
                    player = entityPlayer;
                    health = entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj();
                }
                if (player == null) break;
                placeInfo = this.calculateBestPlacement(new EntityInfo(player, (Boolean)this.predict.getValue()), self, posList);
                break;
            }
            case "Smart": {
                ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
                for (EntityPlayer entityPlayer : playerList) {
                    if (!((Double)this.smartHealth.getValue() >= (double)(entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj()))) continue;
                    players.add(entityPlayer);
                }
                EntityPlayer target = players.stream().min(Comparator.comparing(p -> Float.valueOf(p.func_110143_aJ() + p.func_110139_bj()))).orElse(null);
                PlaceInfo best = null;
                if (target != null) {
                    EntityInfo player = new EntityInfo(target, (Boolean)this.predict.getValue());
                    best = this.calculateBestPlacement(player, self, posList);
                }
                if (best == null) {
                    for (EntityPlayer entityPlayer : playerList) {
                        if (entityPlayer == null) continue;
                        EntityInfo player = new EntityInfo(entityPlayer, (Boolean)this.predict.getValue());
                        PlaceInfo info = this.calculateBestPlacement(player, self, posList);
                        if (best != null && !(info.damage > best.damage)) continue;
                        best = info;
                    }
                }
                placeInfo = best;
                break;
            }
        }
        return placeInfo;
    }

    private List<BlockPos> findBlocksExcluding(boolean calcWithOutBase) {
        return EntityUtil.getSphere(PlayerUtil.getEyesPos(), (Double)this.range.getValue() + 1.0, (Double)this.yRange.getValue(), false, false, 0).stream().filter(pos -> this.canPlaceBed((BlockPos)pos, !calcWithOutBase)).collect(Collectors.toList());
    }

    private boolean canFacePlace(EntityInfo target) {
        if (target.hp <= (double)((Integer)this.facePlaceValue.getValue()).intValue()) {
            return true;
        }
        for (ItemStack itemStack : target.defaultPlayer.func_184193_aE()) {
            float dmg;
            if (itemStack.func_190926_b() || itemStack.func_190916_E() > (Integer)this.armorRate.getValue() || !((dmg = ((float)itemStack.func_77958_k() - (float)itemStack.func_77952_i()) / (float)itemStack.func_77958_k()) < (float)((Integer)this.armorRate.getValue()).intValue() / 100.0f)) continue;
            return true;
        }
        return false;
    }

    private PlaceInfo calculateBestPlacement(EntityInfo target, EntityInfo self, List<BlockPos> blocks) {
        PlaceInfo best = new PlaceInfo(target, null, (float)Math.min(Math.min((Double)this.minDmg.getValue(), (Double)this.slowMinDmg.getValue()), this.fpMinDmg.getMin()), -1.0f, null);
        if (target == null || self == null) {
            return best;
        }
        boolean facePlace = this.canFacePlace(target);
        for (BlockPos pos : blocks) {
            boolean canPlace;
            BlockPos basePos = null;
            boolean air = BlockUtil.canReplace(pos);
            boolean bl = canPlace = (Boolean)this.highVersion.getValue() != false || !air && !this.needBase(pos);
            if (!canPlace && (!((Boolean)this.base.getValue()).booleanValue() || (double)best.damage >= (Double)this.toggleDmg.getValue() && best.basePos == null || (double)(pos.func_177956_o() + 1) > target.player.field_70163_u + (Double)this.maxY.getValue() || BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1 || LemonClient.speedUtil.getPlayerSpeed(target.defaultPlayer) > (Double)this.maxSpeed.getValue() || (basePos = this.getBestBasePos(pos)) == null)) continue;
            double x = (double)pos.func_177958_n() + 0.5;
            double y = (double)pos.func_177956_o() + 1.5625;
            double z = (double)pos.func_177952_p() + 0.5;
            float targetDamage = DamageUtil.calculateDamage((EntityLivingBase)target.defaultPlayer, target.position, target.boundingBox, x, y, z, 5.0f, "Bed");
            if (!canPlace && ((double)targetDamage < (Double)this.baseMinDmg.getValue() || targetDamage == best.damage) || targetDamage < best.damage || (facePlace ? (double)targetDamage < (Double)this.fpMinDmg.getValue() : (double)targetDamage < (Double)this.minDmg.getValue() && ((double)targetDamage < (Double)this.slowMinDmg.getValue() || (Boolean)this.slowFP.getValue() == false))) continue;
            float selfDamage = 0.0f;
            if (!self.player.func_184812_l_()) {
                selfDamage = DamageUtil.calculateDamage((EntityLivingBase)self.defaultPlayer, self.position, self.boundingBox, x, y, z, 5.0f, "Bed");
                if ((double)selfDamage + (Double)this.balance.getValue() > (Double)this.maxSelfDmg.getValue() && (!((double)targetDamage >= target.hp) ? (Boolean)this.ignore.getValue() == false : (Boolean)this.forcePlace.getValue() == false)) continue;
                if (((Boolean)this.suicide.getValue()).booleanValue() && (double)selfDamage + (Double)this.balance.getValue() >= self.hp) continue;
            }
            best = new PlaceInfo(target, pos, targetDamage, selfDamage, basePos);
        }
        return best;
    }

    private PlaceInfo calculatePlacement(EntityLivingBase target, EntityInfo self, List<BlockPos> poslist) {
        PlaceInfo best = new PlaceInfo(new EntityInfo(target), null, (float)Math.min(Math.min((Double)this.minDmg.getValue(), (Double)this.slowMinDmg.getValue()), this.fpMinDmg.getMin()), -1.0f, null);
        if (target == null || self == null) {
            return best;
        }
        for (BlockPos pos : poslist) {
            double x = (double)pos.func_177958_n() + 0.5;
            double y = (double)pos.func_177956_o() + 1.5625;
            double z = (double)pos.func_177952_p() + 0.5;
            float targetDamage = DamageUtil.calculateDamage(target, target.func_174791_d(), target.field_70121_D, x, y, z, 5.0f, "Bed");
            float selfDamage = DamageUtil.calculateDamage((EntityLivingBase)self.defaultPlayer, self.position, self.boundingBox, x, y, z, 5.0f, "Bed");
            if ((double)targetDamage < (Double)this.minDmg.getValue() && ((double)targetDamage < (Double)this.slowMinDmg.getValue() || !((Boolean)this.slowFP.getValue()).booleanValue()) && (double)targetDamage < (Double)this.fpMinDmg.getValue() || !self.player.func_184812_l_() && ((double)selfDamage + (Double)this.balance.getValue() > (Double)this.maxSelfDmg.getValue() && !((Boolean)this.ignore.getValue()).booleanValue() || ((Boolean)this.suicide.getValue()).booleanValue() && (double)selfDamage + (Double)this.balance.getValue() >= self.hp) || !(targetDamage > best.damage)) continue;
            best = new PlaceInfo(new EntityInfo(target), pos, targetDamage, selfDamage, null);
        }
        return best;
    }

    private boolean near(EntityInfo player) {
        AxisAlignedBB box = player.defaultPlayer.field_70121_D;
        if (box.func_72326_a(this.bedBoundingBox(this.basePos)) && box.func_72326_a(this.bedBoundingBox(this.basePos))) {
            return false;
        }
        boolean near = (int)(player.defaultPlayer.field_70163_u + 0.5) + 2 >= this.headPos.field_177960_b && (player.defaultPlayer.func_70011_f((double)this.headPos.func_177958_n() + 0.5, (double)this.headPos.func_177956_o() + 0.25, (double)this.headPos.func_177952_p() + 0.5) < 2.5 || player.defaultPlayer.func_70011_f((double)this.basePos.func_177958_n() + 0.5, player.defaultPlayer.field_70163_u, (double)this.basePos.func_177952_p() + 0.5) < 2.5) && player.defaultPlayer.func_70032_d((Entity)BedAura.mc.field_71439_g) <= 6.0f;
        boolean predictNear = player.player.field_70163_u > (double)this.headPos.field_177960_b && (player.player.func_70011_f((double)this.headPos.func_177958_n() + 0.5, (double)this.headPos.func_177956_o() + 0.25, (double)this.headPos.func_177952_p() + 0.5) < 2.5 || player.player.func_70011_f((double)this.basePos.func_177958_n() + 0.5, player.player.field_70163_u, (double)this.basePos.func_177952_p() + 0.5) < 1.5) && player.player.func_70032_d((Entity)BedAura.mc.field_71439_g) <= 6.0f;
        return near || predictNear;
    }

    private boolean stuck(EntityPlayer player) {
        return player.field_70163_u - (double)((int)player.field_70163_u) > 0.3;
    }

    private boolean stuck(EntityInfo target) {
        EntityPlayer player = target.defaultPlayer;
        EntityPlayer predict = target.player;
        boolean inAir = true;
        for (Vec3d vec3d : new Vec3d[]{new Vec3d(0.25, 0.0, 0.25), new Vec3d(0.25, 0.0, -0.25), new Vec3d(-0.25, 0.0, 0.25), new Vec3d(-0.25, 0.0, -0.25)}) {
            BlockPos pos = new BlockPos(player.field_70165_t + vec3d.field_72450_a, player.field_70163_u + 0.7, player.field_70161_v + vec3d.field_72449_c);
            if (BlockUtil.canReplace(pos = pos.func_177977_b()) || BlockUtil.getBlock(pos) == Blocks.field_150324_C) continue;
            inAir = false;
            break;
        }
        double y = predict.field_70163_u - player.field_70163_u;
        return this.near(target) && (this.stuck(player) || this.stuck(predict) || inAir || y > 0.5 || y < -0.5);
    }

    private AxisAlignedBB bedBoundingBox(BlockPos pos) {
        return new AxisAlignedBB((double)pos.field_177962_a, (double)pos.field_177960_b, (double)pos.field_177961_c, (double)(pos.field_177962_a + 1), (double)pos.field_177960_b + 0.4, (double)(pos.field_177961_c + 1));
    }

    private int getPlaceDelay(int value) {
        if ((double)this.damage < (Double)this.minDmg.getValue()) {
            return (Integer)this.slowPlaceDelay.getValue();
        }
        return value;
    }

    private int getBreakDelay(int value) {
        if ((double)this.damage < (Double)this.minDmg.getValue()) {
            return (Integer)this.slowBreakDelay.getValue();
        }
        return value;
    }

    private EntityLivingBase getNearestEntity(List<Entity> list) {
        return list.stream().filter(target -> target instanceof EntityLivingBase).min(Comparator.comparing(p -> Float.valueOf(BedAura.mc.field_71439_g.func_70032_d(p)))).orElse(null);
    }

    private boolean canPlaceBed(BlockPos blockPos, boolean baseCheck) {
        if (!this.block(blockPos, (Boolean)this.highVersion.getValue() == false || (Boolean)this.allPossible.getValue() == false || baseCheck, false)) {
            return false;
        }
        if (((Boolean)this.autorotate.getValue()).booleanValue() && !this.burrow) {
            for (EnumFacing facing : EnumFacing.field_82609_l) {
                if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
                BlockPos pos = blockPos.func_177972_a(facing);
                if (!this.block(pos, (Boolean)this.highVersion.getValue() != false && (Boolean)this.placeInAir.getValue() == false || baseCheck, true)) continue;
                return true;
            }
            return false;
        }
        BlockPos pos = blockPos.func_177967_a(RotationUtil.getFacing(PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i), -1);
        return this.block(pos, (Boolean)this.highVersion.getValue() != false && (Boolean)this.placeInAir.getValue() == false || baseCheck, true) && this.inRange(pos.func_177984_a());
    }

    private boolean canPlaceBase(BlockPos pos) {
        if (((Boolean)this.detectBreak.getValue()).booleanValue() && ColorMain.INSTANCE.breakList.contains(pos)) {
            return false;
        }
        if (!this.inRange(pos)) {
            return false;
        }
        if (BurrowUtil.getBedFacing(pos) == null) {
            return false;
        }
        return this.space(pos.func_177984_a()) && !this.intersectsWithEntity(pos);
    }

    private boolean needBase(BlockPos pos) {
        for (BlockPos side : this.sides) {
            BlockPos blockPos = pos.func_177971_a((Vec3i)side);
            if (!this.space(blockPos.func_177984_a()) || !this.inRange(blockPos.func_177984_a()) || BlockUtil.canReplace(blockPos) || !this.solid(pos)) continue;
            return false;
        }
        return true;
    }

    private BlockPos getBestBasePos(BlockPos pos) {
        BlockPos bestPos = null;
        double bestRange = 1000.0;
        if (!((Boolean)this.autorotate.getValue()).booleanValue() || this.burrow) {
            BlockPos base = pos.func_177967_a(RotationUtil.getFacing(PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i), -1);
            if (this.canPlaceBase(base)) {
                return base;
            }
        } else {
            for (BlockPos side : this.sides) {
                BlockPos base = pos.func_177971_a((Vec3i)side);
                if (!this.canPlaceBase(base) || this.intersectsWithEntity(pos) || bestPos != null && !(bestRange > BedAura.mc.field_71439_g.func_174818_b(base))) continue;
                bestRange = BedAura.mc.field_71439_g.func_174818_b(base);
                bestPos = base;
            }
            return bestPos;
        }
        return null;
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : BedAura.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private boolean block(BlockPos pos, boolean baseCheck, boolean rangeCheck) {
        if (!this.space(pos.func_177984_a())) {
            return false;
        }
        if (BlockUtil.canReplace(pos) ? baseCheck || !this.canPlaceBase(pos) : (Boolean)this.highVersion.getValue() == false && !this.solid(pos)) {
            return false;
        }
        return !rangeCheck || this.inRange(pos.func_177984_a());
    }

    private boolean isBed(BlockPos pos) {
        Block block = BedAura.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return block == Blocks.field_150324_C || block instanceof BlockBed;
    }

    private boolean space(BlockPos pos) {
        return BedAura.mc.field_71441_e.func_175623_d(pos) || BedAura.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C;
    }

    private boolean solid(BlockPos pos) {
        return !BlockUtil.isBlockUnSolid(pos) && !(BedAura.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockBed) && BedAura.mc.field_71441_e.func_180495_p(pos).isSideSolid((IBlockAccess)BedAura.mc.field_71441_e, pos, EnumFacing.UP) && BlockUtil.getBlock((BlockPos)pos).field_149787_q;
    }

    private boolean inRange(BlockPos pos) {
        double x = (double)pos.field_177962_a - BedAura.mc.field_71439_g.field_70165_t;
        double z = (double)pos.field_177961_c - BedAura.mc.field_71439_g.field_70161_v;
        double y = pos.field_177960_b - PlayerUtil.getEyesPos().field_177960_b;
        double add = Math.sqrt(y * y) / 2.0;
        return x * x + z * z <= ((Double)this.range.getValue() - add) * ((Double)this.range.getValue() - add) && y * y <= (Double)this.yRange.getValue() * (Double)this.yRange.getValue();
    }

    private static boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    public void refill_bed() {
        int airSlot;
        if ((!(BedAura.mc.field_71462_r instanceof GuiContainer) || BedAura.mc.field_71462_r instanceof GuiInventory) && (airSlot = this.isSpace()) != -1) {
            for (int i = 9; i < 36; ++i) {
                if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151104_aV) continue;
                if (((String)this.clickMode.getValue()).equalsIgnoreCase("Quick")) {
                    if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(airSlot).func_77973_b() != Items.field_190931_a) {
                        BedAura.mc.field_71442_b.func_187098_a(BedAura.mc.field_71439_g.field_71069_bz.field_75152_c, airSlot + 36, 0, ClickType.QUICK_MOVE, (EntityPlayer)BedAura.mc.field_71439_g);
                    }
                    BedAura.mc.field_71442_b.func_187098_a(BedAura.mc.field_71439_g.field_71069_bz.field_75152_c, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)BedAura.mc.field_71439_g);
                    break;
                }
                if (((String)this.clickMode.getValue()).equalsIgnoreCase("Swap")) {
                    BedAura.mc.field_71442_b.func_187098_a(0, i, airSlot, ClickType.SWAP, (EntityPlayer)BedAura.mc.field_71439_g);
                    break;
                }
                BedAura.mc.field_71442_b.func_187098_a(BedAura.mc.field_71439_g.field_71069_bz.field_75152_c, i, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.field_71439_g);
                BedAura.mc.field_71442_b.func_187098_a(BedAura.mc.field_71439_g.field_71069_bz.field_75152_c, airSlot + 36, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.field_71439_g);
                break;
            }
        }
    }

    private int isSpace() {
        int slot;
        block4: {
            block5: {
                block2: {
                    block3: {
                        slot = -1;
                        if (!((Boolean)this.force.getValue()).booleanValue()) break block2;
                        if (!((String)this.refillMode.getValue()).equals("Only")) break block3;
                        int slot1 = (Integer)this.slotS.getValue() - 1;
                        if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(slot1).func_77973_b() == Items.field_151104_aV) break block4;
                        slot = slot1;
                        break block4;
                    }
                    for (int i = 0; i < 9; ++i) {
                        if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_151104_aV) continue;
                        slot = i;
                    }
                    break block4;
                }
                if (!((String)this.refillMode.getValue()).equals("Only")) break block5;
                int slot1 = (Integer)this.slotS.getValue() - 1;
                if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(slot1).func_77973_b() != Items.field_190931_a) break block4;
                slot = slot1;
                break block4;
            }
            for (int i = 0; i < 9; ++i) {
                if (BedAura.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190931_a) continue;
                slot = i;
            }
        }
        return slot;
    }

    private Vec3d getHitVecOffset(EnumFacing face) {
        Vec3i vec = face.func_176730_m();
        return new Vec3d((double)((float)vec.field_177962_a * 0.5f + 0.5f), (double)((float)vec.field_177960_b * 0.5f + 0.5f), (double)((float)vec.field_177961_c * 0.5f + 0.5f));
    }

    private void swing(EnumHand hand) {
        if (((Boolean)this.packetSwing.getValue()).booleanValue()) {
            BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(hand));
        } else {
            BedAura.mc.field_71439_g.func_184609_a(hand);
        }
    }

    private boolean inNether() {
        return BedAura.mc.field_71439_g.field_71093_bK == 0;
    }

    @Override
    public void onEnable() {
        this.calctiming.reset();
        this.basetiming.reset();
        this.placetiming.reset();
        this.breaktiming.reset();
        this.updatetiming.reset();
        this.continuE = null;
        this.switching = true;
        this.updateTimeBase = System.currentTimeMillis();
        this.updateTimeHead = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
        this.lastBestBase = null;
        this.lastBestHead = null;
        this.movingBaseNow = new Vec3d(-1.0, -1.0, -1.0);
        this.movingHeadNow = new Vec3d(-1.0, -1.0, -1.0);
    }

    @Override
    public void onDisable() {
        this.headPos = null;
        this.basePos = null;
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (BedAura.mc.field_71441_e == null || BedAura.mc.field_71439_g == null) {
            return;
        }
        BlockPos nowBase = this.basePos;
        BlockPos nowHead = this.headPos;
        if (nowBase != this.lastBestBase) {
            if (this.basePos != null && this.lastBestBase == null) {
                this.movingBaseNow = new Vec3d((double)this.basePos.func_177958_n(), (double)this.basePos.func_177956_o(), (double)this.basePos.func_177952_p());
            }
            this.updateTimeBase = System.currentTimeMillis();
            if (this.basePos == null) {
                this.startTime = System.currentTimeMillis();
            } else if (this.lastBestBase == null) {
                this.startTime = System.currentTimeMillis();
            }
            this.lastBestBase = this.basePos;
        }
        if (nowHead != this.lastBestHead) {
            if (this.headPos != null && this.lastBestHead == null) {
                this.movingHeadNow = new Vec3d((double)this.headPos.func_177958_n(), (double)this.headPos.func_177956_o(), (double)this.headPos.func_177952_p());
            }
            this.updateTimeHead = System.currentTimeMillis();
            this.lastBestHead = this.headPos;
        }
        if (this.lastBestBase != null) {
            if (this.movingBaseNow.field_72450_a == -1.0 && this.movingBaseNow.field_72448_b == -1.0 && this.movingBaseNow.field_72449_c == -1.0) {
                this.movingBaseNow = new Vec3d((double)this.lastBestBase.func_177958_n(), (double)this.lastBestBase.func_177956_o(), (double)this.lastBestBase.func_177952_p());
            }
            this.movingBaseNow = (Integer)this.movingTime.getValue() == 0 ? new Vec3d((Vec3i)this.lastBestBase) : new Vec3d(this.movingBaseNow.field_72450_a + ((double)this.lastBestBase.func_177958_n() - this.movingBaseNow.field_72450_a) * (double)this.toDelta(this.updateTimeBase, ((Integer)this.movingTime.getValue()).intValue()), this.movingBaseNow.field_72448_b + ((double)this.lastBestBase.func_177956_o() - this.movingBaseNow.field_72448_b) * (double)this.toDelta(this.updateTimeBase, ((Integer)this.movingTime.getValue()).intValue()), this.movingBaseNow.field_72449_c + ((double)this.lastBestBase.func_177952_p() - this.movingBaseNow.field_72449_c) * (double)this.toDelta(this.updateTimeBase, ((Integer)this.movingTime.getValue()).intValue()));
            if (this.movingHeadNow.field_72450_a == -1.0 && this.movingHeadNow.field_72448_b == -1.0 && this.movingHeadNow.field_72449_c == -1.0) {
                this.movingHeadNow = new Vec3d((double)this.lastBestHead.func_177958_n(), (double)this.lastBestHead.func_177956_o(), (double)this.lastBestHead.func_177952_p());
            }
            this.movingHeadNow = (Integer)this.movingTime.getValue() == 0 ? new Vec3d((Vec3i)this.lastBestHead) : new Vec3d(this.movingHeadNow.field_72450_a + ((double)this.lastBestHead.func_177958_n() - this.movingHeadNow.field_72450_a) * (double)this.toDelta(this.updateTimeHead, ((Integer)this.movingTime.getValue()).intValue()), this.movingHeadNow.field_72448_b + ((double)this.lastBestHead.func_177956_o() - this.movingHeadNow.field_72448_b) * (double)this.toDelta(this.updateTimeHead, ((Integer)this.movingTime.getValue()).intValue()), this.movingHeadNow.field_72449_c + ((double)this.lastBestHead.func_177952_p() - this.movingHeadNow.field_72449_c) * (double)this.toDelta(this.updateTimeHead, ((Integer)this.movingTime.getValue()).intValue()));
        }
        if (this.movingBaseNow.field_72450_a != -1.0 || this.movingBaseNow.field_72448_b != -1.0 || this.movingBaseNow.field_72449_c != -1.0) {
            this.drawBoxMain(this.movingBaseNow.field_72450_a, this.movingBaseNow.field_72448_b, this.movingBaseNow.field_72449_c, this.movingHeadNow.field_72450_a, this.movingHeadNow.field_72448_b, this.movingHeadNow.field_72449_c);
        }
    }

    float toDelta(long start, float length) {
        float value = (float)this.toDelta(start) / length;
        if (value > 1.0f) {
            value = 1.0f;
        }
        if (value < 0.0f) {
            value = 0.0f;
        }
        return value;
    }

    long toDelta(long start) {
        return System.currentTimeMillis() - start;
    }

    private void drawAnimationRender(AxisAlignedBB box1, AxisAlignedBB box2) {
        float size = this.basePos == null ? 1.0f - this.toDelta(this.startTime, ((Integer)this.lifeTime.getValue()).intValue()) : this.toDelta(this.startTime, ((Integer)this.lifeTime.getValue()).intValue());
        int alpha = (int)((float)((Integer)this.alpha.getValue()).intValue() * size);
        int outAlpha = (int)((float)((Integer)this.outAlpha.getValue()).intValue() * size);
        GSColor baseColor = new GSColor(this.color.getValue(), alpha);
        GSColor baseOutColor = new GSColor(this.color.getValue(), outAlpha);
        GSColor headColor = new GSColor(this.color2.getValue(), alpha);
        GSColor headOutColor = new GSColor(this.color2.getValue(), outAlpha);
        AxisAlignedBB box = new AxisAlignedBB(Math.min(box1.field_72340_a, box2.field_72340_a), box1.field_72338_b, Math.min(box1.field_72339_c, box2.field_72339_c), Math.max(box1.field_72336_d, box2.field_72336_d), box1.field_72337_e, Math.max(box1.field_72334_f, box2.field_72334_f));
        if (baseColor.equals(headColor)) {
            RenderUtil.drawBox(box, false, 0.5625, baseColor, 63);
            RenderUtil.drawBoundingBox(box, (double)((Integer)this.width.getValue()).intValue(), baseOutColor);
        } else {
            switch (this.face) {
                case "WEST": {
                    if (((Boolean)this.gradient.getValue()).booleanValue()) {
                        RenderUtil.drawBoxDire(box, 0.5625, baseColor, 0, 16);
                        RenderUtil.drawBoxDire(box, 0.5625, headColor, 0, 32);
                    } else {
                        RenderUtil.drawBox(box2, false, 0.5625, baseColor, 31);
                        RenderUtil.drawBox(box1, false, 0.5625, headColor, 47);
                    }
                    if (!((Boolean)this.outGradient.getValue()).booleanValue()) break;
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), baseOutColor, 0, 16);
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), headOutColor, 0, 32);
                    break;
                }
                case "EAST": {
                    if (((Boolean)this.gradient.getValue()).booleanValue()) {
                        RenderUtil.drawBoxDire(box, 0.5625, baseColor, 0, 32);
                        RenderUtil.drawBoxDire(box, 0.5625, headColor, 0, 16);
                    } else {
                        RenderUtil.drawBox(box2, false, 0.5625, baseColor, 47);
                        RenderUtil.drawBox(box1, false, 0.5625, headColor, 31);
                    }
                    if (!((Boolean)this.outGradient.getValue()).booleanValue()) break;
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), baseOutColor, 0, 32);
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), headOutColor, 0, 16);
                    break;
                }
                case "SOUTH": {
                    if (((Boolean)this.gradient.getValue()).booleanValue()) {
                        RenderUtil.drawBoxDire(box, 0.5625, baseColor, 0, 8);
                        RenderUtil.drawBoxDire(box, 0.5625, headColor, 0, 4);
                    } else {
                        RenderUtil.drawBox(box2, false, 0.5625, baseColor, 59);
                        RenderUtil.drawBox(box1, false, 0.5625, headColor, 55);
                    }
                    if (!((Boolean)this.outGradient.getValue()).booleanValue()) break;
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), baseOutColor, 0, 8);
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), headOutColor, 0, 4);
                    break;
                }
                case "NORTH": {
                    if (((Boolean)this.gradient.getValue()).booleanValue()) {
                        RenderUtil.drawBoxDire(box, 0.5625, baseColor, 0, 4);
                        RenderUtil.drawBoxDire(box, 0.5625, headColor, 0, 8);
                    } else {
                        RenderUtil.drawBox(box2, false, 0.5625, baseColor, 55);
                        RenderUtil.drawBox(box1, false, 0.5625, headColor, 59);
                    }
                    if (!((Boolean)this.outGradient.getValue()).booleanValue()) break;
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), baseOutColor, 0, 4);
                    RenderUtil.drawBoundingBoxDire(box, 0.5625, (double)((Integer)this.width.getValue()).intValue(), headOutColor, 0, 8);
                }
            }
            if (!((Boolean)this.outGradient.getValue()).booleanValue()) {
                RenderUtil.drawBoundingBox(box2, (double)((Integer)this.width.getValue()).intValue(), baseOutColor);
                RenderUtil.drawBoundingBox(box1, (double)((Integer)this.width.getValue()).intValue(), headOutColor);
            }
        }
        if (((Boolean)this.showDamage.getValue()).booleanValue() && this.basePos != null) {
            String[] damageText = new String[]{String.format("%.1f", Float.valueOf(this.damage))};
            if (((Boolean)this.showSelfDamage.getValue()).booleanValue()) {
                damageText = new String[]{String.format("%.1f", Float.valueOf(this.damage)) + "/" + String.format("%.1f", Float.valueOf(this.selfDamage))};
            }
            RenderUtil.drawNametag(box2.field_72340_a + 0.5, box2.field_72338_b + 0.28125, box2.field_72339_c + 0.5, damageText, new GSColor(255, 255, 255), 1, 0.02666666666666667, 0.0);
        }
    }

    void drawBoxMain(double x, double y, double z, double x2, double y2, double z2) {
        AxisAlignedBB box = new AxisAlignedBB(x, y, z, x + 1.0, y + 0.5625, z + 1.0);
        AxisAlignedBB box2 = new AxisAlignedBB(x2, y2, z2, x2 + 1.0, y2 + 0.5625, z2 + 1.0);
        this.drawAnimationRender(box, box2);
    }

    @Override
    public String getHudInfo() {
        EntityLivingBase currentTarget = null;
        if (this.target != null) {
            currentTarget = this.target.defaultPlayer == null ? this.target.entity : this.target.defaultPlayer;
        }
        boolean isNull = currentTarget == null;
        switch ((String)this.hudDisplay.getValue()) {
            case "Target": {
                return isNull ? "[" + ChatFormatting.WHITE + "None" + ChatFormatting.GRAY + "]" : "[" + ChatFormatting.WHITE + currentTarget.func_70005_c_() + ChatFormatting.GRAY + "]";
            }
            case "Damage": {
                return "[" + ChatFormatting.WHITE + String.format("%.1f", Float.valueOf(this.damage)) + ((Boolean)this.hudSelfDamage.getValue() != false ? " Self: " + String.format("%.1f", Float.valueOf(this.selfDamage)) : "") + ChatFormatting.GRAY + "]";
            }
            case "Both": {
                return "[" + ChatFormatting.WHITE + (isNull ? "None" : currentTarget.func_70005_c_()) + " " + String.format("%.1f", Float.valueOf(this.damage)) + ((Boolean)this.hudSelfDamage.getValue() != false ? " Self: " + String.format("%.1f", Float.valueOf(this.selfDamage)) : "") + ChatFormatting.GRAY + "]";
            }
        }
        return "";
    }

    class MoveRotation {
        double yaw;
        double lastYaw;
        int tick;

        public MoveRotation(EntityPlayer player, double lastYaw, int tick) {
            this.yaw = RotationUtil.getRotationTo((Vec3d)player.func_174791_d(), (Vec3d)new Vec3d((double)player.field_70169_q, (double)player.field_70167_r, (double)player.field_70166_s)).field_189982_i;
            this.lastYaw = lastYaw;
            double difference = this.yaw - lastYaw;
            if (lastYaw != 512.0 && (difference > (Double)BedAura.this.resetRotate.getValue() || difference < -((Double)BedAura.this.resetRotate.getValue()).doubleValue()) || LemonClient.speedUtil.getPlayerSpeed(player) == 0.0) {
                this.tick = 0;
                return;
            }
            this.tick = tick;
        }
    }

    class EntityInfo {
        EntityPlayer player = null;
        EntityPlayer defaultPlayer = null;
        Vec3d position;
        AxisAlignedBB boundingBox;
        EntityLivingBase entity = null;
        double hp;

        public EntityInfo(EntityPlayer player, boolean predict) {
            if (player == null) {
                return;
            }
            this.defaultPlayer = player;
            this.player = predict ? PredictUtil.predictPlayer((EntityLivingBase)player, new PredictUtil.PredictSettings(BedAura.this.playerSpeed.get((Object)player).tick, (Boolean)BedAura.this.calculateYPredict.getValue(), (Integer)BedAura.this.startDecrease.getValue(), (Integer)BedAura.this.exponentStartDecrease.getValue(), (Integer)BedAura.this.decreaseY.getValue(), (Integer)BedAura.this.exponentDecreaseY.getValue(), (Boolean)BedAura.this.splitXZ.getValue(), (Boolean)BedAura.this.manualOutHole.getValue(), (Boolean)BedAura.this.aboveHoleManual.getValue(), (Boolean)BedAura.this.stairPredict.getValue(), (Integer)BedAura.this.nStair.getValue(), (Double)BedAura.this.speedActivationStair.getValue())) : player;
            this.position = this.player.func_174791_d();
            this.boundingBox = this.player.func_174813_aQ();
            this.hp = player.func_110143_aJ() + player.func_110139_bj();
        }

        public EntityInfo(EntityLivingBase entity) {
            if (entity == null) {
                return;
            }
            this.entity = entity;
            this.hp = entity.func_110143_aJ() + entity.func_110139_bj();
        }
    }

    class PlaceInfo {
        EntityInfo target;
        BlockPos placePos;
        BlockPos basePos;
        float damage;
        float selfDamage;

        public PlaceInfo(EntityInfo target, BlockPos placePos, float damage, float selfDamage, BlockPos basePos) {
            this.target = target;
            this.placePos = placePos;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.basePos = basePos;
        }
    }
}
