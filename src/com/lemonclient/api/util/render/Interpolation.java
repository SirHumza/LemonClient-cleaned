/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.lemonclient.api.util.render;

import net.minecraft.client.Minecraft;

public class Interpolation {
    public static Minecraft mc = Minecraft.func_71410_x();

    public static double getRenderPosX() {
        return Interpolation.mc.func_175598_ae().field_78725_b;
    }

    public static double getRenderPosY() {
        return Interpolation.mc.func_175598_ae().field_78726_c;
    }

    public static double getRenderPosZ() {
        return Interpolation.mc.func_175598_ae().field_78723_d;
    }
}
