/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.lemonclient.api.util.verify.End
 *  com.lemonclient.api.util.verify.Manager
 *  com.lemonclient.api.util.verify.Nigger
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLConstructionEvent
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package com.lemonclient.client;

import com.lemonclient.api.config.LoadConfig;
import com.lemonclient.api.util.chat.notification.NotificationType;
import com.lemonclient.api.util.chat.notification.NotificationsManager;
import com.lemonclient.api.util.chat.notification.notifications.BottomRightNotification;
import com.lemonclient.api.util.font.CFontRenderer;
import com.lemonclient.api.util.log4j.Fixer;
import com.lemonclient.api.util.misc.IconUtil;
import com.lemonclient.api.util.misc.ServerUtil;
import com.lemonclient.api.util.player.PositionUtil;
import com.lemonclient.api.util.player.SpeedUtil;
import com.lemonclient.api.util.render.CapeUtil;
import com.lemonclient.api.util.verify.End;
import com.lemonclient.api.util.verify.Manager;
import com.lemonclient.api.util.verify.Nigger;
import com.lemonclient.client.clickgui.LemonClientGUI;
import com.lemonclient.client.command.CommandManager;
import com.lemonclient.client.manager.ManagerLoader;
import com.lemonclient.client.module.ModuleManager;
import java.awt.Font;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="lemonclient", name="Lemon Client", version="v0.0.9")
public class LemonClient {
    public static final String MODNAME = "Lemon Client";
    public static final String MODID = "lemonclient";
    public static final String MODVER = "v0.0.9";
    public static String Ver = "009";
    public static String KEY = "vMQtVc69qr";
    public static final Logger LOGGER = LogManager.getLogger((String)"Lemon Client");
    public static final EventBus EVENT_BUS = new EventManager();
    public static List<String> hwidList = new ArrayList<String>();
    public static PositionUtil positionUtil;
    public static ServerUtil serverUtil;
    public static SpeedUtil speedUtil;
    Manager manager;
    Nigger nigger;
    public static boolean isMe;
    public static End end;
    public static Runtime runtime;
    @Mod.Instance
    public static LemonClient INSTANCE;
    public CFontRenderer cFontRenderer;
    public LemonClientGUI gameSenseGUI;

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        try {
            Fixer.disableJndiManager();
        }
        catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Fixer.doRuntimeTest(event.getModLog());
    }

    public LemonClient() {
        INSTANCE = this;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.verify();
        LOGGER.info("Starting up Lemon Client v0.0.9!");
        this.startClient();
        LOGGER.info("Finished initialization for Lemon Client v0.0.9!");
        NotificationType type = NotificationType.WELCOME;
        int length = 20;
        String msg = "You are on the latest version";
        NotificationsManager.show(new BottomRightNotification(type, "LemonClient", msg, length));
        CapeUtil.init();
        Display.setTitle((String)"Lemon Client v0.0.9");
        LemonClient.setWindowIcon();
    }

    private void startClient() {
        this.cFontRenderer = new CFontRenderer(new Font("Comic Sans Ms", 0, 17), false, true);
        LoadConfig.init();
        ModuleManager.init();
        CommandManager.init();
        ManagerLoader.init();
        this.gameSenseGUI = new LemonClientGUI();
        LoadConfig.init();
        positionUtil = new PositionUtil();
        serverUtil = new ServerUtil();
        speedUtil = new SpeedUtil();
        LemonClient.INSTANCE.gameSenseGUI.refresh();
    }

    private void verify() {
    }

    public static void shutdown() {
    }

    public static void setWindowIcon() {
        if (Util.func_110647_a() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/lemonclient/icons/icon-16x.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/lemonclient/icons/icon-32x.png");){
                ByteBuffer[] icons = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream32x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon((ByteBuffer[])icons);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    static {
        runtime = Runtime.getRuntime();
    }
}
