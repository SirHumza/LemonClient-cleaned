/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.boss.EntityWither
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemNameTag
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;

@Module.Declaration(name="AutoNametag", category=Category.Misc)
public class AutoNametag
extends Module {
    ModeSetting modeSetting = this.registerMode("Mode", Arrays.asList("Any", "Wither"), "Wither");
    DoubleSetting range = this.registerDouble("Range", 3.5, 0.0, 10.0);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting check = this.registerBoolean("Switch Check", true);
    BooleanSetting disable = this.registerBoolean("Auto Disable", true);
    private String currentName = "";
    private int currentSlot = -1;

    @Override
    public void onUpdate() {
        this.findNameTags();
        this.useNameTag();
    }

    private void switchTo(int slot) {
        if (!(slot <= -1 || slot >= 9 || ((Boolean)this.check.getValue()).booleanValue() && AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c == slot)) {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                AutoNametag.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            AutoNametag.mc.field_71442_b.func_78765_e();
        }
    }

    private void useNameTag() {
        int originalSlot = AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c;
        for (Entity w : AutoNametag.mc.field_71441_e.func_72910_y()) {
            switch ((String)this.modeSetting.getValue()) {
                case "Wither": {
                    if (!(w instanceof EntityWither) || w.func_145748_c_().func_150260_c().equals(this.currentName) || !((double)AutoNametag.mc.field_71439_g.func_70032_d(w) <= (Double)this.range.getValue())) break;
                    int oldslot = AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c;
                    this.selectNameTags();
                    AutoNametag.mc.field_71442_b.func_187097_a((EntityPlayer)AutoNametag.mc.field_71439_g, w, EnumHand.MAIN_HAND);
                    this.switchTo(oldslot);
                    break;
                }
                case "Any": {
                    if (!(w instanceof EntityMob) && !(w instanceof EntityAnimal) || w.func_145748_c_().func_150260_c().equals(this.currentName) || !((double)AutoNametag.mc.field_71439_g.func_70032_d(w) <= (Double)this.range.getValue())) break;
                    int oldslot = AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c;
                    this.selectNameTags();
                    AutoNametag.mc.field_71442_b.func_187097_a((EntityPlayer)AutoNametag.mc.field_71439_g, w, EnumHand.MAIN_HAND);
                    this.switchTo(oldslot);
                }
            }
        }
        AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c = originalSlot;
    }

    private void selectNameTags() {
        if (this.currentSlot == -1 || !this.isNametag(this.currentSlot)) {
            if (((Boolean)this.disable.getValue()).booleanValue()) {
                this.disable();
            }
            return;
        }
        this.switchTo(this.currentSlot);
    }

    private void findNameTags() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoNametag.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || stack.func_77973_b() instanceof ItemBlock || !this.isNametag(i)) continue;
            this.currentName = stack.func_82833_r();
            this.currentSlot = i;
        }
    }

    private boolean isNametag(int i) {
        ItemStack stack = AutoNametag.mc.field_71439_g.field_71071_by.func_70301_a(i);
        Item tag = stack.func_77973_b();
        return tag instanceof ItemNameTag && !stack.func_82833_r().equals("Name Tag");
    }
}
