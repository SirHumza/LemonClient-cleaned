/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IContainerRenderer;
import java.awt.Color;
import java.awt.Rectangle;

class GameSenseTheme.2
implements IContainerRenderer {
    final /* synthetic */ int val$graphicalLevel;

    GameSenseTheme.2(int n) {
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderBackground(Context context, boolean focus) {
        if (this.val$graphicalLevel > 0) {
            Color color = GameSenseTheme.this.scheme.getColor("Outline Color");
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y, context.getSize().width, 1), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(context.getPos().x, context.getPos().y + context.getSize().height - 1, context.getSize().width, 1), color, color, color, color);
        }
    }

    @Override
    public int getTop() {
        return this.val$graphicalLevel <= 0 ? 0 : 1;
    }

    @Override
    public int getBottom() {
        return this.val$graphicalLevel <= 0 ? 0 : 1;
    }
}
