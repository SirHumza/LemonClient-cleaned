/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package com.lemonclient.mixin.mixins;

import com.lemonclient.api.event.events.EventPlayerIsHandActive;
import com.lemonclient.api.event.events.MotionUpdateEvent;
import com.lemonclient.api.event.events.MotionUpdateEvents;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PlayerMoveEvent;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.dev.AntiPush;
import com.lemonclient.client.module.modules.exploits.Portal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP
extends AbstractClientPlayer {
    @Shadow
    @Final
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    protected Minecraft field_71159_c;
    @Shadow
    private boolean field_184841_cd;
    @Shadow
    private float field_175164_bL;
    @Shadow
    private float field_175165_bM;
    @Shadow
    private int field_175168_bP;
    @Shadow
    private double field_175172_bI;
    @Shadow
    private double field_175166_bJ;
    @Shadow
    private double field_175167_bK;
    @Shadow
    private boolean field_189811_cr;
    @Shadow
    private boolean field_175171_bO;
    @Shadow
    private boolean field_175170_bN;
    @Shadow
    public MovementInput field_71158_b;
    private MotionUpdateEvents motionEvent;

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    private void onUpdateWalkingPlayer_Head(CallbackInfo callbackInfo) {
        this.motionEvent = new MotionUpdateEvents(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v, this.field_70177_z, this.field_70125_A, this.field_70122_E);
        LemonClient.EVENT_BUS.post(this.motionEvent);
        if (this.motionEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    public MixinEntityPlayerSP() {
        super((World)Minecraft.func_71410_x().field_71441_e, Minecraft.func_71410_x().field_71449_j.func_148256_e());
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo p_Info) {
        MotionUpdateEvent l_Event = new MotionUpdateEvent(0);
        LemonClient.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled()) {
            p_Info.cancel();
        }
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="RETURN")}, cancellable=true)
    public void OnPostUpdateWalkingPlayer(CallbackInfo p_Info) {
        MotionUpdateEvent l_Event = new MotionUpdateEvent(1);
        LemonClient.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled()) {
            p_Info.cancel();
        }
    }

    @Shadow
    protected abstract boolean func_175160_A();

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.isModuleEnabled(AntiPush.class)) {
            cir.cancel();
        }
    }

    @Inject(method={"isHandActive"}, at={@At(value="HEAD")}, cancellable=true)
    public void isHandActive(CallbackInfoReturnable<Boolean> info) {
        EventPlayerIsHandActive event = new EventPlayerIsHandActive();
        LemonClient.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
            info.setReturnValue(false);
        }
    }

    @Inject(method={"Lnet/minecraft/client/entity/EntityPlayerSP;setServerBrand(Ljava/lang/String;)V"}, at={@At(value="HEAD")})
    public void getBrand(String serverBrand, CallbackInfo callbackInfo) {
        if (LemonClient.serverUtil != null) {
            LemonClient.serverUtil.setServerBrand(serverBrand);
        }
    }

    @Redirect(method={"move"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
        if (type != MoverType.PLAYER && type != MoverType.SELF && ModuleManager.isModuleEnabled(AntiPush.class)) {
            moveEvent.cancel();
        } else {
            LemonClient.EVENT_BUS.post(moveEvent);
            super.func_70091_d(type, moveEvent.getX(), moveEvent.getY(), moveEvent.getZ());
        }
    }

    @Inject(method={"onUpdate"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift=At.Shift.AFTER)})
    private void onUpdateInvokeOnUpdateWalkingPlayer(CallbackInfo ci) {
        Vec3d serverSidePos = PlayerPacketManager.INSTANCE.getServerSidePosition();
        float serverSideRotationX = PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i;
        float serverSideRotationY = PlayerPacketManager.INSTANCE.getServerSideRotation().field_189983_j;
        this.field_175172_bI = serverSidePos.field_72450_a;
        this.field_175166_bJ = serverSidePos.field_72448_b;
        this.field_175167_bK = serverSidePos.field_72449_c;
        this.field_175164_bL = serverSideRotationX;
        this.field_175165_bM = serverSideRotationY;
        this.field_70759_as = serverSideRotationX;
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    public void onUpdateWalkingPlayerPre(CallbackInfo callbackInfo) {
        Vec3d position = new Vec3d(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v);
        Vec2f rotation = new Vec2f(this.field_70177_z, this.field_70125_A);
        OnUpdateWalkingPlayerEvent event = new OnUpdateWalkingPlayerEvent(position, rotation);
        LemonClient.EVENT_BUS.post(event);
        event = event.nextPhase();
        LemonClient.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
            boolean moving = event.isMoving() || this.isMoving(position);
            boolean rotating = event.isRotating() || this.isRotating(rotation);
            position = event.getPosition();
            rotation = event.getRotation();
            ++this.field_175168_bP;
            this.sendSprintPacket();
            this.sendSneakPacket();
            this.sendPlayerPacket(moving, rotating, position, rotation);
        }
        event = event.nextPhase();
        LemonClient.EVENT_BUS.post(event);
    }

    private void sendSprintPacket() {
        boolean sprinting = this.func_70051_ag();
        if (sprinting != this.field_175171_bO) {
            if (sprinting) {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = sprinting;
        }
    }

    private void sendSneakPacket() {
        boolean sneaking = this.func_70093_af();
        if (sneaking != this.field_175170_bN) {
            if (sneaking) {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = sneaking;
        }
    }

    public void sendPlayerPacket(boolean moving, boolean rotating, Vec3d position, Vec2f rotation) {
        if (!this.func_175160_A()) {
            return;
        }
        if (this.func_184218_aH()) {
            this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(this.field_70159_w, -999.0, this.field_70179_y, rotation.field_189982_i, rotation.field_189983_j, this.field_70122_E));
            moving = false;
        } else if (moving && rotating) {
            this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(position.field_72450_a, position.field_72448_b, position.field_72449_c, rotation.field_189982_i, rotation.field_189983_j, this.field_70122_E));
        } else if (moving) {
            this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(position.field_72450_a, position.field_72448_b, position.field_72449_c, this.field_70122_E));
        } else if (rotating) {
            this.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotation.field_189982_i, rotation.field_189983_j, this.field_70122_E));
        } else if (this.field_184841_cd != this.field_70122_E) {
            this.field_71174_a.func_147297_a((Packet)new CPacketPlayer(this.field_70122_E));
        }
        if (moving) {
            this.field_175172_bI = position.field_72450_a;
            this.field_175166_bJ = position.field_72448_b;
            this.field_175167_bK = position.field_72449_c;
            this.field_175168_bP = 0;
        }
        if (rotating) {
            this.field_175164_bL = rotation.field_189982_i;
            this.field_175165_bM = rotation.field_189983_j;
        }
        this.field_184841_cd = this.field_70122_E;
        this.field_189811_cr = this.field_71159_c.field_71474_y.field_189989_R;
    }

    private boolean isMoving(Vec3d position) {
        double xDiff = position.field_72450_a - this.field_175172_bI;
        double yDiff = position.field_72448_b - this.field_175166_bJ;
        double zDiff = position.field_72449_c - this.field_175167_bK;
        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff > 9.0E-4 || this.field_175168_bP >= 20;
    }

    private boolean isRotating(Vec2f rotation) {
        double yawDiff = rotation.field_189982_i - this.field_175164_bL;
        double pitchDiff = rotation.field_189983_j - this.field_175165_bM;
        return yawDiff != 0.0 || pitchDiff != 0.0;
    }

    @Redirect(method={"onLivingUpdate"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreenHook(EntityPlayerSP entityPlayerSP) {
        Portal portal = ModuleManager.getModule(Portal.class);
        if (!portal.isEnabled() || !((Boolean)portal.chat.getValue()).booleanValue()) {
            entityPlayerSP.func_71053_j();
        }
    }
}
