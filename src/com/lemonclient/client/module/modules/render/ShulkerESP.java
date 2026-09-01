/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="ShulkerESP", category=Category.Render)
public class ShulkerESP
extends Module {
    IntegerSetting range = this.registerInteger("Range", 24, 0, 256);
    ColorSetting color = this.registerColor("Color", new GSColor(255, 255, 255));
    IntegerSetting alpha = this.registerInteger("Alpha", 75, 0, 255);
    IntegerSetting outlineAlpha = this.registerInteger("Outline Alpha", 125, 0, 255);
    List<BlockPos> renderList = new ArrayList<BlockPos>();

    @Override
    public void onTick() {
        this.renderList = new ArrayList<BlockPos>();
        this.renderList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), ((Integer)this.range.getValue()).doubleValue(), ((Integer)this.range.getValue()).doubleValue(), false, false, 0);
        this.renderList.removeIf(p -> !(BlockUtil.getBlock(p) instanceof BlockShulkerBox) || ShulkerESP.mc.field_71439_g.func_70011_f((double)p.func_177958_n() + 0.5, (double)p.func_177956_o() + 0.5, (double)p.func_177952_p() + 0.5) > (double)((Integer)this.range.getValue()).intValue());
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        for (BlockPos pos : this.renderList) {
            RenderUtil.drawBox(new AxisAlignedBB(pos), false, 1.0, new GSColor(this.color.getValue(), (Integer)this.alpha.getValue()), 63);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(pos), 1.0, new GSColor(this.color.getValue(), (Integer)this.outlineAlpha.getValue()), (Integer)this.outlineAlpha.getValue());
        }
    }
}
