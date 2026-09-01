/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.NewRenderEntityEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

@Module.Declaration(name="CrystalChams", category=Category.Render)
public class CrystalChams
extends Module {
    IntegerSetting range = this.registerInteger("Range", 32, 0, 256);
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("Normal", "Gradient"), "Normal");
    BooleanSetting chams = this.registerBoolean("Chams", false);
    BooleanSetting throughWalls = this.registerBoolean("ThroughWalls", false);
    BooleanSetting wireframe = this.registerBoolean("Wireframe", false);
    BooleanSetting wireWalls = this.registerBoolean("WireThroughWalls", false);
    DoubleSetting spinSpeed = this.registerDouble("SpinSpeed", 1.0, 0.0, 4.0);
    DoubleSetting floatSpeed = this.registerDouble("FloatSpeed", 1.0, 0.0, 4.0);
    ColorSetting color = this.registerColor("Color", new GSColor(255, 255, 255, 255), true);
    ColorSetting wireFrameColor = this.registerColor("WireframeColor", new GSColor(255, 255, 255, 255), true);
    DoubleSetting lineWidth = this.registerDouble("lineWidth", 1.0, 0.0, 4.0);
    DoubleSetting lineWidthInterp = this.registerDouble("lineWidthInterp", 1.0, 0.1, 4.0);
    BooleanSetting show = this.registerBoolean("ShowEntity ;;", false);
    @EventHandler
    private final Listener<NewRenderEntityEvent> renderEntityHeadEventListener = new Listener<NewRenderEntityEvent>(event -> {
        if (CrystalChams.mc.field_71439_g == null || CrystalChams.mc.field_71441_e == null || event.entityIn == null || event.entityIn.func_70005_c_().length() == 0) {
            return;
        }
        if (!(event.entityIn instanceof EntityEnderCrystal) || CrystalChams.mc.field_71439_g.func_70032_d(event.entityIn) > (float)((Integer)this.range.getValue()).intValue()) {
            return;
        }
        if (!((Boolean)this.show.getValue()).booleanValue()) {
            event.cancel();
        }
        this.prepare();
        float spinTicks = (float)((EntityEnderCrystal)event.entityIn).field_70261_a + Minecraft.func_71410_x().func_184121_ak();
        float floatTicks = MathHelper.func_76126_a((float)(spinTicks * 0.2f * ((Double)this.floatSpeed.getValue()).floatValue())) / 2.0f + 0.5f;
        float spinSpeed = ((Double)this.spinSpeed.getValue()).floatValue();
        float scale = 0.0625f;
        float swingAmount = spinTicks * 3.0f * spinSpeed;
        floatTicks = floatTicks * floatTicks + floatTicks;
        floatTicks *= 0.2f;
        GlStateManager.func_187441_d((float)this.getInterpolatedLinWid(CrystalChams.mc.field_71439_g.func_70032_d(event.entityIn) + 1.0f, ((Double)this.lineWidth.getValue()).floatValue(), ((Double)this.lineWidthInterp.getValue()).floatValue()));
        GL11.glDisable((int)3553);
        if (((String)this.mode.getValue()).equals("Gradient")) {
            GL11.glPushAttrib((int)1048575);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)3553);
            float alpha = (float)this.color.getValue().getAlpha() / 255.0f;
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
            event.modelBase.func_78088_a(event.entityIn, 0.0f, swingAmount, floatTicks, 0.0f, 0.0f, scale);
            GL11.glEnable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            float f = (float)event.entityIn.field_70173_aa + Minecraft.func_71410_x().func_184121_ak();
            mc.func_110434_K().func_110577_a(new ResourceLocation("textures/rainbow.png"));
            Minecraft.func_71410_x().field_71460_t.func_191514_d(true);
            GlStateManager.func_179147_l();
            GlStateManager.func_179143_c((int)514);
            GlStateManager.func_179132_a((boolean)false);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.func_179140_f();
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
                GlStateManager.func_179128_n((int)5890);
                GlStateManager.func_179096_D();
                GlStateManager.func_179114_b((float)(30.0f - (float)i * 60.0f), (float)0.0f, (float)0.0f, (float)0.5f);
                GlStateManager.func_179109_b((float)0.0f, (float)(f * (0.001f + (float)i * 0.003f) * 20.0f), (float)0.0f);
                GlStateManager.func_179128_n((int)5888);
                event.modelBase.func_78088_a(event.entityIn, 0.0f, swingAmount, floatTicks, 0.0f, 0.0f, scale);
            }
            GlStateManager.func_179128_n((int)5890);
            GlStateManager.func_179096_D();
            GlStateManager.func_179128_n((int)5888);
            GlStateManager.func_179145_e();
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179143_c((int)515);
            GlStateManager.func_179084_k();
            CrystalChams.mc.field_71460_t.func_191514_d(false);
            GL11.glPopAttrib();
        } else {
            if (((Boolean)this.wireframe.getValue()).booleanValue()) {
                GSColor wireColor = this.wireFrameColor.getValue();
                GL11.glPushAttrib((int)1048575);
                GL11.glEnable((int)3042);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glPolygonMode((int)1032, (int)6913);
                if (((Boolean)this.wireWalls.getValue()).booleanValue()) {
                    GL11.glDepthMask((boolean)false);
                    GL11.glDisable((int)2929);
                }
                GL11.glColor4f((float)((float)wireColor.getRed() / 255.0f), (float)((float)wireColor.getGreen() / 255.0f), (float)((float)wireColor.getBlue() / 255.0f), (float)((float)wireColor.getAlpha() / 255.0f));
                event.modelBase.func_78088_a(event.entityIn, 0.0f, swingAmount, floatTicks, 0.0f, 0.0f, scale);
                GL11.glPopAttrib();
            }
            if (((Boolean)this.chams.getValue()).booleanValue()) {
                GSColor chamsColor = this.color.getValue();
                GL11.glPushAttrib((int)1048575);
                GL11.glEnable((int)3042);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glDisable((int)3008);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glEnable((int)2960);
                GL11.glEnable((int)10754);
                if (((Boolean)this.throughWalls.getValue()).booleanValue()) {
                    GL11.glDepthMask((boolean)false);
                    GL11.glDisable((int)2929);
                }
                GL11.glColor4f((float)((float)chamsColor.getRed() / 255.0f), (float)((float)chamsColor.getGreen() / 255.0f), (float)((float)chamsColor.getBlue() / 255.0f), (float)((float)chamsColor.getAlpha() / 255.0f));
                event.modelBase.func_78088_a(event.entityIn, 0.0f, swingAmount, floatTicks, 0.0f, 0.0f, scale);
                GL11.glPopAttrib();
            }
        }
        event.limbSwing = 0.0f;
        event.limbSwingAmount = swingAmount;
        event.ageInTicks = floatTicks;
        event.netHeadYaw = 0.0f;
        event.headPitch = 0.0f;
        event.scale = scale;
        this.release();
    }, new Predicate[0]);

    void prepare() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179118_c();
        GlStateManager.func_179147_l();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
    }

    void release() {
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        GlStateManager.func_179141_d();
        GlStateManager.func_179121_F();
        GL11.glEnable((int)3553);
        GL11.glPolygonMode((int)1032, (int)6914);
        new GSColor(255, 255, 255, 255).glColor();
    }

    float getInterpolatedLinWid(float distance, float line, float lineFactor) {
        return line * lineFactor / distance;
    }
}
