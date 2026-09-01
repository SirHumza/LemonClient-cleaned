/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.entity.Entity
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.util.world.BlockUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;

static class FakePlayerDev.movingPlayer {
    private final int id;
    private final String type;
    private final double speed;
    private final int direction;
    private final double range;
    private final boolean follow;
    int rad = 0;

    public FakePlayerDev.movingPlayer(int id, String type, double speed, int direction, double range, boolean follow) {
        this.id = id;
        this.type = type;
        this.speed = speed;
        this.direction = Math.abs(direction);
        this.range = range;
        this.follow = follow;
    }

    void move() {
        Entity player = mc.field_71441_e.func_73045_a(this.id);
        if (player != null) {
            switch (this.type) {
                case "Line": {
                    double posX = this.follow ? mc.field_71439_g.field_70165_t : player.field_70165_t;
                    double posY = this.follow ? mc.field_71439_g.field_70163_u : player.field_70163_u;
                    double posZ = this.follow ? mc.field_71439_g.field_70161_v : player.field_70161_v;
                    switch (this.direction) {
                        case 0: {
                            posZ += this.speed;
                            break;
                        }
                        case 1: {
                            posX -= this.speed / 2.0;
                            posZ += this.speed / 2.0;
                            break;
                        }
                        case 2: {
                            posX -= this.speed / 2.0;
                            break;
                        }
                        case 3: {
                            posZ -= this.speed / 2.0;
                            posX -= this.speed / 2.0;
                            break;
                        }
                        case 4: {
                            posZ -= this.speed;
                            break;
                        }
                        case 5: {
                            posX += this.speed / 2.0;
                            posZ -= this.speed / 2.0;
                            break;
                        }
                        case 6: {
                            posX += this.speed;
                            break;
                        }
                        case 7: {
                            posZ += this.speed / 2.0;
                            posX += this.speed / 2.0;
                        }
                    }
                    if (BlockUtil.getBlock(posX, posY, posZ) instanceof BlockAir) {
                        for (int i = 0; i < 5 && BlockUtil.getBlock(posX, posY - 1.0, posZ) instanceof BlockAir; ++i) {
                            posY -= 1.0;
                        }
                    } else {
                        for (int i = 0; i < 5 && !(BlockUtil.getBlock(posX, posY, posZ) instanceof BlockAir); ++i) {
                            posY += 1.0;
                        }
                    }
                    player.func_70634_a(posX, posY, posZ);
                    break;
                }
                case "Circle": {
                    double posXCir = Math.cos((double)this.rad / 100.0) * this.range + mc.field_71439_g.field_70165_t;
                    double posZCir = Math.sin((double)this.rad / 100.0) * this.range + mc.field_71439_g.field_70161_v;
                    double posYCir = mc.field_71439_g.field_70163_u;
                    if (BlockUtil.getBlock(posXCir, posYCir, posZCir) instanceof BlockAir) {
                        for (int i = 0; i < 5 && BlockUtil.getBlock(posXCir, posYCir - 1.0, posZCir) instanceof BlockAir; ++i) {
                            posYCir -= 1.0;
                        }
                    } else {
                        for (int i = 0; i < 5 && !(BlockUtil.getBlock(posXCir, posYCir, posZCir) instanceof BlockAir); ++i) {
                            posYCir += 1.0;
                        }
                    }
                    player.func_70634_a(posXCir, posYCir, posZCir);
                    this.rad = (int)((double)this.rad + this.speed * 10.0);
                    break;
                }
            }
        }
    }
}
