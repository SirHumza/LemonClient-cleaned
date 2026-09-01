/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiNewChat
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package com.lemonclient.api.util.chat;

import com.lemonclient.api.util.chat.SkippingCounter;
import com.lemonclient.api.util.chat.SubscriberImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil
extends SubscriberImpl {
    public static Minecraft mc = Minecraft.func_71410_x();
    private static final Map<Integer, Map<String, Integer>> message_ids = new ConcurrentHashMap<Integer, Map<String, Integer>>();
    private static final SkippingCounter counter = new SkippingCounter(1337, i -> i != -1);

    public void clear() {
        if (ChatUtil.mc.field_71456_v != null) {
            message_ids.values().forEach(m -> m.values().forEach(id -> ChatUtil.mc.field_71456_v.func_146158_b().func_146242_c(id.intValue())));
        }
        message_ids.clear();
        counter.reset();
    }

    public static void sendMessage(String message) {
        ChatUtil.sendMessage(message, 0);
    }

    public static void sendClientMessage(String append, String modulename) {
        ChatUtil.sendDeleteMessage(append, modulename, 1000);
    }

    public static void sendMessage(String message, int id) {
        ChatUtil.sendComponent((ITextComponent)new TextComponentString(message == null ? "null" : message), id);
    }

    public static void sendComponent(ITextComponent component) {
        ChatUtil.sendComponent(component, 0);
    }

    public static void sendComponent(ITextComponent c, int id) {
        ChatUtil.applyIfPresent(g -> g.func_146234_a(c, id));
    }

    public void sendDeleteMessageScheduled(String message, String uniqueWord, int senderID) {
        Integer id = message_ids.computeIfAbsent(senderID, v -> new ConcurrentHashMap()).computeIfAbsent(uniqueWord, v -> counter.next());
        mc.func_152344_a(() -> ChatUtil.sendMessage(message, id));
    }

    public static void sendDeleteMessage(String message, String uniqueWord, int senderID) {
        Integer id = message_ids.computeIfAbsent(senderID, v -> new ConcurrentHashMap()).computeIfAbsent(uniqueWord, v -> counter.next());
        ChatUtil.sendMessage(message, id);
    }

    public void deleteMessage(String uniqueWord, int senderID) {
        Integer id;
        Map<String, Integer> map = message_ids.get(senderID);
        if (map != null && (id = map.remove(uniqueWord)) != null) {
            ChatUtil.deleteMessage(id);
        }
    }

    public static void deleteMessage(int id) {
        ChatUtil.applyIfPresent(g -> g.func_146242_c(id));
    }

    public static void applyIfPresent(Consumer<GuiNewChat> consumer) {
        GuiNewChat chat = ChatUtil.getChatGui();
        if (chat != null) {
            consumer.accept(chat);
        }
    }

    public static GuiNewChat getChatGui() {
        if (ChatUtil.mc.field_71456_v != null) {
            return ChatUtil.mc.field_71456_v.func_146158_b();
        }
        return null;
    }
}
