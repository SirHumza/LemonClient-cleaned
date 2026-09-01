/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemElytra
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.InvStack;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.Locks;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AutoMend;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Module.Declaration(name="AutoArmor", category=Category.Combat)
public class AutoArmor
extends Module {
    IntegerSetting delay = this.registerInteger("Delay", 1, 1, 10);
    BooleanSetting noDesync = this.registerBoolean("No Desync", true);
    BooleanSetting illegalSync = this.registerBoolean("Illegal Sync", true);
    IntegerSetting checkDelay = this.registerInteger("Check Delay", 1, 0, 20, () -> (Boolean)this.noDesync.getValue());
    BooleanSetting strict = this.registerBoolean("Strict", false);
    BooleanSetting stackArmor = this.registerBoolean("Stack Armor", false);
    IntegerSetting slot = this.registerInteger("Swap Slot", 1, 1, 9, () -> (Boolean)this.stackArmor.getValue());
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> (Boolean)this.stackArmor.getValue());
    BooleanSetting armorSaver = this.registerBoolean("Armor Saver", false);
    BooleanSetting pauseWhenSafe = this.registerBoolean("Pause When Safe", false);
    IntegerSetting depletion = this.registerInteger("Depletion", 20, 0, 99, () -> (Boolean)this.armorSaver.getValue());
    BooleanSetting allowMend = this.registerBoolean("Allow Mend", false);
    IntegerSetting repair = this.registerInteger("Repair", 80, 0, 100);
    Timing rightClickTimer = new Timing();
    Timing timer = new Timing();
    private boolean sleep;
    @EventHandler
    private final Listener<PlayerInteractEvent.RightClickItem> listener = new Listener<PlayerInteractEvent.RightClickItem>(event -> {
        if (event.getEntityPlayer() != AutoArmor.mc.field_71439_g) {
            return;
        }
        if (event.getItemStack().func_77973_b() != Items.field_151062_by) {
            return;
        }
        this.rightClickTimer.reset();
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        boolean replaceFeet;
        List proximity;
        if (AutoArmor.mc.field_71441_e == null || AutoArmor.mc.field_71439_g == null || AutoArmor.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (AutoArmor.mc.field_71439_g.field_70173_aa % (Integer)this.delay.getValue() != 0 || this.checkDesync()) {
            return;
        }
        if (((Boolean)this.strict.getValue()).booleanValue() && (AutoArmor.mc.field_71439_g.field_70159_w != 0.0 || AutoArmor.mc.field_71439_g.field_70179_y != 0.0)) {
            return;
        }
        if (((Boolean)this.pauseWhenSafe.getValue()).booleanValue() && (proximity = AutoArmor.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityPlayer && !e.equals((Object)AutoArmor.mc.field_71439_g) && AutoArmor.mc.field_71439_g.func_70032_d(e) <= 6.0f || e instanceof EntityEnderCrystal && AutoArmor.mc.field_71439_g.func_70032_d(e) <= 12.0f).collect(Collectors.toList())).isEmpty()) {
            return;
        }
        boolean isMending = ModuleManager.isModuleEnabled(AutoMend.class);
        if (((Boolean)this.allowMend.getValue()).booleanValue() && !this.rightClickTimer.passedMs(500L)) {
            for (int i = 0; i < AutoArmor.mc.field_71439_g.field_71071_by.field_70460_b.size(); ++i) {
                ItemStack armorPiece = (ItemStack)AutoArmor.mc.field_71439_g.field_71071_by.field_70460_b.get(i);
                if (armorPiece.field_190928_g) {
                    return;
                }
                boolean mending = false;
                for (Map.Entry entry : EnchantmentHelper.func_82781_a((ItemStack)armorPiece).entrySet()) {
                    if (!((Enchantment)entry.getKey()).func_77320_a().contains("mending")) continue;
                    mending = true;
                    break;
                }
                if (!mending || armorPiece.func_190926_b()) continue;
                long freeSlots = AutoArmor.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(is -> is.func_190926_b() || is.func_77973_b() == Items.field_190931_a).map(is -> AutoArmor.mc.field_71439_g.field_71071_by.func_184429_b(is)).count();
                if (freeSlots <= 0L) {
                    return;
                }
                if (armorPiece.func_77952_i() == 0) continue;
                this.shiftClickSpot(8 - i);
                return;
            }
            return;
        }
        if (AutoArmor.mc.field_71462_r instanceof GuiContainer && !(AutoArmor.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        AtomicBoolean hasSwapped = new AtomicBoolean(false);
        if (this.sleep) {
            this.sleep = false;
            return;
        }
        HashSet<InvStack> replacements = new HashSet<InvStack>();
        for (int slot = 0; slot < 45; ++slot) {
            if (slot > 4 && slot < 9) continue;
            InvStack invStack2 = new InvStack(slot, AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c());
            if (!(invStack2.stack.func_77973_b() instanceof ItemArmor) && !(invStack2.stack.func_77973_b() instanceof ItemElytra)) continue;
            replacements.add(invStack2);
        }
        List armors = replacements.stream().filter(invStack -> invStack.stack.func_77973_b() instanceof ItemArmor).filter(invStack -> (Boolean)this.armorSaver.getValue() == false || invStack.stack.func_77973_b().getDurabilityForDisplay(invStack.stack) < (double)((Integer)this.depletion.getValue()).intValue()).sorted(Comparator.comparingInt(invStack -> invStack.slot)).sorted(Comparator.comparingInt(invStack -> ((ItemArmor)invStack.stack.func_77973_b()).field_77879_b)).collect(Collectors.toList());
        boolean wasEmpty = armors.isEmpty();
        if (wasEmpty) {
            armors = replacements.stream().filter(invStack -> invStack.stack.func_77973_b() instanceof ItemArmor).sorted(Comparator.comparingInt(invStack -> invStack.slot)).sorted(Comparator.comparingInt(invStack -> ((ItemArmor)invStack.stack.func_77973_b()).field_77879_b)).collect(Collectors.toList());
        }
        ItemStack currentHeadItem = AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(39);
        ItemStack currentChestItem = AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(38);
        ItemStack currentLegsItem = AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(37);
        ItemStack currentFeetItem = AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(36);
        boolean saveHead = !wasEmpty && currentHeadItem.func_190916_E() == 1 && (Boolean)this.armorSaver.getValue() != false && this.getItemDamage(5) <= (Integer)this.depletion.getValue();
        boolean saveChest = !wasEmpty && currentChestItem.func_190916_E() == 1 && (Boolean)this.armorSaver.getValue() != false && this.getItemDamage(6) <= (Integer)this.depletion.getValue();
        boolean saveLegs = !wasEmpty && currentLegsItem.func_190916_E() == 1 && (Boolean)this.armorSaver.getValue() != false && this.getItemDamage(7) <= (Integer)this.depletion.getValue();
        boolean saveFeet = !wasEmpty && currentFeetItem.func_190916_E() == 1 && (Boolean)this.armorSaver.getValue() != false && this.getItemDamage(8) <= (Integer)this.depletion.getValue();
        boolean replaceHead = currentHeadItem.field_190928_g || saveHead || isMending && this.getItemDamage(5) >= (Integer)this.repair.getValue();
        boolean replaceChest = currentChestItem.field_190928_g || saveChest || isMending && this.getItemDamage(6) >= (Integer)this.repair.getValue();
        boolean replaceLegs = currentLegsItem.field_190928_g || saveLegs || isMending && this.getItemDamage(7) >= (Integer)this.repair.getValue();
        boolean bl = replaceFeet = currentFeetItem.field_190928_g || saveFeet || isMending && this.getItemDamage(8) >= (Integer)this.repair.getValue();
        if (replaceHead && !hasSwapped.get()) {
            armors.stream().filter(invStack -> invStack.stack.func_77973_b() instanceof ItemArmor).filter(invStack -> ((ItemArmor)invStack.stack.func_77973_b()).field_77881_a.equals((Object)EntityEquipmentSlot.HEAD)).filter(invStack -> !saveHead || this.getItemDamage(invStack.slot) > (Integer)this.depletion.getValue()).filter(invStack -> !isMending || this.getItemDamage(invStack.slot) <= (Integer)this.repair.getValue()).findFirst().ifPresent(invStack -> {
                this.swapSlot(invStack.slot, 5);
                hasSwapped.set(true);
            });
        }
        if (replaceChest || currentChestItem.func_77973_b() instanceof ItemElytra && !hasSwapped.get()) {
            armors.stream().filter(invStack -> invStack.stack.func_77973_b() instanceof ItemArmor).filter(invStack -> ((ItemArmor)invStack.stack.func_77973_b()).field_77881_a.equals((Object)EntityEquipmentSlot.CHEST)).filter(invStack -> !saveChest || this.getItemDamage(invStack.slot) > (Integer)this.depletion.getValue()).filter(invStack -> !isMending || this.getItemDamage(invStack.slot) <= (Integer)this.repair.getValue()).findFirst().ifPresent(invStack -> {
                this.swapSlot(invStack.slot, 6);
                hasSwapped.set(true);
            });
        }
        if (replaceLegs && !hasSwapped.get()) {
            armors.stream().filter(invStack -> invStack.stack.func_77973_b() instanceof ItemArmor).filter(invStack -> ((ItemArmor)invStack.stack.func_77973_b()).field_77881_a.equals((Object)EntityEquipmentSlot.LEGS)).filter(invStack -> !saveLegs || this.getItemDamage(invStack.slot) > (Integer)this.depletion.getValue()).filter(invStack -> !isMending || this.getItemDamage(invStack.slot) <= (Integer)this.repair.getValue()).findFirst().ifPresent(invStack -> {
                this.swapSlot(invStack.slot, 7);
                hasSwapped.set(true);
            });
        }
        if (replaceFeet && !hasSwapped.get()) {
            armors.stream().filter(invStack -> invStack.stack.func_77973_b() instanceof ItemArmor).filter(invStack -> ((ItemArmor)invStack.stack.func_77973_b()).field_77881_a.equals((Object)EntityEquipmentSlot.FEET)).filter(invStack -> !saveFeet || this.getItemDamage(invStack.slot) > (Integer)this.depletion.getValue()).filter(invStack -> !isMending || this.getItemDamage(invStack.slot) <= (Integer)this.repair.getValue()).findFirst().ifPresent(invStack -> {
                this.swapSlot(invStack.slot, 8);
                hasSwapped.set(true);
            });
        }
    }

    private int getItemDamage(int slot) {
        ItemStack itemStack = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c();
        float green = ((float)itemStack.func_77958_k() - (float)itemStack.func_77952_i()) / (float)itemStack.func_77958_k();
        float red = 1.0f - green;
        return 100 - (int)(red * 100.0f);
    }

    private void swapSlot(int source, int target) {
        boolean stacked;
        ItemStack sourceStack = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(source).func_75211_c();
        boolean bl = stacked = sourceStack.func_190916_E() > 1;
        if (stacked) {
            this.swapStack(source, target);
        } else {
            this.swap(source, target);
        }
        this.sleep = true;
    }

    private void swapStack(int slotFrom, int slotTo) {
        if (!((Boolean)this.stackArmor.getValue()).booleanValue()) {
            return;
        }
        if (AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(slotTo).func_75211_c() != ItemStack.field_190927_a) {
            AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotTo, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.field_71439_g);
        }
        int slot = (Integer)this.slot.getValue() - 1;
        if (slotFrom < 36) {
            this.swapToHotbar(slotFrom);
        } else {
            slot = slotFrom - 36;
        }
        InventoryUtil.run(slot, (Boolean)this.packetSwitch.getValue(), () -> AutoArmor.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
        if (slotFrom < 36) {
            this.swapToHotbar(slotFrom);
        }
    }

    private boolean checkDesync() {
        if (((Boolean)this.noDesync.getValue()).booleanValue() && !(AutoArmor.mc.field_71462_r instanceof GuiContainer) || AutoArmor.mc.field_71462_r instanceof GuiInventory && this.timer.passedMs((Integer)this.checkDelay.getValue() * 50)) {
            int bestSlot = -1;
            int clientValue = 0;
            boolean foundType = false;
            int armorValue = AutoArmor.mc.field_71439_g.func_70658_aO();
            for (int i = 5; i < 9; ++i) {
                ItemStack stack = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (stack.func_190926_b() && !foundType) {
                    bestSlot = i;
                    foundType = true;
                    continue;
                }
                if (!(stack.func_77973_b() instanceof ItemArmor)) continue;
                ItemArmor itemArmor = (ItemArmor)stack.func_77973_b();
                clientValue += itemArmor.field_77879_b;
            }
            if (clientValue != armorValue && this.timer.passedMs((Integer)this.delay.getValue() * 50)) {
                if (((Boolean)this.illegalSync.getValue()).booleanValue()) {
                    InventoryUtil.illegalSync();
                } else if (bestSlot != -1 && AutoArmor.getSlot(AutoArmor.mc.field_71439_g.field_71071_by.func_70445_o()) == AutoArmor.fromSlot(bestSlot)) {
                    Item i = AutoArmor.get(bestSlot).func_77973_b();
                    AutoArmor.clickLocked(bestSlot, bestSlot, i, i);
                } else {
                    Item i = AutoArmor.get(20).func_77973_b();
                    AutoArmor.clickLocked(20, 20, i, i);
                }
                this.timer.reset();
                return true;
            }
        }
        return false;
    }

    public static void clickLocked(int slot, int to, Item inSlot, Item inTo) {
        Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> {
            if ((slot == -1 || AutoArmor.get(slot).func_77973_b() == inSlot) && AutoArmor.get(to).func_77973_b() == inTo) {
                boolean multi;
                boolean bl = multi = slot >= 0;
                if (multi) {
                    AutoArmor.click(slot);
                }
                AutoArmor.click(to);
            }
        });
    }

    public static void click(int slot) {
        AutoArmor.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.field_71439_g);
    }

    public static ItemStack get(int slot) {
        if (slot == -2) {
            return AutoArmor.mc.field_71439_g.field_71071_by.func_70445_o();
        }
        return (ItemStack)AutoArmor.mc.field_71439_g.field_71069_bz.func_75138_a().get(slot);
    }

    public static EntityEquipmentSlot fromSlot(int slot) {
        switch (slot) {
            case 5: {
                return EntityEquipmentSlot.HEAD;
            }
            case 6: {
                return EntityEquipmentSlot.CHEST;
            }
            case 7: {
                return EntityEquipmentSlot.LEGS;
            }
            case 8: {
                return EntityEquipmentSlot.FEET;
            }
        }
        ItemStack stack = AutoArmor.get(slot);
        return AutoArmor.getSlot(stack);
    }

    public static EntityEquipmentSlot getSlot(ItemStack stack) {
        if (!stack.func_190926_b()) {
            if (stack.func_77973_b() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor)stack.func_77973_b();
                return armor.func_185083_B_();
            }
            if (stack.func_77973_b() instanceof ItemElytra) {
                return EntityEquipmentSlot.CHEST;
            }
        }
        return null;
    }

    private void swapToHotbar(int InvSlot) {
        AutoArmor.mc.field_71442_b.func_187098_a(0, InvSlot, (Integer)this.slot.getValue() - 1, ClickType.SWAP, (EntityPlayer)AutoArmor.mc.field_71439_g);
        AutoArmor.mc.field_71442_b.func_78765_e();
    }

    private void swap(int slotFrom, int slotTo) {
        if (AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a((int)slotTo).func_75211_c().field_190928_g) {
            AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotFrom, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.field_71439_g);
        } else {
            boolean hasEmpty = false;
            for (int l_I = 0; l_I < 36; ++l_I) {
                ItemStack l_Stack = AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(l_I);
                if (!l_Stack.field_190928_g) continue;
                hasEmpty = true;
                break;
            }
            if (hasEmpty) {
                AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotTo, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.field_71439_g);
                AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotFrom, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.field_71439_g);
            } else {
                AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotFrom, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.field_71439_g);
                AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotTo, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.field_71439_g);
                AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, slotFrom, 0, ClickType.PICKUP, (EntityPlayer)AutoArmor.mc.field_71439_g);
            }
        }
        AutoArmor.mc.field_71442_b.func_78765_e();
    }

    private void shiftClickSpot(int source) {
        AutoArmor.mc.field_71442_b.func_187098_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, source, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.field_71439_g);
    }
}
