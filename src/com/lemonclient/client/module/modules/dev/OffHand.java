/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemSkull
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

@Module.Declaration(name="Offhand", category=Category.Dev)
public class OffHand
extends Module {
    public static OffHand INSTANCE;
    public boolean autoCrystal;
    ModeSetting defaultItem = this.registerMode("Default", Arrays.asList("Totem", "Crystal", "Gapple", "Plates", "Obby", "EChest", "Pot", "Exp", "Bed"), "Totem");
    ModeSetting nonDefaultItem = this.registerMode("Non Default", Arrays.asList("Totem", "Crystal", "Gapple", "Obby", "EChest", "Pot", "Exp", "Plates", "String", "Skull", "Bed"), "Crystal");
    ModeSetting noPlayerItem = this.registerMode("No Player", Arrays.asList("Totem", "Crystal", "Gapple", "Plates", "Obby", "EChest", "Pot", "Exp", "Bed"), "Gapple");
    ModeSetting swordMode = this.registerMode("Sword Switch", Arrays.asList("Gapple", "Crystal", "Pot", "None"), "Gapple");
    ModeSetting gappleMode = this.registerMode("Gap Switch", Arrays.asList("Totem", "Gapple", "Crystal", "None"), "Crystal");
    ModeSetting pickaxeMode = this.registerMode("Pick Switch", Arrays.asList("Obsidian", "EChest", "Gapple", "Crystal", "None"), "Gapple");
    ModeSetting shiftPickaxeMode = this.registerMode("Shift Pick", Arrays.asList("Obsidian", "EChest", "Gapple", "Crystal", "None"), "Gapple");
    ModeSetting potionChoose = this.registerMode("Potion", Arrays.asList("first", "strength", "swiftness"), "first");
    IntegerSetting healthSwitch = this.registerInteger("Health Switch", 14, 0, 36);
    IntegerSetting swordHealth = this.registerInteger("Sword Health", 14, 0, 36);
    IntegerSetting tickDelay = this.registerInteger("Tick Delay", 0, 0, 20);
    IntegerSetting fallDistance = this.registerInteger("Fall Distance", 12, 0, 30);
    IntegerSetting maxSwitchPerSecond = this.registerInteger("Max Switch", 6, 2, 10);
    DoubleSetting biasDamage = this.registerDouble("Bias Damage", 1.0, 0.0, 3.0);
    DoubleSetting playerDistance = this.registerDouble("Player Distance", 0.0, 0.0, 30.0);
    BooleanSetting rightGap = this.registerBoolean("Right Click Gap", false);
    BooleanSetting shiftPot = this.registerBoolean("Shift Pot", false);
    BooleanSetting swordCheck = this.registerBoolean("Only Sword", true);
    BooleanSetting crystalGap = this.registerBoolean("Crystal Gap", false);
    BooleanSetting fallDistanceBol = this.registerBoolean("Fall Distance", true);
    BooleanSetting crystalCheck = this.registerBoolean("Crystal Check", false);
    IntegerSetting predict = this.registerInteger("Predict Tick", 1, 0, 20);
    BooleanSetting noHotBar = this.registerBoolean("No HotBar", false);
    BooleanSetting onlyHotBar = this.registerBoolean("Only HotBar", false);
    BooleanSetting antiWeakness = this.registerBoolean("AntiWeakness", false);
    BooleanSetting hotBarTotem = this.registerBoolean("Switch HotBar Totem", false);
    BooleanSetting refill = this.registerBoolean("ReFill", true, () -> (Boolean)this.hotBarTotem.getValue());
    BooleanSetting check = this.registerBoolean("Check", true, () -> (Boolean)this.hotBarTotem.getValue() != false && (Boolean)this.refill.getValue() != false);
    IntegerSetting totemSlot = this.registerInteger("Totem Slot", 1, 1, 9, () -> (Boolean)this.hotBarTotem.getValue() != false && (Boolean)this.refill.getValue() != false);
    ModeSetting HudMode = this.registerMode("Hud Mode", Arrays.asList("Totem", "Offhand"), "Offhand");
    BooleanSetting debug = this.registerBoolean("Debug Msg", false);
    String ItemName;
    String itemCheck = "";
    int prevSlot;
    int tickWaited;
    int counts;
    int totems;
    boolean returnBack;
    boolean stepChanging;
    boolean firstChange;
    Item item;
    private final ArrayList<Long> switchDone = new ArrayList();
    Map<String, Item> allowedItemsItem = new HashMap<String, Item>(){
        {
            this.put("Totem", Items.field_190929_cY);
            this.put("Crystal", Items.field_185158_cP);
            this.put("Gapple", Items.field_151153_ao);
            this.put("Pot", Items.field_151068_bn);
            this.put("Exp", Items.field_151062_by);
            this.put("Bed", Items.field_151104_aV);
            this.put("String", Items.field_151007_F);
        }
    };
    Map<String, Block> allowedItemsBlock = new HashMap<String, Block>(){
        {
            this.put("Plates", Blocks.field_150452_aw);
            this.put("EChest", Blocks.field_150477_bB);
            this.put("Skull", Blocks.field_150465_bP);
            this.put("Obby", Blocks.field_150343_Z);
        }
    };
    int nowSlot;
    @EventHandler
    private final Listener<PacketEvent.Send> postSendListener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.nowSlot = ((CPacketHeldItemChange)event.getPacket()).func_149614_c();
        }
    }, new Predicate[0]);

    public OffHand() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.autoCrystal = false;
        this.firstChange = true;
        this.returnBack = false;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
        if (OffHand.mc.field_71441_e == null || OffHand.mc.field_71439_g == null || OffHand.mc.field_71439_g.field_70128_L || OffHand.mc.field_71462_r instanceof GuiContainer && !(OffHand.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        if (((Boolean)this.hotBarTotem.getValue()).booleanValue() && ((Boolean)this.refill.getValue()).booleanValue()) {
            int i;
            boolean hasTotem = false;
            for (i = 0; i < 9; ++i) {
                if (OffHand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190929_cY) continue;
                hasTotem = true;
            }
            if (!hasTotem || !((Boolean)this.check.getValue()).booleanValue()) {
                for (i = 9; i < 36; ++i) {
                    if (OffHand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190929_cY) continue;
                    OffHand.mc.field_71442_b.func_187098_a(0, i, (Integer)this.totemSlot.getValue() - 1, ClickType.SWAP, (EntityPlayer)OffHand.mc.field_71439_g);
                    break;
                }
            }
        }
        if (this.stepChanging) {
            if (this.tickWaited++ >= (Integer)this.tickDelay.getValue()) {
                this.tickWaited = 0;
                this.stepChanging = false;
                OffHand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffHand.mc.field_71439_g);
                this.switchDone.add(System.currentTimeMillis());
            } else {
                return;
            }
        }
        this.totems = OffHand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (this.returnBack) {
            if (this.tickWaited++ >= (Integer)this.tickDelay.getValue()) {
                this.changeBack();
            } else {
                return;
            }
        }
        this.itemCheck = this.getItem(false);
        if (this.offHandSame(this.itemCheck)) {
            if (((Boolean)this.hotBarTotem.getValue()).booleanValue() && this.itemCheck.equals("Totem")) {
                this.itemCheck = this.getItem(this.switchItemTotemHot());
            }
            if (this.offHandSame(this.itemCheck)) {
                this.switchItemNormal(this.itemCheck);
            }
        }
        this.GetOffhand();
    }

    private void GetOffhand() {
        if (((String)this.HudMode.getValue()).equals("Offhand")) {
            this.item = OffHand.mc.field_71439_g.func_184592_cb().func_77973_b();
            int items = OffHand.mc.field_71439_g.func_184592_cb().func_190916_E();
            this.ItemName = OffHand.mc.field_71439_g.func_184592_cb().func_82833_r();
            this.counts = OffHand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == this.item).mapToInt(ItemStack::func_190916_E).sum() + items;
        }
    }

    private void changeBack() {
        if (this.prevSlot == -1 || !OffHand.mc.field_71439_g.field_71071_by.func_70301_a(this.prevSlot).func_190926_b()) {
            this.prevSlot = this.findEmptySlot();
        }
        if (this.prevSlot != -1) {
            OffHand.mc.field_71442_b.func_187098_a(0, this.prevSlot < 9 ? this.prevSlot + 36 : this.prevSlot, 0, ClickType.PICKUP, (EntityPlayer)OffHand.mc.field_71439_g);
        } else if (((Boolean)this.debug.getValue()).booleanValue()) {
            MessageBus.printDebug("Your inventory is full.", true);
        }
        this.returnBack = false;
        this.tickWaited = 0;
    }

    private boolean switchItemTotemHot() {
        int slot = InventoryUtil.findTotemSlot(0, 8);
        if (slot != -1) {
            if (this.nowSlot != slot) {
                OffHand.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                OffHand.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            }
            return true;
        }
        return false;
    }

    private void switchItemNormal(String itemCheck) {
        int t = this.getInventorySlot(itemCheck);
        if (t == -1) {
            return;
        }
        if (!itemCheck.equals("Totem") && this.canSwitch()) {
            return;
        }
        this.toOffHand(t);
    }

    private String getItem(boolean mainTotem) {
        Item mainHandItem;
        String itemCheck = "";
        boolean normalOffHand = true;
        if (!mainTotem && ((Boolean)this.fallDistanceBol.getValue() != false && OffHand.mc.field_71439_g.field_70143_R >= (float)((Integer)this.fallDistance.getValue()).intValue() && OffHand.mc.field_71439_g.field_70167_r != OffHand.mc.field_71439_g.field_70163_u && !OffHand.mc.field_71439_g.func_184613_cA() || ((Boolean)this.crystalCheck.getValue()).booleanValue() && this.crystalDamage())) {
            normalOffHand = false;
            itemCheck = "Totem";
        }
        if ((mainHandItem = OffHand.mc.field_71439_g.func_184614_ca().func_77973_b()) instanceof ItemSword) {
            boolean can = true;
            if (OffHand.mc.field_71474_y.field_74313_G.func_151470_d() && ((Boolean)this.swordCheck.getValue()).booleanValue()) {
                if (((Boolean)this.shiftPot.getValue()).booleanValue() && OffHand.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    can = false;
                    itemCheck = "Pot";
                    normalOffHand = false;
                } else if (((Boolean)this.rightGap.getValue()).booleanValue() && !((String)this.swordMode.getValue()).equals("Gapple")) {
                    can = false;
                    itemCheck = "Gapple";
                    normalOffHand = false;
                }
            }
            if (can) {
                switch ((String)this.swordMode.getValue()) {
                    case "Gapple": {
                        itemCheck = "Gapple";
                        normalOffHand = false;
                        break;
                    }
                    case "Crystal": {
                        itemCheck = "Crystal";
                        normalOffHand = false;
                        break;
                    }
                    case "Pot": {
                        itemCheck = "Pot";
                        normalOffHand = false;
                    }
                }
            }
        } else if (!((Boolean)this.swordCheck.getValue()).booleanValue()) {
            if (((Boolean)this.shiftPot.getValue()).booleanValue() && OffHand.mc.field_71474_y.field_74311_E.func_151470_d()) {
                itemCheck = "Pot";
                normalOffHand = false;
            } else if (((Boolean)this.rightGap.getValue()).booleanValue() && !((String)this.swordMode.getValue()).equals("Gapple")) {
                itemCheck = "Gapple";
                normalOffHand = false;
            }
        }
        if (mainHandItem == Items.field_151046_w) {
            if (!OffHand.mc.field_71474_y.field_74311_E.func_151470_d() || OffHand.mc.field_71474_y.field_74311_E.func_151470_d()) {
                switch ((String)this.pickaxeMode.getValue()) {
                    case "Obsidian": {
                        itemCheck = "Obby";
                        normalOffHand = false;
                        break;
                    }
                    case "EChest": {
                        itemCheck = "EChest";
                        normalOffHand = false;
                        break;
                    }
                    case "Gapple": {
                        itemCheck = "Gapple";
                        normalOffHand = false;
                        break;
                    }
                    case "Crystal": {
                        itemCheck = "Crystal";
                        normalOffHand = false;
                    }
                }
            }
            if (OffHand.mc.field_71474_y.field_74311_E.func_151470_d()) {
                switch ((String)this.shiftPickaxeMode.getValue()) {
                    case "Obsidian": {
                        itemCheck = "Obby";
                        normalOffHand = false;
                        break;
                    }
                    case "EChest": {
                        itemCheck = "EChest";
                        normalOffHand = false;
                        break;
                    }
                    case "Gapple": {
                        itemCheck = "Gapple";
                        normalOffHand = false;
                        break;
                    }
                    case "Crystal": {
                        itemCheck = "Crystal";
                        normalOffHand = false;
                    }
                }
            }
        }
        if (mainHandItem == Items.field_151153_ao) {
            switch ((String)this.gappleMode.getValue()) {
                case "Totem": {
                    itemCheck = "Totem";
                    normalOffHand = false;
                    break;
                }
                case "Gapple": {
                    itemCheck = "Gapple";
                    normalOffHand = false;
                    break;
                }
                case "Crystal": {
                    itemCheck = "Crystal";
                    normalOffHand = false;
                }
            }
        }
        if (((Boolean)this.crystalGap.getValue()).booleanValue() && mainHandItem == Items.field_185158_cP) {
            itemCheck = "Gapple";
            normalOffHand = false;
        }
        if (normalOffHand && ((Boolean)this.antiWeakness.getValue()).booleanValue() && OffHand.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
            normalOffHand = false;
            itemCheck = "Crystal";
        }
        if (this.autoCrystal) {
            itemCheck = "Crystal";
            normalOffHand = false;
        }
        if (normalOffHand && !this.nearPlayer()) {
            itemCheck = (String)this.noPlayerItem.getValue();
        }
        itemCheck = this.getItemToCheck(itemCheck, mainTotem);
        return itemCheck;
    }

    private boolean canSwitch() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < this.switchDone.size() && now - this.switchDone.get(i) > 1000L; ++i) {
            this.switchDone.remove(i);
        }
        if (this.switchDone.size() / 2 >= (Integer)this.maxSwitchPerSecond.getValue()) {
            return true;
        }
        this.switchDone.add(now);
        return false;
    }

    private boolean nearPlayer() {
        if (((Double)this.playerDistance.getValue()).intValue() == 0) {
            return true;
        }
        for (EntityPlayer pl : OffHand.mc.field_71441_e.field_73010_i) {
            if (pl == OffHand.mc.field_71439_g || !((double)OffHand.mc.field_71439_g.func_70032_d((Entity)pl) < (Double)this.playerDistance.getValue())) continue;
            return true;
        }
        return false;
    }

    private boolean crystalDamage() {
        PredictUtil.PredictSettings settings = new PredictUtil.PredictSettings((Integer)this.predict.getValue(), true, 39, 2, 2, 1, true, true, true, true, 2, 0.15);
        for (Entity t : OffHand.mc.field_71441_e.field_72996_f) {
            EntityPlayer player;
            if (!(t instanceof EntityEnderCrystal) || !(OffHand.mc.field_71439_g.func_70032_d(t) <= 12.0f) || !((double)DamageUtil.calculateCrystalDamage((EntityLivingBase)OffHand.mc.field_71439_g, (player = PredictUtil.predictPlayer((EntityLivingBase)OffHand.mc.field_71439_g, settings)).func_174791_d(), player.func_174813_aQ(), t.field_70165_t, t.field_70163_u, t.field_70161_v) * (Double)this.biasDamage.getValue() >= (double)EntityUtil.getHealth((Entity)OffHand.mc.field_71439_g)) && (!((double)DamageUtil.calculateCrystalDamage((EntityLivingBase)OffHand.mc.field_71439_g, player.func_174791_d(), player.func_174813_aQ(), t.field_70165_t, t.field_70163_u, t.field_70161_v) * (Double)this.biasDamage.getValue() >= (double)EntityUtil.getHealth((Entity)OffHand.mc.field_71439_g)) || this.totems <= 0)) continue;
            return true;
        }
        return false;
    }

    private int findEmptySlot() {
        for (int i = 35; i > -1; --i) {
            if (!OffHand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190926_b()) continue;
            return i;
        }
        return -1;
    }

    private boolean offHandSame(String itemCheck) {
        Item offHandItem = OffHand.mc.field_71439_g.func_184592_cb().func_77973_b();
        if (this.allowedItemsBlock.containsKey(itemCheck)) {
            Block item = this.allowedItemsBlock.get(itemCheck);
            if (offHandItem instanceof ItemBlock) {
                return ((ItemBlock)offHandItem).func_179223_d() != item;
            }
            if (offHandItem instanceof ItemSkull && item == Blocks.field_150465_bP) {
                return true;
            }
        } else {
            Item item = this.allowedItemsItem.get(itemCheck);
            return item != offHandItem;
        }
        return true;
    }

    private String getItemToCheck(String str, boolean mainTotem) {
        if (mainTotem) {
            return str.isEmpty() ? (String)this.nonDefaultItem.getValue() : str;
        }
        if (OffHand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) {
            return PlayerUtil.getHealth() > (float)((Integer)this.swordHealth.getValue()).intValue() ? (str.isEmpty() ? (String)this.nonDefaultItem.getValue() : str) : (String)this.defaultItem.getValue();
        }
        return PlayerUtil.getHealth() > (float)((Integer)this.healthSwitch.getValue()).intValue() ? (str.isEmpty() ? (String)this.nonDefaultItem.getValue() : str) : (String)this.defaultItem.getValue();
    }

    private int getInventorySlot(String itemName) {
        int res;
        Item item;
        boolean blockBool = false;
        if (this.allowedItemsItem.containsKey(itemName)) {
            item = this.allowedItemsItem.get(itemName);
        } else {
            item = this.allowedItemsBlock.get(itemName);
            blockBool = true;
        }
        if (!this.firstChange && this.prevSlot != -1 && (res = this.isCorrect(this.prevSlot, blockBool, item, itemName)) != -1) {
            return res;
        }
        for (int i = (Boolean)this.onlyHotBar.getValue() != false ? 8 : 35; i > ((Boolean)this.noHotBar.getValue() != false ? 9 : -1); --i) {
            res = this.isCorrect(i, blockBool, item, itemName);
            if (res == -1) continue;
            return res;
        }
        return -1;
    }

    private int isCorrect(int i, boolean blockBool, Object item, String itemName) {
        Item temp = OffHand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
        if (blockBool) {
            if (temp instanceof ItemBlock ? ((ItemBlock)temp).func_179223_d() == item : temp instanceof ItemSkull && item == Blocks.field_150465_bP) {
                return i;
            }
        } else if (item == temp) {
            if (itemName.equals("Pot") && !((String)this.potionChoose.getValue()).equalsIgnoreCase("first") && !OffHand.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77990_d.toString().split(":")[2].contains((CharSequence)this.potionChoose.getValue())) {
                return -1;
            }
            return i;
        }
        return -1;
    }

    private void toOffHand(int t) {
        if (!OffHand.mc.field_71439_g.func_184592_cb().func_190926_b()) {
            if (this.firstChange) {
                this.prevSlot = t;
            }
            this.returnBack = true;
            this.firstChange = !this.firstChange;
        } else {
            this.prevSlot = -1;
        }
        OffHand.mc.field_71442_b.func_187098_a(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, (EntityPlayer)OffHand.mc.field_71439_g);
        if ((Integer)this.tickDelay.getValue() == 0) {
            OffHand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffHand.mc.field_71439_g);
            this.switchDone.add(System.currentTimeMillis());
        } else {
            this.stepChanging = true;
        }
        this.tickWaited = 0;
    }

    @Override
    public String getHudInfo() {
        if (((String)this.HudMode.getValue()).equals("Totem")) {
            this.counts = this.totems;
            if (OffHand.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
                ++this.counts;
            }
            return "[" + ChatFormatting.WHITE + "Totem " + this.counts + ChatFormatting.GRAY + "]";
        }
        if (this.itemCheck.isEmpty()) {
            return "[" + ChatFormatting.WHITE + "None" + ChatFormatting.GRAY + "]";
        }
        return "[" + ChatFormatting.WHITE + this.itemCheck + " " + this.counts + ChatFormatting.GRAY + "]";
    }
}
