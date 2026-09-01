/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.command;

import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.command.Command;
import com.lemonclient.client.command.commands.AutoGearCommand;
import com.lemonclient.client.command.commands.BackupConfigCommand;
import com.lemonclient.client.command.commands.BindCommand;
import com.lemonclient.client.command.commands.CmdListCommand;
import com.lemonclient.client.command.commands.CoordsCommand;
import com.lemonclient.client.command.commands.DisableAllCommand;
import com.lemonclient.client.command.commands.DrawnCommand;
import com.lemonclient.client.command.commands.EnemyCommand;
import com.lemonclient.client.command.commands.FixGUICommand;
import com.lemonclient.client.command.commands.FixHUDCommand;
import com.lemonclient.client.command.commands.FontCommand;
import com.lemonclient.client.command.commands.FriendCommand;
import com.lemonclient.client.command.commands.IgnoreCommand;
import com.lemonclient.client.command.commands.LoadCapeCommand;
import com.lemonclient.client.command.commands.LoadConfigCommand;
import com.lemonclient.client.command.commands.ModulesCommand;
import com.lemonclient.client.command.commands.MsgsCommand;
import com.lemonclient.client.command.commands.OpenFolderCommand;
import com.lemonclient.client.command.commands.PrefixCommand;
import com.lemonclient.client.command.commands.RefreshGUICommand;
import com.lemonclient.client.command.commands.SaveConfigCommand;
import com.lemonclient.client.command.commands.SetCommand;
import com.lemonclient.client.command.commands.ToggleCommand;
import java.util.ArrayList;

public class CommandManager {
    private static String commandPrefix = "-";
    public static final ArrayList<Command> commands = new ArrayList();
    public static boolean isValidCommand = false;

    public static void init() {
        CommandManager.addCommand(new AutoGearCommand());
        CommandManager.addCommand(new BackupConfigCommand());
        CommandManager.addCommand(new BindCommand());
        CommandManager.addCommand(new CmdListCommand());
        CommandManager.addCommand(new CoordsCommand());
        CommandManager.addCommand(new DisableAllCommand());
        CommandManager.addCommand(new DrawnCommand());
        CommandManager.addCommand(new EnemyCommand());
        CommandManager.addCommand(new FixGUICommand());
        CommandManager.addCommand(new FixHUDCommand());
        CommandManager.addCommand(new FontCommand());
        CommandManager.addCommand(new FriendCommand());
        CommandManager.addCommand(new IgnoreCommand());
        CommandManager.addCommand(new LoadCapeCommand());
        CommandManager.addCommand(new LoadConfigCommand());
        CommandManager.addCommand(new ModulesCommand());
        CommandManager.addCommand(new MsgsCommand());
        CommandManager.addCommand(new OpenFolderCommand());
        CommandManager.addCommand(new PrefixCommand());
        CommandManager.addCommand(new RefreshGUICommand());
        CommandManager.addCommand(new SaveConfigCommand());
        CommandManager.addCommand(new SetCommand());
        CommandManager.addCommand(new ToggleCommand());
    }

    public static void addCommand(Command command) {
        commands.add(command);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String getCommandPrefix() {
        return commandPrefix;
    }

    public static void setCommandPrefix(String prefix) {
        commandPrefix = prefix;
    }

    public static void callCommand(String input, boolean none) {
        String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String command1 = split[0];
        String args = input.substring(command1.length()).trim();
        isValidCommand = false;
        commands.forEach(command -> {
            for (String string : command.getAlias()) {
                if (!string.equalsIgnoreCase(command1)) continue;
                isValidCommand = true;
                try {
                    command.onCommand(args, args.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"), none);
                }
                catch (Exception e) {
                    MessageBus.sendCommandMessage(command.getSyntax(), true);
                }
            }
        });
        if (!isValidCommand) {
            MessageBus.sendCommandMessage("Error! Invalid command!", true);
        }
    }
}
