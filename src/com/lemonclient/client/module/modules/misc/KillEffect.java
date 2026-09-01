/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

@Module.Declaration(name="KillEffect", category=Category.Misc)
public class KillEffect
extends Module {
    BooleanSetting thunder = this.registerBoolean("Thunder", true);
    IntegerSetting numbersThunder = this.registerInteger("Number Thunder", 1, 1, 10);
    BooleanSetting sound = this.registerBoolean("Sound", true);
    IntegerSetting numberSound = this.registerInteger("Number Sound", 1, 1, 10);
    ArrayList<EntityPlayer> playersDead = new ArrayList();

    @Override
    protected void onEnable() {
        this.playersDead.clear();
    }

    @Override
    public void onUpdate() {
        if (KillEffect.mc.field_71441_e == null) {
            this.playersDead.clear();
            return;
        }
        KillEffect.mc.field_71441_e.field_73010_i.forEach(entity -> {
            if (this.playersDead.contains(entity)) {
                if (entity.func_110143_aJ() > 0.0f) {
                    this.playersDead.remove(entity);
                }
            } else if (entity.func_110143_aJ() == 0.0f) {
                int i;
                if (((Boolean)this.thunder.getValue()).booleanValue()) {
                    for (i = 0; i < (Integer)this.numbersThunder.getValue(); ++i) {
                        KillEffect.mc.field_71441_e.func_72838_d((Entity)new EntityLightningBolt((World)KillEffect.mc.field_71441_e, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, true));
                    }
                }
                if (((Boolean)this.sound.getValue()).booleanValue()) {
                    for (i = 0; i < (Integer)this.numberSound.getValue(); ++i) {
                        KillEffect.mc.field_71439_g.func_184185_a(SoundEvents.field_187754_de, 0.5f, 1.0f);
                    }
                }
                this.playersDead.add((EntityPlayer)entity);
            }
        });
    }
}
