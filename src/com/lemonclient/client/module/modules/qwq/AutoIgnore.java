/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 */
package com.lemonclient.client.module.modules.qwq;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;

@Module.Declaration(name="AutoIgnore", category=Category.qwq)
public class AutoIgnore
extends Module {
    BooleanSetting filterFriend = this.registerBoolean("Filter Friend", false);
    BooleanSetting ignoreAll = this.registerBoolean("AllWhisper", false);
    BooleanSetting playerCheck = this.registerBoolean("PlayerCheck", true);
    IntegerSetting times = this.registerInteger("Times", 10, 0, 30);
    IntegerSetting life = this.registerInteger("LifeTime", 600, 0, 3000);
    HashMap<String, Integer> messageTimes = new HashMap();
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (AutoIgnore.mc.field_71439_g == null) {
            return;
        }
        if (!(event.getPacket() instanceof SPacketChat)) {
            return;
        }
        String message = ((SPacketChat)event.getPacket()).func_148915_c().func_150260_c();
        if (((Boolean)this.ignoreAll.getValue()).booleanValue() && message.contains(":")) {
            String username = "";
            int spaceIndex = message.indexOf(" ");
            if (spaceIndex != -1) {
                username = message.substring(0, spaceIndex);
            }
            if (!username.isEmpty() && !SocialManager.isOnIgnoreList(username) && !SocialManager.isOnFriendList(username) || !((Boolean)this.filterFriend.getValue()).booleanValue()) {
                SocialManager.addIgnore(username);
                MessageBus.sendClientDeleteMessage(username + " has been added to ignore list", Notification.Type.INFO, "AutoIgnore", 13);
            }
        }
        String s = message.replaceAll("\\[.*?]|<.*?>|\\d+", "");
        this.addToList(s);
        if (this.messageTimes.get(s) > (Integer)this.times.getValue()) {
            int spaceIndex;
            Matcher matcher = Pattern.compile("<.*?> ").matcher(message);
            String username = "";
            if (matcher.find()) {
                username = matcher.group();
                username = username.substring(1, username.length() - 2);
            } else if (message.contains(":") && (spaceIndex = message.indexOf(" ")) != -1) {
                username = message.substring(0, spaceIndex);
            }
            username = ColorMain.cleanColor(username);
            if (username.equals(AutoIgnore.mc.field_71439_g.func_70005_c_()) || ((Boolean)this.playerCheck.getValue()).booleanValue() && AutoIgnore.mc.field_71439_g.field_71174_a.func_175104_a(username) == null) {
                return;
            }
            if (!username.isEmpty() && !SocialManager.isOnIgnoreList(username) && !SocialManager.isOnFriendList(username) || !((Boolean)this.filterFriend.getValue()).booleanValue()) {
                SocialManager.addIgnore(username);
                MessageBus.sendClientDeleteMessage(username + " has been added to ignore list", Notification.Type.INFO, "AutoIgnore", 13);
            }
            event.cancel();
        }
    }, new Predicate[0]);

    public void addToList(final String string) {
        int time = 1;
        if (this.messageTimes.containsKey(string)) {
            time += this.messageTimes.get(string).intValue();
        }
        this.messageTimes.put(string, time);
        new Timer().schedule(new TimerTask(){

            @Override
            public void run() {
                AutoIgnore.this.messageTimes.put(string, AutoIgnore.this.messageTimes.get(string) - 1);
            }
        }, (Integer)this.life.getValue() * 1000);
    }
}
