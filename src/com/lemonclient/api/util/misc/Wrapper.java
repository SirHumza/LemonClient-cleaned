/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.world.World
 */
package com.lemonclient.api.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;

public class Wrapper {
    public static EntityPlayerSP getPlayer() {
        EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
        return player;
    }

    public static Minecraft getMinecraft() {
        Minecraft minecraft = Minecraft.func_71410_x();
        return minecraft;
    }

    public static World getWorld() {
        WorldClient world = Minecraft.func_71410_x().field_71441_e;
        return world;
    }
}
