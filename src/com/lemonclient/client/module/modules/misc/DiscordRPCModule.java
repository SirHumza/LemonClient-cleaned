/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 */
package com.lemonclient.client.module.modules.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

@Module.Declaration(name="DiscordRPC", category=Category.Misc)
public class DiscordRPCModule
extends Module {
    private static final String applicationId = "899193061324775454";
    BooleanSetting PlayerID = this.registerBoolean("Player ID", true);
    BooleanSetting ServerIp = this.registerBoolean("Server IP", true);
    BooleanSetting coords = this.registerBoolean("Coords", true);
    private final DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    DiscordEventHandlers handlers = new DiscordEventHandlers();
    DiscordRichPresence presence = new DiscordRichPresence();
    static String lastChat;
    static ServerData svr;
    @EventHandler
    private final Listener<ClientChatReceivedEvent> listener = new Listener<ClientChatReceivedEvent>(event -> {
        lastChat = event.getMessage().func_150260_c();
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        this.init();
    }

    @Override
    public void onDisable() {
        this.discordRPC.Discord_Shutdown();
        this.discordRPC.Discord_ClearPresence();
    }

    private void init() {
        this.discordRPC.Discord_Initialize(applicationId, this.handlers, true, "");
        this.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        this.presence.state = "Main Menu";
        this.presence.details = (Boolean)this.PlayerID.getValue() != false ? DiscordRPCModule.ID() : "";
        this.presence.largeImageKey = "lemonclient";
        this.presence.largeImageText = "Lemon Client v0.0.9";
        this.discordRPC.Discord_UpdatePresence(this.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && this.isEnabled()) {
                try {
                    this.discordRPC.Discord_RunCallbacks();
                    this.presence.details = (Boolean)this.PlayerID.getValue() != false ? DiscordRPCModule.ID() : "";
                    this.presence.state = "";
                    if (((Boolean)this.coords.getValue()).booleanValue() && DiscordRPCModule.mc.field_71439_g != null && DiscordRPCModule.mc.field_71441_e != null) {
                        this.presence.smallImageKey = "lazy_crocodile";
                        String dimension = this.dimension() == -1 ? "Nether" : (this.dimension() == 0 ? "Overworld" : "The End");
                        this.presence.smallImageText = "X:" + (int)DiscordRPCModule.mc.field_71439_g.field_70165_t + " Y:" + (int)DiscordRPCModule.mc.field_71439_g.field_70163_u + " Z:" + (int)DiscordRPCModule.mc.field_71439_g.field_70161_v + " (" + dimension + ")";
                    } else {
                        this.presence.smallImageText = "";
                    }
                    if (mc.func_71387_A()) {
                        this.presence.state = "Single Player";
                    } else if (mc.func_147104_D() != null) {
                        svr = mc.func_147104_D();
                        if (!DiscordRPCModule.svr.field_78845_b.equals("")) {
                            if (((Boolean)this.ServerIp.getValue()).booleanValue()) {
                                this.presence.state = "Multi Player (" + DiscordRPCModule.svr.field_78845_b + ")";
                                if (DiscordRPCModule.svr.field_78845_b.equals("2b2t.org")) {
                                    try {
                                        if (lastChat.contains("Position in queue: ")) {
                                            this.presence.details = this.presence.details + " (in queue" + Integer.parseInt(lastChat.substring(19)) + ")";
                                        }
                                    }
                                    catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                this.presence.state = "Multi Player";
                            }
                        }
                    } else {
                        this.presence.details = "Main Menu";
                    }
                    this.discordRPC.Discord_UpdatePresence(this.presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }

    private int dimension() {
        return DiscordRPCModule.mc.field_71439_g.field_71093_bK;
    }

    public static String ID() {
        if (DiscordRPCModule.mc.field_71439_g != null) {
            return DiscordRPCModule.mc.field_71439_g.func_70005_c_();
        }
        return mc.func_110432_I().func_111285_a();
    }
}
