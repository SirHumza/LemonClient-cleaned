/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketPlayerListItem$AddPlayerData
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FOVModifier
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogColors
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogDensity
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingEntityUseItemEvent$Finish
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$MouseInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ServerTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$WorldTickEvent
 *  net.minecraftforge.fml.common.network.internal.FMLProxyPacket
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package com.lemonclient.client.manager.managers;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.PlayerJoinEvent;
import com.lemonclient.api.event.events.PlayerLeaveEvent;
import com.lemonclient.api.event.events.Render2DEvent;
import com.lemonclient.api.event.events.Render3DEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.event.events.SendMessageEvent;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.chat.NotificationManager;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.player.NameUtil;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.TimerUtils;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.PeekCmd;
import com.lemonclient.client.command.CommandManager;
import com.lemonclient.client.manager.Manager;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.dev.AntiPush;
import com.lemonclient.client.module.modules.misc.ShulkerBypass;
import com.lemonclient.client.module.modules.qwq.AntiUnicdoe;
import com.lemonclient.mixin.mixins.accessor.AccessorCPacketCustomPayload;
import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.buffer.Unpooled;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public enum ClientEventManager implements Manager
{
    INSTANCE;

    final String LAG_MESSAGE = "\u0101\u0201\u0301\u0401\u0601\u0701\u0801\u0901\u0a01\u0b01\u0e01\u0f01\u1001\u1101\u1201\u1301\u1401\u1501\u1601\u1701\u1801\u1901\u1a01\u1b01\u1c01\u1d01\u1e01\u1f01 \u2101\u2201\u2301\u2401\u2501\u2701\u2801\u2901\u2a01\u2b01\u2c01\u2d01\u2e01\u2f01\u3001\u3101\u3201\u3301\u3401\u3501\u3601\u3701\u3801\u3901\u3a01\u3b01\u3c01\u3d01\u3e01\u3f01\u4001\u4101\u4201\u4301\u4401\u4501\u4601\u4701\u4801\u4901\u4a01\u4b01\u4c01\u4d01\u4e01\u4f01\u5001\u5101\u5201\u5301\u5401\u5501\u5601\u5701\u5801\u5901\u5a01\u5b01\u5c01\u5d01\u5e01\u5f01\u6001\u6101\u6201\u6301\u6401\u6501\u6601\u6701\u6801\u6901\u6a01\u6b01\u6c01\u6d01\u6e01\u6f01\u7001\u7101\u7201\u7301\u7401\u7501\u7601\u7701\u7801\u7901\u7a01\u7b01\u7c01\u7d01\u7e01\u7f01\u8001\u8101\u8201\u8301\u8401\u8501\u8601\u8701\u8801\u8901\u8a01\u8b01\u8c01\u8d01\u8e01\u8f01\u9001\u9101\u9201\u9301\u9401\u9501\u9601\u9701\u9801\u9901\u9a01\u9b01\u9c01\u9d01\u9e01\u9f01\ua001\ua101\ua201\ua301\ua401\ua501\ua601\ua701\ua801\ua901\uaa01\uab01\uac01\uad01\uae01\uaf01\ub001\ub101\ub201\ub301\ub401\ub501\ub601\ub701\ub801\ub901\uba01\ubb01\ubc01\ubd01";
    final Set<Character> lagMessageSet = new HashSet<Character>();
    @EventHandler
    private final Listener<PacketEvent.Send> packetSend = new Listener<PacketEvent.Send>(event -> {
        CPacketCustomPayload packet;
        if (event.getPacket() instanceof FMLProxyPacket && !Minecraft.func_71410_x().func_71356_B()) {
            event.cancel();
        }
        if (event.getPacket() instanceof CPacketCustomPayload && (packet = (CPacketCustomPayload)event.getPacket()).func_149559_c().equalsIgnoreCase("MC|Brand")) {
            ((AccessorCPacketCustomPayload)packet).setData(new PacketBuffer(Unpooled.buffer()).func_180714_a("vanilla"));
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (packet.func_179768_b() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.func_179767_a()) {
                    if (playerData.func_179962_a().getId() == this.getMinecraft().field_71449_j.func_148256_e().getId()) continue;
                    new Thread(() -> {
                        String name = NameUtil.resolveName(playerData.func_179962_a().getId().toString());
                        if (name != null && this.getPlayer() != null && this.getPlayer().field_70173_aa >= 1000) {
                            LemonClient.EVENT_BUS.post(new PlayerJoinEvent(name));
                        }
                    }).start();
                }
            }
            if (packet.func_179768_b() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.func_179767_a()) {
                    if (playerData.func_179962_a().getId() == this.getMinecraft().field_71449_j.func_148256_e().getId()) continue;
                    new Thread(() -> {
                        String name = NameUtil.resolveName(playerData.func_179962_a().getId().toString());
                        if (name != null && this.getPlayer() != null && this.getPlayer().field_70173_aa >= 1000) {
                            LemonClient.EVENT_BUS.post(new PlayerLeaveEvent(name));
                        }
                    }).start();
                }
            }
        }
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            LemonClient.serverUtil.update();
        }
    }, new Predicate[0]);

    @SubscribeEvent(priority=EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(Minecraft.func_71410_x());
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            for (Module module : ModuleManager.getModules()) {
                if (!module.isEnabled()) continue;
                this.getProfiler().func_76320_a(module.getName());
                module.onRender2D(render2DEvent);
                this.getProfiler().func_76319_b();
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
        }
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (this.lagMessageSet.isEmpty()) {
            for (int i = 0; i < "\u0101\u0201\u0301\u0401\u0601\u0701\u0801\u0901\u0a01\u0b01\u0e01\u0f01\u1001\u1101\u1201\u1301\u1401\u1501\u1601\u1701\u1801\u1901\u1a01\u1b01\u1c01\u1d01\u1e01\u1f01 \u2101\u2201\u2301\u2401\u2501\u2701\u2801\u2901\u2a01\u2b01\u2c01\u2d01\u2e01\u2f01\u3001\u3101\u3201\u3301\u3401\u3501\u3601\u3701\u3801\u3901\u3a01\u3b01\u3c01\u3d01\u3e01\u3f01\u4001\u4101\u4201\u4301\u4401\u4501\u4601\u4701\u4801\u4901\u4a01\u4b01\u4c01\u4d01\u4e01\u4f01\u5001\u5101\u5201\u5301\u5401\u5501\u5601\u5701\u5801\u5901\u5a01\u5b01\u5c01\u5d01\u5e01\u5f01\u6001\u6101\u6201\u6301\u6401\u6501\u6601\u6701\u6801\u6901\u6a01\u6b01\u6c01\u6d01\u6e01\u6f01\u7001\u7101\u7201\u7301\u7401\u7501\u7601\u7701\u7801\u7901\u7a01\u7b01\u7c01\u7d01\u7e01\u7f01\u8001\u8101\u8201\u8301\u8401\u8501\u8601\u8701\u8801\u8901\u8a01\u8b01\u8c01\u8d01\u8e01\u8f01\u9001\u9101\u9201\u9301\u9401\u9501\u9601\u9701\u9801\u9901\u9a01\u9b01\u9c01\u9d01\u9e01\u9f01\ua001\ua101\ua201\ua301\ua401\ua501\ua601\ua701\ua801\ua901\uaa01\uab01\uac01\uad01\uae01\uaf01\ub001\ub101\ub201\ub301\ub401\ub501\ub601\ub701\ub801\ub901\uba01\ubb01\ubc01\ubd01".length(); ++i) {
                this.lagMessageSet.add(Character.valueOf("\u0101\u0201\u0301\u0401\u0601\u0701\u0801\u0901\u0a01\u0b01\u0e01\u0f01\u1001\u1101\u1201\u1301\u1401\u1501\u1601\u1701\u1801\u1901\u1a01\u1b01\u1c01\u1d01\u1e01\u1f01 \u2101\u2201\u2301\u2401\u2501\u2701\u2801\u2901\u2a01\u2b01\u2c01\u2d01\u2e01\u2f01\u3001\u3101\u3201\u3301\u3401\u3501\u3601\u3701\u3801\u3901\u3a01\u3b01\u3c01\u3d01\u3e01\u3f01\u4001\u4101\u4201\u4301\u4401\u4501\u4601\u4701\u4801\u4901\u4a01\u4b01\u4c01\u4d01\u4e01\u4f01\u5001\u5101\u5201\u5301\u5401\u5501\u5601\u5701\u5801\u5901\u5a01\u5b01\u5c01\u5d01\u5e01\u5f01\u6001\u6101\u6201\u6301\u6401\u6501\u6601\u6701\u6801\u6901\u6a01\u6b01\u6c01\u6d01\u6e01\u6f01\u7001\u7101\u7201\u7301\u7401\u7501\u7601\u7701\u7801\u7901\u7a01\u7b01\u7c01\u7d01\u7e01\u7f01\u8001\u8101\u8201\u8301\u8401\u8501\u8601\u8701\u8801\u8901\u8a01\u8b01\u8c01\u8d01\u8e01\u8f01\u9001\u9101\u9201\u9301\u9401\u9501\u9601\u9701\u9801\u9901\u9a01\u9b01\u9c01\u9d01\u9e01\u9f01\ua001\ua101\ua201\ua301\ua401\ua501\ua601\ua701\ua801\ua901\uaa01\uab01\uac01\uad01\uae01\uaf01\ub001\ub101\ub201\ub301\ub401\ub501\ub601\ub701\ub801\ub901\uba01\ubb01\ubc01\ubd01".charAt(i)));
            }
        }
        if (event.getMessage().func_150254_d().contains("{") || event.getMessage().func_150254_d().contains("}")) {
            event.setCanceled(true);
            TextComponentString string = new TextComponentString(event.getMessage().func_150254_d().replace("{", "").replace("}", "").replace("$", "").replace("ldap", ""));
            Minecraft.func_71410_x().field_71439_g.func_145747_a((ITextComponent)string);
            return;
        }
        if (ModuleManager.isModuleEnabled(AntiUnicdoe.class)) {
            int count = 0;
            String text = event.getMessage().func_150254_d();
            for (int i = 0; i < text.length(); ++i) {
                if (!this.lagMessageSet.contains(Character.valueOf(text.charAt(i)))) continue;
                ++count;
            }
            if (count >= 25) {
                event.setCanceled(true);
                TextComponentString string = new TextComponentString("(lag message)");
                Minecraft.func_71410_x().field_71439_g.func_145747_a((ITextComponent)string);
                return;
            }
        }
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
        if (ModuleManager.isModuleEnabled(AntiPush.class)) {
            event.setCanceled(true);
        }
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityItem) {
            PeekCmd.drop = (EntityItem)entity;
            PeekCmd.metadataTicks = 0;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onFogColor(EntityViewRenderEvent.FogColors event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (ModuleManager.isModuleEnabled("Peek") && ShulkerBypass.shulkers) {
            if (event.phase == TickEvent.Phase.END) {
                if (PeekCmd.guiTicks > -1) {
                    ++PeekCmd.guiTicks;
                }
                if (PeekCmd.metadataTicks > -1) {
                    ++PeekCmd.metadataTicks;
                }
            }
            if (PeekCmd.metadataTicks >= ShulkerBypass.delay) {
                PeekCmd.metadataTicks = -1;
                if (PeekCmd.drop.func_92059_d().func_77973_b() instanceof ItemShulkerBox) {
                    MessageBus.sendClientDeleteMessage("New shulker found. use /peek to view its content " + TextFormatting.GREEN + "(" + PeekCmd.drop.func_92059_d().func_82833_r() + ")", Notification.Type.INFO, "Peek", 3);
                    PeekCmd.shulker = PeekCmd.drop.func_92059_d();
                }
            }
            if (PeekCmd.guiTicks == 20) {
                PeekCmd.guiTicks = -1;
                Minecraft.func_71410_x().field_71439_g.func_71007_a((IInventory)PeekCmd.toOpen);
            }
        }
        if (this.getMinecraft().field_71439_g != null && this.getMinecraft().field_71441_e != null) {
            int timerSpeed = (int)TimerUtils.getTimer();
            for (Module module : ModuleManager.getModules()) {
                try {
                    if (!module.isEnabled()) continue;
                    ++module.onTickTimer;
                    if (module.onTickTimer < timerSpeed) continue;
                    module.onTick();
                    module.onTickTimer = 0;
                }
                catch (Exception e) {
                    if (this.getWorld() != null && this.getPlayer() != null) {
                        MessageBus.sendClientPrefixMessage("Disabled " + module.getName() + " due to " + e, Notification.Type.ERROR);
                    }
                    module.setEnabled(false);
                    for (StackTraceElement stack : e.getStackTrace()) {
                        System.out.println(stack.toString());
                    }
                }
            }
        }
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (this.getMinecraft().field_71439_g == null || this.getMinecraft().field_71441_e == null) {
            return;
        }
        if (event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving() == this.getPlayer()) {
            int timerSpeed = (int)TimerUtils.getTimer();
            for (Module module : ModuleManager.getModules()) {
                try {
                    if (!module.isEnabled()) continue;
                    ++module.onUpdateTimer;
                    if (module.onUpdateTimer < timerSpeed) continue;
                    module.onUpdate();
                    module.onUpdateTimer = 0;
                }
                catch (Exception e) {
                    if (this.getWorld() != null && this.getPlayer() != null) {
                        MessageBus.sendClientPrefixMessage("Disabled " + module.getName() + " due to " + e, Notification.Type.ERROR);
                    }
                    module.setEnabled(false);
                    for (StackTraceElement stack : e.getStackTrace()) {
                        System.out.println(stack.toString());
                    }
                }
            }
            LemonClient.EVENT_BUS.post(event);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (this.getMinecraft().field_71439_g == null || this.getMinecraft().field_71441_e == null) {
            return;
        }
        this.getProfiler().func_76320_a("lemonclient");
        this.getProfiler().func_76320_a("setup");
        RenderUtil.prepare();
        RenderEvent event1 = new RenderEvent(event.getPartialTicks());
        this.getProfiler().func_76319_b();
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            this.getProfiler().func_76320_a(module.getName());
            module.onWorldRender(event1);
            this.getProfiler().func_76319_b();
        }
        this.getProfiler().func_76320_a("release");
        RenderUtil.release();
        this.getProfiler().func_76319_b();
        this.getProfiler().func_76319_b();
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (this.getMinecraft().field_71439_g == null || this.getMinecraft().field_71441_e == null) {
            return;
        }
        this.getProfiler().func_76320_a("lemonclient");
        this.getProfiler().func_76320_a("setup");
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        GlStateManager.func_179097_i();
        GlStateManager.func_187441_d((float)1.0f);
        Render3DEvent event2 = new Render3DEvent(event.getPartialTicks());
        this.getProfiler().func_76319_b();
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            this.getProfiler().func_76320_a(module.getName());
            module.onRender3D(event2);
            this.getProfiler().func_76319_b();
        }
        this.getProfiler().func_76320_a("release");
        GlStateManager.func_187441_d((float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
        this.getProfiler().func_76319_b();
        this.getProfiler().func_76319_b();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (this.getMinecraft().field_71439_g == null || this.getMinecraft().field_71441_e == null) {
            return;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            for (Module module : ModuleManager.getModules()) {
                if (!module.isEnabled()) continue;
                module.onRender();
                NotificationManager.draw();
            }
            LemonClient.INSTANCE.gameSenseGUI.render();
        }
        LemonClient.EVENT_BUS.post(event);
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int key;
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0) {
            return;
        }
        EntityPlayerSP player = this.getPlayer();
        if (player != null && !player.func_70093_af()) {
            String prefix = CommandManager.getCommandPrefix();
            char typedChar = Keyboard.getEventCharacter();
            if (prefix.length() == 1 && prefix.charAt(0) == typedChar) {
                this.getMinecraft().func_147108_a((GuiScreen)new GuiChat(prefix));
            }
        }
        if ((key = Keyboard.getEventKey()) != 0) {
            for (Module module : ModuleManager.getModules()) {
                if (module.getBind() != key) continue;
                module.toggle();
            }
        }
        LemonClient.INSTANCE.gameSenseGUI.handleKeyEvent(Keyboard.getEventKey());
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            LemonClient.EVENT_BUS.post(event);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(CommandManager.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                this.getMinecraft().field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                CommandManager.callCommand(event.getMessage().substring(1), false);
            }
            catch (Exception e) {
                e.printStackTrace();
                MessageBus.sendCommandMessage(ChatFormatting.DARK_RED + "Error: " + e.getMessage(), true);
            }
        } else {
            SendMessageEvent eventNow = new SendMessageEvent(event.getMessage());
            LemonClient.EVENT_BUS.post(eventNow);
            if (eventNow.isCancelled()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void init(TickEvent.ClientTickEvent event) {
        this.fastest();
    }

    @SubscribeEvent
    public void init(TickEvent.ServerTickEvent event) {
        this.fastest();
    }

    @SubscribeEvent
    public void init(TickEvent.PlayerTickEvent event) {
        this.fastest();
    }

    @SubscribeEvent
    public void init(TickEvent.WorldTickEvent event) {
        this.fastest();
    }

    public void fastest() {
        if (this.getMinecraft().field_71439_g != null && this.getMinecraft().field_71441_e != null) {
            int timerSpeed = (int)TimerUtils.getTimer();
            for (Module module : ModuleManager.getModules()) {
                try {
                    if (!module.isEnabled()) continue;
                    ++module.fastTimer;
                    if (module.fastTimer < timerSpeed) continue;
                    module.fast();
                    module.fastTimer = 0;
                }
                catch (Exception e) {
                    if (this.getWorld() != null && this.getPlayer() != null) {
                        MessageBus.sendClientPrefixMessage("Disabled " + module.getName() + " due to " + e, Notification.Type.ERROR);
                    }
                    module.setEnabled(false);
                    for (StackTraceElement stack : e.getStackTrace()) {
                        System.out.println(stack.toString());
                    }
                }
            }
        }
    }
}
