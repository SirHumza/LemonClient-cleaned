/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.MovementInputFromOptions
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.InputUpdateEvent
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.api.util.world.MotionUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Declaration(name="AutoSkull", category=Category.Combat)
public class AutoSkull
extends Module {
    BooleanSetting moving = this.registerBoolean("Moving", false);
    IntegerSetting delay = this.registerInteger("Delay", 50, 0, 1000);
    BooleanSetting packet = this.registerBoolean("Packet Place", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting onlyHoles = this.registerBoolean("Only Holes", false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting disableAfter = this.registerBoolean("Disable After", true);
    BooleanSetting disable = this.registerBoolean("Auto Disable", true);
    Timing timer = new Timing();
    double y;
    @EventHandler
    private final Listener<InputUpdateEvent> inputUpdateEventListener = new Listener<InputUpdateEvent>(event -> {
        if (!((Boolean)this.disable.getValue()).booleanValue()) {
            return;
        }
        if (event.getMovementInput() instanceof MovementInputFromOptions) {
            double posY;
            if (event.getMovementInput().field_78901_c) {
                this.disable();
            }
            if ((event.getMovementInput().field_187255_c || event.getMovementInput().field_187256_d || event.getMovementInput().field_187257_e || event.getMovementInput().field_187258_f) && (posY = AutoSkull.mc.field_71439_g.field_70163_u - this.y) * posY > 0.25) {
                this.disable();
            }
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        if (AutoSkull.mc.field_71441_e == null || AutoSkull.mc.field_71439_g == null || AutoSkull.mc.field_71439_g.field_70128_L) {
            this.disable();
            return;
        }
        this.y = AutoSkull.mc.field_71439_g.field_70163_u;
    }

    @Override
    public void fast() {
        if (AutoSkull.mc.field_71441_e == null || AutoSkull.mc.field_71439_g == null || AutoSkull.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (((Boolean)this.onlyHoles.getValue()).booleanValue() && !HoleUtil.isInHole((Entity)AutoSkull.mc.field_71439_g, true, true, false)) {
            return;
        }
        if (!((Boolean)this.moving.getValue()).booleanValue() && MotionUtil.isMoving((EntityLivingBase)AutoSkull.mc.field_71439_g)) {
            return;
        }
        int slot = InventoryUtil.findSkullSlot();
        if (slot == -1) {
            return;
        }
        BlockPos pos = PlayerUtil.getPlayerPos();
        if (BurrowUtil.getFirstFacing(pos) == null || !BlockUtil.isAir(pos)) {
            return;
        }
        if (this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            InventoryUtil.run(slot, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
            if (((Boolean)this.disableAfter.getValue()).booleanValue()) {
                this.disable();
            }
            this.timer.reset();
        }
    }
}
