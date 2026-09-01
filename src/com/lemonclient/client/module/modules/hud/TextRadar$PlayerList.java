/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.text.TextFormatting
 */
package com.lemonclient.client.module.modules.hud;

import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lukflug.panelstudio.hud.HUDList;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

private class TextRadar.PlayerList
implements HUDList {
    public List<EntityPlayer> players = new ArrayList<EntityPlayer>();

    private TextRadar.PlayerList() {
    }

    @Override
    public int getSize() {
        return this.players.size();
    }

    @Override
    public String getItem(int index) {
        EntityPlayer e = this.players.get(index);
        TextFormatting friendcolor = SocialManager.isFriend(e.func_70005_c_()) ? ModuleManager.getModule(ColorMain.class).getFriendColor() : (SocialManager.isEnemy(e.func_70005_c_()) ? ModuleManager.getModule(ColorMain.class).getEnemyColor() : TextFormatting.GRAY);
        float health = e.func_110143_aJ() + e.func_110139_bj();
        TextFormatting healthcolor = health <= 5.0f ? TextFormatting.RED : (health > 5.0f && health < 15.0f ? TextFormatting.YELLOW : TextFormatting.GREEN);
        float distance = mc.field_71439_g.func_70032_d((Entity)e);
        TextFormatting distancecolor = distance < 20.0f ? TextFormatting.RED : (distance >= 20.0f && distance < 50.0f ? TextFormatting.YELLOW : TextFormatting.GREEN);
        return TextFormatting.GRAY + "[" + healthcolor + (int)health + TextFormatting.GRAY + "] " + friendcolor + e.func_70005_c_() + TextFormatting.GRAY + " [" + distancecolor + (int)distance + TextFormatting.GRAY + "]";
    }

    @Override
    public Color getItemColor(int index) {
        return new Color(255, 255, 255);
    }

    @Override
    public boolean sortUp() {
        return (Boolean)TextRadar.this.sortUp.getValue();
    }

    @Override
    public boolean sortRight() {
        return (Boolean)TextRadar.this.sortRight.getValue();
    }
}
