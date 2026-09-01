/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketBlockBreakAnim
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraftforge.fml.common.network.internal.FMLProxyPacket
 *  org.lwjgl.opengl.Display
 */
package com.lemonclient.client.module.modules.gui;

import com.lemonclient.api.event.events.EntityUseTotemEvent;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.ColorUtil;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.movement.SpeedPlus;
import com.lemonclient.client.module.modules.qwq.AutoEz;
import com.lemonclient.mixin.mixins.accessor.AccessorCPacketCustomPayload;
import io.netty.buffer.Unpooled;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.lwjgl.opengl.Display;

@Module.Declaration(name="Colors", category=Category.GUI, enabled=true, drawn=false, priority=10000)
public class ColorMain
extends Module {
    public static ColorMain INSTANCE;
    public ColorSetting enabledColor = this.registerColor("Main Color", new GSColor(255, 0, 0, 255));
    public DoubleSetting rainbowSpeed = this.registerDouble("Rainbow Speed", 1.0, 0.1, 10.0);
    public ModeSetting rainbowMode = this.registerMode("Rainbow Mode", Arrays.asList("Normal", "Sin", "Tan", "Sec", "CoTan", "CoSec"), "Normal");
    public BooleanSetting customFont = this.registerBoolean("Custom Font", true);
    public BooleanSetting textFont = this.registerBoolean("Custom Text", false);
    public BooleanSetting highlightSelf = this.registerBoolean("Highlight SelfName", false);
    public ModeSetting selfColor = this.registerMode("Self Color", ColorUtil.colors, "Blue");
    public ModeSetting friendColor = this.registerMode("Friend Color", ColorUtil.colors, "Green");
    public ModeSetting enemyColor = this.registerMode("Enemy Color", ColorUtil.colors, "Red");
    public ModeSetting chatModuleColor = this.registerMode("Msg Module", ColorUtil.colors, "Aqua");
    public ModeSetting chatEnableColor = this.registerMode("Msg Enable", ColorUtil.colors, "Green");
    public ModeSetting chatDisableColor = this.registerMode("Msg Disable", ColorUtil.colors, "Red");
    public ColorSetting Title = this.registerColor("Title Color", new GSColor(90, 145, 240));
    public ColorSetting Enabled = this.registerColor("Enabled Color", new GSColor(90, 145, 240));
    public ColorSetting Disabled = this.registerColor("Disabled", new GSColor(64, 64, 64));
    public ColorSetting Background = this.registerColor("BackGround Color", new GSColor(195, 195, 195, 150), true);
    public ColorSetting Font = this.registerColor("Font Color", new GSColor(255, 255, 255));
    public ColorSetting ScrollBar = this.registerColor("ScrollBar Color", new GSColor(90, 145, 240));
    public ColorSetting Highlight = this.registerColor("Highlight Color", new GSColor(0, 0, 240));
    public ModeSetting colorModel = this.registerMode("Color Model", Arrays.asList("RGB", "HSB"), "HSB");
    Color title;
    Color enable;
    Color disable;
    Color background;
    Color font;
    Color scrollBar;
    Color highlight;
    public boolean sneaking;
    public double velocityBoost;
    public List<BlockPos> breakList = new ArrayList<BlockPos>();
    HashMap<EntityPlayer, BlockPos> list = new HashMap();
    BlockPos lastBreak;
    @EventHandler
    private final Listener<PacketEvent.PostSend> postSendListener = new Listener<PacketEvent.PostSend>(event -> {
        if (event.getPacket() instanceof CPacketEntityAction) {
            if (((CPacketEntityAction)event.getPacket()).func_180764_b() == CPacketEntityAction.Action.START_SNEAKING) {
                this.sneaking = true;
            }
            if (((CPacketEntityAction)event.getPacket()).func_180764_b() == CPacketEntityAction.Action.STOP_SNEAKING) {
                this.sneaking = false;
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> packetSend = new Listener<PacketEvent.Send>(event -> {
        CPacketCustomPayload packet;
        if (event.getPacket() instanceof FMLProxyPacket && !mc.func_71356_B()) {
            event.cancel();
        }
        if (event.getPacket() instanceof CPacketCustomPayload && (packet = (CPacketCustomPayload)event.getPacket()).func_149559_c().equalsIgnoreCase("MC|Brand")) {
            ((AccessorCPacketCustomPayload)packet).setData(new PacketBuffer(Unpooled.buffer()).func_180714_a("vanilla"));
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            this.lastBreak = packet.func_179715_a();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (ColorMain.mc.field_71441_e == null || ColorMain.mc.field_71439_g == null) {
            return;
        }
        LemonClient.speedUtil.update();
        LemonClient.positionUtil.updatePosition();
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        SPacketBlockBreakAnim packet;
        if (ColorMain.mc.field_71441_e == null || ColorMain.mc.field_71439_g == null || EntityUtil.isDead((Entity)ColorMain.mc.field_71439_g)) {
            return;
        }
        if (event.getPacket() instanceof SPacketChat) {
            int spaceIndex;
            String message = ((SPacketChat)event.getPacket()).func_148915_c().func_150260_c();
            Matcher matcher = Pattern.compile("<(.*?)>").matcher(message);
            String username = "";
            if (matcher.find()) {
                username = matcher.group();
            } else if (message.contains(":") && (spaceIndex = message.indexOf(" ")) != -1) {
                username = message.substring(0, spaceIndex);
            }
            username = ColorMain.cleanColor(username);
            if (SocialManager.isIgnore(username)) {
                event.cancel();
            }
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            packet = (SPacketBlockBreakAnim)event.getPacket();
            BlockPos blockPos = packet.func_179821_b();
            EntityPlayer entityPlayer = (EntityPlayer)ColorMain.mc.field_71441_e.func_73045_a(packet.func_148845_c());
            if (entityPlayer == null) {
                return;
            }
            this.list.put(entityPlayer, blockPos);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            packet = (SPacketEntityVelocity)event.getPacket();
            Entity entity = ColorMain.mc.field_71441_e.func_73045_a(packet.field_149417_a);
            if (entity != null && entity == ColorMain.mc.field_71439_g) {
                this.velocityBoost = (Boolean)SpeedPlus.INSTANCE.sum.getValue() != false ? this.velocityBoost + Math.hypot((float)packet.field_149415_b / 8000.0f, (float)packet.field_149414_d / 8000.0f) : Math.max(this.velocityBoost, Math.hypot((float)packet.field_149415_b / 8000.0f, (float)packet.field_149414_d / 8000.0f));
            }
        }
    }, new Predicate[0]);
    @EventHandler
    public Listener<EntityUseTotemEvent> listListener = new Listener<EntityUseTotemEvent>(event -> {
        if (event.getEntity() == ColorMain.mc.field_71439_g && ColorMain.mc.field_71462_r instanceof GuiContainer && !(ColorMain.mc.field_71462_r instanceof GuiInventory)) {
            ColorMain.mc.field_71439_g.func_71053_j();
        }
    }, new Predicate[0]);

    public ColorMain() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.enable();
    }

    @Override
    public void fast() {
        if (this.title != this.Title.getColor() || this.enable != this.Enabled.getColor() || this.disable != this.Disabled.getColor() || this.background != this.Background.getColor() || this.font != this.Font.getColor() || this.scrollBar != this.ScrollBar.getColor() || this.highlight != this.Highlight.getColor()) {
            this.title = this.Title.getColor();
            this.enable = this.Enabled.getColor();
            this.disable = this.Disabled.getColor();
            this.background = this.Background.getColor();
            this.font = this.Font.getColor();
            this.scrollBar = this.ScrollBar.getColor();
            this.highlight = this.Highlight.getColor();
            LemonClient.INSTANCE.gameSenseGUI.refresh();
        }
        if (!((Boolean)AutoEz.INSTANCE.hi.getValue()).booleanValue()) {
            AutoEz.INSTANCE.hi.setValue(true);
        }
        this.breakList = new ArrayList<BlockPos>();
        this.breakList.add(this.lastBreak);
        List playerList = ColorMain.mc.field_71441_e.field_73010_i;
        for (EntityPlayer player : playerList) {
            if (!this.list.containsKey(player)) continue;
            BlockPos pos = this.list.get(player);
            this.breakList.add(pos);
        }
    }

    @Override
    public void onUpdate() {
        if (!Display.getTitle().equals("Lemon Client v0.0.9")) {
            Display.setTitle((String)"Lemon Client v0.0.9");
            LemonClient.setWindowIcon();
        }
        if (!SpeedPlus.INSTANCE.isEnabled() && MotionUtil.moving((EntityLivingBase)ColorMain.mc.field_71439_g)) {
            this.velocityBoost = 0.0;
        }
    }

    public String highlight(String string) {
        if (string != null && this.isEnabled()) {
            String username = mc.func_110432_I().func_111285_a();
            return string.replace(username, this.getSelfColor() + username).replace(username.toLowerCase(), this.getSelfColor() + username.toLowerCase()).replace(username.toUpperCase(), this.getSelfColor() + username.toUpperCase());
        }
        return string;
    }

    public static String cleanColor(String input) {
        return input.replaceAll("(?i)\\u00A7.", "");
    }

    public TextFormatting getSelfColor() {
        return ColorUtil.settingToTextFormatting(this.selfColor);
    }

    public TextFormatting getFriendColor() {
        return ColorUtil.settingToTextFormatting(this.friendColor);
    }

    public TextFormatting getEnemyColor() {
        return ColorUtil.settingToTextFormatting(this.enemyColor);
    }

    public TextFormatting getModuleColor() {
        return ColorUtil.settingToTextFormatting(this.chatModuleColor);
    }

    public TextFormatting getEnabledColor() {
        return ColorUtil.settingToTextFormatting(this.chatEnableColor);
    }

    public TextFormatting getDisabledColor() {
        return ColorUtil.settingToTextFormatting(this.chatDisableColor);
    }

    public GSColor getFriendGSColor() {
        return new GSColor(ColorUtil.settingToColor(this.friendColor));
    }

    public GSColor getEnemyGSColor() {
        return new GSColor(ColorUtil.settingToColor(this.enemyColor));
    }
}
