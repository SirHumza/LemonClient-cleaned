/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;

@Module.Declaration(name="NoKick", category=Category.Misc)
public class NoKick
extends Module {
    public BooleanSetting noPacketKick = this.registerBoolean("Packet", true);
    BooleanSetting noSlimeCrash = this.registerBoolean("Slime", false);
    BooleanSetting noOffhandCrash = this.registerBoolean("Offhand", false);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (((Boolean)this.noOffhandCrash.getValue()).booleanValue() && event.getPacket() instanceof SPacketSoundEffect && ((SPacketSoundEffect)event.getPacket()).func_186978_a() == SoundEvents.field_187719_p) {
            event.cancel();
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        if (NoKick.mc.field_71441_e != null && ((Boolean)this.noSlimeCrash.getValue()).booleanValue()) {
            NoKick.mc.field_71441_e.field_72996_f.forEach(entity -> {
                EntitySlime slime;
                if (entity instanceof EntitySlime && (slime = (EntitySlime)entity).func_70809_q() > 4) {
                    NoKick.mc.field_71441_e.func_72900_e(entity);
                }
            });
        }
    }
}
