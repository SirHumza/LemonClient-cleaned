/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.ColorUtil;
import com.lemonclient.api.util.misc.Pair;
import com.lemonclient.client.command.CommandManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

@Module.Declaration(name="ChatModifier", category=Category.Misc)
public class ChatModifier
extends Module {
    public BooleanSetting clearBkg = this.registerBoolean("Clear Chat", false);
    public Pair<Object, Object> watermarkSpecial;
    BooleanSetting greenText = this.registerBoolean("Green Text", false);
    BooleanSetting chatTimeStamps = this.registerBoolean("Chat Time Stamps", false);
    ModeSetting format = this.registerMode("Format", Arrays.asList("H24:mm", "H12:mm", "H12:mm a", "H24:mm:ss", "H12:mm:ss", "H12:mm:ss a"), "H24:mm");
    ModeSetting decoration = this.registerMode("Deco", Arrays.asList("< >", "[ ]", "{ }", " "), "[ ]");
    ModeSetting color = this.registerMode("Color", ColorUtil.colors, ChatFormatting.GRAY.getName());
    BooleanSetting space = this.registerBoolean("Space", false);
    @EventHandler
    private final Listener<ClientChatReceivedEvent> chatReceivedEventListener = new Listener<ClientChatReceivedEvent>(event -> {
        if (((Boolean)this.chatTimeStamps.getValue()).booleanValue()) {
            String decoRight;
            String decoLeft = ((String)this.decoration.getValue()).equalsIgnoreCase(" ") ? "" : ((String)this.decoration.getValue()).split(" ")[0];
            String string = decoRight = ((String)this.decoration.getValue()).equalsIgnoreCase(" ") ? "" : ((String)this.decoration.getValue()).split(" ")[1];
            if (((Boolean)this.space.getValue()).booleanValue()) {
                decoRight = decoRight + " ";
            }
            String dateFormat = ((String)this.format.getValue()).replace("H24", "k").replace("H12", "h");
            String date = new SimpleDateFormat(dateFormat).format(new Date());
            TextComponentString time = new TextComponentString(ChatFormatting.getByName((String)((String)this.color.getValue())) + decoLeft + date + decoRight + ChatFormatting.RESET);
            event.setMessage(time.func_150257_a(event.getMessage()));
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (((Boolean)this.greenText.getValue()).booleanValue() && event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith("/") || ((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith(CommandManager.getCommandPrefix())) {
                return;
            }
            String message = ((CPacketChatMessage)event.getPacket()).func_149439_c();
            String prefix = "";
            prefix = ">";
            String s = prefix + message;
            if (s.length() > 255) {
                return;
            }
            ((CPacketChatMessage)event.getPacket()).field_149440_a = s;
        }
    }, new Predicate[0]);
}
