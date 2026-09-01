/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Declaration(name="32kTotem", category=Category.Combat)
public class Anti32kTotem
extends Module {
    IntegerSetting slot = this.registerInteger("Slot", 1, 1, 9);

    @Override
    public void fast() {
        if ((!(Anti32kTotem.mc.field_71462_r instanceof GuiContainer) || Anti32kTotem.mc.field_71462_r instanceof GuiInventory) && Anti32kTotem.mc.field_71439_g.field_71071_by.func_70301_a((Integer)this.slot.getValue() - 1).func_77973_b() != Items.field_190929_cY) {
            for (int i = 9; i < 36; ++i) {
                if (Anti32kTotem.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190929_cY) continue;
                Anti32kTotem.mc.field_71442_b.func_187098_a(0, i, (Integer)this.slot.getValue() - 1, ClickType.SWAP, (EntityPlayer)Anti32kTotem.mc.field_71439_g);
                break;
            }
        }
    }

    @Override
    public String getHudInfo() {
        int totems = Anti32kTotem.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (Anti32kTotem.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            ++totems;
        }
        return "[" + ChatFormatting.WHITE + "Totem " + totems + ChatFormatting.GRAY + "]";
    }
}
