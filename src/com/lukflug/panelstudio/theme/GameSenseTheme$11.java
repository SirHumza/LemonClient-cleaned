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

class GameSenseTheme.11
implements ITextFieldRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$logicalLevel;
    final /* synthetic */ int val$graphicalLevel;
    final /* synthetic */ boolean val$embed;

    GameSenseTheme.11(boolean bl, int n, int n2, boolean bl2) {
        this.val$container = bl;
        this.val$logicalLevel = n;
        this.val$graphicalLevel = n2;
        this.val$embed = bl2;
    }

    @Override
    public int renderTextField(Context context, String title, boolean focus, String content, int position, int select, int boxPosition, boolean insertMode) {
        int maxPosition;
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Color color = focus ? GameSenseTheme.this.scheme.getColor("Outline Color") : GameSenseTheme.this.scheme.getColor("Settings Color");
        Color textColor = GameSenseTheme.this.getFontColor(effFocus);
        Color highlightColor = GameSenseTheme.this.scheme.getColor("Highlight Color");
        Rectangle rect = this.getTextArea(context, title);
        int strlen = context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, position));
        if (boxPosition < position) {
            int minPosition;
            for (minPosition = boxPosition; minPosition < position && context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, minPosition)) + rect.width - GameSenseTheme.this.padding < strlen; ++minPosition) {
            }
            if (boxPosition < minPosition) {
                boxPosition = minPosition;
            }
        } else if (boxPosition > position) {
            boxPosition = position - 1;
        }
        for (maxPosition = content.length(); maxPosition > 0; --maxPosition) {
            if (context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(maxPosition)) < rect.width - GameSenseTheme.this.padding) continue;
            ++maxPosition;
            break;
        }
        if (boxPosition > maxPosition) {
            boxPosition = maxPosition;
        } else if (boxPosition < 0) {
            boxPosition = 0;
        }
        int offset = context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, boxPosition));
        int x1 = rect.x + GameSenseTheme.this.padding / 2 - offset + strlen;
        int x2 = rect.x + GameSenseTheme.this.padding / 2 - offset;
        x2 = position < content.length() ? (x2 += context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, position + 1))) : (x2 += context.getInterface().getFontWidth(GameSenseTheme.this.height, content + "X"));
        GameSenseTheme.this.fillBaseRect(context, effFocus, false, this.val$logicalLevel, this.val$graphicalLevel, null);
        GameSenseTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getRect().x + GameSenseTheme.this.padding, context.getRect().y + GameSenseTheme.this.padding / (this.val$embed ? 2 : 1)), GameSenseTheme.this.height, title + (this.val$embed ? GameSenseTheme.this.separator : ""), textColor);
        context.getInterface().window(rect);
        if (select >= 0) {
            int x3 = rect.x + GameSenseTheme.this.padding / 2 - offset + context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, select));
            context.getInterface().fillRect(new Rectangle(Math.min(x1, x3), rect.y + GameSenseTheme.this.padding / 2, Math.abs(x3 - x1), GameSenseTheme.this.height), highlightColor, highlightColor, highlightColor, highlightColor);
        }
        context.getInterface().drawString(new Point(rect.x + GameSenseTheme.this.padding / 2 - offset, rect.y + GameSenseTheme.this.padding / 2), GameSenseTheme.this.height, content, textColor);
        if (System.currentTimeMillis() / 500L % 2L == 0L && focus) {
            if (insertMode) {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + GameSenseTheme.this.padding / 2 + GameSenseTheme.this.height, x2 - x1, 1), textColor, textColor, textColor, textColor);
            } else {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + GameSenseTheme.this.padding / 2, 1, GameSenseTheme.this.height), textColor, textColor, textColor, textColor);
            }
        }
        ITheme.drawRect(context.getInterface(), rect, color);
        context.getInterface().restore();
        return boxPosition;
    }

    @Override
    public int getDefaultHeight() {
        if (this.val$embed) {
            int height = GameSenseTheme.this.getBaseHeight() - GameSenseTheme.this.padding;
            if (height % 2 == 1) {
                ++height;
            }
            return height;
        }
        return 2 * GameSenseTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getTextArea(Context context, String title) {
        Rectangle rect = context.getRect();
        if (this.val$embed) {
            int length = GameSenseTheme.this.padding + context.getInterface().getFontWidth(GameSenseTheme.this.height, title + GameSenseTheme.this.separator);
            return new Rectangle(rect.x + length, rect.y, rect.width - length, rect.height);
        }
        return new Rectangle(rect.x + GameSenseTheme.this.padding, rect.y + GameSenseTheme.this.getBaseHeight(), rect.width - 2 * GameSenseTheme.this.padding, rect.height - GameSenseTheme.this.getBaseHeight() - GameSenseTheme.this.padding);
    }

    @Override
    public int transformToCharPos(Context context, String title, String content, int boxPosition) {
        Rectangle rect = this.getTextArea(context, title);
        Point mouse = context.getInterface().getMouse();
        int offset = context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, boxPosition));
        if (rect.contains(mouse)) {
            for (int i = 1; i <= content.length(); ++i) {
                if (rect.x + GameSenseTheme.this.padding / 2 - offset + context.getInterface().getFontWidth(GameSenseTheme.this.height, content.substring(0, i)) <= mouse.x) continue;
                return i - 1;
            }
            return content.length();
        }
        return -1;
    }
}
