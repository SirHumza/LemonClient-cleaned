/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 */
package com.lemonclient.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.lemonclient.api.setting.Setting;
import com.lemonclient.api.setting.SettingsManager;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.api.util.player.social.Enemy;
import com.lemonclient.api.util.player.social.Friend;
import com.lemonclient.api.util.player.social.Ignore;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.clickgui.GuiConfig;
import com.lemonclient.client.clickgui.LemonClientGUI;
import com.lemonclient.client.command.CommandManager;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

public class SaveConfig {
    public static final String fileName = "LemonClient/";
    private static final String moduleName = "Modules/";
    private static final String mainName = "Main/";
    private static final String miscName = "Misc/";

    public static void init() {
        try {
            SaveConfig.saveConfig();
            SaveConfig.saveModules();
            SaveConfig.saveEnabledModules();
            SaveConfig.saveModuleKeyBinds();
            SaveConfig.saveDrawnModules();
            SaveConfig.saveToggleMessagesModules();
            SaveConfig.saveCommandPrefix();
            SaveConfig.saveCustomFont();
            SaveConfig.saveFriendsList();
            SaveConfig.saveEnemiesList();
            SaveConfig.saveIgnoresList();
            SaveConfig.saveClickGUIPositions();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveConfig() throws IOException {
        Path path3;
        Path path2;
        Path path1;
        Path path = Paths.get(fileName, new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            Files.createDirectories(path, new FileAttribute[0]);
        }
        if (!Files.exists(path1 = Paths.get("LemonClient/Modules/", new String[0]), new LinkOption[0])) {
            Files.createDirectories(path1, new FileAttribute[0]);
        }
        if (!Files.exists(path2 = Paths.get("LemonClient/Main/", new String[0]), new LinkOption[0])) {
            Files.createDirectories(path2, new FileAttribute[0]);
        }
        if (!Files.exists(path3 = Paths.get("LemonClient/Misc/", new String[0]), new LinkOption[0])) {
            Files.createDirectories(path3, new FileAttribute[0]);
        }
    }

    private static void registerFiles(String location, String name) throws IOException {
        Path path = Paths.get(fileName + location + name + ".json", new String[0]);
        if (Files.exists(path, new LinkOption[0])) {
            File file = new File(fileName + location + name + ".json");
            file.delete();
        }
        Files.createFile(path, new FileAttribute[0]);
    }

    private static void saveModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            try {
                SaveConfig.saveModuleDirect(module);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveModuleDirect(Module module) throws IOException {
        SaveConfig.registerFiles(moduleName, module.getName());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Modules/" + module.getName() + ".json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject moduleObject = new JsonObject();
        JsonObject settingObject = new JsonObject();
        moduleObject.add("Module", (JsonElement)new JsonPrimitive(module.getName()));
        for (Setting setting : SettingsManager.getSettingsForModule(module)) {
            if (setting instanceof BooleanSetting) {
                settingObject.add(setting.getConfigName(), (JsonElement)new JsonPrimitive((Boolean)((BooleanSetting)setting).getValue()));
                continue;
            }
            if (setting instanceof IntegerSetting) {
                settingObject.add(setting.getConfigName(), (JsonElement)new JsonPrimitive((Number)((IntegerSetting)setting).getValue()));
                continue;
            }
            if (setting instanceof DoubleSetting) {
                settingObject.add(setting.getConfigName(), (JsonElement)new JsonPrimitive((Number)((DoubleSetting)setting).getValue()));
                continue;
            }
            if (setting instanceof ColorSetting) {
                settingObject.add(setting.getConfigName(), (JsonElement)new JsonPrimitive((Number)((ColorSetting)setting).toLong()));
                continue;
            }
            if (setting instanceof ModeSetting) {
                settingObject.add(setting.getConfigName(), (JsonElement)new JsonPrimitive((String)((ModeSetting)setting).getValue()));
                continue;
            }
            if (!(setting instanceof StringSetting)) continue;
            settingObject.add(setting.getConfigName(), (JsonElement)new JsonPrimitive(((StringSetting)setting).getText()));
        }
        moduleObject.add("Settings", (JsonElement)settingObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveEnabledModules() throws IOException {
        SaveConfig.registerFiles(mainName, "Toggle");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Main/Toggle.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject moduleObject = new JsonObject();
        JsonObject enabledObject = new JsonObject();
        for (Module module : ModuleManager.getModules()) {
            enabledObject.add(module.getName(), (JsonElement)new JsonPrimitive(Boolean.valueOf(module.isEnabled())));
        }
        moduleObject.add("Modules", (JsonElement)enabledObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveModuleKeyBinds() throws IOException {
        SaveConfig.registerFiles(mainName, "Bind");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Main/Bind.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject moduleObject = new JsonObject();
        JsonObject bindObject = new JsonObject();
        for (Module module : ModuleManager.getModules()) {
            bindObject.add(module.getName(), (JsonElement)new JsonPrimitive((Number)module.getBind()));
        }
        moduleObject.add("Modules", (JsonElement)bindObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveDrawnModules() throws IOException {
        SaveConfig.registerFiles(mainName, "Drawn");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Main/Drawn.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject moduleObject = new JsonObject();
        JsonObject drawnObject = new JsonObject();
        for (Module module : ModuleManager.getModules()) {
            drawnObject.add(module.getName(), (JsonElement)new JsonPrimitive(Boolean.valueOf(module.isDrawn())));
        }
        moduleObject.add("Modules", (JsonElement)drawnObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveToggleMessagesModules() throws IOException {
        SaveConfig.registerFiles(mainName, "ToggleMessages");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Main/ToggleMessages.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject moduleObject = new JsonObject();
        JsonObject toggleMessagesObject = new JsonObject();
        for (Module module : ModuleManager.getModules()) {
            toggleMessagesObject.add(module.getName(), (JsonElement)new JsonPrimitive(Boolean.valueOf(module.isToggleMsg())));
        }
        moduleObject.add("Modules", (JsonElement)toggleMessagesObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveCommandPrefix() throws IOException {
        SaveConfig.registerFiles(mainName, "CommandPrefix");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Main/CommandPrefix.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject prefixObject = new JsonObject();
        prefixObject.add("Prefix", (JsonElement)new JsonPrimitive(CommandManager.getCommandPrefix()));
        String jsonString = gson.toJson(new JsonParser().parse(prefixObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveCustomFont() throws IOException {
        SaveConfig.registerFiles(miscName, "CustomFont");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Misc/CustomFont.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject fontObject = new JsonObject();
        fontObject.add("Font Name", (JsonElement)new JsonPrimitive(LemonClient.INSTANCE.cFontRenderer.getFontName()));
        fontObject.add("Font Size", (JsonElement)new JsonPrimitive((Number)LemonClient.INSTANCE.cFontRenderer.getFontSize()));
        fontObject.add("Anti Alias", (JsonElement)new JsonPrimitive(Boolean.valueOf(LemonClient.INSTANCE.cFontRenderer.getAntiAlias())));
        fontObject.add("Fractional Metrics", (JsonElement)new JsonPrimitive(Boolean.valueOf(LemonClient.INSTANCE.cFontRenderer.getFractionalMetrics())));
        String jsonString = gson.toJson(new JsonParser().parse(fontObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveFriendsList() throws IOException {
        SaveConfig.registerFiles(miscName, "Friends");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Misc/Friends.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray friendArray = new JsonArray();
        for (Friend friend : SocialManager.getFriends()) {
            friendArray.add(friend.getName());
        }
        mainObject.add("Friends", (JsonElement)friendArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveEnemiesList() throws IOException {
        SaveConfig.registerFiles(miscName, "Enemies");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Misc/Enemies.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray enemyArray = new JsonArray();
        for (Enemy enemy : SocialManager.getEnemies()) {
            enemyArray.add(enemy.getName());
        }
        mainObject.add("Enemies", (JsonElement)enemyArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveIgnoresList() throws IOException {
        SaveConfig.registerFiles(miscName, "Ignores");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("LemonClient/Misc/Ignores.json", new String[0]), new OpenOption[0]), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray ignoreArray = new JsonArray();
        for (Ignore ignore : SocialManager.getIgnores()) {
            ignoreArray.add(ignore.getName());
        }
        mainObject.add("Ignores", (JsonElement)ignoreArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    private static void saveClickGUIPositions() throws IOException {
        SaveConfig.registerFiles(mainName, "ClickGUI");
        LemonClientGUI.gui.saveConfig(new GuiConfig("LemonClient/Main/"));
    }
}
