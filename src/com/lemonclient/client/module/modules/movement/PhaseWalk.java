/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

@Module.Declaration(name="PhaseWalk", category=Category.Movement)
public class PhaseWalk
extends Module {
    BooleanSetting phaseCheck = this.registerBoolean("Only In Block", true);
    ModeSetting noClipMode = this.registerMode("NoClipMode", Arrays.asList("Bypass", "NoClip", "None", "Fall"), "NoClip");
    BooleanSetting fallPacket = this.registerBoolean("Fall Packet", true);
    BooleanSetting sprintPacket = this.registerBoolean("Sprint Packet", true);
    BooleanSetting instantWalk = this.registerBoolean("Instant Walk", true);
    BooleanSetting antiVoid = this.registerBoolean("Anti Void", false);
    BooleanSetting clip = this.registerBoolean("Clip", true);
    IntegerSetting antiVoidHeight = this.registerInteger("Anti Void Height", 5, 1, 100);
    DoubleSetting instantWalkSpeed = this.registerDouble("Instant Speed", 1.8, 0.1, 2.0, () -> (Boolean)this.instantWalk.getValue());
    DoubleSetting phaseSpeed = this.registerDouble("Phase Walk Speed", 42.4, 0.1, 70.0);
    BooleanSetting downOnShift = this.registerBoolean("Phase Down When Crouch", true);
    BooleanSetting stopMotion = this.registerBoolean("Attempt Clips", true);
    IntegerSetting stopMotionDelay = this.registerInteger("Attempt Clips Delay", 5, 0, 20, () -> (Boolean)this.stopMotion.getValue());
    int delay;

    @Override
    public void onDisable() {
        PhaseWalk.mc.field_71439_g.field_70145_X = false;
    }

    private boolean air(BlockPos pos) {
        Block blockState = BlockUtil.getBlock(pos);
        return !BlockUtil.airBlocks.contains(blockState) && blockState != Blocks.field_150321_G;
    }

    @Override
    public void onUpdate() {
        RayTraceResult rayTraceBlocks;
        ++this.delay;
        double n = (Double)this.phaseSpeed.getValue() / 1000.0;
        double n2 = (Double)this.instantWalkSpeed.getValue() / 10.0;
        if (((Boolean)this.antiVoid.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70163_u <= (double)((Integer)this.antiVoidHeight.getValue()).intValue() && ((rayTraceBlocks = PhaseWalk.mc.field_71441_e.func_147447_a(PhaseWalk.mc.field_71439_g.func_174791_d(), new Vec3d(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v), false, false, false)) == null || rayTraceBlocks.field_72313_a != RayTraceResult.Type.BLOCK)) {
            PhaseWalk.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        }
        if (((Boolean)this.phaseCheck.getValue()).booleanValue()) {
            if ((PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) && (!this.eChestCheck() && this.air(PlayerUtil.getPlayerPos()) || this.air(PlayerUtil.getPlayerPos().func_177984_a()))) {
                if (PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151468_f() && PhaseWalk.mc.field_71439_g.func_70093_af()) {
                    double[] motion = this.getMotion(n);
                    if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.0424, PhaseWalk.mc.field_71439_g.field_70161_v + motion[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                    } else {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                    }
                    if (((String)this.noClipMode.getValue()).equals("Fall")) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t, -1300.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70177_z * -5.0f, PhaseWalk.mc.field_71439_g.field_70125_A * -5.0f, true));
                    }
                    if (((String)this.noClipMode.getValue()).equals("NoClip")) {
                        PhaseWalk.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                        if (PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d()) {
                            double[] directionSpeed = MathUtil.directionSpeed(0.06f);
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t + directionSpeed[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + directionSpeed[1], PhaseWalk.mc.field_71439_g.field_70122_E));
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        }
                        if (PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u - (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        }
                        if (PhaseWalk.mc.field_71474_y.field_74314_A.func_151470_d()) {
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u + (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        }
                    }
                    if (((String)this.noClipMode.getValue()).equals("Bypass")) {
                        PhaseWalk.mc.field_71439_g.field_70145_X = true;
                    }
                    if (((Boolean)this.fallPacket.getValue()).booleanValue()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                    }
                    if (((Boolean)this.sprintPacket.getValue()).booleanValue()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
                    }
                    if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.0424, PhaseWalk.mc.field_71439_g.field_70161_v + motion[1]);
                    } else {
                        PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion[1]);
                    }
                    PhaseWalk.mc.field_71439_g.field_70179_y = 0.0;
                    PhaseWalk.mc.field_71439_g.field_70181_x = 0.0;
                    PhaseWalk.mc.field_71439_g.field_70159_w = 0.0;
                    PhaseWalk.mc.field_71439_g.field_70145_X = true;
                }
                if (PhaseWalk.mc.field_71439_g.field_70123_F && ((Boolean)this.clip.getValue()).booleanValue() && !PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() && !PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() && !PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d()) {
                    PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d();
                }
                if (!(PhaseWalk.mc.field_71439_g.field_70123_F && (Boolean)this.stopMotion.getValue() != false ? this.delay < (Integer)this.stopMotionDelay.getValue() : !PhaseWalk.mc.field_71439_g.field_70123_F)) {
                    double[] motion2 = this.getMotion(n);
                    if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion2[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.1, PhaseWalk.mc.field_71439_g.field_70161_v + motion2[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                    } else {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion2[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion2[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                    }
                    if (((String)this.noClipMode.getValue()).equals("Fall")) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t, -1300.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70177_z * -5.0f, PhaseWalk.mc.field_71439_g.field_70125_A * -5.0f, true));
                    }
                    if (((String)this.noClipMode.getValue()).equals("NoClip")) {
                        PhaseWalk.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                        if (PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d()) {
                            double[] directionSpeed2 = MathUtil.directionSpeed(0.06f);
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t + directionSpeed2[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + directionSpeed2[1], PhaseWalk.mc.field_71439_g.field_70122_E));
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        }
                        if (PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u - (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        }
                        if (PhaseWalk.mc.field_71474_y.field_74314_A.func_151470_d()) {
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u + (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        }
                    }
                    if (((String)this.noClipMode.getValue()).equals("Bypass")) {
                        PhaseWalk.mc.field_71439_g.field_70145_X = true;
                    }
                    if (((Boolean)this.fallPacket.getValue()).booleanValue()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                    }
                    if (((Boolean)this.sprintPacket.getValue()).booleanValue()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
                    }
                    if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion2[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.1, PhaseWalk.mc.field_71439_g.field_70161_v + motion2[1]);
                    } else {
                        PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion2[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion2[1]);
                    }
                    PhaseWalk.mc.field_71439_g.field_70179_y = 0.0;
                    PhaseWalk.mc.field_71439_g.field_70181_x = 0.0;
                    PhaseWalk.mc.field_71439_g.field_70159_w = 0.0;
                    PhaseWalk.mc.field_71439_g.field_70145_X = true;
                    this.delay = 0;
                    return;
                }
                if (((Boolean)this.instantWalk.getValue()).booleanValue()) {
                    double[] directionSpeed3 = MathUtil.directionSpeed(n2);
                    PhaseWalk.mc.field_71439_g.field_70159_w = directionSpeed3[0];
                    PhaseWalk.mc.field_71439_g.field_70179_y = directionSpeed3[1];
                }
            }
        } else if (PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
            if (PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151468_f() && PhaseWalk.mc.field_71439_g.func_70093_af()) {
                double[] motion3 = this.getMotion(n);
                if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion3[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.0424, PhaseWalk.mc.field_71439_g.field_70161_v + motion3[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                } else {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion3[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion3[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                }
                if (((String)this.noClipMode.getValue()).equals("Fall")) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t, -1300.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70177_z * -5.0f, PhaseWalk.mc.field_71439_g.field_70125_A * -5.0f, true));
                }
                if (((String)this.noClipMode.getValue()).equals("NoClip")) {
                    PhaseWalk.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                    if (PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d()) {
                        double[] directionSpeed4 = MathUtil.directionSpeed(0.06f);
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t + directionSpeed4[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + directionSpeed4[1], PhaseWalk.mc.field_71439_g.field_70122_E));
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                    }
                    if (PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u - (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                    }
                    if (PhaseWalk.mc.field_71474_y.field_74314_A.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u + (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                    }
                }
                if (((String)this.noClipMode.getValue()).equals("Bypass")) {
                    PhaseWalk.mc.field_71439_g.field_70145_X = true;
                }
                if (((Boolean)this.fallPacket.getValue()).booleanValue()) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (((Boolean)this.sprintPacket.getValue()).booleanValue()) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion3[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.0424, PhaseWalk.mc.field_71439_g.field_70161_v + motion3[1]);
                } else {
                    PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion3[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion3[1]);
                }
                PhaseWalk.mc.field_71439_g.field_70179_y = 0.0;
                PhaseWalk.mc.field_71439_g.field_70181_x = 0.0;
                PhaseWalk.mc.field_71439_g.field_70159_w = 0.0;
                PhaseWalk.mc.field_71439_g.field_70145_X = true;
            }
            if (!(PhaseWalk.mc.field_71439_g.field_70123_F && (Boolean)this.stopMotion.getValue() != false ? this.delay < (Integer)this.stopMotionDelay.getValue() : !PhaseWalk.mc.field_71439_g.field_70123_F)) {
                double[] motion4 = this.getMotion(n);
                if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion4[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.1, PhaseWalk.mc.field_71439_g.field_70161_v + motion4[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                } else {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t + motion4[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion4[1], PhaseWalk.mc.field_71439_g.field_70177_z, PhaseWalk.mc.field_71439_g.field_70125_A, false));
                }
                if (((String)this.noClipMode.getValue()).equals("Fall")) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.field_71439_g.field_70165_t, -1300.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70177_z * -5.0f, PhaseWalk.mc.field_71439_g.field_70125_A * -5.0f, true));
                }
                if (((String)this.noClipMode.getValue()).equals("NoClip")) {
                    PhaseWalk.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                    if (PhaseWalk.mc.field_71474_y.field_74351_w.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74368_y.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74370_x.func_151470_d() || PhaseWalk.mc.field_71474_y.field_74366_z.func_151470_d()) {
                        double[] directionSpeed5 = MathUtil.directionSpeed(0.06f);
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t + directionSpeed5[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + directionSpeed5[1], PhaseWalk.mc.field_71439_g.field_70122_E));
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                    }
                    if (PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u - (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                    }
                    if (PhaseWalk.mc.field_71474_y.field_74314_A.func_151470_d()) {
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u + (double)0.06f, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                        PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, 0.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
                    }
                }
                if (((String)this.noClipMode.getValue()).equals("Bypass")) {
                    PhaseWalk.mc.field_71439_g.field_70145_X = true;
                }
                if (((Boolean)this.fallPacket.getValue()).booleanValue()) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (((Boolean)this.sprintPacket.getValue()).booleanValue()) {
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (((Boolean)this.downOnShift.getValue()).booleanValue() && PhaseWalk.mc.field_71439_g.field_70124_G && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion4[0], PhaseWalk.mc.field_71439_g.field_70163_u - 0.1, PhaseWalk.mc.field_71439_g.field_70161_v + motion4[1]);
                } else {
                    PhaseWalk.mc.field_71439_g.func_70107_b(PhaseWalk.mc.field_71439_g.field_70165_t + motion4[0], PhaseWalk.mc.field_71439_g.field_70163_u, PhaseWalk.mc.field_71439_g.field_70161_v + motion4[1]);
                }
                PhaseWalk.mc.field_71439_g.field_70179_y = 0.0;
                PhaseWalk.mc.field_71439_g.field_70181_x = 0.0;
                PhaseWalk.mc.field_71439_g.field_70159_w = 0.0;
                PhaseWalk.mc.field_71439_g.field_70145_X = true;
                this.delay = 0;
                return;
            }
            if (((Boolean)this.instantWalk.getValue()).booleanValue()) {
                double[] directionSpeed6 = MathUtil.directionSpeed(n2);
                PhaseWalk.mc.field_71439_g.field_70159_w = directionSpeed6[0];
                PhaseWalk.mc.field_71439_g.field_70179_y = directionSpeed6[1];
            }
        }
    }

    private boolean eChestCheck() {
        return String.valueOf(PhaseWalk.mc.field_71439_g.field_70163_u).split("\\.")[1].equals("875") || String.valueOf(PhaseWalk.mc.field_71439_g.field_70163_u).split("\\.")[1].equals("5");
    }

    private double[] getMotion(double n) {
        float moveForward = PhaseWalk.mc.field_71439_g.field_71158_b.field_192832_b;
        float moveStrafe = PhaseWalk.mc.field_71439_g.field_71158_b.field_78902_a;
        float n2 = PhaseWalk.mc.field_71439_g.field_70126_B + (PhaseWalk.mc.field_71439_g.field_70177_z - PhaseWalk.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                n2 += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                n2 += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        return new double[]{(double)moveForward * n * -Math.sin(Math.toRadians(n2)) + (double)moveStrafe * n * Math.cos(Math.toRadians(n2)), (double)moveForward * n * Math.cos(Math.toRadians(n2)) - (double)moveStrafe * n * -Math.sin(Math.toRadians(n2))};
    }
}
