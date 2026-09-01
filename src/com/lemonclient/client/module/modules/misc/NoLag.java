/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.entity.passive.EntityParrot
 *  net.minecraft.entity.projectile.EntityWitherSkull
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketEffect
 *  net.minecraft.network.play.server.SPacketParticles
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnMob
 *  net.minecraft.util.SoundCategory
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.RenderEntityEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.util.SoundCategory;

@Module.Declaration(name="AntiLag", category=Category.Misc)
public class NoLag
extends Module {
    BooleanSetting particles = this.registerBoolean("Particles", true);
    BooleanSetting effect = this.registerBoolean("Effect", true);
    BooleanSetting soundEffect = this.registerBoolean("Sound Effect", true);
    BooleanSetting skulls = this.registerBoolean("Skull", true);
    BooleanSetting tnt = this.registerBoolean("Tnt", true);
    BooleanSetting parrots = this.registerBoolean("Parrot", true);
    BooleanSetting spawn = this.registerBoolean("Spawn", true);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketParticles && ((Boolean)this.particles.getValue()).booleanValue()) {
            event.cancel();
        }
        if (event.getPacket() instanceof SPacketEffect && ((Boolean)this.effect.getValue()).booleanValue()) {
            event.cancel();
        }
        if (event.getPacket() instanceof SPacketSoundEffect && ((Boolean)this.soundEffect.getValue()).booleanValue()) {
            packet = (SPacketSoundEffect)event.getPacket();
            if (packet.func_186977_b() == SoundCategory.PLAYERS && packet.func_186978_a() == SoundEvents.field_187719_p) {
                event.cancel();
            }
            if (packet.func_186977_b() == SoundCategory.WEATHER && packet.func_186978_a() == SoundEvents.field_187754_de) {
                event.cancel();
            }
        }
        if (event.getPacket() instanceof SPacketSpawnMob && ((Boolean)this.spawn.getValue()).booleanValue() && (packet = (SPacketSpawnMob)event.getPacket()).func_149025_e() == 55) {
            event.cancel();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<RenderEntityEvent> renderEntityEventListener = new Listener<RenderEntityEvent>(event -> {
        if (((Boolean)this.skulls.getValue()).booleanValue() && event.getEntity() instanceof EntityWitherSkull) {
            event.cancel();
        }
        if (((Boolean)this.tnt.getValue()).booleanValue() && event.getEntity() instanceof EntityTNTPrimed) {
            event.cancel();
        }
        if (((Boolean)this.parrots.getValue()).booleanValue() && event.getEntity() instanceof EntityParrot) {
            event.cancel();
        }
    }, new Predicate[0]);

    @Override
    public void onDisable() {
        NoLag.mc.field_71438_f.func_72712_a();
        super.onDisable();
    }
}
