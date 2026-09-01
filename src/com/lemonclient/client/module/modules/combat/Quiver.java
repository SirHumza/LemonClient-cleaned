/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.potion.PotionUtils
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.PotionUtils;

@Module.Declaration(name="Quiver", category=Category.Combat)
public class Quiver
extends Module {
    IntegerSetting tickDelay = this.registerInteger("TickDelay", 3, 0, 8);

    @Override
    public void onUpdate() {
        if (Quiver.mc.field_71439_g != null) {
            List<Integer> arrowSlots;
            if (Quiver.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && Quiver.mc.field_71439_g.func_184587_cr() && Quiver.mc.field_71439_g.func_184612_cw() >= (Integer)this.tickDelay.getValue()) {
                Quiver.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(Quiver.mc.field_71439_g.field_71109_bG, -90.0f, Quiver.mc.field_71439_g.field_70122_E));
                Quiver.mc.field_71442_b.func_78766_c((EntityPlayer)Quiver.mc.field_71439_g);
            }
            if ((arrowSlots = Quiver.getItemInventory(Items.field_185167_i)).get(0) == -1) {
                return;
            }
            int speedSlot = -1;
            int strengthSlot = -1;
            for (Integer slot : arrowSlots) {
                if (PotionUtils.func_185191_c((ItemStack)Quiver.mc.field_71439_g.field_71071_by.func_70301_a(slot.intValue())).getRegistryName().func_110623_a().contains("swiftness")) {
                    speedSlot = slot;
                    continue;
                }
                if (!Objects.requireNonNull(PotionUtils.func_185191_c((ItemStack)Quiver.mc.field_71439_g.field_71071_by.func_70301_a(slot.intValue())).getRegistryName()).func_110623_a().contains("strength")) continue;
                strengthSlot = slot;
            }
        }
    }

    public static List<Integer> getItemInventory(Item item) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = 9; i < 36; ++i) {
            Item target = Quiver.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (!(item instanceof ItemBlock) || !((ItemBlock)item).func_179223_d().equals(item)) continue;
            ints.add(i);
        }
        if (ints.size() == 0) {
            ints.add(-1);
        }
        return ints;
    }
}
