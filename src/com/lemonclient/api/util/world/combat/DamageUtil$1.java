/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.EnumDifficulty
 */
package com.lemonclient.api.util.world.combat;

import net.minecraft.world.EnumDifficulty;

static class DamageUtil.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$world$EnumDifficulty;

    static {
        $SwitchMap$net$minecraft$world$EnumDifficulty = new int[EnumDifficulty.values().length];
        try {
            DamageUtil.1.$SwitchMap$net$minecraft$world$EnumDifficulty[EnumDifficulty.PEACEFUL.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            DamageUtil.1.$SwitchMap$net$minecraft$world$EnumDifficulty[EnumDifficulty.EASY.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            DamageUtil.1.$SwitchMap$net$minecraft$world$EnumDifficulty[EnumDifficulty.HARD.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
