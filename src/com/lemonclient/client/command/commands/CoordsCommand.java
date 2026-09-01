/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.command.commands;

import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.command.Command;

@Command.Declaration(name="Coords", syntax="coords [module]", alias={"coords", "position", "pos"})
public class CoordsCommand
extends Command {
    @Override
    public void onCommand(String command, String[] message, boolean none) {
        if (CoordsCommand.mc.field_71439_g == null || CoordsCommand.mc.field_71441_e == null) {
            return;
        }
        String name = message[0];
        MessageBus.sendServerMessage("/msg " + name + " X:" + (int)CoordsCommand.mc.field_71439_g.field_70165_t + ", Y:" + (int)CoordsCommand.mc.field_71439_g.field_70163_u + ", Z:" + (int)CoordsCommand.mc.field_71439_g.field_70161_v);
    }
}
