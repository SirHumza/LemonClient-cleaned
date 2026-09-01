/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.client.module.modules.combat.HoleFill;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;

class HoleFill.managerClassRenderBlocks {
    ArrayList<HoleFill.renderBlock> blocks = new ArrayList();

    HoleFill.managerClassRenderBlocks() {
    }

    void update(int time) {
        this.blocks.removeIf(e -> System.currentTimeMillis() - ((HoleFill.renderBlock)e).start > (long)time);
    }

    void render() {
        this.blocks.forEach(HoleFill.renderBlock::render);
    }

    void addRender(BlockPos pos) {
        boolean render = true;
        for (HoleFill.renderBlock block : this.blocks) {
            if (!HoleFill.this.sameBlockPos(block.pos, pos)) continue;
            render = false;
            block.resetTime();
            break;
        }
        if (render) {
            this.blocks.add(new HoleFill.renderBlock(HoleFill.this, pos));
        }
    }
}
