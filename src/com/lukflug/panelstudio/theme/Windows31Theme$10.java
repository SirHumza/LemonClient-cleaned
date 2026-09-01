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

class Windows31Theme.10
implements ITextFieldRenderer {
    final /* synthetic */ boolean val$container;

    Windows31Theme.10(boolean bl) {
        this.val$container = bl;
    }

    @Override
    public int renderTextField(Context context, String title, boolean focus, String content, int position, int select, int boxPosition, boolean insertMode) {
        int maxPosition;
        boolean effFocus = this.val$container ? context.hasFocus() || focus : focus;
        Color textColor = Windows31Theme.this.getFontColor(effFocus);
        Color titleColor = this.val$container && effFocus ? Windows31Theme.this.getMainColor(effFocus, false) : textColor;
        Color highlightColor = Windows31Theme.this.getMainColor(effFocus, true);
        Rectangle rect = this.getTextArea(context, title);
        int strlen = context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, position));
        if (this.val$container && effFocus) {
            context.getInterface().fillRect(context.getRect(), highlightColor, highlightColor, highlightColor, highlightColor);
            context.getInterface().fillRect(rect, titleColor, titleColor, titleColor, titleColor);
        }
        if (boxPosition < position) {
            int minPosition;
            for (minPosition = boxPosition; minPosition < position && context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, minPosition)) + rect.width - Windows31Theme.this.padding < strlen; ++minPosition) {
            }
            if (boxPosition < minPosition) {
                boxPosition = minPosition;
            }
        } else if (boxPosition > position) {
            boxPosition = position - 1;
        }
        for (maxPosition = content.length(); maxPosition > 0; --maxPosition) {
            if (context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(maxPosition)) < rect.width - Windows31Theme.this.padding) continue;
            ++maxPosition;
            break;
        }
        if (boxPosition > maxPosition) {
            boxPosition = maxPosition;
        } else if (boxPosition < 0) {
            boxPosition = 0;
        }
        int offset = context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, boxPosition));
        int x1 = rect.x + Windows31Theme.this.padding / 2 - offset + strlen;
        int x2 = rect.x + Windows31Theme.this.padding / 2 - offset;
        x2 = position < content.length() ? (x2 += context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, position + 1))) : (x2 += context.getInterface().getFontWidth(Windows31Theme.this.height, content + "X"));
        context.getInterface().drawString(new Point(context.getPos().x + Windows31Theme.this.padding, context.getPos().y + Windows31Theme.this.padding), Windows31Theme.this.height, title + Windows31Theme.this.separator, titleColor);
        context.getInterface().window(rect);
        if (select >= 0) {
            int x3 = rect.x + Windows31Theme.this.padding / 2 - offset + context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, select));
            context.getInterface().fillRect(new Rectangle(Math.min(x1, x3), rect.y + Windows31Theme.this.padding, Math.abs(x3 - x1), Windows31Theme.this.height), highlightColor, highlightColor, highlightColor, highlightColor);
            context.getInterface().drawString(new Point(rect.x + Windows31Theme.this.padding / 2 - offset, rect.y + Windows31Theme.this.padding), Windows31Theme.this.height, content.substring(0, Math.min(position, select)), textColor);
            context.getInterface().drawString(new Point(Math.min(x1, x3), rect.y + Windows31Theme.this.padding), Windows31Theme.this.height, content.substring(Math.min(position, select), Math.max(position, select)), Windows31Theme.this.getMainColor(effFocus, false));
            context.getInterface().drawString(new Point(Math.max(x1, x3), rect.y + Windows31Theme.this.padding), Windows31Theme.this.height, content.substring(Math.max(position, select)), textColor);
        } else {
            context.getInterface().drawString(new Point(rect.x + Windows31Theme.this.padding / 2 - offset, rect.y + Windows31Theme.this.padding), Windows31Theme.this.height, content, textColor);
        }
        if (System.currentTimeMillis() / 500L % 2L == 0L && focus) {
            if (insertMode) {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + Windows31Theme.this.padding + Windows31Theme.this.height, x2 - x1, 1), textColor, textColor, textColor, textColor);
            } else {
                context.getInterface().fillRect(new Rectangle(x1, rect.y + Windows31Theme.this.padding, 1, Windows31Theme.this.height), textColor, textColor, textColor, textColor);
            }
        }
        ITheme.drawRect(context.getInterface(), rect, textColor);
        context.getInterface().restore();
        return boxPosition;
    }

    @Override
    public int getDefaultHeight() {
        int height = Windows31Theme.this.getBaseHeight();
        if (height % 2 == 1) {
            ++height;
        }
        return height;
    }

    @Override
    public Rectangle getTextArea(Context context, String title) {
        Rectangle rect = context.getRect();
        int length = Windows31Theme.this.padding + context.getInterface().getFontWidth(Windows31Theme.this.height, title + Windows31Theme.this.separator);
        return new Rectangle(rect.x + length, rect.y, rect.width - length, rect.height);
    }

    @Override
    public int transformToCharPos(Context context, String title, String content, int boxPosition) {
        Rectangle rect = this.getTextArea(context, title);
        Point mouse = context.getInterface().getMouse();
        int offset = context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, boxPosition));
        if (rect.contains(mouse)) {
            for (int i = 1; i <= content.length(); ++i) {
                if (rect.x + Windows31Theme.this.padding / 2 - offset + context.getInterface().getFontWidth(Windows31Theme.this.height, content.substring(0, i)) <= mouse.x) continue;
                return i - 1;
            }
            return content.length();
        }
        return -1;
    }
}
