/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.network.play.server.SPacketEntityMetadata
 */
package com.lemonclient.mixin.mixins;

import com.lemonclient.api.event.events.DeathEvent;
import com.lemonclient.api.util.player.Locks;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.TotemPopManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
    @Inject(method={"handleEntityMetadata"}, at={@At(value="RETURN")}, cancellable=true)
    private void handleEntityMetadataHook(SPacketEntityMetadata sPacketEntityMetadata, CallbackInfo callbackInfo) {
        Entity getEntityByID;
        if (Minecraft.func_71410_x().field_71441_e != null && (getEntityByID = Minecraft.func_71410_x().field_71441_e.func_73045_a(sPacketEntityMetadata.func_149375_d())) instanceof EntityPlayer) {
            EntityPlayer entityPlayer;
            EntityPlayer entityPlayer2 = (EntityPlayer)getEntityByID;
            if (entityPlayer.func_110143_aJ() <= 0.0f) {
                LemonClient.EVENT_BUS.post(new DeathEvent(entityPlayer2));
                if (TotemPopManager.INSTANCE.sendMsgs) {
                    TotemPopManager.INSTANCE.death(entityPlayer2);
                }
            }
        }
    }

    @Redirect(method={"handleHeldItemChange"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/player/InventoryPlayer;currentItem:I"))
    public void handleHeldItemChangeHook(InventoryPlayer inventoryPlayer, int value) {
        Locks.acquire(Locks.PLACE_SWITCH_LOCK, () -> {
            inventoryPlayer.field_70461_c = value;
        });
    }
}
