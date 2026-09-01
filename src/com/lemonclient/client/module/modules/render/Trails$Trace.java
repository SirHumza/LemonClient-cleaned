/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.DimensionType
 */
package com.lemonclient.client.module.modules.render;

import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;

public static class Trails.Trace {
    private String name;
    private int index;
    private Vec3d pos;
    private final List<TracePos> trace;
    private DimensionType type;

    public Trails.Trace(int index, String name, DimensionType type, Vec3d pos, List<TracePos> trace) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.pos = pos;
        this.trace = trace;
    }

    public int getIndex() {
        return this.index;
    }

    public DimensionType getType() {
        return this.type;
    }

    public List<TracePos> getTrace() {
        return this.trace;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vec3d getPos() {
        return this.pos;
    }

    public void setType(DimensionType type) {
        this.type = type;
    }

    public static class TracePos {
        private final Vec3d pos;

        public TracePos(Vec3d pos) {
            this.pos = pos;
        }

        public Vec3d getPos() {
            return this.pos;
        }
    }
}
