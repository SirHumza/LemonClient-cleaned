/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHandSide
 */
package com.lemonclient.mixin.mixins;

import com.lemonclient.api.event.events.TransformSideFirstPersonEvent;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.render.NoRender;
import com.lemonclient.client.module.modules.render.RenderTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public class MixinItemRenderer {
    @Final
    @Shadow
    public Minecraft field_78455_a;

    @Inject(method={"updateEquippedItem"}, at={@At(value="RETURN")})
    public void updateEquippedItem(CallbackInfo ci) {
        RenderTweaks renderTweaks = ModuleManager.getModule(RenderTweaks.class);
        if (renderTweaks.isEnabled() && ((Boolean)renderTweaks.noAnimation.getValue()).booleanValue()) {
            this.field_78455_a.field_71460_t.field_78516_c.field_187469_f = 1.0f;
            this.field_78455_a.field_71460_t.field_78516_c.field_187467_d = this.field_78455_a.field_71439_g.func_184614_ca();
            this.field_78455_a.field_71460_t.field_78516_c.field_187471_h = 1.0f;
            this.field_78455_a.field_71460_t.field_78516_c.field_187468_e = this.field_78455_a.field_71439_g.func_184592_cb();
        }
    }

    @Inject(method={"resetEquippedProgress"}, at={@At(value="HEAD")}, cancellable=true)
    public void resetEquippedProgress(CallbackInfo ci) {
        RenderTweaks renderTweaks = ModuleManager.getModule(RenderTweaks.class);
        if (renderTweaks.isEnabled() && ((Boolean)renderTweaks.noAnimation.getValue()).booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"transformSideFirstPerson"}, at={@At(value="HEAD")})
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        LemonClient.EVENT_BUS.post(event);
    }

    @Inject(method={"transformEatFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        LemonClient.EVENT_BUS.post(event);
        RenderTweaks renderTweaks = ModuleManager.getModule(RenderTweaks.class);
        if (renderTweaks.isEnabled() && ((Boolean)renderTweaks.noEat.getValue()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"transformFirstPerson"}, at={@At(value="HEAD")})
    public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        LemonClient.EVENT_BUS.post(event);
    }

    @Inject(method={"renderOverlays"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderOverlays(float partialTicks, CallbackInfo callbackInfo) {
        NoRender noRender = ModuleManager.getModule(NoRender.class);
        if (noRender.isEnabled() && ((Boolean)noRender.noOverlay.getValue()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}
