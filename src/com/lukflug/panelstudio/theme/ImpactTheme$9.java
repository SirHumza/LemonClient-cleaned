/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.theme.ISliderRenderer;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

class ImpactTheme.9
implements ISliderRenderer {
    final /* synthetic */ boolean val$container;
    final /* synthetic */ int val$graphicalLevel;

    ImpactTheme.9(boolean bl, int n) {
        this.val$container = bl;
        this.val$graphicalLevel = n;
    }

    @Override
    public void renderSlider(Context context, String title, String state, boolean focus, double value) {
        Color color;
        boolean effFocus;
        boolean bl = effFocus = this.val$container ? context.hasFocus() : focus;
        if (this.val$graphicalLevel <= 0) {
            if (this.val$container) {
                color = ImpactTheme.this.scheme.getColor("Title Color");
                context.getInterface().fillRect(context.getRect(), color, color, color, color);
            } else {
                ImpactTheme.this.renderBackground(context, effFocus);
            }
        }
        if (!this.val$container) {
            color = this.val$graphicalLevel <= 0 ? ImpactTheme.this.scheme.getColor("Panel Outline Color") : ImpactTheme.this.scheme.getColor("Component Outline Color");
            ITheme.drawRect(context.getInterface(), context.getRect(), color);
            ImpactTheme.this.renderOverlay(context);
        }
        Rectangle rect = context.getRect();
        if (!this.val$container) {
            rect = new Rectangle(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
        }
        if (ImpactTheme.this.getColor(null) != null && (title.equals("Red") || title.equals("Green") || title.equals("Blue") || title.equals("Hue") || title.equals("Saturation") || title.equals("Brightness"))) {
            Color main = ImpactTheme.this.getColor(null);
            Color colorA = null;
            Color colorB = null;
            float[] hsb = Color.RGBtoHSB(main.getRed(), main.getGreen(), main.getBlue(), null);
            if (title.equals("Red")) {
                colorA = new Color(0, main.getGreen(), main.getBlue());
                colorB = new Color(255, main.getGreen(), main.getBlue());
            } else if (title.equals("Green")) {
                colorA = new Color(main.getRed(), 0, main.getBlue());
                colorB = new Color(main.getRed(), 255, main.getBlue());
            } else if (title.equals("Blue")) {
                colorA = new Color(main.getRed(), main.getGreen(), 0);
                colorB = new Color(main.getRed(), main.getGreen(), 255);
            } else if (title.equals("Saturation")) {
                colorA = Color.getHSBColor(hsb[0], 0.0f, hsb[2]);
                colorB = Color.getHSBColor(hsb[0], 1.0f, hsb[2]);
            } else if (title.equals("Brightness")) {
                colorA = Color.getHSBColor(hsb[0], hsb[1], 0.0f);
                colorB = Color.getHSBColor(hsb[0], hsb[1], 1.0f);
            }
            if (colorA != null && colorB != null) {
                context.getInterface().fillRect(new Rectangle(context.getPos().x + 1, context.getPos().y + 1, context.getSize().width - 2, context.getSize().height - 2), colorA, colorB, colorB, colorA);
            } else {
                int a = rect.x;
                int b = rect.width / 6;
                int c = rect.width * 2 / 6;
                int d = rect.width * 3 / 6;
                int e = rect.width * 4 / 6;
                int f = rect.width * 5 / 6;
                int g = rect.width;
                b += a;
                c += a;
                d += a;
                e += a;
                f += a;
                g += a;
                Color c0 = Color.getHSBColor(0.0f, hsb[1], hsb[2]);
                Color c1 = Color.getHSBColor(0.16666667f, hsb[1], hsb[2]);
                Color c2 = Color.getHSBColor(0.33333334f, hsb[1], hsb[2]);
                Color c3 = Color.getHSBColor(0.5f, hsb[1], hsb[2]);
                Color c4 = Color.getHSBColor(0.6666667f, hsb[1], hsb[2]);
                Color c5 = Color.getHSBColor(0.8333333f, hsb[1], hsb[2]);
                context.getInterface().fillRect(new Rectangle(a, rect.y, b - a, rect.height), c0, c1, c1, c0);
                context.getInterface().fillRect(new Rectangle(b, rect.y, c - b, rect.height), c1, c2, c2, c1);
                context.getInterface().fillRect(new Rectangle(c, rect.y, d - c, rect.height), c2, c3, c3, c2);
                context.getInterface().fillRect(new Rectangle(d, rect.y, e - d, rect.height), c3, c4, c4, c3);
                context.getInterface().fillRect(new Rectangle(e, rect.y, f - e, rect.height), c4, c5, c5, c4);
                context.getInterface().fillRect(new Rectangle(f, rect.y, g - f, rect.height), c5, c0, c0, c5);
            }
            ImpactTheme.this.renderOverlay(context);
            Color lineColor = ImpactTheme.this.scheme.getColor("Active Font Color");
            int separator = (int)Math.round((double)(rect.width - 1) * value);
            context.getInterface().fillRect(new Rectangle(rect.x + separator, rect.y, 1, rect.height), lineColor, lineColor, lineColor, lineColor);
        } else {
            Color valueColor = ImpactTheme.this.scheme.getColor("Active Font Color");
            Color fontColor = ImpactTheme.this.getFontColor(effFocus);
            if (context.isHovered() && context.getInterface().getMouse().x > context.getPos().x + context.getSize().height - ImpactTheme.this.padding) {
                fontColor = ImpactTheme.this.scheme.getColor("Active Font Color");
            }
            int xpos = context.getPos().x + context.getSize().height - ImpactTheme.this.padding;
            if (this.val$container && this.val$graphicalLevel <= 0) {
                xpos = context.getPos().x + context.getSize().width / 2 - context.getInterface().getFontWidth(ImpactTheme.this.height, title) / 2;
            }
            context.getInterface().drawString(new Point(xpos, context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, title, fontColor);
            if (context.isHovered()) {
                context.getInterface().drawString(new Point(context.getPos().x + context.getSize().width - ImpactTheme.this.padding - context.getInterface().getFontWidth(ImpactTheme.this.height, state), context.getPos().y + ImpactTheme.this.padding - (this.val$container ? 1 : 0)), ImpactTheme.this.height, state, valueColor);
            }
            Color lineColor = ImpactTheme.this.scheme.getColor("Active Font Color");
            int separator = (int)Math.round((double)(context.getSize().width - context.getSize().height + ImpactTheme.this.padding - (this.val$container ? 0 : 1)) * value);
            context.getInterface().fillRect(new Rectangle(context.getPos().x + context.getSize().height - ImpactTheme.this.padding, context.getPos().y + context.getSize().height - (this.val$container ? 1 : 2), separator, 1), lineColor, lineColor, lineColor, lineColor);
        }
    }

    @Override
    public int getDefaultHeight() {
        return this.val$container ? ImpactTheme.this.getBaseHeight() - 2 : ImpactTheme.this.getBaseHeight();
    }

    @Override
    public Rectangle getSlideArea(Context context, String title, String state) {
        if (ImpactTheme.this.getColor(null) != null && (title.equals("Red") || title.equals("Green") || title.equals("Blue") || title.equals("Hue") || title.equals("Saturation") || title.equals("Brightness"))) {
            Rectangle rect = context.getRect();
            if (!this.val$container) {
                rect = new Rectangle(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
            }
            return rect;
        }
        return new Rectangle(context.getPos().x + context.getSize().height - ImpactTheme.this.padding, context.getPos().y, context.getSize().width - context.getSize().height + ImpactTheme.this.padding - (this.val$container ? 0 : 1), context.getSize().height);
    }
}
