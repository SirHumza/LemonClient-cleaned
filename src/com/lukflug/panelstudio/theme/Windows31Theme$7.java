/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class Windows31Theme.7
implements IButtonRenderer<Void> {
    final /* synthetic */ int val$symbol;

    Windows31Theme.7(int n) {
        this.val$symbol = n;
    }

    @Override
    public void renderButton(Context context, String title, boolean focus, Void state) {
        Windows31Theme.this.drawButton(context.getInterface(), context.getRect(), focus, context.isClicked(0), true);
        Point[] points = new Point[3];
        int padding = context.getSize().height <= 12 ? 4 : 6;
        Rectangle rect = new Rectangle(context.getPos().x + padding / 2, context.getPos().y + padding / 2, context.getSize().height - 2 * (padding / 2), context.getSize().height - 2 * (padding / 2));
        if (title == null) {
            rect.x += context.getSize().width / 2 - context.getSize().height / 2;
        }
        Color color = Windows31Theme.this.getFontColor(focus);
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
            context.getInterface().drawString(new Point(context.getPos().x + (this.val$symbol == 0 ? padding : context.getSize().height), context.getPos().y + padding), Windows31Theme.this.height, title, Windows31Theme.this.getFontColor(focus));
        }
    }

    @Override
    public int getDefaultHeight() {
        return Windows31Theme.this.getBaseHeight();
    }
}
