/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Sphere
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.util.font.FontUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lemonclient.client.module.modules.render.Nametags;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, GSColor color) {
        RenderUtil.drawLine(posx, posy, posz, posx2, posy2, posz2, color, 1.0f);
    }

    public static void drawRectOutline(double x, double y, double width, double height, Color color) {
        RenderUtil.drawGradientRectOutline(x, y, width, height, GradientDirection.Normal, color, color);
    }

    public static void drawGradientRectOutline(double x, double y, double width, double height, GradientDirection direction, Color startColor, Color endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        Color[] result = RenderUtil.checkColorDirection(direction, startColor, endColor);
        GL11.glBegin((int)2);
        GL11.glColor4f((float)((float)result[2].getRed() / 255.0f), (float)((float)result[2].getGreen() / 255.0f), (float)((float)result[2].getBlue() / 255.0f), (float)((float)result[2].getAlpha() / 255.0f));
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glColor4f((float)((float)result[3].getRed() / 255.0f), (float)((float)result[3].getGreen() / 255.0f), (float)((float)result[3].getBlue() / 255.0f), (float)((float)result[3].getAlpha() / 255.0f));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glColor4f((float)((float)result[0].getRed() / 255.0f), (float)((float)result[0].getGreen() / 255.0f), (float)((float)result[0].getBlue() / 255.0f), (float)((float)result[0].getAlpha() / 255.0f));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glColor4f((float)((float)result[1].getRed() / 255.0f), (float)((float)result[1].getGreen() / 255.0f), (float)((float)result[1].getBlue() / 255.0f), (float)((float)result[1].getAlpha() / 255.0f));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
    }

    public static void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x3, (double)y3);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawRect(double x, double y, double width, double height, Color color) {
        RenderUtil.drawGradientRect(x, y, width, height, GradientDirection.Normal, color, color);
    }

    public static void setColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    private static Color[] checkColorDirection(GradientDirection direction, Color start, Color end) {
        Color[] dir = new Color[4];
        if (direction == GradientDirection.Normal) {
            for (int a = 0; a < dir.length; ++a) {
                dir[a] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            }
        } else if (direction == GradientDirection.DownToUp) {
            dir[0] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[1] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[2] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[3] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
        } else if (direction == GradientDirection.UpToDown) {
            dir[0] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[1] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[2] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[3] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
        } else if (direction == GradientDirection.RightToLeft) {
            dir[0] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[1] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[2] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[3] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
        } else if (direction == GradientDirection.LeftToRight) {
            dir[0] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[1] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[2] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[3] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
        } else {
            for (int a = 0; a < dir.length; ++a) {
                dir[a] = new Color(255, 255, 255);
            }
        }
        return dir;
    }

    public static void drawGradientRect(double x, double y, double width, double height, GradientDirection direction, Color startColor, Color endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        Color[] result = RenderUtil.checkColorDirection(direction, startColor, endColor);
        GL11.glBegin((int)7);
        RenderUtil.setColor(result[0]);
        GL11.glVertex2d((double)(x + width), (double)y);
        RenderUtil.setColor(result[1]);
        GL11.glVertex2d((double)x, (double)y);
        RenderUtil.setColor(result[2]);
        GL11.glVertex2d((double)x, (double)(y + height));
        RenderUtil.setColor(result[3]);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.color(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
    }

    public static void drawRectSOutline(double x, double y, double x2, double y2, Color color) {
        RenderUtil.drawGradientRectSOutline(x, y, x2, y2, GradientDirection.Normal, color, color);
    }

    public static void drawGradientRectSOutline(double x, double y, double x2, double y2, GradientDirection direction, Color startColor, Color endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        Color[] result = RenderUtil.checkColorDirection(direction, startColor, endColor);
        GL11.glBegin((int)2);
        GL11.glColor4f((float)((float)result[2].getRed() / 255.0f), (float)((float)result[2].getGreen() / 255.0f), (float)((float)result[2].getBlue() / 255.0f), (float)((float)result[2].getAlpha() / 255.0f));
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glColor4f((float)((float)result[3].getRed() / 255.0f), (float)((float)result[3].getGreen() / 255.0f), (float)((float)result[3].getBlue() / 255.0f), (float)((float)result[3].getAlpha() / 255.0f));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glColor4f((float)((float)result[0].getRed() / 255.0f), (float)((float)result[0].getGreen() / 255.0f), (float)((float)result[0].getBlue() / 255.0f), (float)((float)result[0].getAlpha() / 255.0f));
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glColor4f((float)((float)result[1].getRed() / 255.0f), (float)((float)result[1].getGreen() / 255.0f), (float)((float)result[1].getBlue() / 255.0f), (float)((float)result[1].getAlpha() / 255.0f));
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
    }

    public static void drawRectS(double x1, double y1, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.color(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
    }

    public static void color(int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
    }

    public static void prepareGL() {
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_187441_d((float)Float.intBitsToFloat(Float.floatToIntBits(5.0675106f) ^ 0x7F22290C));
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179124_c((float)Float.intBitsToFloat(Float.floatToIntBits(11.925059f) ^ 0x7EBECD0B), (float)Float.intBitsToFloat(Float.floatToIntBits(18.2283f) ^ 0x7E11D38F), (float)Float.intBitsToFloat(Float.floatToIntBits(9.73656f) ^ 0x7E9BC8F3));
    }

    public static void releaseGL() {
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
        GlStateManager.func_179124_c((float)Float.intBitsToFloat(Float.floatToIntBits(12.552789f) ^ 0x7EC8D839), (float)Float.intBitsToFloat(Float.floatToIntBits(7.122752f) ^ 0x7F63ED96), (float)Float.intBitsToFloat(Float.floatToIntBits(5.4278784f) ^ 0x7F2DB12E));
        GL11.glColor4f((float)Float.intBitsToFloat(Float.floatToIntBits(10.5715685f) ^ 0x7EA92525), (float)Float.intBitsToFloat(Float.floatToIntBits(4.9474883f) ^ 0x7F1E51D3), (float)Float.intBitsToFloat(Float.floatToIntBits(4.9044757f) ^ 0x7F1CF177), (float)Float.intBitsToFloat(Float.floatToIntBits(9.482457f) ^ 0x7E97B825));
    }

    public static void draw2DGradientRect(float left, float top, float right, float bottom, int leftBottomColor, int leftTopColor, int rightBottomColor, int rightTopColor) {
        float lba = (float)(leftBottomColor >> 24 & 0xFF) / 255.0f;
        float lbr = (float)(leftBottomColor >> 16 & 0xFF) / 255.0f;
        float lbg = (float)(leftBottomColor >> 8 & 0xFF) / 255.0f;
        float lbb = (float)(leftBottomColor & 0xFF) / 255.0f;
        float rba = (float)(rightBottomColor >> 24 & 0xFF) / 255.0f;
        float rbr = (float)(rightBottomColor >> 16 & 0xFF) / 255.0f;
        float rbg = (float)(rightBottomColor >> 8 & 0xFF) / 255.0f;
        float rbb = (float)(rightBottomColor & 0xFF) / 255.0f;
        float lta = (float)(leftTopColor >> 24 & 0xFF) / 255.0f;
        float ltr = (float)(leftTopColor >> 16 & 0xFF) / 255.0f;
        float ltg = (float)(leftTopColor >> 8 & 0xFF) / 255.0f;
        float ltb = (float)(leftTopColor & 0xFF) / 255.0f;
        float rta = (float)(rightTopColor >> 24 & 0xFF) / 255.0f;
        float rtr = (float)(rightTopColor >> 16 & 0xFF) / 255.0f;
        float rtg = (float)(rightTopColor >> 8 & 0xFF) / 255.0f;
        float rtb = (float)(rightTopColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)right, (double)top, 0.0).func_181666_a(rtr, rtg, rtb, rta).func_181675_d();
        bufferbuilder.func_181662_b((double)left, (double)top, 0.0).func_181666_a(ltr, ltg, ltb, lta).func_181675_d();
        bufferbuilder.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(lbr, lbg, lbb, lba).func_181675_d();
        bufferbuilder.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(rbr, rbg, rbb, rba).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, GSColor color, float width) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_187441_d((float)width);
        color.glColor();
        bufferbuilder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        RenderUtil.vertex(posx, posy, posz, bufferbuilder);
        RenderUtil.vertex(posx2, posy2, posz2, bufferbuilder);
        tessellator.func_78381_a();
    }

    public static void draw2DRect(int posX, int posY, int width, int height, int zHeight, GSColor color) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        color.glColor();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b((double)posX, (double)(posY + height), (double)zHeight).func_181675_d();
        bufferbuilder.func_181662_b((double)(posX + width), (double)(posY + height), (double)zHeight).func_181675_d();
        bufferbuilder.func_181662_b((double)(posX + width), (double)posY, (double)zHeight).func_181675_d();
        bufferbuilder.func_181662_b((double)posX, (double)posY, (double)zHeight).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    private static void drawBorderedRect(double x, double y, double x1, GSColor inside, GSColor border) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        inside.glColor();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(x, 1.0, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, 1.0, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x, y, 0.0).func_181675_d();
        tessellator.func_78381_a();
        border.glColor();
        GlStateManager.func_187441_d((float)1.8f);
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(x, y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x, 1.0, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, 1.0, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x, y, 0.0).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawCircle(float x, float y, float z, Double radius, GSColor colour) {
        GlStateManager.func_179129_p();
        GlStateManager.func_179118_c();
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        int alpha = 255 - colour.getAlpha();
        if (alpha == 0) {
            alpha = 1;
        }
        for (int i = 0; i < 361; ++i) {
            bufferbuilder.func_181662_b((double)x + Math.sin(Math.toRadians(i)) * radius - RenderUtil.mc.func_175598_ae().field_78730_l, (double)y - RenderUtil.mc.func_175598_ae().field_78731_m, (double)z + Math.cos(Math.toRadians(i)) * radius - RenderUtil.mc.func_175598_ae().field_78728_n).func_181666_a((float)colour.getRed() / 255.0f, (float)colour.getGreen() / 255.0f, (float)colour.getBlue() / 255.0f, (float)alpha).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179089_o();
        GlStateManager.func_179141_d();
        GlStateManager.func_179103_j((int)7424);
    }

    public static void drawCircle(float x, float y, float z, Double radius, int stepCircle, int alphaVal) {
        GlStateManager.func_179129_p();
        GlStateManager.func_179118_c();
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        int alpha = 255 - alphaVal;
        if (alpha == 0) {
            alpha = 1;
        }
        for (int i = 0; i < 361; ++i) {
            GSColor colour = ColorSetting.getRainbowColor(i % 180 * stepCircle);
            bufferbuilder.func_181662_b((double)x + Math.sin(Math.toRadians(i)) * radius - RenderUtil.mc.func_175598_ae().field_78730_l, (double)y - RenderUtil.mc.func_175598_ae().field_78731_m, (double)z + Math.cos(Math.toRadians(i)) * radius - RenderUtil.mc.func_175598_ae().field_78728_n).func_181666_a((float)colour.getRed() / 255.0f, (float)colour.getGreen() / 255.0f, (float)colour.getBlue() / 255.0f, (float)alpha).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179089_o();
        GlStateManager.func_179141_d();
        GlStateManager.func_179103_j((int)7424);
    }

    public static void drawBox(BlockPos blockPos, double height, GSColor color, int sides) {
        RenderUtil.drawBox(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0, height, 1.0, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, GSColor color, int sides) {
        RenderUtil.drawBox(bb, check, height, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, GSColor color, int alpha, int sides) {
        if (check) {
            RenderUtil.drawBox(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, bb.field_72337_e - bb.field_72338_b, bb.field_72334_f - bb.field_72339_c, color, alpha, sides);
        } else {
            RenderUtil.drawBox(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, height, bb.field_72334_f - bb.field_72339_c, color, alpha, sides);
        }
    }

    public static void drawBox(double x, double y, double z, double w, double h, double d, GSColor color, int alpha, int sides) {
        GlStateManager.func_179118_c();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        color.glColor();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        RenderUtil.doVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides, false);
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
    }

    public static void drawBoxDire(AxisAlignedBB bb, double height, GSColor color, int alpha, int sides) {
        RenderUtil.drawBoxDire(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, height, bb.field_72334_f - bb.field_72339_c, color, alpha, sides);
        RenderUtil.drawFixBoxDire(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, height, bb.field_72334_f - bb.field_72339_c, color, alpha, sides);
    }

    public static void drawBoxDire(double x, double y, double z, double w, double h, double d, GSColor color, int alpha, int sides) {
        GlStateManager.func_179118_c();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        color.glColor();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        RenderUtil.doVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides);
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
    }

    public static void drawFixBoxDire(double x, double y, double z, double w, double h, double d, GSColor color, int alpha, int sides) {
        GlStateManager.func_179118_c();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        color.glColor();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        RenderUtil.doFixVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides);
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
    }

    public static void drawBoundingBoxDire(BlockPos pos, double height, double width, GSColor color, int alpha, int sides) {
        RenderUtil.drawBoundingBoxDire(new AxisAlignedBB(pos), height, width, color, alpha, sides);
    }

    public static void drawBoundingBoxDire(AxisAlignedBB bb, double height, double width, GSColor color, int alpha, int sides) {
        RenderUtil.drawBoundingBoxDire(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, height, bb.field_72334_f - bb.field_72339_c, width, color, alpha, sides);
    }

    public static void drawBoundingBoxDire(double x, double y, double z, double w, double h, double d, double width, GSColor color, int alpha, int sides) {
        GlStateManager.func_179118_c();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_187441_d((float)((float)width));
        color.glColor();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + w, y + h, z + d);
        if ((sides & 0x20) != 0) {
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
        }
        if ((sides & 0x10) != 0) {
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 4) != 0) {
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 8) != 0) {
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
        }
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
    }

    public static void drawBoundingBox(AxisAlignedBB bb, double width, GSColor[] otherPos) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_187441_d((float)((float)width));
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, otherPos[0], otherPos[0].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, otherPos[1], otherPos[1].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, otherPos[2], otherPos[2].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, otherPos[3], otherPos[3].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, otherPos[0], otherPos[0].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, otherPos[4], otherPos[4].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, otherPos[5], otherPos[5].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, otherPos[1], otherPos[1].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, otherPos[2], otherPos[2].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, otherPos[6], otherPos[6].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, otherPos[5], otherPos[5].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, otherPos[6], otherPos[6].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, otherPos[7], otherPos[7].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, otherPos[3], otherPos[3].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, otherPos[7], otherPos[7].getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, otherPos[4], otherPos[4].getAlpha(), bufferbuilder);
        tessellator.func_78381_a();
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, double width, GSColor[] color, boolean five, int sides) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_187441_d((float)((float)width));
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        if ((sides & 0x20) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[6], color[6].getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 0x10) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[5], color[5].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[4], color[4].getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 4) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[4], color[4].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 8) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[6], color[6].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[5], color[5].getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 2) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[6], color[6].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[5], color[5].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[4], color[4].getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 1) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            }
        }
        tessellator.func_78381_a();
    }

    public static void drawBoundingBox(BlockPos bp, double height, float width, GSColor color) {
        RenderUtil.drawBoundingBox(RenderUtil.getBoundingBox(bp, height), (double)width, color, color.getAlpha());
    }

    public static void drawBoundingBox(AxisAlignedBB bb, double width, GSColor color) {
        RenderUtil.drawBoundingBox(bb, width, color, color.getAlpha());
    }

    public static void drawBoundingBox(AxisAlignedBB bb, double width, GSColor color, int alpha) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_187441_d((float)((float)width));
        color.glColor();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c, color, color.getAlpha(), bufferbuilder);
        RenderUtil.colorVertex(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
        RenderUtil.colorVertex(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c, color, alpha, bufferbuilder);
        tessellator.func_78381_a();
    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, double high, int width, GSColor color, int sides) {
        RenderUtil.drawBoundingBoxWithSides(RenderUtil.getBoundingBox(blockPos, high), width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, GSColor color, int sides) {
        RenderUtil.drawBoundingBoxWithSides(RenderUtil.getBoundingBox(blockPos, 1.0), width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, GSColor color, int alpha, int sides) {
        RenderUtil.drawBoundingBoxWithSides(RenderUtil.getBoundingBox(blockPos, 1.0), width, color, alpha, sides);
    }

    public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, GSColor color, int sides) {
        RenderUtil.drawBoundingBoxWithSides(axisAlignedBB, width, color, color.getAlpha(), sides);
    }

    public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, GSColor color, int alpha, int sides) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_187441_d((float)width);
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        RenderUtil.doVerticies(axisAlignedBB, color, alpha, bufferbuilder, sides, true);
        tessellator.func_78381_a();
    }

    public static void drawBoxProva2(AxisAlignedBB bb, GSColor[] color, int sides) {
        RenderUtil.drawBoxProva(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, bb.field_72337_e - bb.field_72338_b, bb.field_72334_f - bb.field_72339_c, color, sides);
    }

    public static void drawBoxProva(double x, double y, double z, double w, double h, double d, GSColor[] color, int sides) {
        GlStateManager.func_179118_c();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        RenderUtil.doVerticiesProva(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, bufferbuilder, sides);
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
    }

    private static void doVerticiesProva(AxisAlignedBB axisAlignedBB, GSColor[] color, BufferBuilder bufferbuilder, int sides) {
        if ((sides & 0x20) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[6], color[6].getAlpha(), bufferbuilder);
        }
        if ((sides & 0x10) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[5], color[5].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[4], color[4].getAlpha(), bufferbuilder);
        }
        if ((sides & 4) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[4], color[4].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
        }
        if ((sides & 8) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[6], color[6].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[5], color[5].getAlpha(), bufferbuilder);
        }
        if ((sides & 2) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[7], color[7].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color[6], color[6].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[5], color[5].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color[4], color[4].getAlpha(), bufferbuilder);
        }
        if ((sides & 1) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[3], color[3].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[2], color[2].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color[1], color[1].getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color[0], color[0].getAlpha(), bufferbuilder);
        }
    }

    public static void drawBoxWithDirection(AxisAlignedBB bb, GSColor color, float rotation, float width, int mode) {
        double xCenter = bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) / 2.0;
        double zCenter = bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) / 2.0;
        Points square = new Points(bb.field_72338_b, bb.field_72337_e, xCenter, zCenter, rotation);
        if (mode == 0) {
            square.addPoints(bb.field_72340_a, bb.field_72339_c);
            square.addPoints(bb.field_72340_a, bb.field_72334_f);
            square.addPoints(bb.field_72336_d, bb.field_72334_f);
            square.addPoints(bb.field_72336_d, bb.field_72339_c);
        }
        if (mode == 0) {
            RenderUtil.drawDirection(square, color, width);
        }
    }

    public static void drawDirection(Points square, GSColor color, float width) {
        int i;
        for (i = 0; i < 4; ++i) {
            RenderUtil.drawLine(square.getPoint(i)[0], square.yMin, square.getPoint(i)[1], square.getPoint((i + 1) % 4)[0], square.yMin, square.getPoint((i + 1) % 4)[1], color, width);
        }
        for (i = 0; i < 4; ++i) {
            RenderUtil.drawLine(square.getPoint(i)[0], square.yMax, square.getPoint(i)[1], square.getPoint((i + 1) % 4)[0], square.yMax, square.getPoint((i + 1) % 4)[1], color, width);
        }
        for (i = 0; i < 4; ++i) {
            RenderUtil.drawLine(square.getPoint(i)[0], square.yMin, square.getPoint(i)[1], square.getPoint(i)[0], square.yMax, square.getPoint(i)[1], color, width);
        }
    }

    public static void drawSphere(double x, double y, double z, float size, int slices, int stacks, float lineWidth, GSColor color) {
        Sphere sphere = new Sphere();
        GlStateManager.func_187441_d((float)lineWidth);
        color.glColor();
        sphere.setDrawStyle(100013);
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)(x - RenderUtil.mc.func_175598_ae().field_78730_l), (double)(y - RenderUtil.mc.func_175598_ae().field_78731_m), (double)(z - RenderUtil.mc.func_175598_ae().field_78728_n));
        sphere.draw(size, slices, stacks);
        GlStateManager.func_179121_F();
    }

    public static void drawNametag(Entity entity, String[] text, GSColor color, int type) {
        Vec3d pos = EntityUtil.getInterpolatedPos(entity, mc.func_184121_ak());
        RenderUtil.drawNametag(pos.field_72450_a, pos.field_72448_b + (double)entity.field_70131_O, pos.field_72449_c, text, color, type, 0.0, 0.0);
    }

    public static double getDistance(double x, double y, double z) {
        Entity viewEntity = mc.func_175606_aa();
        if (viewEntity == null) {
            viewEntity = RenderUtil.mc.field_71439_g;
        }
        double d0 = viewEntity.field_70165_t - x;
        double d1 = viewEntity.field_70163_u - y;
        double d2 = viewEntity.field_70161_v - z;
        return MathHelper.func_76133_a((double)(d0 * d0 + d1 * d1 + d2 * d2));
    }

    public static void drawNametag(double x, double y, double z, String[] text, GSColor color, int type, double customScale, double maxSize) {
        ColorMain colorMain = ModuleManager.getModule(ColorMain.class);
        double dist = RenderUtil.getDistance(x, y, z);
        double scale = 1.0;
        double offset = 0.0;
        int start = 0;
        switch (type) {
            case 0: {
                scale = dist / 20.0 * Math.pow(1.2589254, 0.1 / (dist < 25.0 ? 0.5 : 2.0));
                scale = Math.min(Math.max(scale, 0.5), 5.0);
                offset = scale > 2.0 ? scale / 2.0 : scale;
                scale /= 40.0;
                start = 10;
                break;
            }
            case 1: {
                scale = customScale;
                break;
            }
            case 2: {
                scale = 0.0018 + 0.003 * dist;
                if (dist <= 8.0) {
                    scale = 0.0245;
                }
                start = -8;
            }
        }
        if (maxSize != 0.0 && scale > maxSize) {
            scale = maxSize;
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)(x - RenderUtil.mc.func_175598_ae().field_78730_l), (double)(y + offset - RenderUtil.mc.func_175598_ae().field_78731_m), (double)(z - RenderUtil.mc.func_175598_ae().field_78728_n));
        GlStateManager.func_179114_b((float)(-RenderUtil.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)RenderUtil.mc.func_175598_ae().field_78732_j, (float)(RenderUtil.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)(-scale), (double)(-scale), (double)scale);
        if (type == 2) {
            Nametags nametags = ModuleManager.getModule(Nametags.class);
            double width = 0.0;
            GSColor bcolor = new GSColor(0, 0, 0, 0);
            if (((Boolean)nametags.outline.getValue()).booleanValue()) {
                bcolor = color;
                if (((Boolean)nametags.customColor.getValue()).booleanValue()) {
                    bcolor = nametags.borderColor.getValue();
                }
            }
            for (String s : text) {
                double w = (double)FontUtil.getStringWidth((Boolean)colorMain.customFont.getValue(), s) / 2.0;
                if (!(w > width)) continue;
                width = w;
            }
            RenderUtil.drawBorderedRect(-width - 1.0, -RenderUtil.mc.field_71466_p.field_78288_b, width + 2.0, new GSColor(0, 4, 0, (Boolean)nametags.border.getValue() != false ? 85 : 0), bcolor);
        }
        GlStateManager.func_179098_w();
        for (int i = 0; i < text.length; ++i) {
            FontUtil.drawStringWithShadow((Boolean)colorMain.customFont.getValue(), text[i], -FontUtil.getStringWidth((Boolean)colorMain.customFont.getValue(), text[i]) / 2, i * (RenderUtil.mc.field_71466_p.field_78288_b + 1) + start, color);
        }
        GlStateManager.func_179090_x();
        if (type != 2) {
            GlStateManager.func_179121_F();
        }
    }

    private static void vertex(double x, double y, double z, BufferBuilder bufferbuilder) {
        bufferbuilder.func_181662_b(x - RenderUtil.mc.func_175598_ae().field_78730_l, y - RenderUtil.mc.func_175598_ae().field_78731_m, z - RenderUtil.mc.func_175598_ae().field_78728_n).func_181675_d();
    }

    private static void colorVertex(double x, double y, double z, GSColor color, int alpha, BufferBuilder bufferbuilder) {
        bufferbuilder.func_181662_b(x - RenderUtil.mc.func_175598_ae().field_78730_l, y - RenderUtil.mc.func_175598_ae().field_78731_m, z - RenderUtil.mc.func_175598_ae().field_78728_n).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), alpha).func_181675_d();
    }

    private static AxisAlignedBB getBoundingBox(BlockPos bp, double height) {
        double x = bp.func_177958_n();
        double y = bp.func_177956_o();
        double z = bp.func_177952_p();
        return new AxisAlignedBB(x, y, z, x + 1.0, y + height, z + 1.0);
    }

    private static void doVerticies(AxisAlignedBB axisAlignedBB, GSColor color, int alpha, BufferBuilder bufferbuilder, int sides, boolean five) {
        if ((sides & 0x20) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 0x10) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 4) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 8) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            }
        }
        if ((sides & 2) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 1) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            }
        }
    }

    public static void doVerticies(AxisAlignedBB axisAlignedBB, GSColor color, int alpha, BufferBuilder bufferbuilder, int sides) {
        if ((sides & 0x20) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 0x10) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 4) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 8) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
        }
    }

    public static void doFixVerticies(AxisAlignedBB axisAlignedBB, GSColor color, int alpha, BufferBuilder bufferbuilder, int sides) {
        if ((sides & 0x20) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
        }
        if ((sides & 0x10) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 4) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, color.getAlpha(), bufferbuilder);
        }
        if ((sides & 8) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, color.getAlpha(), bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
        }
    }

    public static void prepare() {
        GL11.glHint((int)3154, (int)4354);
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179103_j((int)7425);
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GL11.glEnable((int)2848);
        GL11.glEnable((int)34383);
    }

    public static void release() {
        GL11.glDisable((int)34383);
        GL11.glDisable((int)2848);
        GlStateManager.func_179141_d();
        GlStateManager.func_179089_o();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_187441_d((float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GL11.glHint((int)3154, (int)4352);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks, boolean wrap) {
        Vec3d amount = new Vec3d((entity.field_70165_t - entity.field_70142_S) * (double)partialTicks, (entity.field_70163_u - entity.field_70137_T) * (double)partialTicks, (entity.field_70161_v - entity.field_70136_U) * (double)partialTicks);
        Vec3d vec = new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U).func_178787_e(amount);
        if (wrap) {
            return vec.func_178786_a(RenderUtil.mc.func_175598_ae().field_78725_b, RenderUtil.mc.func_175598_ae().field_78726_c, RenderUtil.mc.func_175598_ae().field_78723_d);
        }
        return vec;
    }

    public static AxisAlignedBB getAxisAlignedBB(BlockPos pos, double size) {
        AxisAlignedBB bb = RenderUtil.mc.field_71441_e.func_180495_p(pos).func_185918_c((World)RenderUtil.mc.field_71441_e, pos);
        Vec3d center = bb.func_189972_c();
        return new AxisAlignedBB(center.field_72450_a - (bb.field_72336_d - bb.field_72340_a) * size, center.field_72448_b - (bb.field_72337_e - bb.field_72340_a) * size, center.field_72449_c - (bb.field_72334_f - bb.field_72339_c) * size, center.field_72450_a + (bb.field_72336_d - bb.field_72340_a) * size, center.field_72448_b + (bb.field_72337_e - bb.field_72338_b) * size, center.field_72449_c + (bb.field_72334_f - bb.field_72339_c) * size);
    }

    public static AxisAlignedBB getInterpolatedAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.field_72340_a - RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b - RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c - RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d - RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e - RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f - RenderUtil.mc.func_175598_ae().field_78728_n);
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
        return RenderUtil.interpolateEntity(entity, ticks).func_178786_a(RenderUtil.mc.func_175598_ae().field_78725_b, RenderUtil.mc.func_175598_ae().field_78726_c, RenderUtil.mc.func_175598_ae().field_78723_d);
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)time);
    }

    public static double getInterpolatedDouble(double pre, double current, float partialTicks) {
        return pre + (current - pre) * (double)partialTicks;
    }

    public static float getInterpolatedFloat(float pre, float current, float partialTicks) {
        return pre + (current - pre) * partialTicks;
    }

    private static class Points {
        double[][] point = new double[10][2];
        private int count = 0;
        private final double xCenter;
        private final double zCenter;
        public final double yMin;
        public final double yMax;
        private final float rotation;

        public Points(double yMin, double yMax, double xCenter, double zCenter, float rotation) {
            this.yMin = yMin;
            this.yMax = yMax;
            this.xCenter = xCenter;
            this.zCenter = zCenter;
            this.rotation = rotation;
        }

        public void addPoints(double x, double z) {
            double rotateX = (x -= this.xCenter) * Math.cos(this.rotation) - (z -= this.zCenter) * Math.sin(this.rotation);
            double rotateZ = x * Math.sin(this.rotation) + z * Math.cos(this.rotation);
            this.point[this.count++] = new double[]{rotateX += this.xCenter, rotateZ += this.zCenter};
        }

        public double[] getPoint(int index) {
            return this.point[index];
        }
    }

    public static enum GradientDirection {
        LeftToRight,
        RightToLeft,
        UpToDown,
        DownToUp,
        Normal;

    }
}
