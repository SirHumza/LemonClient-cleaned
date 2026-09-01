/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Callback
 */
package club.minnced.discord.rpc;

import com.sun.jna.Callback;

public static interface DiscordEventHandlers.OnStatus
extends Callback {
    public void accept(int var1, String var2);
}
