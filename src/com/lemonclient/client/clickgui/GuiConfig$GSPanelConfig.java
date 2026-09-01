/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 */
package com.lemonclient.client.clickgui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.lukflug.panelstudio.config.IPanelConfig;
import java.awt.Dimension;
import java.awt.Point;

private static class GuiConfig.GSPanelConfig
implements IPanelConfig {
    private final JsonObject configObject;

    public GuiConfig.GSPanelConfig(JsonObject configObject) {
        this.configObject = configObject;
    }

    @Override
    public void savePositon(Point position) {
        this.configObject.add("PosX", (JsonElement)new JsonPrimitive((Number)position.x));
        this.configObject.add("PosY", (JsonElement)new JsonPrimitive((Number)position.y));
    }

    @Override
    public void saveSize(Dimension size) {
    }

    @Override
    public Point loadPosition() {
        Point point = new Point();
        JsonElement panelPosXObject = this.configObject.get("PosX");
        if (panelPosXObject == null || !panelPosXObject.isJsonPrimitive()) {
            return null;
        }
        point.x = panelPosXObject.getAsInt();
        JsonElement panelPosYObject = this.configObject.get("PosY");
        if (panelPosYObject == null || !panelPosYObject.isJsonPrimitive()) {
            return null;
        }
        point.y = panelPosYObject.getAsInt();
        return point;
    }

    @Override
    public Dimension loadSize() {
        return null;
    }

    @Override
    public void saveState(boolean state) {
        this.configObject.add("State", (JsonElement)new JsonPrimitive(Boolean.valueOf(state)));
    }

    @Override
    public boolean loadState() {
        JsonElement panelOpenObject = this.configObject.get("State");
        if (panelOpenObject != null && panelOpenObject.isJsonPrimitive()) {
            return panelOpenObject.getAsBoolean();
        }
        return false;
    }
}
