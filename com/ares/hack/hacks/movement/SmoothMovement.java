package com.ares.hack.hacks.movement;

import net.minecraft.network.Packet;
import com.ares.utils.math.AngleHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.ares.event.client.movement.CancelSprint;
import net.minecraft.network.play.client.CPacketPlayer;
import com.ares.event.packet.PacketSent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.client.movement.SmoothElytraLand;
import com.ares.hack.settings.settings.number.FloatSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.utils.math.Angle;
import net.minecraft.entity.Entity;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "SmoothMovement", description = "Better Movement", category = EnumCategory.MOVEMENT)
public class SmoothMovement extends BaseHack
{
    private Setting<Boolean> betterElytraLand;
    private Setting<Boolean> faceMoveDirection;
    private Setting<Float> faceSensitivity;
    private Setting<Boolean> faceSilent;
    private Setting<Boolean> antiSprintStop;
    private Entity debugEntity;
    private Angle debugAngle;
    
    public SmoothMovement() {
        this.betterElytraLand = new BooleanSetting("Elytra Land", this, true);
        this.faceMoveDirection = new BooleanSetting("Face Movement", this, false);
        this.faceSensitivity = new FloatSetting("Face Sensitivity", this, 0.001f, 0.0f, 0.1f);
        this.faceSilent = new BooleanSetting("Face Silent", this, true);
        this.antiSprintStop = new BooleanSetting("Anti StopSprint", this, true);
    }
    
    @SubscribeEvent
    public void smoothElytra(final SmoothElytraLand a1) {
        /*SL:37*/if (this.getEnabled()) {
            /*SL:38*/a1.isWorldRemote = false;
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSent v0) {
        /*SL:44*/if (v0.packet instanceof CPacketPlayer) {
            final CPacketPlayer v = /*EL:46*/(CPacketPlayer)v0.packet;
            final float v2 = /*EL:48*/v.func_186999_a(-420.0f);
            /*SL:50*/if (v2 != -420.0f) {
                final float a1 = /*EL:52*/v.func_186998_b(-420.0f);
                /*SL:54*/if (a1 != -420.0f) {
                    /*SL:56*/this.debugAngle = Angle.degrees(a1, v2);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onCancelSprint(final CancelSprint a1) {
        /*SL:65*/if (this.getEnabled() && this.antiSprintStop.getValue()) {
            /*SL:67*/a1.shouldCancel = false;
        }
    }
    
    public void onLogic() {
        /*SL:74*/if (this.getEnabled()) {
            /*SL:76*/if (this.debugEntity == null) {
                /*SL:77*/this.debugEntity = (Entity)new EntityOtherPlayerMP((World)SmoothMovement.mc.field_71441_e, SmoothMovement.mc.field_71439_g.func_146103_bH());
            }
            /*SL:79*/if (this.debugAngle != null) {
                /*SL:81*/this.debugEntity.func_70107_b(SmoothMovement.mc.field_71439_g.field_70165_t, SmoothMovement.mc.field_71439_g.field_70163_u, SmoothMovement.mc.field_71439_g.field_70161_v);
                /*SL:82*/this.debugEntity.field_70145_X = true;
                /*SL:83*/this.debugEntity.field_70177_z = this.debugAngle.getYaw();
                /*SL:84*/this.debugEntity.field_70125_A = this.debugAngle.getPitch();
            }
            else {
                /*SL:88*/this.debugEntity.func_70107_b(0.0, -420.0, 0.0);
            }
        }
        /*SL:92*/if (this.getEnabled() && this.faceMoveDirection.getValue()) {
            final Vec3d v0 = /*EL:94*/new Vec3d(SmoothMovement.mc.field_71439_g.field_70159_w, 0.0, SmoothMovement.mc.field_71439_g.field_70179_y).func_178787_e(SmoothMovement.mc.field_71439_g.func_174791_d());
            /*SL:95*/if (SmoothMovement.mc.field_71439_g.field_70159_w > this.faceSensitivity.getValue() || SmoothMovement.mc.field_71439_g.field_70179_y > this.faceSensitivity.getValue()) {
                final Vec3d v = /*EL:97*/new Vec3d(SmoothMovement.mc.field_71439_g.field_70165_t, SmoothMovement.mc.field_71439_g.field_70163_u + SmoothMovement.mc.field_71439_g.func_70047_e(), SmoothMovement.mc.field_71439_g.field_70161_v);
                final Angle v2 = /*EL:98*/AngleHelper.getAngleFacingInDegrees(v0.func_178788_d(v).func_72432_b());
                /*SL:100*/if (this.faceSilent.getValue()) {
                    SmoothMovement.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:103*/(Packet)new CPacketPlayer.Rotation(v2.getYaw(), SmoothMovement.mc.field_71439_g.field_70125_A, SmoothMovement.mc.field_71439_g.field_70122_E));
                }
                else {
                    SmoothMovement.mc.field_71439_g.field_70177_z = /*EL:109*/v2.getYaw();
                }
            }
        }
    }
}
