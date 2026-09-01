/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.CommandBase
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.inventory.InventoryBasic
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraftforge.client.IClientCommand
 */
package com.lemonclient.client;

import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.PeekCmd;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.misc.ShulkerBypass;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraftforge.client.IClientCommand;

public static class PeekCmd.PeekCommand
extends CommandBase
implements IClientCommand {
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    public String func_71517_b() {
        return "peek";
    }

    public String func_71518_a(ICommandSender sender) {
        return null;
    }

    public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) {
        if (PeekCmd.mc.field_71439_g != null && ModuleManager.getModule("Peek").isEnabled() && ShulkerBypass.shulkers) {
            if (!shulker.func_190926_b()) {
                NBTTagCompound shulkerNBT = PeekCmd.getShulkerNBT(shulker);
                if (shulkerNBT != null) {
                    TileEntityShulkerBox fakeShulker = new TileEntityShulkerBox();
                    fakeShulker.func_190586_e(shulkerNBT);
                    String customName = "container.shulkerBox";
                    boolean hasCustomName = false;
                    if (shulkerNBT.func_150297_b("CustomName", 8)) {
                        customName = shulkerNBT.func_74779_i("CustomName");
                        hasCustomName = true;
                    }
                    InventoryBasic inv = new InventoryBasic(customName, hasCustomName, 27);
                    for (int i = 0; i < 27; ++i) {
                        inv.func_70299_a(i, fakeShulker.func_70301_a(i));
                    }
                    toOpen = inv;
                    guiTicks = 0;
                }
            } else {
                MessageBus.sendMessage("No shulker detected! please drop and pickup your shulker.", Notification.Type.ERROR, "Peek", 3, ShulkerBypass.notification);
            }
        }
    }

    public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
