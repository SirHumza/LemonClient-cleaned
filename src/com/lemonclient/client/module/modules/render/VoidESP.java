/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import io.netty.util.internal.ConcurrentSet;
import java.util.Arrays;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="VoidESP", category=Category.Render)
public class VoidESP
extends Module {
    IntegerSetting renderDistance = this.registerInteger("Distance", 10, 1, 40);
    IntegerSetting activeYValue = this.registerInteger("Activate Y", 20, 0, 256);
    ModeSetting renderType = this.registerMode("Render", Arrays.asList("Outline", "Fill", "Both"), "Both");
    ModeSetting renderMode = this.registerMode("Mode", Arrays.asList("Box", "Flat"), "Flat");
    IntegerSetting width = this.registerInteger("Width", 1, 1, 10);
    ColorSetting color = this.registerColor("Color", new GSColor(255, 255, 0));
    private ConcurrentSet<BlockPos> voidHoles;

    @Override
    public void onUpdate() {
        if (VoidESP.mc.field_71439_g.field_71093_bK == 1) {
            return;
        }
        if (VoidESP.mc.field_71439_g.func_180425_c().func_177956_o() > (Integer)this.activeYValue.getValue()) {
            return;
        }
        if (this.voidHoles == null) {
            this.voidHoles = new ConcurrentSet();
        } else {
            this.voidHoles.clear();
        }
        List<BlockPos> blockPosList = BlockUtil.getCircle(VoidESP.getPlayerPos(), 0, ((Integer)this.renderDistance.getValue()).intValue(), false);
        for (BlockPos blockPos : blockPosList) {
            if (VoidESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150357_h) || this.isAnyBedrock(blockPos, Offsets.center)) continue;
            this.voidHoles.add((Object)blockPos);
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (VoidESP.mc.field_71439_g == null || this.voidHoles == null) {
            return;
        }
        if (VoidESP.mc.field_71439_g.func_180425_c().func_177956_o() > (Integer)this.activeYValue.getValue()) {
            return;
        }
        if (this.voidHoles.isEmpty()) {
            return;
        }
        this.voidHoles.forEach(blockPos -> {
            if (((String)this.renderMode.getValue()).equalsIgnoreCase("Box")) {
                this.drawBox((BlockPos)blockPos);
            } else {
                this.drawFlat((BlockPos)blockPos);
            }
            this.drawOutline((BlockPos)blockPos, (Integer)this.width.getValue());
        });
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(VoidESP.mc.field_71439_g.field_70165_t), Math.floor(VoidESP.mc.field_71439_g.field_70163_u), Math.floor(VoidESP.mc.field_71439_g.field_70161_v));
    }

    private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
        for (BlockPos pos : offset) {
            if (!VoidESP.mc.field_71441_e.func_180495_p(origin.func_177971_a((Vec3i)pos)).func_177230_c().equals(Blocks.field_150357_h)) continue;
            return true;
        }
        return false;
    }

    private void drawFlat(BlockPos blockPos) {
        if (((String)this.renderType.getValue()).equalsIgnoreCase("Fill") || ((String)this.renderType.getValue()).equalsIgnoreCase("Both")) {
            GSColor c = new GSColor(this.color.getValue(), 50);
            if (((String)this.renderMode.getValue()).equalsIgnoreCase("Flat")) {
                RenderUtil.drawBox(blockPos, 1.0, c, 1);
            }
        }
    }

    private void drawBox(BlockPos blockPos) {
        if (((String)this.renderType.getValue()).equalsIgnoreCase("Fill") || ((String)this.renderType.getValue()).equalsIgnoreCase("Both")) {
            GSColor c = new GSColor(this.color.getValue(), 50);
            RenderUtil.drawBox(blockPos, 1.0, c, 63);
        }
    }

    private void drawOutline(BlockPos blockPos, int width) {
        if (((String)this.renderType.getValue()).equalsIgnoreCase("Outline") || ((String)this.renderType.getValue()).equalsIgnoreCase("Both")) {
            if (((String)this.renderMode.getValue()).equalsIgnoreCase("Box")) {
                RenderUtil.drawBoundingBox(blockPos, 1.0, width, this.color.getValue());
            }
            if (((String)this.renderMode.getValue()).equalsIgnoreCase("Flat")) {
                RenderUtil.drawBoundingBoxWithSides(blockPos, width, this.color.getValue(), 1);
            }
        }
    }

    private static class Offsets {
        static final BlockPos[] center = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};

        private Offsets() {
        }
    }
}
