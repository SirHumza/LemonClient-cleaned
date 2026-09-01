/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package com.lemonclient.client.clickgui;

import com.lemonclient.api.util.font.FontUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lukflug.panelstudio.mc12.MinecraftGUI;
import java.awt.Color;
import java.awt.Point;
import net.minecraft.client.renderer.GlStateManager;

class LemonClientGUI.1
extends MinecraftGUI.GUIInterface {
    final /* synthetic */ ColorMain val$colorMain;

    LemonClientGUI.1(boolean x0, ColorMain colorMain) {
        this.val$colorMain = colorMain;
        super(LemonClientGUI.this, x0);
    }

    @Override
    public void drawString(Point pos, int height, String s, Color c) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)pos.x, (float)pos.y, (float)0.0f);
        double scale = (double)height / (double)(FontUtil.getFontHeight((Boolean)this.val$colorMain.customFont.getValue()) + ((Boolean)this.val$colorMain.customFont.getValue() != false ? 1 : 0));
        this.end(false);
        FontUtil.drawStringWithShadow((Boolean)this.val$colorMain.customFont.getValue(), s, 0.0f, 0.0f, new GSColor(c));
        this.begin(false);
        GlStateManager.func_179139_a((double)scale, (double)scale, (double)1.0);
        GlStateManager.func_179121_F();
    }

    @Override
    public int getFontWidth(int height, String s) {
        double scale = (double)height / (double)(FontUtil.getFontHeight((Boolean)this.val$colorMain.customFont.getValue()) + ((Boolean)this.val$colorMain.customFont.getValue() != false ? 1 : 0));
        return (int)Math.round((double)FontUtil.getStringWidth((Boolean)this.val$colorMain.customFont.getValue(), s) * scale);
    }

    @Override
    public double getScreenWidth() {
        return super.getScreenWidth();
    }

    @Override
    public double getScreenHeight() {
        return super.getScreenHeight();
    }

    @Override
    public String getResourcePrefix() {
        return "lemonclient:gui/";
    }
}
