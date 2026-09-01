/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.clickgui;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ITextFieldRenderer;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class Theme.12
implements ITextFieldRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    Theme.12(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public int renderTextField(Context context, String title, boolean focus, String content, int position, int select, int boxPosition, boolean insertMode) {
        int maxPosition;
        boolean effFocus = this.val$container ? context.hasFocus() : focus;
        Theme.this.renderBackground(context, effFocus, this.val$graphicalLevel);
        Color textColor = Theme.this.getFontColor(effFocus);
        Color highlightColor = Theme.this.hgihlight;
        Rectangle rect = this.getTextArea(context, title);
        int strlen = context.getInterface().getFontWidth(Theme.this.height, content.substring(0, position));
        context.getInterface().fillRect(rect, new Color(0, 0, 0, 64), new Color(0, 0, 0, 64), new Color(0, 0, 0, 64), new Color(0, 0, 0, 64));
        if (boxPosition < position) {
            int minPosition;
            for (minPosition = boxPosition; minPosition < position && context.getInterface().getFontWidth(Theme.this.height, content.substring(0, minPosition)) + rect.width - Theme.this.padding < strlen; ++minPosition) {
            }
            if (boxPosition < minPosition) {
                boxPosition = minPosition;
            }
        } else if (boxPosition > position) {
            boxPosition = position - 1;
        }
        for (maxPosition = content.length(); maxPosition > 0; --maxPosition) {
            if (context.getInterface().getFontWidth(Theme.this.height, content.substring(maxPosition)) < rect.width - Theme.this.padding) continue;
            ++maxPosition;
            break;
        }
        if (boxPosition > maxPosition) {
            boxPosition = maxPosition;
        } else if (boxPosition < 0) {
            boxPosition = 0;
        }
        int offset = context.getInterface().getFontWidth(Theme.this.height, content.substring(0, boxPosition));
        int x1 = rect.x + Theme.this.padding / 2 - offset + strlen;
        int x2 = rect.x + Theme.this.padding / 2 - offset;
        x2 = position < content.length() ? (x2 += context.getInterface().getFontWidth(Theme.this.height, content.substring(0, position + 1))) : (x2 += context.getInterface().getFontWidth(Theme.this.height, content + "X"));
        Theme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + Theme.this.padding, context.getPos().y + Theme.this.padding / 2), Theme.this.height, title + Theme.this.separator, textColor);
        context.getInterface().window(rect);
        if (select >= 0) {
            int x3 = rect.x + Theme.this.padding / 2 - offset + context.getInterface().getFontWidth(Theme.this.height, content.substring(0, select));
            context.getInterface().fillRect(new Rectangle(Math.min(x1, x3), rect.y + Theme.this.padding / 2, Math.abs(x3 - x1), Theme.this.height), highlightColor, highlightColor, highlightColor, highlightColor);
        }
        context.getInterface().drawString(new Point(rect.x + Theme.this.padding / 2 - offset, rect.y + Theme.this.padding / 2), Theme.this.height, content, textColor);
        if (System.currentTimeMillis() / 500L % 2L == 0L && focus) {
            if (insertMode) {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + Theme.this.padding / 2 + Theme.this.height, x2 - x1, 1), textColor, textColor, textColor, textColor);
            } else {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + Theme.this.padding / 2, 1, Theme.this.height), textColor, textColor, textColor, textColor);
            }
        }
        context.getInterface().restore();
        return boxPosition;
    }

    @Override
    public int getDefaultHeight() {
        int height = Theme.this.getBaseHeight() - Theme.this.padding;
        if (height % 2 == 1) {
            ++height;
        }
        return height;
    }

    @Override
    public Rectangle getTextArea(Context context, String title) {
        Rectangle rect = context.getRect();
        int length = Theme.this.padding + context.getInterface().getFontWidth(Theme.this.height, title + Theme.this.separator);
        return new Rectangle(rect.x + length, rect.y, rect.width - length, rect.height);
    }

    @Override
    public int transformToCharPos(Context context, String title, String content, int boxPosition) {
        Rectangle rect = this.getTextArea(context, title);
        Point mouse = context.getInterface().getMouse();
        int offset = context.getInterface().getFontWidth(Theme.this.height, content.substring(0, boxPosition));
        if (rect.contains(mouse)) {
            for (int i = 1; i <= content.length(); ++i) {
                if (rect.x + Theme.this.padding / 2 - offset + context.getInterface().getFontWidth(Theme.this.height, content.substring(0, i)) <= mouse.x) continue;
                return i - 1;
            }
            return content.length();
        }
        return -1;
    }
}
