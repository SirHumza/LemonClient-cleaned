/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.util.NonNullList
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.misc.Pair;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.List;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;

@Module.Declaration(name="SwordSwitch", category=Category.Dev)
public class SwordSwitch
extends Module {
    BooleanSetting disable = this.registerBoolean("Disable", true);

    @Override
    public void onUpdate() {
        Pair<Float, Integer> newSlot = new Pair<Float, Integer>(Float.valueOf(0.0f), -1);
        newSlot = this.findSwordSlot();
        if (newSlot.getValue() == -1) {
            MessageBus.sendClientPrefixMessage("Cant find sword", Notification.Type.ERROR);
            this.disable();
            return;
        }
        SwordSwitch.mc.field_71439_g.field_71071_by.field_70461_c = newSlot.getValue();
        if (((Boolean)this.disable.getValue()).booleanValue()) {
            this.disable();
        }
    }

    private Pair<Float, Integer> findSwordSlot() {
        List<Integer> items = InventoryUtil.findAllItemSlots(ItemSword.class);
        NonNullList inventory = SwordSwitch.mc.field_71439_g.field_71071_by.field_70462_a;
        float bestModifier = 0.0f;
        int correspondingSlot = -1;
        for (Integer integer : items) {
            ItemStack stack;
            float modifier;
            if (integer > 8 || !((modifier = (EnchantmentHelper.func_152377_a((ItemStack)(stack = (ItemStack)inventory.get(integer)), (EnumCreatureAttribute)EnumCreatureAttribute.UNDEFINED) + 1.0f) * ((ItemSword)stack.func_77973_b()).func_150931_i()) > bestModifier)) continue;
            bestModifier = modifier;
            correspondingSlot = integer;
        }
        return new Pair<Float, Integer>(Float.valueOf(bestModifier), correspondingSlot);
    }
}
