/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.DimensionType
 *  org.lwjgl.opengl.GL11
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.Interpolation;
import com.lemonclient.api.util.render.animation.AnimationMode;
import com.lemonclient.api.util.render.animation.TimeAnimation;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import org.lwjgl.opengl.GL11;

@Module.Declaration(name="Trails", category=Category.Render)
public class Trails
extends Module {
    BooleanSetting arrows = this.registerBoolean("Arrows", false);
    BooleanSetting pearls = this.registerBoolean("Pearls", false);
    BooleanSetting snowballs = this.registerBoolean("Snowballs", false);
    IntegerSetting time = this.registerInteger("Time", 1, 1, 10);
    ColorSetting color = this.registerColor("Color", new GSColor(255, 0, 0, 255));
    IntegerSetting alpha = this.registerInteger("Alpha", 255, 1, 255);
    DoubleSetting width = this.registerDouble("Width", 1.6f, 0.1f, 10.0);
    Map<Integer, TimeAnimation> ids = new ConcurrentHashMap<Integer, TimeAnimation>();
    Map<Integer, List<Trace>> traceLists = new ConcurrentHashMap<Integer, List<Trace>>();
    Map<Integer, Trace> traces = new ConcurrentHashMap<Integer, Trace>();
    public static final Vec3d ORIGIN = new Vec3d(8.0, 64.0, 8.0);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (Trails.mc.field_71441_e == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            if ((Boolean)this.pearls.getValue() != false && packet.func_148993_l() == 65 || (Boolean)this.arrows.getValue() != false && packet.func_148993_l() == 60 || ((Boolean)this.snowballs.getValue()).booleanValue() && packet.func_148993_l() == 61) {
                TimeAnimation animation = new TimeAnimation((Integer)this.time.getValue() * 1000, 0.0, ((Integer)this.alpha.getValue()).intValue(), false, AnimationMode.LINEAR);
                animation.stop();
                this.ids.put(packet.func_149001_c(), animation);
                this.traceLists.put(packet.func_149001_c(), new ArrayList());
                this.traces.put(packet.func_149001_c(), new Trace(0, null, Trails.mc.field_71441_e.field_73011_w.func_186058_p(), new Vec3d(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()), new ArrayList<Trace.TracePos>()));
            }
        }
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            for (int id : ((SPacketDestroyEntities)event.getPacket()).func_149098_c()) {
                if (!this.ids.containsKey(id)) continue;
                this.ids.get(id).play();
            }
        }
    }, new Predicate[0]);

    @Override
    public void onTick() {
        if (Trails.mc.field_71441_e == null) {
            return;
        }
        if (this.ids.keySet().isEmpty()) {
            return;
        }
        for (Integer id : this.ids.keySet()) {
            if (id == null) continue;
            if (Trails.mc.field_71441_e.field_72996_f == null) {
                return;
            }
            if (Trails.mc.field_71441_e.field_72996_f.isEmpty()) {
                return;
            }
            Trace idTrace = this.traces.get(id);
            Entity entity = Trails.mc.field_71441_e.func_73045_a(id.intValue());
            if (entity != null) {
                List<Trace.TracePos> trace;
                Vec3d vec3d;
                Vec3d vec = entity.func_174791_d();
                if (vec.equals((Object)ORIGIN)) continue;
                if (!this.traces.containsKey(id) || idTrace == null) {
                    this.traces.put(id, new Trace(0, null, Trails.mc.field_71441_e.field_73011_w.func_186058_p(), vec, new ArrayList<Trace.TracePos>()));
                    idTrace = this.traces.get(id);
                }
                Vec3d vec3d2 = vec3d = (trace = idTrace.getTrace()).isEmpty() ? vec : trace.get(trace.size() - 1).getPos();
                if (!trace.isEmpty() && (vec.func_72438_d(vec3d) > 100.0 || idTrace.getType() != Trails.mc.field_71441_e.field_73011_w.func_186058_p())) {
                    this.traceLists.get(id).add(idTrace);
                    trace = new ArrayList<Trace.TracePos>();
                    this.traces.put(id, new Trace(this.traceLists.get(id).size() + 1, null, Trails.mc.field_71441_e.field_73011_w.func_186058_p(), vec, new ArrayList<Trace.TracePos>()));
                }
                if (trace.isEmpty() || !vec.equals((Object)vec3d)) {
                    trace.add(new Trace.TracePos(vec));
                }
            }
            TimeAnimation animation = this.ids.get(id);
            if (entity instanceof EntityArrow && (entity.field_70122_E || entity.field_70132_H || !entity.field_70160_al)) {
                animation.play();
            }
            if (animation == null || !((double)((Integer)this.alpha.getValue()).intValue() - animation.getCurrent() <= 0.0)) continue;
            animation.stop();
            this.ids.remove(id);
            this.traceLists.remove(id);
            this.traces.remove(id);
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        for (Map.Entry<Integer, List<Trace>> entry : this.traceLists.entrySet()) {
            GL11.glLineWidth((float)((Double)this.width.getValue()).floatValue());
            TimeAnimation animation = this.ids.get(entry.getKey());
            animation.add();
            GL11.glColor4f((float)this.color.getColor().getRed(), (float)this.color.getColor().getGreen(), (float)this.color.getColor().getBlue(), (float)MathHelper.func_76131_a((float)((float)((double)((Integer)this.alpha.getValue()).intValue() - animation.getCurrent() / 255.0)), (float)0.0f, (float)255.0f));
            entry.getValue().forEach(trace -> {
                GL11.glBegin((int)3);
                trace.getTrace().forEach(this::renderVec);
                GL11.glEnd();
            });
            GL11.glColor4f((float)this.color.getColor().getRed(), (float)this.color.getColor().getGreen(), (float)this.color.getColor().getBlue(), (float)MathHelper.func_76131_a((float)((float)((double)((Integer)this.alpha.getValue()).intValue() - animation.getCurrent() / 255.0)), (float)0.0f, (float)255.0f));
            GL11.glBegin((int)3);
            Trace trace2 = this.traces.get(entry.getKey());
            if (trace2 != null) {
                trace2.getTrace().forEach(this::renderVec);
            }
            GL11.glEnd();
        }
    }

    private void renderVec(Trace.TracePos tracePos) {
        double x = tracePos.getPos().field_72450_a - Interpolation.getRenderPosX();
        double y = tracePos.getPos().field_72448_b - Interpolation.getRenderPosY();
        double z = tracePos.getPos().field_72449_c - Interpolation.getRenderPosZ();
        GL11.glVertex3d((double)x, (double)y, (double)z);
    }

    public static class Trace {
        private String name;
        private int index;
        private Vec3d pos;
        private final List<TracePos> trace;
        private DimensionType type;

        public Trace(int index, String name, DimensionType type, Vec3d pos, List<TracePos> trace) {
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
}
