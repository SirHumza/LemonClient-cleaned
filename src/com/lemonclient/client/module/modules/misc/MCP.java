/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraftforge.fml.common.gameevent.InputEvent$MouseInputEvent
 *  org.lwjgl.input.Mouse
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

@Module.Declaration(name="MCP", category=Category.Misc)
public class MCP
extends Module {
    BooleanSetting clipRotate = this.registerBoolean("clipRotate", false);
    IntegerSetting pearlPitch = this.registerInteger("Pitch", 85, -90, 90, () -> (Boolean)this.clipRotate.getValue());
    BooleanSetting block = this.registerBoolean("nearBlock", true, () -> (Boolean)this.clipRotate.getValue());
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", false);
    BooleanSetting check = this.registerBoolean("Switch Check", false);
    @EventHandler
    private final Listener<InputEvent.MouseInputEvent> listener = new Listener<InputEvent.MouseInputEvent>(event -> {
        if (MCP.mc.field_71441_e == null || MCP.mc.field_71439_g == null || MCP.mc.field_71439_g.field_70128_L || MCP.mc.field_71439_g.field_71071_by == null) {
            return;
        }
        if (Mouse.getEventButton() == 2) {
            if (MCP.mc.field_71476_x.field_72313_a == RayTraceResult.Type.ENTITY) {
                return;
            }
            if (((Boolean)this.clipRotate.getValue()).booleanValue() && (!((Boolean)this.block.getValue()).booleanValue() || MCP.mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK)) {
                MCP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(MCP.mc.field_71439_g.field_70177_z, ((Integer)this.pearlPitch.getValue()).floatValue(), MCP.mc.field_71439_g.field_70122_E));
            }
            int pearlInvSlot = InventoryUtil.findFirstItemSlot(ItemEnderPearl.class, 0, 35);
            int pearlHotSlot = InventoryUtil.findFirstItemSlot(ItemEnderPearl.class, 0, 8);
            if (pearlInvSlot == -1 && pearlHotSlot == -1) {
                return;
            }
            int oldSlot = MCP.mc.field_71439_g.field_71071_by.field_70461_c;
            if (pearlHotSlot == -1) {
                ItemStack itemStack = MCP.mc.field_71439_g.field_71071_by.func_70301_a(pearlInvSlot);
                MCP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, pearlInvSlot, MCP.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, ItemStack.field_190927_a, MCP.mc.field_71439_g.field_71070_bA.func_75136_a(MCP.mc.field_71439_g.field_71071_by)));
                MCP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                MCP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, pearlInvSlot, MCP.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, itemStack, MCP.mc.field_71439_g.field_71070_bA.func_75136_a(MCP.mc.field_71439_g.field_71071_by)));
            } else {
                this.switchTo(pearlHotSlot);
                MCP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                this.switchTo(oldSlot);
            }
        }
    }, new Predicate[0]);

    private void switchTo(int slot) {
        if (!(slot <= -1 || slot >= 9 || ((Boolean)this.check.getValue()).booleanValue() && MCP.mc.field_71439_g.field_71071_by.field_70461_c == slot)) {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                MCP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                MCP.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
        }
    }
}
