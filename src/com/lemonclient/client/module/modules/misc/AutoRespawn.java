/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketClientStatus$State
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.GuiOpenEvent
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;

@Module.Declaration(name="AutoRespawn", category=Category.Misc)
public class AutoRespawn
extends Module {
    BooleanSetting respawn = this.registerBoolean("Respawn", false);
    BooleanSetting coords = this.registerBoolean("Death Coords", false);
    BooleanSetting respawnMessage = this.registerBoolean("Respawn Message", false);
    StringSetting message = this.registerString("Message", "/kit name");
    IntegerSetting respawnMessageDelay = this.registerInteger("Msg Delay(ms)", 0, 0, 5000);
    private boolean isDead;
    private boolean sentRespawnMessage = true;
    long timeSinceRespawn;
    BlockPos deathPos;
    @EventHandler
    private final Listener<GuiOpenEvent> livingDeathEventListener = new Listener<GuiOpenEvent>(event -> {
        if (!this.isEnabled()) {
            return;
        }
        if (event.getGui() instanceof GuiGameOver) {
            this.isDead = true;
            this.deathPos = PlayerUtil.getPlayerPos();
            this.sentRespawnMessage = true;
            if (((Boolean)this.respawn.getValue()).booleanValue()) {
                event.setCanceled(true);
                AutoRespawn.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
            }
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        if (AutoRespawn.mc.field_71439_g == null) {
            return;
        }
        if (this.isDead && AutoRespawn.mc.field_71439_g.func_70089_S()) {
            if (((Boolean)this.coords.getValue()).booleanValue()) {
                MessageBus.sendMessage("You died at X:" + this.deathPos.func_177958_n() + ", Y:" + this.deathPos.func_177956_o() + ", Z:" + this.deathPos.func_177952_p() + ".", false);
            }
            if (((Boolean)this.respawnMessage.getValue()).booleanValue()) {
                this.sentRespawnMessage = false;
                this.timeSinceRespawn = System.currentTimeMillis();
            }
            this.isDead = false;
        }
        if (!this.sentRespawnMessage && System.currentTimeMillis() - this.timeSinceRespawn > (long)((Integer)this.respawnMessageDelay.getValue()).intValue()) {
            AutoRespawn.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage(this.message.getText()));
            this.sentRespawnMessage = true;
        }
    }
}
