/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package com.lemonclient.client.module.modules.hud;

import com.lukflug.panelstudio.hud.HUDList;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

private class PotionEffects.PotionList
implements HUDList {
    private PotionEffects.PotionList() {
    }

    @Override
    public int getSize() {
        return mc.field_71439_g.func_70651_bq().size();
    }

    @Override
    public String getItem(int index) {
        PotionEffect effect = (PotionEffect)mc.field_71439_g.func_70651_bq().toArray()[index];
        String name = I18n.func_135052_a((String)effect.func_188419_a().func_76393_a(), (Object[])new Object[0]);
        int amplifier = effect.func_76458_c() + 1;
        return name + " " + amplifier + ChatFormatting.GRAY + " " + Potion.func_188410_a((PotionEffect)effect, (float)1.0f);
    }

    @Override
    public Color getItemColor(int i) {
        if (mc.field_71439_g.func_70651_bq().toArray().length != 0) {
            return PotionEffects.this.getColour((PotionEffect)mc.field_71439_g.func_70651_bq().toArray()[i]);
        }
        return null;
    }

    @Override
    public boolean sortUp() {
        return (Boolean)PotionEffects.this.sortUp.getValue();
    }

    @Override
    public boolean sortRight() {
        return (Boolean)PotionEffects.this.sortRight.getValue();
    }
}
