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

class ImpactTheme.12
implements ITextFieldRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.12(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public int renderTextField(Context context, String title, boolean focus, String content, int position, int select, int boxPosition, boolean insertMode) {
        int maxPosition;
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel <= 0) {
            ImpactTheme.this.renderBackground(context, effFocus);
        }
        if (!this.val$container) {
            Color color = this.val$graphicalLevel <= 0 ? ImpactTheme.this.scheme.getColor("Panel Outline Color") : ImpactTheme.this.scheme.getColor("Component Outline Color");
            ITheme.drawRect(context.getInterface(), context.getRect(), color);
            ImpactTheme.this.renderOverlay(context);
        }
        Color textColor = ImpactTheme.this.getFontColor(effFocus);
        if (context.isHovered() && context.getInterface().getMouse().x > context.getPos().x + context.getSize().height - ImpactTheme.this.padding) {
            textColor = ImpactTheme.this.scheme.getColor("Active Font Color");
        }
        Color highlightColor = ImpactTheme.this.scheme.getColor("Highlight Color");
        Rectangle rect = this.getTextArea(context, title);
        int strlen = context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, position));
        context.getInterface().fillRect(rect, new Color(0, 0, 0, 64), new Color(0, 0, 0, 64), new Color(0, 0, 0, 64), new Color(0, 0, 0, 64));
        if (boxPosition < position) {
            int minPosition;
            for (minPosition = boxPosition; minPosition < position && context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, minPosition)) + rect.width - ImpactTheme.this.padding < strlen; ++minPosition) {
            }
            if (boxPosition < minPosition) {
                boxPosition = minPosition;
            }
        } else if (boxPosition > position) {
            boxPosition = position - 1;
        }
        for (maxPosition = content.length(); maxPosition > 0; --maxPosition) {
            if (context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(maxPosition)) < rect.width - ImpactTheme.this.padding) continue;
            ++maxPosition;
            break;
        }
        if (boxPosition > maxPosition) {
            boxPosition = maxPosition;
        } else if (boxPosition < 0) {
            boxPosition = 0;
        }
        int offset = context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, boxPosition));
        int x1 = rect.x + ImpactTheme.this.padding / 2 - offset + strlen;
        int x2 = rect.x + ImpactTheme.this.padding / 2 - offset;
        x2 = position < content.length() ? (x2 += context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, position + 1))) : (x2 += context.getInterface().getFontWidth(ImpactTheme.this.height, content + "X"));
        ImpactTheme.this.renderOverlay(context);
        context.getInterface().drawString(new Point(context.getPos().x + context.getSize().height - ImpactTheme.this.padding, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, title, textColor);
        context.getInterface().window(rect);
        if (select >= 0) {
            int x3 = rect.x + ImpactTheme.this.padding / 2 - offset + context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, select));
            context.getInterface().fillRect(new Rectangle(Math.min(x1, x3), context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0), Math.abs(x3 - x1), ImpactTheme.this.height), highlightColor, highlightColor, highlightColor, highlightColor);
        }
        context.getInterface().drawString(new Point(rect.x + ImpactTheme.this.padding / 2 - offset, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, content, textColor);
        if (System.currentTimeMillis() / 500L % 2L == 0L && focus) {
            if (insertMode) {
                context.getInterface().fillRect(new Rectangle(x1, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0) + ImpactTheme.this.height, x2 - x1, 1), textColor, textColor, textColor, textColor);
            } else {
                context.getInterface().fillRect(new Rectangle(x1, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0), 1, ImpactTheme.this.height), textColor, textColor, textColor, textColor);
            }
        }
        context.getInterface().restore();
        return boxPosition;
    }

    @Override
    public int getDefaultHeight() {
        return this.val$container ? ImpactTheme.this.getBaseHeight() - 2 : ImpactTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getTextArea(Context context, String title) {
        Rectangle rect = context.getRect();
        int length = rect.height - ImpactTheme.this.padding + context.getInterface().getFontWidth(ImpactTheme.this.height, title + "X");
        return new Rectangle(rect.x + length, rect.y + (this.val$container ? 0 : 1), rect.width - length, rect.height - (this.val$container ? 0 : 2));
    }

    @Override
    public int transformToCharPos(Context context, String title, String content, int boxPosition) {
        Rectangle rect = this.getTextArea(context, title);
        Point mouse = context.getInterface().getMouse();
        int offset = context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, boxPosition));
        if (rect.contains(mouse)) {
            for (int i = 1; i <= content.length(); ++i) {
                if (rect.x + ImpactTheme.this.padding / 2 - offset + context.getInterface().getFontWidth(ImpactTheme.this.height, content.substring(0, i)) <= mouse.x) continue;
                return i - 1;
            }
            return content.length();
        }
        return -1;
    }
}
