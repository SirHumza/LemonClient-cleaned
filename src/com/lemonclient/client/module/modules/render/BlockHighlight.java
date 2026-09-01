/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@Module.Declaration(name="BlockHighlight", category=Category.Render)
public class BlockHighlight
extends Module {
    ModeSetting renderLook = this.registerMode("Render", Arrays.asList("Block", "Side"), "Block");
    ModeSetting renderType = this.registerMode("Type", Arrays.asList("Outline", "Fill", "Both"), "Outline");
    IntegerSetting lineWidth = this.registerInteger("Width", 1, 1, 5);
    ColorSetting renderColor = this.registerColor("Color", new GSColor(255, 0, 0, 255));
    private int lookInt;

    @Override
    public void onWorldRender(RenderEvent event) {
        RayTraceResult rayTraceResult = BlockHighlight.mc.field_71476_x;
        if (rayTraceResult == null) {
            return;
        }
        EnumFacing enumFacing = BlockHighlight.mc.field_71476_x.field_178784_b;
        if (enumFacing == null) {
            return;
        }
        GSColor colorWithOpacity = new GSColor(this.renderColor.getValue(), 50);
        switch ((String)this.renderLook.getValue()) {
            case "Block": {
                this.lookInt = 0;
                break;
            }
            case "Side": {
                this.lookInt = 1;
            }
        }
        if (rayTraceResult.field_72313_a == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = rayTraceResult.func_178782_a();
            AxisAlignedBB axisAlignedBB = BlockHighlight.mc.field_71441_e.func_180495_p(blockPos).func_185918_c((World)BlockHighlight.mc.field_71441_e, blockPos);
            if (BlockHighlight.mc.field_71441_e.func_180495_p(blockPos).func_185904_a() != Material.field_151579_a) {
                switch ((String)this.renderType.getValue()) {
                    case "Outline": {
                        this.renderOutline(axisAlignedBB, (Integer)this.lineWidth.getValue(), this.renderColor.getValue(), enumFacing, this.lookInt);
                        break;
                    }
                    case "Fill": {
                        this.renderFill(axisAlignedBB, colorWithOpacity, enumFacing, this.lookInt);
                        break;
                    }
                    case "Both": {
                        this.renderOutline(axisAlignedBB, (Integer)this.lineWidth.getValue(), this.renderColor.getValue(), enumFacing, this.lookInt);
                        this.renderFill(axisAlignedBB, colorWithOpacity, enumFacing, this.lookInt);
                    }
                }
            }
        }
    }

    public void renderOutline(AxisAlignedBB axisAlignedBB, int width, GSColor color, EnumFacing enumFacing, int lookInt) {
        if (lookInt == 0) {
            RenderUtil.drawBoundingBox(axisAlignedBB, (double)width, color);
        } else if (lookInt == 1) {
            RenderUtil.drawBoundingBoxWithSides(axisAlignedBB, width, color, this.findRenderingSide(enumFacing));
        }
    }

    public void renderFill(AxisAlignedBB axisAlignedBB, GSColor color, EnumFacing enumFacing, int lookInt) {
        int facing = 0;
        if (lookInt == 0) {
            facing = 63;
        } else if (lookInt == 1) {
            facing = this.findRenderingSide(enumFacing);
        }
        RenderUtil.drawBox(axisAlignedBB, true, 1.0, color, facing);
    }

    private int findRenderingSide(EnumFacing enumFacing) {
        int facing = 0;
        if (enumFacing == EnumFacing.EAST) {
            facing = 32;
        } else if (enumFacing == EnumFacing.WEST) {
            facing = 16;
        } else if (enumFacing == EnumFacing.NORTH) {
            facing = 4;
        } else if (enumFacing == EnumFacing.SOUTH) {
            facing = 8;
        } else if (enumFacing == EnumFacing.UP) {
            facing = 2;
        } else if (enumFacing == EnumFacing.DOWN) {
            facing = 1;
        }
        return facing;
    }
}
