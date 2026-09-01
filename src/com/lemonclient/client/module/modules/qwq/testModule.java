/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.qwq;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="test Module", category=Category.qwq)
public class testModule
extends Module {
    BooleanSetting ewe = this.registerBoolean("Don't Use or AutoCrash", true);
    BlockPos pos;
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        CPacketClickWindow s;
        Packet pack = event.getPacket();
        if (pack instanceof CPacketClickWindow) {
            s = (CPacketClickWindow)pack;
            this.sendMessage("CPacketClickWindow\n - Acton Number: " + s.func_149547_f() + "\n - Window ID: " + s.func_149548_c() + "\n - Slot ID: " + s.func_149544_d() + "\n - Button: " + s.func_149543_e() + "\n - Item Name: " + s.func_149546_g().func_82833_r() + "\n - Click Type Name: " + s.func_186993_f().name());
        } else if (pack instanceof CPacketConfirmTeleport) {
            s = (CPacketConfirmTeleport)pack;
            this.sendMessage("CPacketConfirmTeleport\n - Tp id: " + s.func_186987_a());
        } else if (pack instanceof CPacketConfirmTransaction) {
            s = (CPacketConfirmTransaction)pack;
            this.sendMessage("CPacketConfirmTransaction\n - Id: " + s.func_149533_d());
        } else if (pack instanceof CPacketClientStatus) {
            s = (CPacketClientStatus)pack;
            this.sendMessage("CPacketClientStatus\n - Status Name: " + s.func_149435_c().name());
        } else if (pack instanceof CPacketPlayerTryUseItemOnBlock) {
            s = (CPacketPlayerTryUseItemOnBlock)pack;
            this.sendMessage("CPacketPlayerTryUseItemOnBlock\n - Pos: " + s.func_187023_a().field_177962_a + ", " + s.func_187023_a().field_177960_b + ", " + s.func_187023_a().field_177961_c + "\n - Side: " + s.func_187024_b() + "\n - HitVec: " + s.func_187026_d() + ", " + s.func_187025_e() + ", " + s.func_187020_f());
        } else if (pack instanceof CPacketPlayerTryUseItem) {
            s = (CPacketPlayerTryUseItem)pack;
            this.sendMessage("CPacketPlayerTryUseItem\n - Hand: " + s.func_187028_a().name());
        }
        if (pack instanceof CPacketHeldItemChange) {
            s = (CPacketHeldItemChange)pack;
            this.sendMessage("CPacketHeldItemChange\n - Slot: " + s.func_149614_c());
        } else if (pack instanceof CPacketEntityAction) {
            s = (CPacketEntityAction)pack;
            this.sendMessage("CPacketEntityAction\n - Action: " + s.func_180764_b().name() + "\n - Data: " + s.func_149512_e());
        } else if (pack instanceof CPacketPlayerDigging) {
            s = (CPacketPlayerDigging)pack;
            this.sendMessage("CPacketPlayerDigging\n - Action: " + s.func_180762_c().name());
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
    }

    @Override
    public void onUpdate() {
    }

    void sendMessage(String message) {
        MessageBus.sendClientRawMessage(message);
    }
}
