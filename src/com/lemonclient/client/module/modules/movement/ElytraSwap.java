/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Declaration(name="ElytraSwap", category=Category.Movement)
public class ElytraSwap
extends Module {
    @Override
    public void onEnable() {
        if (ElytraSwap.mc.field_71439_g != null) {
            int j;
            int i;
            int t;
            InventoryPlayer items = ElytraSwap.mc.field_71439_g.field_71071_by;
            ItemStack body = items.func_70440_f(2);
            String body2 = body.func_77973_b().func_77653_i(body);
            if (body2.equals("Air")) {
                int i2;
                t = 0;
                int c = 0;
                for (i2 = 9; i2 < 45; ++i2) {
                    if (ElytraSwap.mc.field_71439_g.field_71071_by.func_70301_a(i2).func_77973_b() != Items.field_185160_cR) continue;
                    t = i2;
                    break;
                }
                if (t != 0) {
                    MessageBus.sendClientDeleteMessage("Equipping Elytra", Notification.Type.SUCCESS, "ElytraSwap", 1);
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, t, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                }
                if (t == 0) {
                    for (i2 = 9; i2 < 45; ++i2) {
                        if (ElytraSwap.mc.field_71439_g.field_71071_by.func_70301_a(i2).func_77973_b() != Items.field_151163_ad) continue;
                        c = i2;
                        break;
                    }
                    if (c != 0) {
                        MessageBus.sendClientDeleteMessage("Equipping Chestplate", Notification.Type.SUCCESS, "ElytraSwap", 1);
                        ElytraSwap.mc.field_71442_b.func_187098_a(0, c, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                        ElytraSwap.mc.field_71442_b.func_187098_a(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                    }
                }
                if (c == 0 && t == 0) {
                    MessageBus.sendClientDeleteMessage("You do not have an Elytra or a Chestplate in your inventory. Doing nothing", Notification.Type.ERROR, "ElytraSwap", 1);
                }
                this.disable();
            }
            if (body2.equals("Elytra")) {
                t = 0;
                for (i = 9; i < 45; ++i) {
                    if (ElytraSwap.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151163_ad) continue;
                    t = i;
                    break;
                }
                if (t != 0) {
                    int l = 0;
                    MessageBus.sendClientDeleteMessage("Equipping Chestplate", Notification.Type.SUCCESS, "ElytraSwap", 1);
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, t, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                    for (j = 9; j < 45; ++j) {
                        if (ElytraSwap.mc.field_71439_g.field_71071_by.func_70301_a(j).func_77973_b() != Items.field_190931_a) continue;
                        l = j;
                        break;
                    }
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, l, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                }
                if (t == 0) {
                    MessageBus.sendClientDeleteMessage("You do not have a Chestplate in your inventory. Keeping Elytra equipped", Notification.Type.ERROR, "ElytraSwap", 1);
                }
                this.disable();
            }
            if (body2.equals("Diamond Chestplate")) {
                t = 0;
                for (i = 9; i < 45; ++i) {
                    if (ElytraSwap.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_185160_cR) continue;
                    t = i;
                    break;
                }
                if (t != 0) {
                    int u = 0;
                    MessageBus.sendClientDeleteMessage("Equipping Elytra", Notification.Type.SUCCESS, "ElytraSwap", 1);
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, t, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                    for (j = 9; j < 45; ++j) {
                        if (ElytraSwap.mc.field_71439_g.field_71071_by.func_70301_a(j).func_77973_b() != Items.field_190931_a) continue;
                        u = j;
                        break;
                    }
                    ElytraSwap.mc.field_71442_b.func_187098_a(0, u, 0, ClickType.PICKUP, (EntityPlayer)ElytraSwap.mc.field_71439_g);
                }
                if (t == 0) {
                    MessageBus.sendClientDeleteMessage("You do not have a Elytra in your inventory. Keeping Chestplate equipped", Notification.Type.ERROR, "ElytraSwap", 1);
                }
                this.disable();
            }
        }
    }
}
