/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.ContainerShulkerBox
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$PooledMutableBlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="ArmorRegear", category=Category.Combat)
public class ArmorRegear
extends Module {
    IntegerSetting delay = this.registerInteger("Delay", 200, 0, 1000);
    BooleanSetting syncInventory = this.registerBoolean("Sync Inventory", false);
    IntegerSetting helmet = this.registerInteger("Helmet", 6, 1, 9);
    IntegerSetting chest = this.registerInteger("Chest", 7, 1, 9);
    IntegerSetting leg = this.registerInteger("Legs", 8, 1, 9);
    IntegerSetting boots = this.registerInteger("Boots", 9, 1, 9);
    DoubleSetting range = this.registerDouble("Range", 5.0, 0.0, 10.0);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", false);
    BooleanSetting packetPlace = this.registerBoolean("Packet Place", true);
    DoubleSetting targetRange = this.registerDouble("Target Range", 8.0, 0.0, 16.0);
    BooleanSetting placeSwing = this.registerBoolean("Place Swing", true);
    BooleanSetting openShulker = this.registerBoolean("OpenShulker onDisable", true);
    boolean placed;
    boolean tookArmor;
    boolean droppedArmor;
    ItemStack shulker;
    ShulkerPos pos;
    Timer disableTimer = new Timer();
    boolean gotHelmet;
    boolean gotChest;
    boolean gotLeg;
    boolean gotBoots;
    Timer timer = new Timer();
    public static final List<?> airBlocks = Arrays.asList(Blocks.field_150350_a, Blocks.field_150353_l, Blocks.field_150356_k, Blocks.field_150355_j, Blocks.field_150358_i, Blocks.field_150480_ab, Blocks.field_150395_bd, Blocks.field_150431_aC, Blocks.field_150329_H);
    public static final ItemStack ILLEGAL_STACK = new ItemStack(Item.func_150898_a((Block)Blocks.field_150357_h));

    private int getSlot(ItemStack itemStack) {
        NonNullList contentItems = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
        ItemStackHelper.func_191283_b((NBTTagCompound)itemStack.func_77978_p().func_74775_l("BlockEntityTag"), (NonNullList)contentItems);
        for (int i = 0; i < contentItems.size(); ++i) {
            if (!(((ItemStack)contentItems.get(i)).func_77973_b() instanceof ItemArmor) || ((ItemStack)contentItems.get(i)).func_190916_E() != 127) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void onEnable() {
        this.pos = null;
        this.disableTimer.reset();
        this.droppedArmor = false;
        this.tookArmor = false;
        this.placed = false;
        this.gotBoots = false;
        this.gotLeg = false;
        this.gotChest = false;
        this.gotHelmet = false;
        this.shulker = null;
        int shulkerSlot = -1;
        for (int slot = 0; slot < 9; ++slot) {
            ItemStack itemStack = ArmorRegear.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (!(itemStack.func_77973_b() instanceof ItemBlock) || !(((ItemBlock)itemStack.func_77973_b()).func_179223_d() instanceof BlockShulkerBox) || this.getSlot(itemStack) == -1) continue;
            shulkerSlot = slot;
            this.shulker = ArmorRegear.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            break;
        }
        if (shulkerSlot == -1) {
            this.disable();
            return;
        }
        this.pos = this.initValues();
        if (this.pos == null) {
            this.disable();
            return;
        }
        ArmorRegear.runCheck(shulkerSlot, (Boolean)this.packetSwitch.getValue(), () -> {
            ArmorRegear.rightClickBlock(this.pos.neighbour, this.pos.vec, EnumHand.MAIN_HAND, this.pos.opposite, (Boolean)this.packetPlace.getValue());
            if (((Boolean)this.placeSwing.getValue()).booleanValue()) {
                ArmorRegear.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            }
        });
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (pos == null || vec == null || hand == null || direction == null) {
            return;
        }
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            ArmorRegear.mc.field_71442_b.func_187099_a(ArmorRegear.mc.field_71439_g, ArmorRegear.mc.field_71441_e, pos, direction, vec, hand);
        }
    }

    public static Block getBlock(BlockPos pos) {
        return ArmorRegear.getState(pos).func_177230_c();
    }

    public static IBlockState getState(BlockPos pos) {
        return ArmorRegear.mc.field_71441_e.func_180495_p(pos);
    }

    @Override
    public void onTick() {
        if (ArmorRegear.mc.field_71441_e == null || ArmorRegear.mc.field_71439_g == null || ArmorRegear.mc.field_71439_g.field_70128_L || this.pos == null) {
            this.disable();
            return;
        }
        if (this.disableTimer.passedMs(1000 + (Integer)this.delay.getValue())) {
            this.disable();
            return;
        }
        if (!this.placed) {
            this.placed = ArmorRegear.getBlock(this.pos.pos) instanceof BlockShulkerBox;
            if (this.placed) {
                this.openBlock();
            }
            return;
        }
        if (ArmorRegear.mc.field_71439_g.field_71070_bA instanceof ContainerShulkerBox) {
            Container container = ArmorRegear.mc.field_71439_g.field_71070_bA;
            for (int i = 0; i < 27; ++i) {
                ItemStack stack;
                Slot slot = container.func_75139_a(i);
                if (!slot.func_75216_d() || !((stack = slot.func_75211_c()).func_77973_b() instanceof ItemArmor)) continue;
                int hotbarSlot = -1;
                ItemArmor armor = (ItemArmor)stack.func_77973_b();
                switch (armor.field_77881_a) {
                    case HEAD: {
                        if (!this.gotHelmet) {
                            hotbarSlot = (Integer)this.helmet.getValue() - 1;
                        }
                        this.gotHelmet = true;
                        break;
                    }
                    case CHEST: {
                        if (!this.gotChest) {
                            hotbarSlot = (Integer)this.chest.getValue() - 1;
                        }
                        this.gotChest = true;
                        break;
                    }
                    case LEGS: {
                        if (!this.gotLeg) {
                            hotbarSlot = (Integer)this.leg.getValue() - 1;
                        }
                        this.gotLeg = true;
                        break;
                    }
                    case FEET: {
                        if (!this.gotBoots) {
                            hotbarSlot = (Integer)this.boots.getValue() - 1;
                        }
                        this.gotBoots = true;
                    }
                }
                if (hotbarSlot == -1) continue;
                ArmorRegear.mc.field_71442_b.func_187098_a(container.field_75152_c, i, hotbarSlot, ClickType.SWAP, (EntityPlayer)ArmorRegear.mc.field_71439_g);
            }
            if (this.gotHelmet && this.gotChest && this.gotLeg && this.gotBoots) {
                this.tookArmor = true;
            }
            this.timer.reset();
            ArmorRegear.mc.field_71439_g.func_71053_j();
            if (((Boolean)this.syncInventory.getValue()).booleanValue()) {
                ArmorRegear.illegalSync();
            }
        } else if (this.tookArmor && this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            this.gotHelmet = this.isArmor((Integer)this.helmet.getValue());
            if (!(this.gotHelmet && (this.gotChest = this.isArmor((Integer)this.chest.getValue())) && (this.gotLeg = this.isArmor((Integer)this.leg.getValue())) && (this.gotBoots = this.isArmor((Integer)this.boots.getValue())))) {
                this.disable();
                return;
            }
            for (int i = 5; i <= 8; ++i) {
                ItemStack armorStack = ArmorRegear.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
                if (armorStack.func_190926_b()) continue;
                ArmorRegear.mc.field_71442_b.func_187098_a(0, i, 1, ClickType.THROW, (EntityPlayer)ArmorRegear.mc.field_71439_g);
            }
            int previousSlot = ArmorRegear.mc.field_71439_g.field_71071_by.field_70461_c;
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = ArmorRegear.mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (stack.func_190926_b() || !(stack.func_77973_b() instanceof ItemArmor)) continue;
                ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(i));
                ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
            ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(previousSlot));
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (ArmorRegear.mc.field_71441_e != null && ArmorRegear.mc.field_71439_g != null && !ArmorRegear.mc.field_71439_g.field_70128_L && ((Boolean)this.openShulker.getValue()).booleanValue() && this.pos != null && ArmorRegear.getBlock(this.pos.pos) instanceof BlockShulkerBox) {
            this.openBlock();
        }
    }

    private boolean isArmor(int slot) {
        ItemStack stack = ArmorRegear.mc.field_71439_g.field_71071_by.func_70301_a(slot - 1);
        return !stack.func_190926_b() && stack.func_77973_b() instanceof ItemArmor;
    }

    private void openBlock() {
        EnumFacing side = EnumFacing.func_190914_a((BlockPos)this.pos.pos, (EntityLivingBase)ArmorRegear.mc.field_71439_g);
        BlockPos neighbour = this.pos.pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)ArmorRegear.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        ArmorRegear.mc.field_71442_b.func_187099_a(ArmorRegear.mc.field_71439_g, ArmorRegear.mc.field_71441_e, this.pos.pos, opposite, hitVec, EnumHand.MAIN_HAND);
    }

    public static List<BlockPos> getSphere(double r, boolean hollow) {
        ArrayList<BlockPos> sphereBlocks = new ArrayList<BlockPos>();
        double px = ArmorRegear.mc.field_71439_g.field_70165_t;
        double py = ArmorRegear.mc.field_71439_g.field_70163_u + 1.7;
        double pz = ArmorRegear.mc.field_71439_g.field_70161_v;
        double centerX = px - (px > 0.0 ? 0.5 : (px < 0.0 ? -0.5 : 0.0));
        double centerZ = pz - (pz > 0.0 ? 0.5 : (pz < 0.0 ? -0.5 : 0.0));
        double rSq = r * r;
        double innerRSq = (r - 1.0) * (r - 1.0);
        BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.func_185346_s();
        int x = (int)Math.floor(centerX - (r - 0.5));
        while ((double)x <= centerX + r) {
            int z = (int)Math.floor(centerZ - (r - 0.5));
            while ((double)z <= centerZ + r) {
                int y = (int)Math.floor(py - r);
                while ((double)y < py + r) {
                    double dx = px - (double)x;
                    double dy = py - (double)y;
                    double dz = pz - (double)z;
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (!(!(distSq <= rSq) || hollow && distSq <= innerRSq || y < 0 || y > 255)) {
                        pos.func_181079_c(x, y, z);
                        sphereBlocks.add(pos.func_185334_h());
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        pos.func_185344_t();
        return sphereBlocks;
    }

    public static boolean intersectsWithEntity(BlockPos pos) {
        AxisAlignedBB box = new AxisAlignedBB(pos);
        for (Entity entity : ArmorRegear.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || !ArmorRegear.isIntersect(box, entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public static boolean isIntersect(AxisAlignedBB a, AxisAlignedBB b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.field_72336_d <= b.field_72340_a || a.field_72340_a >= b.field_72336_d) {
            return false;
        }
        if (a.field_72337_e <= b.field_72338_b || a.field_72338_b >= b.field_72337_e) {
            return false;
        }
        return !(a.field_72334_f <= b.field_72339_c) && !(a.field_72339_c >= b.field_72334_f);
    }

    private ShulkerPos initValues() {
        List<Object> blocks = ArmorRegear.getSphere((Double)this.range.getValue(), false);
        blocks.removeIf(ArmorRegear::intersectsWithEntity);
        EntityPlayer target = ArmorRegear.getNearestPlayer(12.0);
        List filter = blocks.stream().filter(p -> target == null || target.func_174818_b(p) > (Double)this.targetRange.getValue()).collect(Collectors.toList());
        if (!filter.isEmpty()) {
            blocks = filter;
        }
        ArrayList posList = new ArrayList();
        blocks.forEach(pos -> {
            EnumFacing facing = this.getFacing((BlockPos)pos);
            if (facing == null) {
                return;
            }
            BlockPos neighbour = pos.func_177972_a(facing);
            EnumFacing opposite = facing.func_176734_d();
            Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
            posList.add(new ShulkerPos((BlockPos)pos, neighbour, opposite, hitVec));
        });
        if (target == null) {
            return posList.stream().min(Comparator.comparing(p -> p.getRange((EntityPlayer)ArmorRegear.mc.field_71439_g))).orElse(null);
        }
        return posList.stream().max(Comparator.comparing(p -> this.getWeight((ShulkerPos)p, target))).orElse(null);
    }

    private double getWeight(ShulkerPos pos, EntityPlayer target) {
        double range = pos.getRange(target);
        if (range >= (Double)this.targetRange.getValue()) {
            int y = 256 - pos.pos.func_177956_o();
            range += ((double)y - ArmorRegear.mc.field_71439_g.field_70163_u) * 0.5;
        }
        return range * 1.5;
    }

    public static boolean canBeClicked(BlockPos pos) {
        return ArmorRegear.getBlock(pos).func_176209_a(ArmorRegear.getState(pos), false);
    }

    public static boolean canReplace(BlockPos pos) {
        if (pos == null) {
            return false;
        }
        return ArmorRegear.getState(pos).func_185904_a().func_76222_j() || ArmorRegear.isAir(pos);
    }

    public static boolean isAir(BlockPos blockPos) {
        return ArmorRegear.isAir(ArmorRegear.mc.field_71441_e.func_180495_p(blockPos).func_177230_c());
    }

    public static boolean isAir(Block block) {
        return airBlocks.contains(block);
    }

    private EnumFacing getFacing(BlockPos pos) {
        if (ArmorRegear.intersectsWithEntity(pos) || !ArmorRegear.canReplace(pos)) {
            return null;
        }
        for (EnumFacing facing : EnumFacing.field_82609_l) {
            if (!ArmorRegear.canBeClicked(pos.func_177972_a(facing)) || !airBlocks.contains(ArmorRegear.mc.field_71441_e.func_180495_p(pos.func_177967_a(facing, -1)).func_177230_c())) continue;
            return facing;
        }
        return null;
    }

    public static void runCheck(int slot, boolean packetSwitch, Runnable runnable) {
        ArmorRegear.runCheck(slot, packetSwitch, true, runnable);
    }

    public static void runCheck(int slot, boolean packetSwitch, boolean switchBack, Runnable runnable) {
        if (slot < 0 || slot > 8) {
            return;
        }
        int oldSlot = ArmorRegear.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot == oldSlot) {
            runnable.run();
        } else {
            ArmorRegear.switchSlot(slot, packetSwitch);
            runnable.run();
            if (switchBack) {
                ArmorRegear.switchSlot(oldSlot, packetSwitch);
            }
        }
    }

    public static void switchSlot(int slot, boolean packet) {
        if (packet) {
            ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
        } else {
            ArmorRegear.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            ArmorRegear.mc.field_71442_b.func_78765_e();
        }
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAlive(Entity entity) {
        return ArmorRegear.isLiving(entity) && !entity.field_70128_L && ((EntityLivingBase)entity).func_110143_aJ() > 0.0f;
    }

    public static boolean isDead(Entity entity) {
        return !ArmorRegear.isAlive(entity);
    }

    public static boolean basicChecksEntity(EntityPlayer pl) {
        return pl == null || pl == ArmorRegear.mc.field_71439_g || SocialManager.isFriend(pl.func_70005_c_()) || ArmorRegear.isDead((Entity)pl) || pl.func_184812_l_();
    }

    public static EntityPlayer getNearestPlayer(double range) {
        double rangeSq = range * range;
        List playerList = ArmorRegear.mc.field_71441_e.field_73010_i.stream().filter(p -> !ArmorRegear.basicChecksEntity(p)).filter(p -> ArmorRegear.mc.field_71439_g.func_70068_e((Entity)p) <= rangeSq).collect(Collectors.toList());
        return playerList.stream().min(Comparator.comparing(arg_0 -> ((EntityPlayerSP)ArmorRegear.mc.field_71439_g).func_70032_d(arg_0))).orElse(null);
    }

    public static void click(int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn) {
        ArmorRegear.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(windowIdIn, slotIdIn, usedButtonIn, modeIn, clickedItemIn, actionNumberIn));
    }

    public static void illegalSync() {
        if (ArmorRegear.mc.field_71439_g != null) {
            ArmorRegear.click(0, 0, 0, ClickType.PICKUP, ILLEGAL_STACK, (short)0);
        }
    }

    static class ShulkerPos {
        BlockPos pos;
        Vec3d vec;
        BlockPos neighbour;
        EnumFacing opposite;

        public ShulkerPos(BlockPos pos, BlockPos neighbour, EnumFacing opposite, Vec3d vec3d) {
            this.pos = pos;
            this.neighbour = neighbour;
            this.opposite = opposite;
            this.vec = vec3d;
        }

        public double getRange(EntityPlayer player) {
            return player.func_70011_f((double)this.pos.field_177962_a + 0.5, (double)this.pos.field_177960_b + 0.5, (double)this.pos.field_177961_c + 0.5);
        }
    }

    private static class Timer {
        private long time = -1L;

        private Timer() {
        }

        public boolean passedMs(long ms) {
            return this.passedNS(this.convertToNS(ms));
        }

        public boolean passedNS(long ns) {
            return System.nanoTime() - this.time >= ns;
        }

        public void reset() {
            this.time = System.nanoTime();
        }

        public long convertToNS(long time) {
            return time * 1000000L;
        }
    }
}
