/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.profiler.Profiler
 */
package com.lemonclient.client.manager;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Profiler;

public interface Manager
extends Listenable {
    default public Minecraft getMinecraft() {
        return Minecraft.func_71410_x();
    }

    default public EntityPlayerSP getPlayer() {
        return this.getMinecraft().field_71439_g;
    }

    default public WorldClient getWorld() {
        return this.getMinecraft().field_71441_e;
    }

    default public Profiler getProfiler() {
        return this.getMinecraft().field_71424_I;
    }
}
