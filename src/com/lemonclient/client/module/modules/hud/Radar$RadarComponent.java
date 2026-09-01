/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 */
package com.lemonclient.client.module.modules.hud;

import com.lemonclient.api.util.render.GSColor;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.setting.Labeled;
import com.lukflug.panelstudio.theme.ITheme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

private class Radar.RadarComponent
extends HUDComponent {
    private final int maxRange = 50;

    public Radar.RadarComponent(ITheme theme) {
        super(new Labeled(Radar.this.getName(), null, () -> true), Radar.this.position, Radar.this.getName());
        this.maxRange = 50;
    }

    @Override
    public void render(Context context) {
        super.render(context);
        if (mc.field_71439_g != null && mc.field_71439_g.field_70173_aa >= 10) {
            if (((Boolean)Radar.this.renderPlayer.getValue()).booleanValue()) {
                mc.field_71441_e.field_73010_i.stream().filter(entityPlayer -> entityPlayer != mc.field_71439_g).forEach(entityPlayer -> this.renderEntityPoint((Entity)entityPlayer, Radar.this.getPlayerColor(entityPlayer), context));
            }
            if (((Boolean)Radar.this.renderMobs.getValue()).booleanValue()) {
                mc.field_71441_e.field_72996_f.stream().filter(entity -> !(entity instanceof EntityPlayer)).forEach(entity -> {
                    if (entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid) {
                        this.renderEntityPoint((Entity)entity, Radar.this.getEntityColor(entity), context);
                    }
                });
            }
            GSColor background = new GSColor(Radar.this.fillColor.getValue(), 100);
            context.getInterface().fillRect(context.getRect(), background, background, background, background);
            GSColor outline = new GSColor(Radar.this.outlineColor.getValue(), 255);
            context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(context.getSize().width, 1)), outline, outline, outline, outline);
            context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(1, context.getSize().height)), outline, outline, outline, outline);
            context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x + context.getSize().width - 1, context.getPos().y), new Dimension(1, context.getSize().height)), outline, outline, outline, outline);
            context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x, context.getPos().y + context.getSize().height - 1), new Dimension(context.getSize().width, 1)), outline, outline, outline, outline);
            boolean isNorth = this.isFacing(EnumFacing.NORTH);
            boolean isSouth = this.isFacing(EnumFacing.SOUTH);
            boolean isEast = this.isFacing(EnumFacing.EAST);
            boolean isWest = this.isFacing(EnumFacing.WEST);
            Color selfColor = new Color(255, 255, 255, 255);
            int distanceToCenter = context.getSize().height / 2;
            context.getInterface().drawLine(new Point(context.getPos().x + distanceToCenter + 3, context.getPos().y + distanceToCenter), new Point(context.getPos().x + distanceToCenter + (isEast ? 1 : 0), context.getPos().y + distanceToCenter), isEast ? outline : selfColor, isEast ? outline : selfColor);
            context.getInterface().drawLine(new Point(context.getPos().x + distanceToCenter, context.getPos().y + distanceToCenter + 3), new Point(context.getPos().x + distanceToCenter, context.getPos().y + distanceToCenter + (isSouth ? 1 : 0)), isSouth ? outline : selfColor, isSouth ? outline : selfColor);
            context.getInterface().drawLine(new Point(context.getPos().x + distanceToCenter - (isWest ? 1 : 0), context.getPos().y + distanceToCenter), new Point(context.getPos().x + distanceToCenter - 3, context.getPos().y + distanceToCenter), isWest ? outline : selfColor, isWest ? outline : selfColor);
            context.getInterface().drawLine(new Point(context.getPos().x + distanceToCenter, context.getPos().y + distanceToCenter - (isNorth ? 1 : 0)), new Point(context.getPos().x + distanceToCenter, context.getPos().y + distanceToCenter - 3), isNorth ? outline : selfColor, isNorth ? outline : selfColor);
        }
    }

    private boolean isFacing(EnumFacing enumFacing) {
        return mc.field_71439_g.func_174811_aO().equals((Object)enumFacing);
    }

    private void renderEntityPoint(Entity entity, Color color, Context context) {
        int distanceX = this.findDistance1D(mc.field_71439_g.field_70165_t, entity.field_70165_t);
        int distanceY = this.findDistance1D(mc.field_71439_g.field_70161_v, entity.field_70161_v);
        int distanceToCenter = context.getSize().height / 2;
        if (distanceX > 50 || distanceY > 50 || distanceX < -50 || distanceY < -50) {
            return;
        }
        context.getInterface().drawLine(new Point(context.getPos().x + distanceToCenter + 1 + distanceX, context.getPos().y + distanceToCenter + distanceY), new Point(context.getPos().x + distanceToCenter - 1 + distanceX, context.getPos().y + distanceToCenter + distanceY), color, color);
        context.getInterface().drawLine(new Point(context.getPos().x + distanceToCenter + distanceX, context.getPos().y + distanceToCenter + 1 + distanceY), new Point(context.getPos().x + distanceToCenter + distanceX, context.getPos().y + distanceToCenter - 1 + distanceY), color, color);
    }

    private int findDistance1D(double player, double entity) {
        double player1 = player;
        double entity1 = entity;
        if (player1 < 0.0) {
            player1 *= -1.0;
        }
        if (entity1 < 0.0) {
            entity1 *= -1.0;
        }
        int value = (int)(entity1 - player1);
        if (player > 0.0 && entity < 0.0 || player < 0.0 && entity > 0.0) {
            value = (int)(-1.0 * player + entity);
        }
        if ((player > 0.0 || player < 0.0) && entity < 0.0 && entity1 != player1) {
            value = (int)(-1.0 * player + entity);
        }
        if (player < 0.0 && entity == 0.0 || player == 0.0 && entity < 0.0) {
            value = (int)(-1.0 * (entity1 - player1));
        }
        return value;
    }

    @Override
    public Dimension getSize(IInterface anInterface) {
        return new Dimension(103, 103);
    }
}
