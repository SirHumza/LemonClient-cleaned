/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.EntityEquipmentSlot
 */
package com.lemonclient.client.module.modules.combat;

import net.minecraft.inventory.EntityEquipmentSlot;

static class ArmorRegear.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot;

    static {
        $SwitchMap$net$minecraft$inventory$EntityEquipmentSlot = new int[EntityEquipmentSlot.values().length];
        try {
            ArmorRegear.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.HEAD.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ArmorRegear.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.CHEST.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ArmorRegear.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.LEGS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ArmorRegear.1.$SwitchMap$net$minecraft$inventory$EntityEquipmentSlot[EntityEquipmentSlot.FEET.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
