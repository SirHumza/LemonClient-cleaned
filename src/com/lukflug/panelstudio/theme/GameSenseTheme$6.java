/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class GameSenseTheme.6
implements IButtonRenderer<Void> {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ int val$symbol;

    GameSenseTheme.6(boolean bl, int n, int n2, int n3) {
        this.val$container = bl;
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
        this.val$symbol = n3;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Void state) {
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        GameSenseTheme.this.fillBaseRect(context, effFocus, true, this.val$logicalLevel, this.val$graphicalLevel, null);
        GameSenseTheme.this.renderOverlay(context);
        Point[] points = new Point[3];
        int padding = context.getSize().height <= 2 * GameSenseTheme.this.padding ? 2 : GameSenseTheme.this.padding;
        Rectangle rect = new Rectangle(context.getPos().x + padding / 2, context.getPos().y + padding / 2, context.getSize().height - 2 * (padding / 2), context.getSize().height - 2 * (padding / 2));
        if (title == null) {
            rect.x += context.getSize().width / 2 - context.getSize().height / 2;
        }
        Color color = GameSenseTheme.this.getFontColor(effFocus);
        switch (this.val$symbol) {
            case 1: {
                context.getInterface().drawLine(new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), color, color);
                context.getInterface().drawLine(new Point(rect.x, rect.y + rect.height), new Point(rect.x + rect.width, rect.y), color, color);
                break;
            }
            case 2: {
                context.getInterface().fillRect(new Rectangle(rect.x, rect.y + rect.height - 2, rect.width, 2), color, color, color, color);
                break;
            }
            case 3: {
                if (rect.width % 2 == 1) {
                    --rect.width;
                }
                if (rect.height % 2 == 1) {
                    --rect.height;
                }
                context.getInterface().fillRect(new Rectangle(rect.x + rect.width / 2 - 1, rect.y, 2, rect.height), color, color, color, color);
                context.getInterface().fillRect(new Rectangle(rect.x, rect.y + rect.height / 2 - 1, rect.width, 2), color, color, color, color);
                break;
            }
            case 4: {
                if (rect.height % 2 == 1) {
                    --rect.height;
                }
                points[2] = new Point(rect.x + rect.width, rect.y);
                points[1] = new Point(rect.x + rect.width, rect.y + rect.height);
                points[0] = new Point(rect.x, rect.y + rect.height / 2);
                break;
            }
            case 5: {
                if (rect.height % 2 == 1) {
                    --rect.height;
                }
                points[0] = new Point(rect.x, rect.y);
                points[1] = new Point(rect.x, rect.y + rect.height);
                points[2] = new Point(rect.x + rect.width, rect.y + rect.height / 2);
                break;
            }
            case 6: {
                if (rect.width % 2 == 1) {
                    --rect.width;
                }
                points[0] = new Point(rect.x, rect.y + rect.height);
                points[1] = new Point(rect.x + rect.width, rect.y + rect.height);
                points[2] = new Point(rect.x + rect.width / 2, rect.y);
                break;
            }
            case 7: {
                if (rect.width % 2 == 1) {
                    --rect.width;
                }
                points[2] = new Point(rect.x, rect.y);
                points[1] = new Point(rect.x + rect.width, rect.y);
                points[0] = new Point(rect.x + rect.width / 2, rect.y + rect.height);
            }
        }
        if (this.val$symbol >= 4 && this.val$symbol <= 7) {
            context.getInterface().fillTriangle(points[0], points[1], points[2], color, color, color);
        }
        if (title != null) {
            context.getInterface().drawString(new Point(context.getPos().x + (this.val$symbol == 0 ? padding : context.getSize().height), context.getPos().y + padding), GameSenseTheme.this.height, title, GameSenseTheme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight() {
        return GameSenseTheme.this.getBaseHeight();
    }
}
