/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.render;

import com.google.common.collect.Sets;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="HoleESP", category=Category.Render)
public class HoleESP
extends Module {
    public IntegerSetting range = this.registerInteger("Range", 5, 1, 20);
    IntegerSetting Yrange = this.registerInteger("Y Range", 5, 1, 20);
    BooleanSetting single = this.registerBoolean("1x1", true);
    BooleanSetting Double = this.registerBoolean("2x1", true);
    BooleanSetting fourBlocks = this.registerBoolean("2x2", true);
    BooleanSetting custom = this.registerBoolean("Custom", true);
    ModeSetting type = this.registerMode("Render", Arrays.asList("Outline", "Fill", "Both"), "Both");
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("Air", "Ground", "Flat", "Slab", "Double"), "Air");
    BooleanSetting hideOwn = this.registerBoolean("Hide Own", false);
    BooleanSetting flatOwn = this.registerBoolean("Flat Own", false);
    BooleanSetting fov = this.registerBoolean("In Fov", false);
    DoubleSetting slabHeight = this.registerDouble("Slab Height", 0.5, 0.0, 2.0);
    DoubleSetting outslabHeight = this.registerDouble("Outline Height", 0.5, 0.0, 2.0);
    IntegerSetting width = this.registerInteger("Width", 1, 1, 10);
    ColorSetting bedrockColor = this.registerColor("Bedrock Color", new GSColor(0, 255, 0));
    ColorSetting obsidianColor = this.registerColor("Obsidian Color", new GSColor(255, 0, 0));
    ColorSetting twobedrockColor = this.registerColor("2x1 Bedrock Color", new GSColor(0, 255, 0));
    ColorSetting twoobsidianColor = this.registerColor("2x1 Obsidian Color", new GSColor(255, 0, 0));
    ColorSetting fourColor = this.registerColor("2x2 Color", new GSColor(255, 0, 0));
    ColorSetting customColor = this.registerColor("Custom Color", new GSColor(0, 0, 255));
    IntegerSetting alpha = this.registerInteger("Alpha", 50, 0, 255);
    IntegerSetting ufoAlpha = this.registerInteger("UFOAlpha", 255, 0, 255);
    private ConcurrentHashMap<AxisAlignedBB, GSColor> holes;

    @Override
    public void onUpdate() {
        if (HoleESP.mc.field_71439_g == null || HoleESP.mc.field_71441_e == null) {
            return;
        }
        if (this.holes == null) {
            this.holes = new ConcurrentHashMap();
        } else {
            this.holes.clear();
        }
        HashSet possibleHoles = Sets.newHashSet();
        List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), (double)((Integer)this.range.getValue()), (double)((Integer)this.Yrange.getValue()), false, false, 0);
        for (BlockPos pos2 : blockPosList) {
            if (((Boolean)this.fov.getValue()).booleanValue() && !RotationUtil.isInFov(pos2) || !HoleESP.mc.field_71441_e.func_180495_p(pos2).func_177230_c().equals(Blocks.field_150350_a) || HoleESP.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, -1, 0)).func_177230_c().equals(Blocks.field_150350_a) || !HoleESP.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a) || !HoleESP.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a)) continue;
            possibleHoles.add(pos2);
        }
        possibleHoles.forEach(pos -> {
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false, true);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType != HoleUtil.HoleType.NONE) {
                HoleUtil.BlockSafety holeSafety = holeInfo.getSafety();
                AxisAlignedBB centreBlocks = holeInfo.getCentre();
                if (centreBlocks == null) {
                    return;
                }
                if (((Boolean)this.fourBlocks.getValue()).booleanValue() && holeType == HoleUtil.HoleType.FOUR) {
                    GSColor colour = new GSColor(this.fourColor.getValue(), 255);
                    this.holes.put(centreBlocks, colour);
                } else if (((Boolean)this.custom.getValue()).booleanValue() && holeType == HoleUtil.HoleType.CUSTOM) {
                    GSColor colour = new GSColor(this.customColor.getValue(), 255);
                    this.holes.put(centreBlocks, colour);
                } else if (((Boolean)this.Double.getValue()).booleanValue() && holeType == HoleUtil.HoleType.DOUBLE) {
                    GSColor colour = holeSafety == HoleUtil.BlockSafety.UNBREAKABLE ? new GSColor(this.twobedrockColor.getValue(), 255) : new GSColor(this.twoobsidianColor.getValue(), 255);
                    this.holes.put(centreBlocks, colour);
                } else if (((Boolean)this.single.getValue()).booleanValue() && holeType == HoleUtil.HoleType.SINGLE) {
                    GSColor colour = holeSafety == HoleUtil.BlockSafety.UNBREAKABLE ? new GSColor(this.bedrockColor.getValue(), 255) : new GSColor(this.obsidianColor.getValue(), 255);
                    this.holes.put(centreBlocks, colour);
                }
            }
        });
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (HoleESP.mc.field_71439_g == null || HoleESP.mc.field_71441_e == null || this.holes == null || this.holes.isEmpty()) {
            return;
        }
        this.holes.forEach(this::renderHoles);
    }

    private void renderHoles(AxisAlignedBB hole, GSColor color) {
        switch ((String)this.type.getValue()) {
            case "Outline": {
                this.renderOutline(hole, color);
                break;
            }
            case "Fill": {
                this.renderFill(hole, color);
                break;
            }
            case "Both": {
                this.renderOutline(hole, color);
                this.renderFill(hole, color);
            }
        }
    }

    private void renderFill(AxisAlignedBB hole, GSColor color) {
        GSColor fillColor = new GSColor(color, (Integer)this.alpha.getValue());
        int ufoAlpha = (Integer)this.ufoAlpha.getValue() * 50 / 255;
        if (((Boolean)this.hideOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
            return;
        }
        switch ((String)this.mode.getValue()) {
            case "Air": {
                if (((Boolean)this.flatOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
                    RenderUtil.drawBox(hole, true, 1.0, fillColor, ufoAlpha, 1);
                    break;
                }
                RenderUtil.drawBox(hole, true, 1.0, fillColor, ufoAlpha, 63);
                break;
            }
            case "Ground": {
                RenderUtil.drawBox(hole.func_72317_d(0.0, -1.0, 0.0), true, 1.0, new GSColor(fillColor, ufoAlpha), fillColor.getAlpha(), 63);
                break;
            }
            case "Flat": {
                RenderUtil.drawBox(hole, true, 1.0, fillColor, ufoAlpha, 1);
                break;
            }
            case "Slab": {
                if (((Boolean)this.flatOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
                    RenderUtil.drawBox(hole, true, 1.0, fillColor, ufoAlpha, 1);
                    break;
                }
                RenderUtil.drawBox(hole, false, (Double)this.slabHeight.getValue(), fillColor, ufoAlpha, 63);
                break;
            }
            case "Double": {
                if (((Boolean)this.flatOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
                    RenderUtil.drawBox(hole, true, 1.0, fillColor, ufoAlpha, 1);
                    break;
                }
                RenderUtil.drawBox(hole.func_186666_e(hole.field_72337_e + 1.0), true, 2.0, fillColor, ufoAlpha, 63);
            }
        }
    }

    private void renderOutline(AxisAlignedBB hole, GSColor color) {
        GSColor outlineColor = new GSColor(color, 255);
        if (((Boolean)this.hideOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
            return;
        }
        switch ((String)this.mode.getValue()) {
            case "Air": {
                if (((Boolean)this.flatOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, (int)((Integer)this.width.getValue()), outlineColor, (int)((Integer)this.ufoAlpha.getValue()), 1);
                    break;
                }
                RenderUtil.drawBoundingBox(hole, (double)((Integer)this.width.getValue()).intValue(), outlineColor, (Integer)this.ufoAlpha.getValue());
                break;
            }
            case "Ground": {
                RenderUtil.drawBoundingBox(hole.func_72317_d(0.0, -1.0, 0.0), (double)((Integer)this.width.getValue()).intValue(), new GSColor(outlineColor, (Integer)this.ufoAlpha.getValue()), outlineColor.getAlpha());
                break;
            }
            case "Flat": {
                RenderUtil.drawBoundingBoxWithSides(hole, (int)((Integer)this.width.getValue()), outlineColor, (int)((Integer)this.ufoAlpha.getValue()), 1);
                break;
            }
            case "Slab": {
                if (((Boolean)this.flatOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, (int)((Integer)this.width.getValue()), outlineColor, (int)((Integer)this.ufoAlpha.getValue()), 1);
                    break;
                }
                RenderUtil.drawBoundingBox(hole.func_186666_e(hole.field_72338_b + (Double)this.outslabHeight.getValue()), (double)((Integer)this.width.getValue()).intValue(), outlineColor, (Integer)this.ufoAlpha.getValue());
                break;
            }
            case "Double": {
                if (((Boolean)this.flatOwn.getValue()).booleanValue() && hole.func_72326_a(HoleESP.mc.field_71439_g.func_174813_aQ())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, (int)((Integer)this.width.getValue()), outlineColor, (int)((Integer)this.ufoAlpha.getValue()), 1);
                    break;
                }
                RenderUtil.drawBoundingBox(hole.func_186666_e(hole.field_72337_e + 1.0), (double)((Integer)this.width.getValue()).intValue(), outlineColor, (Integer)this.ufoAlpha.getValue());
            }
        }
    }
}
