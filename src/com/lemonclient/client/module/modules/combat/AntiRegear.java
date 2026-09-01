/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="AntiRegear", category=Category.Combat)
public class AntiRegear
extends Module {
    public static AntiRegear INSTANCE;
    DoubleSetting reach = this.registerDouble("Range", 5.5, 0.0, 10.0);
    BooleanSetting packet = this.registerBoolean("Packet Break", false);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    List<BlockPos> selfPlaced = new ArrayList<BlockPos>();
    public boolean working;
    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        CPacketPlayerTryUseItemOnBlock packet;
        if (AntiRegear.mc.field_71441_e == null || AntiRegear.mc.field_71439_g == null || AntiRegear.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && AntiRegear.mc.field_71439_g.func_184586_b((packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).func_187022_c()).func_77973_b() instanceof ItemShulkerBox) {
            this.selfPlaced.add(packet.func_187023_a().func_177972_a(packet.func_187024_b()));
        }
    }, new Predicate[0]);

    public AntiRegear() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.working = false;
    }

    @Override
    public void fast() {
        if (AntiRegear.mc.field_71441_e == null || AntiRegear.mc.field_71439_g == null || AntiRegear.mc.field_71439_g.field_70128_L) {
            this.working = false;
            return;
        }
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        for (EntityPlayer target : PlayerUtil.getNearPlayers(16.0, 10)) {
            for (BlockPos pos2 : EntityUtil.getSphere(EntityUtil.getEntityPos((Entity)target), 6.5, 6.5, false, false, 0)) {
                if (this.selfPlaced.contains(pos2) || !(AntiRegear.mc.field_71441_e.func_180495_p(pos2).func_177230_c() instanceof BlockShulkerBox) || !(AntiRegear.mc.field_71439_g.func_70011_f((double)pos2.field_177962_a + 0.5, (double)pos2.field_177960_b + 0.5, (double)pos2.field_177961_c + 0.5) <= (Double)this.reach.getValue())) continue;
                sphere.add(pos2);
            }
        }
        this.working = !sphere.isEmpty();
        Iterator<Object> iterator = sphere.iterator();
        if (iterator.hasNext()) {
            BlockPos pos3 = (BlockPos)iterator.next();
            if (((Boolean)this.swing.getValue()).booleanValue()) {
                AntiRegear.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            }
            if (((Boolean)this.packet.getValue()).booleanValue()) {
                AntiRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos3, EnumFacing.UP));
                AntiRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos3, EnumFacing.UP));
            } else {
                AntiRegear.mc.field_71442_b.func_180512_c(pos3, EnumFacing.UP);
            }
        }
        this.selfPlaced.removeIf(pos -> !(AntiRegear.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockShulkerBox));
    }
}
