/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraftforge.fml.common.gameevent.InputEvent$MouseInputEvent
 *  org.lwjgl.input.Mouse
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

@Module.Declaration(name="MCF", category=Category.Misc)
public class MCF
extends Module {
    @EventHandler
    private final Listener<InputEvent.MouseInputEvent> listener = new Listener<InputEvent.MouseInputEvent>(event -> {
        if (MCF.mc.field_71441_e == null || MCF.mc.field_71439_g == null || MCF.mc.field_71439_g.field_70128_L || MCF.mc.field_71476_x == null) {
            return;
        }
        if (MCF.mc.field_71476_x.field_72313_a.equals((Object)RayTraceResult.Type.ENTITY) && MCF.mc.field_71476_x.field_72308_g instanceof EntityPlayer && Mouse.isButtonDown((int)2)) {
            if (SocialManager.isFriend(MCF.mc.field_71476_x.field_72308_g.func_70005_c_())) {
                SocialManager.delFriend(MCF.mc.field_71476_x.field_72308_g.func_70005_c_());
                MessageBus.sendClientPrefixMessage(ModuleManager.getModule(ColorMain.class).getDisabledColor() + "Removed " + MCF.mc.field_71476_x.field_72308_g.func_70005_c_() + " from friends list", Notification.Type.SUCCESS);
            } else {
                SocialManager.addFriend(MCF.mc.field_71476_x.field_72308_g.func_70005_c_());
                MessageBus.sendClientPrefixMessage(ModuleManager.getModule(ColorMain.class).getEnabledColor() + "Added " + MCF.mc.field_71476_x.field_72308_g.func_70005_c_() + " to friends list", Notification.Type.SUCCESS);
            }
        }
    }, new Predicate[0]);
}
