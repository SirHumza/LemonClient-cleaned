/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.nbt.NBTTagList
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.ColorUtil;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.manager.managers.TotemPopManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagList;

@Module.Declaration(name="PvPInfo", category=Category.Misc)
public class PvPInfo
extends Module {
    BooleanSetting visualRange = this.registerBoolean("Visual Range", false);
    BooleanSetting coords = this.registerBoolean("Coords", true, () -> (Boolean)this.visualRange.getValue());
    BooleanSetting pearlAlert = this.registerBoolean("Pearl Alert", false);
    BooleanSetting strengthDetect = this.registerBoolean("Strength Detect", false);
    BooleanSetting weaknessDetect = this.registerBoolean("Weakness Detect", false);
    BooleanSetting popCounter = this.registerBoolean("Pop Counter", false);
    BooleanSetting friend = this.registerBoolean("My Friend", false);
    BooleanSetting sharp32 = this.registerBoolean("sharp32", true);
    ModeSetting type = this.registerMode("Visual Type", Arrays.asList("Friend", "Enemy", "All"), "All");
    ModeSetting type1 = this.registerMode("Pearl Type", Arrays.asList("Friend", "Enemy", "All"), "All");
    ModeSetting type2 = this.registerMode("Strength Type", Arrays.asList("Friend", "Enemy", "All"), "All");
    ModeSetting type3 = this.registerMode("Weakness Type", Arrays.asList("Friend", "Enemy", "All"), "All");
    ModeSetting type4 = this.registerMode("Pop Type", Arrays.asList("Friend", "Enemy", "All"), "All");
    ModeSetting type5 = this.registerMode("32k Type", Arrays.asList("Friend", "Enemy", "All"), "All");
    ModeSetting self = this.registerMode("Self", Arrays.asList("I", "Name", "Disable"), "Name");
    ModeSetting chatColor = this.registerMode("Color", ColorUtil.colors, "Light Purple");
    ModeSetting nameColor = this.registerMode("Name Color", ColorUtil.colors, "Light Purple");
    ModeSetting friColor = this.registerMode("Friend Color", ColorUtil.colors, "Light Purple");
    ModeSetting numberColor = this.registerMode("Number Color", ColorUtil.colors, "Light Purple");
    List<Entity> knownPlayers = new ArrayList<Entity>();
    List<Entity> antiPearlList = new ArrayList<Entity>();
    List<Entity> players;
    List<Entity> pearls;
    private final Set<EntityPlayer> strengthPlayers = Collections.newSetFromMap(new WeakHashMap());
    private final Set<EntityPlayer> weaknessPlayers = Collections.newSetFromMap(new WeakHashMap());
    private final Set<EntityPlayer> sword = Collections.newSetFromMap(new WeakHashMap());

    @Override
    public void onUpdate() {
        String name;
        String name2;
        if (PvPInfo.mc.field_71439_g == null || PvPInfo.mc.field_71441_e == null) {
            return;
        }
        TotemPopManager.INSTANCE.sendMsgs = (Boolean)this.popCounter.getValue();
        if (((Boolean)this.popCounter.getValue()).booleanValue()) {
            TotemPopManager.INSTANCE.chatFormatting = ColorUtil.textToChatFormatting(this.chatColor);
            TotemPopManager.INSTANCE.nameFormatting = ColorUtil.textToChatFormatting(this.nameColor);
            TotemPopManager.INSTANCE.friFormatting = ColorUtil.textToChatFormatting(this.friColor);
            TotemPopManager.INSTANCE.numberFormatting = ColorUtil.textToChatFormatting(this.numberColor);
            TotemPopManager.INSTANCE.friend = (Boolean)this.friend.getValue();
            TotemPopManager.INSTANCE.self = (String)this.self.getValue();
            TotemPopManager.INSTANCE.type4 = (String)this.type4.getValue();
        }
        if (((Boolean)this.visualRange.getValue()).booleanValue()) {
            String xyz;
            this.players = PvPInfo.mc.field_71441_e.field_73010_i.stream().filter(entity -> !entity.func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_())).collect(Collectors.toList());
            try {
                for (Entity e2 : this.players) {
                    if (e2.func_70005_c_().equalsIgnoreCase("fakeplayer") || this.knownPlayers.contains(e2)) continue;
                    this.knownPlayers.add(e2);
                    xyz = (Boolean)this.coords.getValue() != false ? " at x:" + (int)e2.field_70165_t + " y:" + (int)e2.field_70163_u + " z:" + (int)e2.field_70161_v : "";
                    name2 = e2.func_70005_c_();
                    if (name2.equals("") || name2.equals(" ")) {
                        return;
                    }
                    if (name2.equals("I") || SocialManager.isFriend(name2) && !((String)this.type.getValue()).equals("Enemy")) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.chatColor) + "Found (" + ColorUtil.textToChatFormatting(this.friColor) + name2 + ColorUtil.textToChatFormatting(this.chatColor) + ")" + xyz, Notification.Type.INFO, "VisualRange" + name2, 2000);
                    }
                    if (name2.equals("I") || SocialManager.isFriend(name2) || ((String)this.type.getValue()).equals("Friend")) continue;
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.chatColor) + "Found (" + ColorUtil.textToChatFormatting(this.nameColor) + name2 + ColorUtil.textToChatFormatting(this.chatColor) + ")" + xyz, Notification.Type.INFO, "VisualRange" + name2, 2000);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            try {
                for (Entity e2 : this.knownPlayers) {
                    if (e2.func_70005_c_().equalsIgnoreCase("fakeplayer") || this.players.contains(e2)) continue;
                    this.knownPlayers.remove(e2);
                    xyz = (Boolean)this.coords.getValue() != false ? " at x:" + (int)e2.field_70165_t + " y:" + (int)e2.field_70163_u + " z:" + (int)e2.field_70161_v : "";
                    name2 = e2.func_70005_c_();
                    if (name2.equals("") || name2.equals(" ")) {
                        return;
                    }
                    if (name2.equals("I") || SocialManager.isFriend(name2) && !((String)this.type.getValue()).equals("Enemy")) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.chatColor) + "Gone (" + ColorUtil.textToChatFormatting(this.friColor) + name2 + ColorUtil.textToChatFormatting(this.chatColor) + ")" + xyz, Notification.Type.INFO, "VisualRange" + name2, 2000);
                    }
                    if (name2.equals("I") || SocialManager.isFriend(name2) || ((String)this.type.getValue()).equals("Friend")) continue;
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.chatColor) + "Gone (" + ColorUtil.textToChatFormatting(this.nameColor) + name2 + ColorUtil.textToChatFormatting(this.chatColor) + ")" + xyz, Notification.Type.INFO, "VisualRange" + name2, 2000);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (((Boolean)this.pearlAlert.getValue()).booleanValue()) {
            this.pearls = PvPInfo.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityEnderPearl).collect(Collectors.toList());
            try {
                for (Entity e2 : this.pearls) {
                    if (!(e2 instanceof EntityEnderPearl) || e2.func_130014_f_().func_72890_a(e2, 3.0).func_70005_c_().equalsIgnoreCase("fakeplayer") || this.antiPearlList.contains(e2)) continue;
                    this.antiPearlList.add(e2);
                    String faceing = e2.func_174811_aO().toString();
                    if (faceing.equals("west")) {
                        faceing = "east";
                    } else if (faceing.equals("east")) {
                        faceing = "west";
                    }
                    if (PvPInfo.mc.field_71439_g.func_70005_c_().equals(e2.func_130014_f_().func_72890_a(e2, 3.0).func_70005_c_()) && ((String)this.self.getValue()).equals("Disable")) {
                        return;
                    }
                    String string = name2 = e2.func_130014_f_().func_72890_a(e2, 3.0).func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_()) && ((String)this.self.getValue()).equals("I") ? "I" : e2.func_130014_f_().func_72890_a(e2, 3.0).func_70005_c_();
                    if (name2.equals("") || name2.equals(" ")) {
                        return;
                    }
                    if (name2.equals("I") || SocialManager.isFriend(name2) && !((String)this.type1.getValue()).equals("Enemy")) {
                        MessageBus.sendClientPrefixMessage(ColorUtil.textToChatFormatting(this.friColor) + name2 + ColorUtil.textToChatFormatting(this.chatColor) + " has just thrown a pearl! (" + faceing + ")", Notification.Type.INFO);
                    }
                    if (name2.equals("I") || SocialManager.isFriend(name2) || ((String)this.type1.getValue()).equals("Friend")) continue;
                    MessageBus.sendClientPrefixMessage(ColorUtil.textToChatFormatting(this.nameColor) + name2 + ColorUtil.textToChatFormatting(this.chatColor) + " has just thrown a pearl! (" + faceing + ")", Notification.Type.INFO);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (((Boolean)this.strengthDetect.getValue()).booleanValue()) {
            for (EntityPlayer player : PvPInfo.mc.field_71441_e.field_73010_i) {
                if (player.func_70005_c_().equalsIgnoreCase("fakeplayer")) continue;
                if (player.func_70644_a(MobEffects.field_76420_g) && !this.strengthPlayers.contains(player)) {
                    if (PvPInfo.mc.field_71439_g.func_70005_c_().equals(player.func_70005_c_()) && ((String)this.self.getValue()).equals("Disable")) {
                        return;
                    }
                    String string = name = player.func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_()) && ((String)this.self.getValue()).equals("I") ? "I" : player.func_70005_c_();
                    if (name.equals("") || name.equals(" ")) {
                        return;
                    }
                    if (name.equals("I") || SocialManager.isFriend(name) && !((String)this.type2.getValue()).equals("Enemy")) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.friColor) + name + ColorUtil.textToChatFormatting(this.chatColor) + " has drank strength", Notification.Type.INFO, "Strength" + name, 2000);
                    }
                    if (!(name.equals("I") || SocialManager.isFriend(name) || ((String)this.type2.getValue()).equals("Friend"))) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + ChatFormatting.RED + " has drank strength", Notification.Type.INFO, "Strength" + name, 2000);
                    }
                    this.strengthPlayers.add(player);
                }
                if (!this.strengthPlayers.contains(player) || player.func_70644_a(MobEffects.field_76420_g)) continue;
                if (PvPInfo.mc.field_71439_g.func_70005_c_().equals(player.func_70005_c_()) && ((String)this.self.getValue()).equals("Disable")) {
                    return;
                }
                String string = name = player.func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_()) && ((String)this.self.getValue()).equals("I") ? "I" : player.func_70005_c_();
                if (name.equals("") || name.equals(" ")) {
                    return;
                }
                if (name.equals("I") || SocialManager.isFriend(name) && !((String)this.type2.getValue()).equals("Enemy")) {
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.friColor) + name + ColorUtil.textToChatFormatting(this.chatColor) + " no longer has strength", Notification.Type.INFO, "Strength" + name, 2000);
                }
                if (!(name.equals("I") || SocialManager.isFriend(name) || ((String)this.type2.getValue()).equals("Friend"))) {
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + ChatFormatting.GREEN + " no longer has strength", Notification.Type.INFO, "Strength" + name, 2000);
                }
                this.strengthPlayers.remove(player);
            }
        }
        if (((Boolean)this.weaknessDetect.getValue()).booleanValue()) {
            for (EntityPlayer player : PvPInfo.mc.field_71441_e.field_73010_i) {
                if (player.func_70005_c_().equalsIgnoreCase("FakePlayer")) continue;
                if (player.func_70644_a(MobEffects.field_76437_t) && !this.weaknessPlayers.contains(player)) {
                    if (PvPInfo.mc.field_71439_g.func_70005_c_().equals(player.func_70005_c_()) && ((String)this.self.getValue()).equals("Disable")) {
                        return;
                    }
                    String string = name = player.func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_()) && ((String)this.self.getValue()).equals("I") ? "I" : player.func_70005_c_();
                    if (name.isEmpty() || name.equals(" ")) {
                        return;
                    }
                    if (name.equals("I") || SocialManager.isFriend(name) && !((String)this.type3.getValue()).equals("Enemy")) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.friColor) + name + ColorUtil.textToChatFormatting(this.chatColor) + " has drank weekness", Notification.Type.INFO, "Weakness" + name, 2000);
                    }
                    if (!(name.equals("I") || SocialManager.isFriend(name) || ((String)this.type3.getValue()).equals("Friend"))) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + ChatFormatting.GREEN + " has drank weekness", Notification.Type.INFO, "Weakness" + name, 2000);
                    }
                    this.weaknessPlayers.add(player);
                }
                if (!this.weaknessPlayers.contains(player) || player.func_70644_a(MobEffects.field_76437_t)) continue;
                if (PvPInfo.mc.field_71439_g.func_70005_c_().equals(player.func_70005_c_()) && ((String)this.self.getValue()).equals("Disable")) {
                    return;
                }
                String string = name = player.func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_()) && ((String)this.self.getValue()).equals("I") ? "I" : player.func_70005_c_();
                if (name.equals("") || name.equals(" ")) {
                    return;
                }
                if (name.equals("I") || SocialManager.isFriend(name) && !((String)this.type3.getValue()).equals("Enemy")) {
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.friColor) + name + ColorUtil.textToChatFormatting(this.chatColor) + " no longer has weekness", Notification.Type.INFO, "Weakness" + name, 2000);
                }
                if (!(name.equals("I") || SocialManager.isFriend(name) || ((String)this.type3.getValue()).equals("Friend"))) {
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + ChatFormatting.RED + " no longer has weekness", Notification.Type.INFO, "Weakness" + name, 2000);
                }
                this.weaknessPlayers.remove(player);
            }
        }
        if (((Boolean)this.sharp32.getValue()).booleanValue()) {
            for (EntityPlayer player : PvPInfo.mc.field_71441_e.field_73010_i) {
                if (player.func_70005_c_().equalsIgnoreCase("fakeplayer") || player.func_70005_c_().equals(PvPInfo.mc.field_71439_g.func_70005_c_())) continue;
                if (this.is32k(player.field_184831_bT) && !this.sword.contains(player)) {
                    name = player.func_70005_c_();
                    if (name.equals("") || name.equals(" ")) {
                        return;
                    }
                    if (name.equals("I") || SocialManager.isFriend(name) && !((String)this.type5.getValue()).equals("Enemy")) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + " is " + ColorUtil.textToChatFormatting(this.chatColor) + "holding a 32k", Notification.Type.INFO, "32k" + name, 2000);
                    }
                    if (!(name.equals("I") || SocialManager.isFriend(name) || ((String)this.type5.getValue()).equals("Friend"))) {
                        MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + " is " + ChatFormatting.RED + "holding" + ColorUtil.textToChatFormatting(this.chatColor) + " a 32k", Notification.Type.INFO, "32k" + name, 2000);
                    }
                    this.sword.add(player);
                }
                if (!this.sword.contains(player) || this.is32k(player.field_184831_bT)) continue;
                name = player.func_70005_c_();
                if (name.equals("") || name.equals(" ")) {
                    return;
                }
                if (name.equals("I") || SocialManager.isFriend(name) && !((String)this.type5.getValue()).equals("Enemy")) {
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.friColor) + name + " is " + ColorUtil.textToChatFormatting(this.chatColor) + "no longer holding a 32k", Notification.Type.INFO, "32k" + name, 2000);
                }
                if (!(name.equals("I") || SocialManager.isFriend(name) || ((String)this.type5.getValue()).equals("Friend"))) {
                    MessageBus.sendClientDeleteMessage(ColorUtil.textToChatFormatting(this.nameColor) + name + " is " + ChatFormatting.GREEN + "no longer holding" + ColorUtil.textToChatFormatting(this.chatColor) + " a 32k", Notification.Type.INFO, "32k" + name, 2000);
                }
                this.sword.remove(player);
            }
        }
    }

    private boolean is32k(ItemStack stack) {
        if (stack.func_77973_b() instanceof ItemSword) {
            NBTTagList enchants = stack.func_77986_q();
            for (int i = 0; i < enchants.func_74745_c(); ++i) {
                if (enchants.func_150305_b(i).func_74765_d("lvl") < 1000) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        this.knownPlayers.clear();
        TotemPopManager.INSTANCE.sendMsgs = false;
    }
}
