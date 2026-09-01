/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 */
package com.lemonclient.client.manager;

import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.Manager;
import com.lemonclient.client.manager.managers.ClientEventManager;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.manager.managers.TotemPopManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;

public class ManagerLoader {
    private static final List<Manager> managers = new ArrayList<Manager>();

    public static void init() {
        ManagerLoader.register(ClientEventManager.INSTANCE);
        ManagerLoader.register(PlayerPacketManager.INSTANCE);
        ManagerLoader.register(TotemPopManager.INSTANCE);
    }

    private static void register(Manager manager) {
        managers.add(manager);
        LemonClient.EVENT_BUS.subscribe(manager);
        MinecraftForge.EVENT_BUS.register((Object)manager);
    }
}
