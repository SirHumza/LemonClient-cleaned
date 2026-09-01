/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.mc12;

import com.lukflug.panelstudio.mc12.GLInterface;
import java.awt.Point;

public abstract class MinecraftGUI.GUIInterface
extends GLInterface {
    public MinecraftGUI.GUIInterface(boolean clipX) {
        super(clipX);
    }

    @Override
    public long getTime() {
        return MinecraftGUI.this.lastTime;
    }

    @Override
    public boolean getButton(int button) {
        switch (button) {
            case 0: {
                return MinecraftGUI.this.lButton;
            }
            case 1: {
                return MinecraftGUI.this.rButton;
            }
        }
        return false;
    }

    @Override
    public Point getMouse() {
        return new Point(MinecraftGUI.this.mouse);
    }

    @Override
    protected float getZLevel() {
        return MinecraftGUI.this.field_73735_i;
    }
}
