/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

@Module.Declaration(name="AutoDrop", category=Category.Combat)
public class AutoDrop
extends Module {
    IntegerSetting delay = this.registerInteger("Drop Delay", 10, 0, 20);
    ModeSetting mode = this.registerMode("Sharpness", Arrays.asList("Sharp5", "Sharp32k", "Both"), "Both");
    private final Timing timer = new Timing();

    @Override
    public void onUpdate() {
        switch ((String)this.mode.getValue()) {
            case "Sharp32k": {
                if (this.isSuperWeapon(AutoDrop.mc.field_71439_g.func_184614_ca()) && this.timer.passedDs(((Integer)this.delay.getValue()).intValue())) {
                    boolean holding32k = false;
                    AutoDrop.mc.field_71439_g.func_71040_bB(!holding32k);
                    this.timer.reset();
                    break;
                }
            }
            case "Both": {
                if (this.checkSword(AutoDrop.mc.field_71439_g.func_184614_ca()) && this.timer.passedDs(((Integer)this.delay.getValue()).intValue())) {
                    boolean holding = false;
                    AutoDrop.mc.field_71439_g.func_71040_bB(!holding);
                }
            }
            case "Sharp5": {
                if (!this.checkSharpness5(AutoDrop.mc.field_71439_g.func_184614_ca()) || !this.timer.passedDs(((Integer)this.delay.getValue()).intValue())) break;
                boolean holding5 = false;
                AutoDrop.mc.field_71439_g.func_71040_bB(!holding5);
            }
        }
    }

    private boolean checkSword(ItemStack stack) {
        if (stack.func_77978_p() == null) {
            return false;
        }
        if (stack.func_77986_q().func_150303_d() == 0) {
            return false;
        }
        NBTTagList enchants = (NBTTagList)stack.func_77978_p().func_74781_a("ench");
        for (int i = 0; i < enchants.func_74745_c(); ++i) {
            NBTTagCompound enchant = enchants.func_150305_b(i);
            if (enchant.func_74762_e("id") != 16) continue;
            int lvl = enchant.func_74762_e("lvl");
            if (lvl <= 4) break;
            return true;
        }
        return false;
    }

    private boolean isSuperWeapon(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.func_77978_p() == null) {
            return false;
        }
        if (item.func_77986_q().func_150303_d() == 0) {
            return false;
        }
        NBTTagList enchants = (NBTTagList)item.func_77978_p().func_74781_a("ench");
        for (int i = 0; i < enchants.func_74745_c(); ++i) {
            NBTTagCompound enchant = enchants.func_150305_b(i);
            if (enchant.func_74762_e("id") != 16) continue;
            int lvl = enchant.func_74762_e("lvl");
            if (lvl < 16) break;
            return true;
        }
        return false;
    }

    private boolean checkSharpness5(ItemStack stack) {
        if (stack.func_77978_p() == null) {
            return false;
        }
        if (stack.func_77986_q().func_150303_d() == 0) {
            return false;
        }
        NBTTagList enchants = (NBTTagList)stack.func_77978_p().func_74781_a("ench");
        for (int i = 0; i < enchants.func_74745_c(); ++i) {
            NBTTagCompound enchant = enchants.func_150305_b(i);
            if (enchant.func_74762_e("id") != 16) continue;
            int lvl = enchant.func_74762_e("lvl");
            if (lvl != 5) break;
            return true;
        }
        return false;
    }
}
