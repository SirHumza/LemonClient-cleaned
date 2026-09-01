/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@Module.Declaration(name="AutoEat", category=Category.Misc)
public class AutoEat
extends Module {
    IntegerSetting health = this.registerInteger("Health", 10, 1, 36);
    BooleanSetting equal = this.registerBoolean("Equal", false);
    boolean eating = false;

    @Override
    public void onDisable() {
        this.stopEating();
    }

    @Override
    public void onTick() {
        if (EntityUtil.isDead((Entity)AutoEat.mc.field_71439_g)) {
            if (this.eating) {
                this.stopEating();
            }
            return;
        }
        if (this.shouldEat()) {
            EnumHand hand = null;
            if (this.isValid(AutoEat.mc.field_71439_g.func_184614_ca())) {
                hand = EnumHand.MAIN_HAND;
            }
            if (this.isValid(AutoEat.mc.field_71439_g.func_184592_cb())) {
                hand = EnumHand.OFF_HAND;
            }
            if (hand != null) {
                this.eat(hand);
            } else {
                int slot = this.findHotbarFood();
                if (slot != -1) {
                    AutoEat.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                }
            }
        } else if (this.eating) {
            this.stopEating();
        }
    }

    private int findHotbarFood() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoEat.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !this.isValid(stack)) continue;
            return i;
        }
        return -1;
    }

    private boolean shouldEat() {
        if (((Boolean)this.equal.getValue()).booleanValue()) {
            return AutoEat.mc.field_71439_g.func_110143_aJ() + AutoEat.mc.field_71439_g.func_110139_bj() <= (float)((Integer)this.health.getValue()).intValue();
        }
        return AutoEat.mc.field_71439_g.func_110143_aJ() + AutoEat.mc.field_71439_g.func_110139_bj() < (float)((Integer)this.health.getValue()).intValue();
    }

    private void eat(EnumHand hand) {
        if (!this.eating || !AutoEat.mc.field_71439_g.func_184587_cr() || AutoEat.mc.field_71439_g.func_184600_cs() != hand) {
            KeyBinding.func_74510_a((int)AutoEat.mc.field_71474_y.field_74313_G.func_151463_i(), (boolean)true);
            AutoEat.mc.field_71442_b.func_187101_a((EntityPlayer)AutoEat.mc.field_71439_g, (World)AutoEat.mc.field_71441_e, hand);
        }
        this.eating = true;
    }

    private void stopEating() {
        KeyBinding.func_74510_a((int)AutoEat.mc.field_71474_y.field_74313_G.func_151463_i(), (boolean)false);
        this.eating = false;
    }

    private boolean isValid(ItemStack itemStack) {
        Item item = itemStack.field_151002_e;
        return item instanceof ItemFood && item != Items.field_185161_cS && !this.isBadFood(itemStack, (ItemFood)item) && AutoEat.mc.field_71439_g.func_71043_e(item == Items.field_151153_ao);
    }

    private boolean isBadFood(ItemStack itemStack, ItemFood item) {
        return item == Items.field_151078_bh || item == Items.field_151070_bp || item == Items.field_151170_bI || item == Items.field_151115_aP && (itemStack.func_77960_j() == 3 || itemStack.func_77960_j() == 2);
    }
}
