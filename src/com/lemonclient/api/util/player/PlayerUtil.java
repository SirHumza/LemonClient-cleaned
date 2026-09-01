/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class PlayerUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static void setPosition(double x, double y, double z) {
        PlayerUtil.mc.field_71439_g.func_70107_b(x, y, z);
    }

    public static void setPosition(BlockPos pos) {
        PlayerUtil.mc.field_71439_g.func_70107_b((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5);
    }

    public static Vec3d getMotionVector() {
        return new Vec3d(PlayerUtil.mc.field_71439_g.field_70159_w, PlayerUtil.mc.field_71439_g.field_70181_x, PlayerUtil.mc.field_71439_g.field_70179_y);
    }

    public static void vClip(double d) {
        PlayerUtil.mc.field_71439_g.func_70107_b(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + d, PlayerUtil.mc.field_71439_g.field_70161_v);
    }

    public static void move(double x, double y, double z) {
        PlayerUtil.mc.field_71439_g.func_70091_d(MoverType.SELF, x, y, z);
    }

    public static void setMotionVector(Vec3d vec) {
        PlayerUtil.mc.field_71439_g.field_70159_w = vec.field_72450_a;
        PlayerUtil.mc.field_71439_g.field_70181_x = vec.field_72448_b;
        PlayerUtil.mc.field_71439_g.field_70179_y = vec.field_72449_c;
    }

    public static boolean isInsideBlock() {
        try {
            AxisAlignedBB playerBoundingBox = PlayerUtil.mc.field_71439_g.func_174813_aQ();
            for (int x = MathHelper.func_76128_c((double)playerBoundingBox.field_72340_a); x < MathHelper.func_76128_c((double)playerBoundingBox.field_72336_d) + 1; ++x) {
                for (int y = MathHelper.func_76128_c((double)playerBoundingBox.field_72338_b); y < MathHelper.func_76128_c((double)playerBoundingBox.field_72337_e) + 1; ++y) {
                    for (int z = MathHelper.func_76128_c((double)playerBoundingBox.field_72339_c); z < MathHelper.func_76128_c((double)playerBoundingBox.field_72334_f) + 1; ++z) {
                        Block block = PlayerUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                        if (block instanceof BlockAir) continue;
                        AxisAlignedBB boundingBox = Objects.requireNonNull(block.func_180646_a(PlayerUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)), (IBlockAccess)PlayerUtil.mc.field_71441_e, new BlockPos(x, y, z))).func_72317_d((double)x, (double)y, (double)z);
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
                        }
                        if (!playerBoundingBox.func_72326_a(boundingBox)) continue;
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(PlayerUtil.mc.field_71439_g.field_70165_t), Math.floor(PlayerUtil.mc.field_71439_g.field_70163_u + 0.5), Math.floor(PlayerUtil.mc.field_71439_g.field_70161_v));
    }

    public static BlockPos getPlayerFloorPos() {
        return new BlockPos(Math.floor(PlayerUtil.mc.field_71439_g.field_70165_t), Math.floor(PlayerUtil.mc.field_71439_g.field_70163_u), Math.floor(PlayerUtil.mc.field_71439_g.field_70161_v));
    }

    public static boolean isPlayerClipped() {
        return !PlayerUtil.mc.field_71441_e.func_184144_a((Entity)PlayerUtil.mc.field_71439_g, PlayerUtil.mc.field_71439_g.func_174813_aQ()).isEmpty();
    }

    public static void fakeJump() {
        PlayerUtil.fakeJump(5);
    }

    public static void fakeJump(int packets) {
        if (packets > 0 && packets != 5) {
            PlayerUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u, PlayerUtil.mc.field_71439_g.field_70161_v, true));
        }
        if (packets > 1) {
            PlayerUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + 0.419999986887, PlayerUtil.mc.field_71439_g.field_70161_v, true));
        }
        if (packets > 2) {
            PlayerUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + 0.7531999805212, PlayerUtil.mc.field_71439_g.field_70161_v, true));
        }
        if (packets > 3) {
            PlayerUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + 1.0013359791121, PlayerUtil.mc.field_71439_g.field_70161_v, true));
        }
        if (packets > 4) {
            PlayerUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + 1.1661092609382, PlayerUtil.mc.field_71439_g.field_70161_v, true));
        }
    }

    public static double getDistance(Entity entity) {
        return PlayerUtil.mc.field_71439_g.func_70032_d(entity);
    }

    public static double getDistance(BlockPos pos) {
        return PlayerUtil.mc.field_71439_g.func_70011_f((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
    }

    public static double getDistanceI(BlockPos pos) {
        return PlayerUtil.getEyeVec().func_72438_d(new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5));
    }

    public static double getDistanceL(BlockPos pos) {
        double x = (double)pos.field_177962_a - PlayerUtil.mc.field_71439_g.field_70165_t;
        double z = (double)pos.field_177961_c - PlayerUtil.mc.field_71439_g.field_70161_v;
        return Math.hypot(x, z);
    }

    public static BlockPos getEyesPos() {
        return new BlockPos(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + (double)PlayerUtil.mc.field_71439_g.func_70047_e(), PlayerUtil.mc.field_71439_g.field_70161_v);
    }

    public static Vec3d getEyeVec() {
        return new Vec3d(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u + (double)PlayerUtil.mc.field_71439_g.func_70047_e(), PlayerUtil.mc.field_71439_g.field_70161_v);
    }

    public static EntityPlayer getNearestPlayer(double range) {
        List playerList = PlayerUtil.mc.field_71441_e.field_73010_i.stream().filter(p -> (double)PlayerUtil.mc.field_71439_g.func_70032_d((Entity)p) <= range).filter(p -> !EntityUtil.basicChecksEntity(p)).filter(p -> PlayerUtil.mc.field_71439_g.field_145783_c != p.field_145783_c).filter(p -> !EntityUtil.isDead((Entity)p)).collect(Collectors.toList());
        List players = playerList.stream().filter(p -> SocialManager.isEnemy(p.func_70005_c_())).collect(Collectors.toList());
        if (players.isEmpty()) {
            players.addAll(playerList);
        }
        return players.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)PlayerUtil.mc.field_71439_g).func_70032_d(arg_0))).orElse(null);
    }

    public static EntityPlayer findLookingPlayer(double rangeMax) {
        ArrayList<EntityPlayer> listPlayer = new ArrayList<EntityPlayer>();
        for (EntityPlayer playerSin : PlayerUtil.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.basicChecksEntity(playerSin) || !((double)PlayerUtil.mc.field_71439_g.func_70032_d((Entity)playerSin) <= rangeMax)) continue;
            listPlayer.add(playerSin);
        }
        EntityPlayer target = null;
        Vec3d positionEyes = PlayerUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak());
        Vec3d rotationEyes = PlayerUtil.mc.field_71439_g.func_70676_i(mc.func_184121_ak());
        int precision = 2;
        for (int i = 0; i < (int)rangeMax; ++i) {
            for (int j = precision; j > 0; --j) {
                for (EntityPlayer targetTemp : listPlayer) {
                    AxisAlignedBB playerBox = targetTemp.func_174813_aQ();
                    double xArray = positionEyes.field_72450_a + rotationEyes.field_72450_a * (double)i + rotationEyes.field_72450_a / (double)j;
                    double yArray = positionEyes.field_72448_b + rotationEyes.field_72448_b * (double)i + rotationEyes.field_72448_b / (double)j;
                    double zArray = positionEyes.field_72449_c + rotationEyes.field_72449_c * (double)i + rotationEyes.field_72449_c / (double)j;
                    if (!(playerBox.field_72337_e >= yArray) || !(playerBox.field_72338_b <= yArray) || !(playerBox.field_72336_d >= xArray) || !(playerBox.field_72340_a <= xArray) || !(playerBox.field_72334_f >= zArray) || !(playerBox.field_72339_c <= zArray)) continue;
                    target = targetTemp;
                }
            }
        }
        return target;
    }

    public static List<EntityPlayer> getNearPlayers(double range, int count) {
        ArrayList<EntityPlayer> targetList = new ArrayList<EntityPlayer>();
        ArrayList list = new ArrayList();
        for (EntityPlayer player : PlayerUtil.mc.field_71441_e.field_73010_i) {
            if ((double)PlayerUtil.mc.field_71439_g.func_70032_d((Entity)player) > range || EntityUtil.basicChecksEntity(player) || EntityUtil.isDead((Entity)player)) continue;
            targetList.add(player);
        }
        List players = targetList.stream().filter(p -> SocialManager.isEnemy(p.func_70005_c_())).collect(Collectors.toList());
        if (players.isEmpty()) {
            players.addAll(targetList);
        }
        players.stream().sorted(Comparator.comparing(PlayerUtil::getDistance)).forEach(list::add);
        return new ArrayList<EntityPlayer>(list.subList(0, Math.min(count, list.size())));
    }

    public static float getHealth() {
        return PlayerUtil.mc.field_71439_g.func_110143_aJ() + PlayerUtil.mc.field_71439_g.func_110139_bj();
    }

    public static void centerPlayer() {
        int zRel;
        double newX = -2.0;
        double newZ = -2.0;
        int xRel = PlayerUtil.mc.field_71439_g.field_70165_t < 0.0 ? -1 : 1;
        int n = zRel = PlayerUtil.mc.field_71439_g.field_70161_v < 0.0 ? -1 : 1;
        if (BlockUtil.getBlock(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u - 1.0, PlayerUtil.mc.field_71439_g.field_70161_v) instanceof BlockAir) {
            if (Math.abs(PlayerUtil.mc.field_71439_g.field_70165_t % 1.0) * 100.0 <= 30.0) {
                newX = (double)Math.round(PlayerUtil.mc.field_71439_g.field_70165_t - 0.3 * (double)xRel) + 0.5 * (double)(-xRel);
            } else if (Math.abs(PlayerUtil.mc.field_71439_g.field_70165_t % 1.0) * 100.0 >= 70.0) {
                newX = (double)Math.round(PlayerUtil.mc.field_71439_g.field_70165_t + 0.3 * (double)xRel) - 0.5 * (double)(-xRel);
            }
            if (Math.abs(PlayerUtil.mc.field_71439_g.field_70161_v % 1.0) * 100.0 <= 30.0) {
                newZ = (double)Math.round(PlayerUtil.mc.field_71439_g.field_70161_v - 0.3 * (double)zRel) + 0.5 * (double)(-zRel);
            } else if (Math.abs(PlayerUtil.mc.field_71439_g.field_70161_v % 1.0) * 100.0 >= 70.0) {
                newZ = (double)Math.round(PlayerUtil.mc.field_71439_g.field_70161_v + 0.3 * (double)zRel) - 0.5 * (double)(-zRel);
            }
        }
        if (newX == -2.0) {
            newX = PlayerUtil.mc.field_71439_g.field_70165_t > (double)Math.round(PlayerUtil.mc.field_71439_g.field_70165_t) ? (double)Math.round(PlayerUtil.mc.field_71439_g.field_70165_t) + 0.5 : (PlayerUtil.mc.field_71439_g.field_70165_t < (double)Math.round(PlayerUtil.mc.field_71439_g.field_70165_t) ? (double)Math.round(PlayerUtil.mc.field_71439_g.field_70165_t) - 0.5 : PlayerUtil.mc.field_71439_g.field_70165_t);
        }
        if (newZ == -2.0) {
            newZ = PlayerUtil.mc.field_71439_g.field_70161_v > (double)Math.round(PlayerUtil.mc.field_71439_g.field_70161_v) ? (double)Math.round(PlayerUtil.mc.field_71439_g.field_70161_v) + 0.5 : (PlayerUtil.mc.field_71439_g.field_70161_v < (double)Math.round(PlayerUtil.mc.field_71439_g.field_70161_v) ? (double)Math.round(PlayerUtil.mc.field_71439_g.field_70161_v) - 0.5 : PlayerUtil.mc.field_71439_g.field_70161_v);
        }
        PlayerUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(newX, PlayerUtil.mc.field_71439_g.field_70163_u, newZ, true));
        PlayerUtil.mc.field_71439_g.func_70107_b(newX, PlayerUtil.mc.field_71439_g.field_70163_u, newZ);
    }
}
