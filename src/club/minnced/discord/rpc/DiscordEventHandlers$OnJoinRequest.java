/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Callback
 */
package club.minnced.discord.rpc;

import club.minnced.discord.rpc.DiscordUser;
import com.sun.jna.Callback;

public static interface DiscordEventHandlers.OnJoinRequest
extends Callback {
    public void accept(DiscordUser var1);
}
