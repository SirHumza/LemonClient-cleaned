/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.CrystalUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

@Module.Declaration(name="CrystalHit", category=Category.Combat)
public class CrystalHit
extends Module {
    IntegerSetting range = this.registerInteger("Range", 4, 0, 10);
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 40);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting packetBreak = this.registerBoolean("Packet Break", false);
    BooleanSetting antiWeakness = this.registerBoolean("Anti Weakness", false);
    BooleanSetting weakBypass = this.registerBoolean("Bypass Switch", false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> (Boolean)this.weakBypass.getValue() == false);
    BooleanSetting silent = this.registerBoolean("Silent Switch", false, () -> (Boolean)this.weakBypass.getValue() == false);
    int delayTime = 0;

    @Override
    public void onUpdate() {
        EntityEnderCrystal crystal = CrystalHit.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(c -> Float.valueOf(CrystalHit.mc.field_71439_g.func_70032_d((Entity)c)))).orElse(null);
        if (crystal != null && CrystalHit.mc.field_71439_g.func_70032_d((Entity)crystal) <= (float)((Integer)this.range.getValue()).intValue() && this.delayTime++ >= (Integer)this.delay.getValue()) {
            CrystalUtil.breakCrystal((Entity)crystal, (Boolean)this.packetBreak.getValue(), (Boolean)this.swing.getValue(), (Boolean)this.packetSwitch.getValue(), (Boolean)this.silent.getValue(), (Boolean)this.antiWeakness.getValue(), (Boolean)this.weakBypass.getValue());
        }
    }
}
