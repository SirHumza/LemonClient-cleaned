/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.command.commands;

import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.command.Command;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;

@Command.Declaration(name="Toggle", syntax="toggle [module]", alias={"toggle", "t", "enable", "disable"})
public class ToggleCommand
extends Command {
    @Override
    public void onCommand(String command, String[] message, boolean none) {
        String string;
        String main = message[0];
        Module module = ModuleManager.getModule(main);
        if (module == null) {
            string = this.getSyntax();
        } else {
            module.toggle();
            string = module.isEnabled() ? "Module " + module.getName() + " set to: ENABLED!" : "Module " + module.getName() + " set to: DISABLED!";
        }
        if (none) {
            MessageBus.sendServerMessage(string);
        } else {
            MessageBus.sendCommandMessage(string, true);
        }
    }
}
