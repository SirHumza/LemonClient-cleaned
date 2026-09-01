/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.theme.IColorScheme;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.util.function.Supplier;

private static final class LemonClientGUI.GSColorScheme
implements IColorScheme {
    private final String configName;
    private final Supplier<Boolean> isVisible;

    public LemonClientGUI.GSColorScheme(String configName, Supplier<Boolean> isVisible) {
        this.configName = configName;
        this.isVisible = isVisible;
    }

    @Override
    public void createSetting(ITheme theme, String name, String description, boolean hasAlpha, boolean allowsRainbow, Color color, boolean rainbow) {
    }

    @Override
    public Color getColor(String name) {
        return new Color(255, 255, 255);
    }
}
