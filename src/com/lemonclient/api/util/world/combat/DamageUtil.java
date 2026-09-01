/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockBed
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package com.lemonclient.api.util.world.combat;

import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.combat.raytrace.RayTracer;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AutoCrystal;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DamageUtil {
    public static float calculateDamage(EntityLivingBase entity, Vec3d entityPos, AxisAlignedBB entityBox, EntityEnderCrystal crystal) {
        return DamageUtil.calculateCrystalDamage(entity, entityPos, entityBox, crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v);
    }

    public static float calculateDamage(EntityLivingBase entity, Vec3d entityPos, AxisAlignedBB entityBox, double posX, double posY, double posZ, float size, String mode) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        boolean isPlayer = entity instanceof EntityPlayer;
        if (isPlayer && entity.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        float damage = DamageUtil.calcRawDamage(entity, entityPos, entityBox, posX, posY, posZ, size * 2.0f, mutableBlockPos, mode);
        if (isPlayer) {
            damage = DamageUtil.calcDifficultyDamage(entity, damage);
        }
        return DamageUtil.calcReductionDamage(entity, damage);
    }

    public static float calcDamageIgnoreTerrain(EntityLivingBase entity, Vec3d entityPos, AxisAlignedBB entityBox, double crystalX, double crystalY, double crystalZ) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        boolean isPlayer = entity instanceof EntityPlayer;
        if (isPlayer && entity.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        mutableBlockPos.func_181079_c((int)crystalX, (int)crystalY - 1, (int)crystalZ);
        float damage = isPlayer && crystalY - entityPos.field_72448_b > 1.5652173822904127 && DamageUtil.isResistant(entity.field_70170_p.func_180495_p((BlockPos)mutableBlockPos)) ? 1.0f : DamageUtil.calcRawDamage(entity, entityPos, entityBox, crystalX, crystalY, crystalZ, 12.0f, mutableBlockPos, "Crystal");
        if (isPlayer) {
            damage = DamageUtil.calcDifficultyDamage(entity, damage);
        }
        return DamageUtil.calcReductionDamage(entity, damage);
    }

    public static float calculateCrystalDamageMine(EntityLivingBase entity, Vec3d entityPos, AxisAlignedBB entityBox, double posX, double posY, double posZ) {
        float damage;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        boolean isPlayer = entity instanceof EntityPlayer;
        if (isPlayer && entity.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        mutableBlockPos.func_181079_c((int)posX, (int)posY - 1, (int)posZ);
        if (isPlayer && posY - entityPos.field_72448_b > 1.5652173822904127 && ((int)posX != (int)entityPos.field_72450_a || (int)posZ != (int)entityPos.field_72449_c) && DamageUtil.isResistantMine((BlockPos)mutableBlockPos)) {
            damage = 1.0f;
        } else {
            float scaledDist = (float)(entityPos.func_72438_d(new Vec3d(posX, posY, posZ)) / 12.0);
            if (scaledDist > 1.0f) {
                damage = 0.0f;
            } else {
                float factor = (1.0f - scaledDist) * DamageUtil.getBlockDensity(new Vec3d(posX, posY, posZ), entityBox, entity, true, true, true, true);
                damage = Math.abs((factor * factor + factor) * 12.0f * 3.5f);
            }
        }
        if (isPlayer) {
            damage = DamageUtil.calcDifficultyDamage(entity, damage);
        }
        return DamageUtil.calcReductionDamage(entity, damage);
    }

    public static float calculateCrystalDamage(EntityLivingBase entity, Vec3d entityPos, AxisAlignedBB entityBox, double posX, double posY, double posZ) {
        float damage;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        boolean isPlayer = entity instanceof EntityPlayer;
        if (isPlayer && entity.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        mutableBlockPos.func_181079_c((int)posX, (int)posY - 1, (int)posZ);
        if (isPlayer && posY - entityPos.field_72448_b > 1.5652173822904127 && DamageUtil.isResistant(entity.field_70170_p.func_180495_p((BlockPos)mutableBlockPos))) {
            damage = 1.0f;
        } else {
            float scaledDist = (float)(entityPos.func_72438_d(new Vec3d(posX, posY, posZ)) / 12.0);
            if (scaledDist > 1.0f) {
                damage = 0.0f;
            } else {
                float factor = (1.0f - scaledDist) * DamageUtil.getBlockDensity(new Vec3d(posX, posY, posZ), entityBox, entity, true, true, true, false);
                damage = Math.abs((factor * factor + factor) * 12.0f * 3.5f);
            }
        }
        if (isPlayer) {
            damage = DamageUtil.calcDifficultyDamage(entity, damage);
        }
        return DamageUtil.calcReductionDamage(entity, damage);
    }

    private static float calcRawDamage(EntityLivingBase entity, Vec3d entityPos, AxisAlignedBB entityBox, double posX, double posY, double posZ, float doubleSize, BlockPos.MutableBlockPos mutableBlockPos, String mode) {
        float scaledDist = (float)(entityPos.func_72438_d(new Vec3d(posX, posY, posZ)) / (double)doubleSize);
        if (scaledDist > 1.0f) {
            return 0.0f;
        }
        float factor = (1.0f - scaledDist) * DamageUtil.getBlockDensity(new Vec3d(posX, posY, posZ), entityBox, entity, true, true, true, false);
        return (factor * factor + factor) * doubleSize * 3.5f + 1.0f;
    }

    public static boolean getDistance(BlockPos pos, Vec3d vec) {
        if (pos == null || vec == null) {
            return false;
        }
        double x = (double)pos.field_177962_a + 0.5 - vec.field_72450_a;
        double z = (double)pos.field_177961_c + 0.5 - vec.field_72449_c;
        if (Math.hypot(x, z) >= 2.0) {
            return false;
        }
        double y = (double)pos.field_177960_b - vec.field_72448_b;
        return true;
    }

    public static float ignoreTerrainDensity(Vec3d vec, AxisAlignedBB bb, EntityLivingBase entity, String mode) {
        if (mode.equals("CrystalMine")) {
            BlockPos instantPos = null;
            if (ModuleManager.isModuleEnabled(PacketMine.class)) {
                instantPos = PacketMine.INSTANCE.packetPos;
            }
            if (!DamageUtil.getDistance(instantPos, vec)) {
                mode = "Crystal";
            }
            if (!(((Boolean)AutoCrystal.INSTANCE.civ.getValue()).booleanValue() || instantPos != null && ((double)instantPos.field_177960_b == vec.field_72448_b || (int)vec.field_72450_a == (int)entity.field_70165_t && (int)vec.field_72449_c == (int)entity.field_70161_v))) {
                mode = "Crystal";
            }
        }
        double d0 = 1.0 / ((bb.field_72336_d - bb.field_72340_a) * 2.0 + 1.0);
        double d1 = 1.0 / ((bb.field_72337_e - bb.field_72338_b) * 2.0 + 1.0);
        double d2 = 1.0 / ((bb.field_72334_f - bb.field_72339_c) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        if (d0 >= 0.0 && d1 >= 0.0 && d2 >= 0.0) {
            int j2 = 0;
            int k2 = 0;
            float f = 0.0f;
            while (f <= 1.0f) {
                float f1 = 0.0f;
                while (f1 <= 1.0f) {
                    float f2 = 0.0f;
                    while (f2 <= 1.0f) {
                        double d5 = bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * (double)f;
                        double d6 = bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * (double)f1;
                        double d7 = bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * (double)f2;
                        Vec3d newVec = new Vec3d(d5 + d3, d6, d7 + d4);
                        RayTraceResult result = entity.field_70170_p.func_72933_a(newVec, vec);
                        if (result == null) {
                            ++j2;
                        } else {
                            IBlockState state = BlockUtil.getState(result.func_178782_a());
                            if (DamageUtil.getRaytrace(entity, mode, result.func_178782_a(), state).equals("SKIP")) {
                                ++j2;
                            }
                        }
                        ++k2;
                        f2 = (float)((double)f2 + d2);
                    }
                    f1 = (float)((double)f1 + d1);
                }
                f = (float)((double)f + d0);
            }
            return (float)j2 / (float)k2;
        }
        return 0.0f;
    }

    public static boolean isResistant(IBlockState blockState) {
        return blockState.func_185904_a() != Material.field_151579_a && !(blockState instanceof BlockLiquid) && (double)blockState.func_177230_c().field_149781_w >= 19.7;
    }

    public static boolean isResistantMine(BlockPos pos) {
        IBlockState blockState;
        BlockPos instantPos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            instantPos = PacketMine.INSTANCE.packetPos;
        }
        return (blockState = BlockUtil.getState(pos)).func_185904_a() != Material.field_151579_a && !(blockState instanceof BlockLiquid) && (double)blockState.func_177230_c().field_149781_w >= 19.7 && (!DamageUtil.isPos2(instantPos, pos) || BlockUtil.getState(pos).func_185887_b((World)Minecraft.func_71410_x().field_71441_e, pos) < 0.0f);
    }

    public static String getRaytrace(EntityLivingBase entity, String mode, BlockPos pos, IBlockState blockState) {
        switch (mode) {
            case "Crystal": {
                if (DamageUtil.isResistant(blockState)) {
                    return "CALC";
                }
                return "SKIP";
            }
            case "CrystalMine": {
                if (DamageUtil.isResistantMine(pos)) {
                    return "CALC";
                }
                return "SKIP";
            }
            case "Bed": {
                Block block = blockState.func_177230_c();
                if (block == Blocks.field_150350_a || block == Blocks.field_150324_C || !DamageUtil.isResistant(blockState)) {
                    return "SKIP";
                }
                return "CALC";
            }
            case "Calc": {
                return "Calc";
            }
            case "Skip": {
                return "Skip";
            }
        }
        return blockState.func_185890_d((IBlockAccess)entity.field_70170_p, pos) != null ? "CALC" : "SKIP";
    }

    public static float calcReductionDamage(EntityLivingBase entity, float damage) {
        PotionEffect potionEffect = entity.func_70660_b(MobEffects.field_76429_m);
        float resistance = potionEffect == null ? 1.0f : Math.max(1.0f - (float)(potionEffect.func_76458_c() + 1) * 0.2f, 0.0f);
        float blastReduction = 1.0f - (float)Math.min(DamageUtil.calcTotalEPF(entity), 20) / 25.0f;
        return CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e())) * resistance * blastReduction;
    }

    public static int calcTotalEPF(EntityLivingBase entity) {
        int epf = 0;
        for (ItemStack itemStack : entity.func_184193_aE()) {
            NBTTagList nbtTagList = itemStack.func_77986_q();
            for (int i = 0; i <= nbtTagList.func_74745_c(); ++i) {
                NBTTagCompound nbtTagCompound = nbtTagList.func_150305_b(i);
                int id = nbtTagCompound.func_74762_e("id");
                short level = nbtTagCompound.func_74765_d("lvl");
                if (id == 0) {
                    epf += level;
                    continue;
                }
                if (id != 3) continue;
                epf += level * 2;
            }
        }
        return epf;
    }

    public static float calcDifficultyDamage(EntityLivingBase entity, float damage) {
        switch (entity.field_70170_p.func_175659_aa()) {
            case PEACEFUL: {
                return 0.0f;
            }
            case EASY: {
                return Math.min(damage * 0.5f + 1.0f, damage);
            }
            case HARD: {
                return damage * 1.5f;
            }
        }
        return damage;
    }

    public static boolean in(double number, double floor, double ceil) {
        return number >= floor && number <= ceil;
    }

    public static boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    public static float getBlockDensity(Vec3d vec, AxisAlignedBB bb, EntityLivingBase entity, boolean ignoreWebs, boolean ignoreBeds, boolean terrainCalc, boolean mine) {
        if (mine) {
            BlockPos instantPos = null;
            if (ModuleManager.isModuleEnabled(PacketMine.class)) {
                instantPos = PacketMine.INSTANCE.packetPos;
            }
            if (((Boolean)AutoCrystal.INSTANCE.rangeCheck.getValue()).booleanValue() && !DamageUtil.getDistance(instantPos, vec)) {
                mine = false;
            }
            if (!(((Boolean)AutoCrystal.INSTANCE.civ.getValue()).booleanValue() || instantPos != null && ((double)instantPos.field_177960_b == vec.field_72448_b || (int)vec.field_72450_a == (int)entity.field_70165_t && (int)vec.field_72449_c == (int)entity.field_70161_v))) {
                mine = false;
            }
        }
        double x = 1.0 / ((bb.field_72336_d - bb.field_72340_a) * 2.0 + 1.0);
        double y = 1.0 / ((bb.field_72337_e - bb.field_72338_b) * 2.0 + 1.0);
        double z = 1.0 / ((bb.field_72334_f - bb.field_72339_c) * 2.0 + 1.0);
        double xFloor = (1.0 - Math.floor(1.0 / x) * x) / 2.0;
        double zFloor = (1.0 - Math.floor(1.0 / z) * z) / 2.0;
        if (x >= 0.0 && y >= 0.0 && z >= 0.0) {
            int air = 0;
            int traced = 0;
            float a = 0.0f;
            while (a <= 1.0f) {
                float b = 0.0f;
                while (b <= 1.0f) {
                    float c = 0.0f;
                    while (c <= 1.0f) {
                        double xOff = bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * (double)a;
                        double yOff = bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * (double)b;
                        double zOff = bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * (double)c;
                        RayTraceResult result = DamageUtil.rayTraceBlocks(new Vec3d(xOff + xFloor, yOff, zOff + zFloor), vec, (IBlockAccess)entity.field_70170_p, false, false, false, ignoreWebs, ignoreBeds, terrainCalc, mine);
                        if (result == null) {
                            ++air;
                        }
                        ++traced;
                        c = (float)((double)c + z);
                    }
                    b = (float)((double)b + y);
                }
                a = (float)((double)a + x);
            }
            return (float)air / (float)traced;
        }
        return 0.0f;
    }

    public static RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end, IBlockAccess world, boolean stopOnLiquid, boolean ignoreNoBox, boolean lastUncollidableBlock, boolean ignoreWebs, boolean ignoreBeds, boolean terrainCalc, boolean mine) {
        BlockPos instantPos = ModuleManager.isModuleEnabled(PacketMine.class) ? PacketMine.INSTANCE.packetPos : null;
        return RayTracer.trace((World)Minecraft.func_71410_x().field_71441_e, world, start, end, stopOnLiquid, ignoreNoBox, lastUncollidableBlock, (b, p) -> !(terrainCalc && b.func_149638_a((Entity)Minecraft.func_71410_x().field_71439_g) < 100.0f && p.func_177954_c(end.field_72450_a, end.field_72448_b, end.field_72449_c) <= 36.0 || mine && DamageUtil.isPos2(p, instantPos) && BlockUtil.getState(p).func_185887_b((World)Minecraft.func_71410_x().field_71441_e, p) >= 0.0f || ignoreBeds && b instanceof BlockBed || ignoreWebs && b instanceof BlockWeb));
    }
}
