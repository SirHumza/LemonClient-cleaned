/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.command.commands;

import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.command.Command;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

@Command.Declaration(name="OpenFolder", syntax="openfolder", alias={"openfolder", "open", "folder"})
public class OpenFolderCommand
extends Command {
    @Override
    public void onCommand(String command, String[] message, boolean none) {
        try {
            Desktop.getDesktop().open(new File("LemonClient/".replace("/", "")));
            MessageBus.sendCommandMessage("Opened config folder!", true);
        }
        catch (IOException e) {
            MessageBus.sendCommandMessage("Could not open config folder!", true);
            e.printStackTrace();
        }
    }
}
