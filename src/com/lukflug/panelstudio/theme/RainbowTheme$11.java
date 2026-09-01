/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ITextFieldRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class RainbowTheme.11
implements ITextFieldRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    RainbowTheme.11(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public int renderTextField(Context context, String title, boolean focus, String content, int position, int select, int boxPosition, boolean insertMode) {
        int maxPosition;
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel == 0 || RainbowTheme.this.buttonRainbow.isOn()) {
            RainbowTheme.this.renderRainbowRect(context.getRect(), context, effFocus);
        }
        Color textColor = RainbowTheme.this.getFontColor(effFocus);
        Color highlightColor = RainbowTheme.this.scheme.getColor("Highlight Color");
        Rectangle rect = this.getTextArea(context, title);
        int strlen = context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, position));
        context.getInterface().fillRect(rect, new Color(0, 0, 0, 64), new Color(0, 0, 0, 64), new Color(0, 0, 0, 64), new Color(0, 0, 0, 64));
        ITheme.drawRect(context.getInterface(), rect, new Color(0, 0, 0, 64));
        if (boxPosition < position) {
            int minPosition;
            for (minPosition = boxPosition; minPosition < position && context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, minPosition)) + rect.width - RainbowTheme.this.padding < strlen; ++minPosition) {
            }
            if (boxPosition < minPosition) {
                boxPosition = minPosition;
            }
        } else if (boxPosition > position) {
            boxPosition = position - 1;
        }
        for (maxPosition = content.length(); maxPosition > 0; --maxPosition) {
            if (context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(maxPosition)) < rect.width - RainbowTheme.this.padding) continue;
            ++maxPosition;
            break;
        }
        if (boxPosition > maxPosition) {
            boxPosition = maxPosition;
        } else if (boxPosition < 0) {
            boxPosition = 0;
        }
        int offset = context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, boxPosition));
        int x1 = rect.x + RainbowTheme.this.padding / 2 - offset + strlen;
        int x2 = rect.x + RainbowTheme.this.padding / 2 - offset;
        x2 = position < content.length() ? (x2 += context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, position + 1))) : (x2 += context.getInterface().getFontWidth(RainbowTheme.this.height, content + "X"));
        RainbowTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + RainbowTheme.this.padding, context.getPos().y + RainbowTheme.this.padding / 2), RainbowTheme.this.height, title + RainbowTheme.this.separator, textColor);
        context.getInterface().window(rect);
        if (select >= 0) {
            int x3 = rect.x + RainbowTheme.this.padding / 2 - offset + context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, select));
            context.getInterface().fillRect(new Rectangle(Math.min(x1, x3), rect.y + RainbowTheme.this.padding / 2, Math.abs(x3 - x1), RainbowTheme.this.height), highlightColor, highlightColor, highlightColor, highlightColor);
        }
        context.getInterface().drawString(new Point(rect.x + RainbowTheme.this.padding / 2 - offset, rect.y + RainbowTheme.this.padding / 2), RainbowTheme.this.height, content, textColor);
        if (System.currentTimeMillis() / 500L % 2L == 0L && focus) {
            if (insertMode) {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + RainbowTheme.this.padding / 2 + RainbowTheme.this.height, x2 - x1, 1), textColor, textColor, textColor, textColor);
            } else {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + RainbowTheme.this.padding / 2, 1, RainbowTheme.this.height), textColor, textColor, textColor, textColor);
            }
        }
        context.getInterface().restore();
        return boxPosition;
    }

    @Override
    public int getDefaultHeight() {
        int height = RainbowTheme.this.getBaseHeight() - RainbowTheme.this.padding;
        if (height % 2 == 1) {
            ++height;
        }
        return height;
    }

    @Override
    public Rectangle getTextArea(Context context, String title) {
        Rectangle rect = context.getRect();
        int length = RainbowTheme.this.padding + context.getInterface().getFontWidth(RainbowTheme.this.height, title + RainbowTheme.this.separator);
        return new Rectangle(rect.x + length, rect.y, rect.width - length, rect.height);
    }

    @Override
    public int transformToCharPos(Context context, String title, String content, int boxPosition) {
        Rectangle rect = this.getTextArea(context, title);
        Point mouse = context.getInterface().getMouse();
        int offset = context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, boxPosition));
        if (rect.contains(mouse)) {
            for (int i = 1; i <= content.length(); ++i) {
                if (rect.x + RainbowTheme.this.padding / 2 - offset + context.getInterface().getFontWidth(RainbowTheme.this.height, content.substring(0, i)) <= mouse.x) continue;
                return i - 1;
            }
            return content.length();
        }
        return -1;
    }
}
