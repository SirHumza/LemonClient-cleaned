/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 */
package com.lemonclient.api.util.misc;

import com.lemonclient.api.util.chat.ChatUtil;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.chat.NotificationManager;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lemonclient.client.module.modules.hud.Notifications;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MessageBus {
    public static String watermark = ChatFormatting.GREEN + "[" + ChatFormatting.YELLOW + "Lemon" + ChatFormatting.GREEN + "] " + ChatFormatting.RESET;
    public static ChatFormatting messageFormatting = ChatFormatting.GRAY;
    protected static final Minecraft mc = Minecraft.func_71410_x();

    public static void printDebug(String text, Boolean error) {
        ColorMain colorMain = ModuleManager.getModule(ColorMain.class);
        MessageBus.sendClientPrefixMessage((error != false ? colorMain.getDisabledColor() : colorMain.getEnabledColor()) + text, error != false ? Notification.Type.ERROR : Notification.Type.INFO);
    }

    public static void sendClientPrefixMessage(String message, Notification.Type type) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        MessageBus.setWatermark();
        TextComponentString string1 = new TextComponentString(watermark + messageFormatting + message);
        Notifications notifications = ModuleManager.getModule(Notifications.class);
        if (notifications.isEnabled()) {
            NotificationManager.add(new Notification(TextFormatting.GRAY + message, type));
            if (((Boolean)notifications.disableChat.getValue()).booleanValue()) {
                return;
            }
        }
        MessageBus.mc.field_71439_g.func_145747_a((ITextComponent)string1);
    }

    public static void sendMessage(String message, Notification.Type type, String uniqueWord, int senderID, boolean notification) {
        if (notification) {
            MessageBus.sendClientDeleteMessage(message, type, uniqueWord, senderID);
        } else {
            MessageBus.sendDeleteMessage(message, uniqueWord, senderID);
        }
    }

    public static void sendClientDeleteMessage(String message, Notification.Type type, String uniqueWord, int senderID) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        MessageBus.setWatermark();
        Notifications notifications = ModuleManager.getModule(Notifications.class);
        if (notifications.isEnabled()) {
            NotificationManager.add(new Notification(TextFormatting.GRAY + message, type));
            if (((Boolean)notifications.disableChat.getValue()).booleanValue()) {
                return;
            }
        }
        ChatUtil.sendDeleteMessage(watermark + messageFormatting + message, uniqueWord, senderID);
    }

    public static void sendDeleteMessage(String message, String uniqueWord, int senderID) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        MessageBus.setWatermark();
        ChatUtil.sendDeleteMessage(watermark + messageFormatting + message, uniqueWord, senderID);
    }

    public static void sendCommandMessage(String message, boolean prefix) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        MessageBus.setWatermark();
        String watermark1 = prefix ? watermark : "";
        ChatUtil.sendDeleteMessage(watermark1 + messageFormatting + message, "Command", 6);
    }

    public static void sendMessage(String message, boolean prefix) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        MessageBus.setWatermark();
        String watermark1 = prefix ? watermark : "";
        TextComponentString string = new TextComponentString(watermark1 + messageFormatting + message);
        MessageBus.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)string, MessageBus.getIdFromString(message));
    }

    public static int getIdFromString(String name) {
        StringBuilder s = new StringBuilder();
        name = name.replace("\u79ae", "e");
        String blacklist = "[^a-z]";
        for (int i = 0; i < name.length(); ++i) {
            s.append(Integer.parseInt(String.valueOf(name.charAt(i)).replaceAll(blacklist, "e"), 36));
        }
        try {
            s = new StringBuilder(s.substring(0, 8));
        }
        catch (StringIndexOutOfBoundsException ignored) {
            s = new StringBuilder(Integer.MAX_VALUE);
        }
        return Integer.MAX_VALUE - Integer.parseInt(s.toString().toLowerCase());
    }

    public static void sendClientRawMessage(String message) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        TextComponentString string = new TextComponentString(messageFormatting + message);
        MessageBus.mc.field_71439_g.func_145747_a((ITextComponent)string);
    }

    public static void sendServerMessage(String message) {
        if (MessageBus.mc.field_71441_e == null || MessageBus.mc.field_71439_g == null) {
            return;
        }
        MessageBus.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage(message));
    }

    public static void setWatermark() {
        watermark = ChatFormatting.GREEN + "[" + ChatFormatting.YELLOW + "Lemon" + ChatFormatting.GREEN + "] " + ChatFormatting.RESET;
    }
}
