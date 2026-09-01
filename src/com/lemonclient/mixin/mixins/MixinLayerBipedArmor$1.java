/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.EntityEquipmentSlot
 */
package com.lemonclient.mixin.mixins;

import net.minecraft.inventory.EntityEquipmentSlot;

static class MixinLayerBipedArmor.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot;

    static {
        $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot = new int[EntityEquipmentSlot.values().length];
        try {
            MixinLayerBipedArmor.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.HEAD.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            MixinLayerBipedArmor.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.CHEST.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            MixinLayerBipedArmor.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.LEGS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            MixinLayerBipedArmor.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.FEET.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
