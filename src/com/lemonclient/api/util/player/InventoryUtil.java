/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockPressurePlate
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemSkull
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.potion.PotionUtils
 *  net.minecraft.util.NonNullList
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.player.Locks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static final ItemStack ILLEGAL_STACK = new ItemStack(Item.func_150898_a((Block)Blocks.field_150357_h));

    public static void run(int slot, boolean packetSwitch, Runnable runnable) {
        int oldslot = InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
        } else {
            if (packetSwitch) {
                InventoryUtil.packetSwitch(slot);
            } else {
                InventoryUtil.switchSlot(slot);
            }
            runnable.run();
            if (packetSwitch) {
                InventoryUtil.packetSwitch(oldslot);
            } else {
                InventoryUtil.switchSlot(oldslot);
            }
            InventoryUtil.mc.field_71439_g.field_71070_bA.func_75142_b();
        }
    }

    public static void switchSlot(int slot) {
        InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        InventoryUtil.mc.field_71442_b.func_78765_e();
    }

    public static void packetSwitch(int slot) {
        InventoryUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
    }

    public static void switchToBypass(int slot) {
        Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> {
            if (InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c != slot && slot > -1 && slot < 9) {
                int lastSlot = InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c;
                int targetSlot = InventoryUtil.hotbarToInventory(slot);
                int currentSlot = InventoryUtil.hotbarToInventory(lastSlot);
                InventoryUtil.mc.field_71442_b.func_187098_a(0, targetSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.field_71439_g);
                InventoryUtil.mc.field_71442_b.func_187098_a(0, currentSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.field_71439_g);
                InventoryUtil.mc.field_71442_b.func_187098_a(0, targetSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.field_71439_g);
            }
        });
    }

    public static void switchToBypassAlt(int slot) {
        Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> {
            if (InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c != slot && slot > -1 && slot < 9) {
                Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> InventoryUtil.mc.field_71442_b.func_187098_a(0, slot, InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)InventoryUtil.mc.field_71439_g));
            }
        });
    }

    public static void bypassSwitch(int slot) {
        if (slot >= 0) {
            InventoryUtil.mc.field_71442_b.func_187100_a(slot);
        }
    }

    public static int hotbarToInventory(int slot) {
        if (slot == -2) {
            return 45;
        }
        if (slot > -1 && slot < 9) {
            return 36 + slot;
        }
        return slot;
    }

    public static void swap(int InvSlot, int newSlot) {
        InventoryUtil.mc.field_71442_b.func_187098_a(0, InvSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.field_71439_g);
        InventoryUtil.mc.field_71442_b.func_187098_a(0, newSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.field_71439_g);
        InventoryUtil.mc.field_71442_b.func_187098_a(0, InvSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.field_71439_g);
        InventoryUtil.mc.field_71442_b.func_78765_e();
    }

    public static int getHotBarPressure(String mode) {
        for (int i = 0; i < 9; ++i) {
            if (!(mode.equals("Pressure") ? InventoryUtil.isPressure(InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i)) : InventoryUtil.isString(InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i)))) continue;
            return i;
        }
        return -1;
    }

    public static boolean isString(ItemStack stack) {
        if (stack == ItemStack.field_190927_a || stack.func_77973_b() instanceof ItemBlock) {
            return false;
        }
        return stack.func_77973_b() == Items.field_151007_F;
    }

    public static boolean isPressure(ItemStack stack) {
        if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock)) {
            return false;
        }
        return ((ItemBlock)stack.func_77973_b()).func_179223_d() instanceof BlockPressurePlate;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = 9; current <= 44; ++current) {
            fullInventorySlots.put(current, (ItemStack)InventoryUtil.mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
        }
        return fullInventorySlots;
    }

    public static boolean isBlock(Item item, Class clazz) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).func_179223_d();
            return clazz.isInstance(block);
        }
        return false;
    }

    public static void click(int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn) {
        InventoryUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(windowIdIn, slotIdIn, usedButtonIn, modeIn, clickedItemIn, actionNumberIn));
    }

    public static int findCrystalBlockSlot() {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (!(block.func_176194_O().func_177622_c().field_149782_v > 6.0f)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static void illegalSync() {
        if (InventoryUtil.mc.field_71439_g != null) {
            InventoryUtil.click(0, 0, 0, ClickType.PICKUP, ILLEGAL_STACK, (short)0);
        }
    }

    public static int findObsidianSlot(boolean offHandActived, boolean activeBefore) {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || !((block = ((ItemBlock)stack.func_77973_b()).func_179223_d()) instanceof BlockObsidian)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int findEChestSlot(boolean offHandActived, boolean activeBefore) {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || !((block = ((ItemBlock)stack.func_77973_b()).func_179223_d()) instanceof BlockEnderChest)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int findSkullSlot() {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemSkull)) continue;
            return i;
        }
        return slot;
    }

    public static int findTotemSlot(int lower, int upper) {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = lower; i <= upper; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || stack.func_77973_b() != Items.field_190929_cY) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int findFirstItemSlot(Class<? extends Item> itemToFind, int lower, int upper) {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = lower; i <= upper; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !itemToFind.isInstance(stack.func_77973_b()) || !itemToFind.isInstance(stack.func_77973_b())) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int findStackInventory(Item input, boolean withHotbar) {
        int i;
        int n = i = withHotbar ? 0 : 9;
        while (i < 36) {
            Item item = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)input) == Item.func_150891_b((Item)item)) {
                return i + (i < 9 ? 36 : 0);
            }
            ++i;
        }
        return -1;
    }

    public static int getItemSlot(Item input) {
        if (InventoryUtil.mc.field_71439_g == null) {
            return 0;
        }
        for (int i = 0; i < InventoryUtil.mc.field_71439_g.field_71069_bz.func_75138_a().size(); ++i) {
            ItemStack s;
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8 || (s = (ItemStack)InventoryUtil.mc.field_71439_g.field_71069_bz.func_75138_a().get(i)).func_190926_b() || s.func_77973_b() != input) continue;
            return i;
        }
        return -1;
    }

    public static int getItemInHotbar(Item p_Item) {
        for (int l_I = 0; l_I < 9; ++l_I) {
            ItemStack l_Stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(l_I);
            if (l_Stack == ItemStack.field_190927_a || l_Stack.func_77973_b() != p_Item) continue;
            return l_I;
        }
        return -1;
    }

    public static int getPotion(String potion) {
        for (int l_I = 0; l_I < 36; ++l_I) {
            ItemStack l_Stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(l_I);
            if (l_Stack == ItemStack.field_190927_a || l_Stack.func_77973_b() != Items.field_185155_bH || !Objects.requireNonNull(PotionUtils.func_185191_c((ItemStack)InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(l_I)).getRegistryName()).func_110623_a().contains(potion)) continue;
            return l_I;
        }
        return -1;
    }

    public static int findFirstBlockSlot(Class<? extends Block> blockToFind, int lower, int upper) {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = lower; i <= upper; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || !blockToFind.isInstance(((ItemBlock)stack.func_77973_b()).func_179223_d())) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static List<Integer> findAllItemSlots(Class<? extends Item> itemToFind) {
        ArrayList<Integer> slots = new ArrayList<Integer>();
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !itemToFind.isInstance(stack.func_77973_b())) continue;
            slots.add(i);
        }
        return slots;
    }

    public static List<Integer> findAllBlockSlots(Class<? extends Block> blockToFind) {
        ArrayList<Integer> slots = new ArrayList<Integer>();
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = (ItemStack)mainInventory.get(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || !blockToFind.isInstance(((ItemBlock)stack.func_77973_b()).func_179223_d())) continue;
            slots.add(i);
        }
        return slots;
    }

    public static int findToolForBlockState(IBlockState iBlockState, int lower, int upper) {
        int slot = -1;
        NonNullList mainInventory = InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a;
        double foundMaxSpeed = 0.0;
        for (int i = lower; i <= upper; ++i) {
            ItemStack itemStack = (ItemStack)mainInventory.get(i);
            if (itemStack == ItemStack.field_190927_a) continue;
            float breakSpeed = itemStack.func_150997_a(iBlockState);
            int efficiencySpeed = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)itemStack);
            if (!(breakSpeed > 1.0f) || !((double)(breakSpeed = (float)((double)breakSpeed + (efficiencySpeed > 0 ? Math.pow(efficiencySpeed, 2.0) + 1.0 : 0.0))) > foundMaxSpeed)) continue;
            foundMaxSpeed = breakSpeed;
            slot = i;
        }
        return slot;
    }

    public static int getEmptyCounts() {
        if (InventoryUtil.mc.field_71439_g == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i <= 35; ++i) {
            ItemStack stack = (ItemStack)InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a.get(i);
            if (stack != ItemStack.field_190927_a && stack.func_77973_b() != Items.field_190931_a) continue;
            ++count;
        }
        return count;
    }
}
