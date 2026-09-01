/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package com.lemonclient.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.SettingsManager;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.api.util.font.CFontRenderer;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.clickgui.GuiConfig;
import com.lemonclient.client.clickgui.LemonClientGUI;
import com.lemonclient.client.command.CommandManager;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadConfig {
    private static final String fileName = "LemonClient/";
    private static final String moduleName = "Modules/";
    private static final String mainName = "Main/";
    private static final String miscName = "Misc/";

    public static void init() {
        try {
            LoadConfig.loadModules();
            LoadConfig.loadEnabledModules();
            LoadConfig.loadModuleKeybinds();
            LoadConfig.loadDrawnModules();
            LoadConfig.loadToggleMessageModules();
            LoadConfig.loadCommandPrefix();
            LoadConfig.loadCustomFont();
            LoadConfig.loadFriendsList();
            LoadConfig.loadIgnoressList();
            LoadConfig.loadEnemiesList();
            LoadConfig.loadClickGUIPositions();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void loadModules() {
        String moduleLocation = "LemonClient/Modules/";
        for (Module module : ModuleManager.getModules()) {
            try {
                LoadConfig.loadModuleDirect(moduleLocation, module);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadModuleDirect(String moduleLocation, Module module) throws IOException {
        JsonObject moduleObject;
        if (!Files.exists(Paths.get(moduleLocation + module.getName() + ".json", new String[0]), new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(Paths.get(moduleLocation + module.getName() + ".json", new String[0]), new OpenOption[0]);
        try {
            moduleObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        }
        catch (IllegalStateException e) {
            return;
        }
        if (moduleObject.get("Module") == null) {
            return;
        }
        JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
        for (Setting setting : SettingsManager.getSettingsForModule(module)) {
            JsonElement dataObject = settingObject.get(setting.getConfigName());
            try {
                if (dataObject == null || !dataObject.isJsonPrimitive()) continue;
                if (setting instanceof BooleanSetting) {
                    setting.setValue(dataObject.getAsBoolean());
                    continue;
                }
                if (setting instanceof IntegerSetting) {
                    setting.setValue(dataObject.getAsInt());
                    continue;
                }
                if (setting instanceof DoubleSetting) {
                    setting.setValue(dataObject.getAsDouble());
                    continue;
                }
                if (setting instanceof ColorSetting) {
                    ((ColorSetting)setting).fromLong(dataObject.getAsLong());
                    continue;
                }
                if (setting instanceof ModeSetting) {
                    setting.setValue(dataObject.getAsString());
                    continue;
                }
                if (!(setting instanceof StringSetting)) continue;
                setting.setValue(dataObject.getAsString());
                ((StringSetting)setting).setText(dataObject.getAsString());
            }
            catch (NumberFormatException numberFormatException) {}
        }
        inputStream.close();
    }

    private static void loadEnabledModules() throws IOException {
        String enabledLocation = "LemonClient/Main/";
        Path path = Paths.get(enabledLocation + "Toggle.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject moduleObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (moduleObject.get("Modules") == null) {
            return;
        }
        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = settingObject.get(module.getName());
            if (dataObject == null || !dataObject.isJsonPrimitive() || !dataObject.getAsBoolean()) continue;
            try {
                module.enable();
            }
            catch (NullPointerException nullPointerException) {}
        }
        inputStream.close();
    }

    private static void loadModuleKeybinds() throws IOException {
        String bindLocation = "LemonClient/Main/";
        Path path = Paths.get(bindLocation + "Bind.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject moduleObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (moduleObject.get("Modules") == null) {
            return;
        }
        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = settingObject.get(module.getName());
            if (dataObject == null || !dataObject.isJsonPrimitive()) continue;
            module.setBind(dataObject.getAsInt());
        }
        inputStream.close();
    }

    private static void loadDrawnModules() throws IOException {
        String drawnLocation = "LemonClient/Main/";
        Path path = Paths.get(drawnLocation + "Drawn.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject moduleObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (moduleObject.get("Modules") == null) {
            return;
        }
        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = settingObject.get(module.getName());
            if (dataObject == null || !dataObject.isJsonPrimitive()) continue;
            module.setDrawn(dataObject.getAsBoolean());
        }
        inputStream.close();
    }

    private static void loadToggleMessageModules() throws IOException {
        String toggleMessageLocation = "LemonClient/Main/";
        Path path = Paths.get(toggleMessageLocation + "ToggleMessages.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject moduleObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (moduleObject.get("Modules") == null) {
            return;
        }
        JsonObject toggleObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = toggleObject.get(module.getName());
            if (dataObject == null || !dataObject.isJsonPrimitive()) continue;
            module.setToggleMsg(dataObject.getAsBoolean());
        }
        inputStream.close();
    }

    private static void loadCommandPrefix() throws IOException {
        String prefixLocation = "LemonClient/Main/";
        Path path = Paths.get(prefixLocation + "CommandPrefix.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (mainObject.get("Prefix") == null) {
            return;
        }
        JsonElement prefixObject = mainObject.get("Prefix");
        if (prefixObject != null && prefixObject.isJsonPrimitive()) {
            CommandManager.setCommandPrefix(prefixObject.getAsString());
        }
        inputStream.close();
    }

    private static void loadCustomFont() throws IOException {
        String fontLocation = "LemonClient/Misc/";
        Path path = Paths.get(fontLocation + "CustomFont.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (mainObject.get("Font Name") == null || mainObject.get("Font Size") == null) {
            return;
        }
        JsonElement fontNameObject = mainObject.get("Font Name");
        String name = null;
        if (fontNameObject != null && fontNameObject.isJsonPrimitive()) {
            name = fontNameObject.getAsString();
        }
        JsonElement fontSizeObject = mainObject.get("Font Size");
        int size = -1;
        if (fontSizeObject != null && fontSizeObject.isJsonPrimitive()) {
            size = fontSizeObject.getAsInt();
        }
        JsonElement antiAliasObject = mainObject.get("Anti Alias");
        boolean alias = true;
        if (antiAliasObject != null && antiAliasObject.isJsonPrimitive()) {
            alias = antiAliasObject.getAsBoolean();
        }
        JsonElement MetricsObject = mainObject.get("Fractional Metrics");
        boolean metrics = false;
        if (MetricsObject != null && MetricsObject.isJsonPrimitive()) {
            metrics = MetricsObject.getAsBoolean();
        }
        if (name != null && size != -1) {
            LemonClient.INSTANCE.cFontRenderer = new CFontRenderer(new Font(name, 0, size), false, true);
            LemonClient.INSTANCE.cFontRenderer.setFont(new Font(name, 0, size));
            LemonClient.INSTANCE.cFontRenderer.setAntiAlias(alias);
            LemonClient.INSTANCE.cFontRenderer.setFractionalMetrics(metrics);
            LemonClient.INSTANCE.cFontRenderer.setFontName(name);
            LemonClient.INSTANCE.cFontRenderer.setFontSize(size);
        }
        inputStream.close();
    }

    private static void loadFriendsList() throws IOException {
        String friendLocation = "LemonClient/Misc/";
        Path path = Paths.get(friendLocation + "Friends.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (mainObject.get("Friends") == null) {
            return;
        }
        JsonArray friendObject = mainObject.get("Friends").getAsJsonArray();
        friendObject.forEach(object -> SocialManager.addFriend(object.getAsString()));
        inputStream.close();
    }

    private static void loadIgnoressList() throws IOException {
        String friendLocation = "LemonClient/Misc/";
        Path path = Paths.get(friendLocation + "Ignores.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (mainObject.get("Ignores") == null) {
            return;
        }
        JsonArray friendObject = mainObject.get("Ignores").getAsJsonArray();
        friendObject.forEach(object -> SocialManager.addIgnore(object.getAsString()));
        inputStream.close();
    }

    private static void loadEnemiesList() throws IOException {
        String enemyLocation = "LemonClient/Misc/";
        Path path = Paths.get(enemyLocation + "Enemies.json", new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            return;
        }
        InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);
        JsonObject mainObject = new JsonParser().parse((Reader)new InputStreamReader(inputStream)).getAsJsonObject();
        if (mainObject.get("Enemies") == null) {
            return;
        }
        JsonArray enemyObject = mainObject.get("Enemies").getAsJsonArray();
        enemyObject.forEach(object -> SocialManager.addEnemy(object.getAsString()));
        inputStream.close();
    }

    private static void loadClickGUIPositions() {
        LemonClientGUI.gui.loadConfig(new GuiConfig("LemonClient/Main/"));
    }
}
