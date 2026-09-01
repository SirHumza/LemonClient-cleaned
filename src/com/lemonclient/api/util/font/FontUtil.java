/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.lemonclient.api.util.font;

import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.client.LemonClient;
import net.minecraft.client.Minecraft;

public class FontUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static float drawStringWithShadow(boolean customFont, String text, float x, float y, GSColor color) {
        if (customFont) {
            return LemonClient.INSTANCE.cFontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return FontUtil.mc.field_71466_p.func_175063_a(text, x, y, color.getRGB());
    }

    public static float drawStringWithShadow(boolean customFont, String text, String mark, float x, float y, GSColor color) {
        FontUtil.mc.field_71466_p.func_175063_a(mark, x, y, color.getRGB());
        if (customFont) {
            return LemonClient.INSTANCE.cFontRenderer.drawStringWithShadow(text, x + (float)FontUtil.mc.field_71466_p.func_78256_a(mark), y, color);
        }
        return FontUtil.mc.field_71466_p.func_175063_a(text, x + (float)FontUtil.mc.field_71466_p.func_78256_a(mark), y, color.getRGB());
    }

    public static int getStringWidth(boolean customFont, String string) {
        if (customFont) {
            return LemonClient.INSTANCE.cFontRenderer.getStringWidth(string);
        }
        return FontUtil.mc.field_71466_p.func_78256_a(string);
    }

    public static int getFontHeight(boolean customFont) {
        if (customFont) {
            return LemonClient.INSTANCE.cFontRenderer.getHeight();
        }
        return FontUtil.mc.field_71466_p.field_78288_b;
    }
}
