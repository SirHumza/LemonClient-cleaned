/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="FastFall", category=Category.Movement)
public class FastFall
extends Module {
    DoubleSetting dist = this.registerDouble("Min Distance", 3.0, 0.0, 25.0);
    DoubleSetting speed = this.registerDouble("Multiplier", 3.0, 0.0, 10.0);

    @Override
    public void onUpdate() {
        if (FastFall.mc.field_71441_e.func_175623_d(new BlockPos(FastFall.mc.field_71439_g.func_174791_d())) && FastFall.mc.field_71439_g.field_70122_E && (!FastFall.mc.field_71439_g.func_184613_cA() || (double)FastFall.mc.field_71439_g.field_70143_R < (Double)this.dist.getValue() || !FastFall.mc.field_71439_g.field_71075_bZ.field_75100_b)) {
            FastFall.mc.field_71439_g.field_70181_x -= ((Double)this.speed.getValue()).doubleValue();
        }
    }
}
