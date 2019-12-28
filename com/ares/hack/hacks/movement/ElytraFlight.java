package com.ares.hack.hacks.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "ElytraFlight", description = "Rockets aren't needed", category = EnumCategory.MOVEMENT)
public class ElytraFlight extends BaseHack
{
    private Setting<FlightMode> mode;
    private Setting<Double> boostSpeed;
    private Setting<Double> speed;
    private Setting<Double> controlSpeed;
    
    public ElytraFlight() {
        this.mode = new EnumSetting<FlightMode>("Mode", this, FlightMode.BOOST);
        this.boostSpeed = new DoubleSetting("Boost-Speed", this, 0.05, 0.01, 0.2);
        this.speed = new DoubleSetting("Flight-Speed", this, 0.05, 0.01, 0.2);
        this.controlSpeed = new DoubleSetting("Control-Speed", this, 0.9, 0.01, 4.0);
    }
    
    public void onLogic() {
        /*SL:46*/if (!ElytraFlight.mc.field_71439_g.func_184613_cA() || !this.getEnabled()) {
            return;
        }
        /*SL:48*/if (this.mode.getValue() == FlightMode.BOOST) {
            /*SL:50*/if (ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b) {
                ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b = /*EL:52*/false;
            }
            /*SL:55*/if (ElytraFlight.mc.field_71439_g.func_70090_H()) {
                ElytraFlight.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:57*/(Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            final float v1 = /*EL:60*/(float)Math.toRadians(ElytraFlight.mc.field_71439_g.field_70177_z);
            /*SL:62*/if (ElytraFlight.mc.field_71474_y.field_74351_w.func_151470_d()) {
                final EntityPlayerSP field_71439_g = ElytraFlight.mc.field_71439_g;
                /*SL:64*/field_71439_g.field_70159_w -= MathHelper.func_76126_a(v1) * this.boostSpeed.getValue();
                final EntityPlayerSP field_71439_g2 = ElytraFlight.mc.field_71439_g;
                /*SL:65*/field_71439_g2.field_70179_y += MathHelper.func_76134_b(v1) * this.boostSpeed.getValue();
            }
            else/*SL:67*/ if (ElytraFlight.mc.field_71474_y.field_74368_y.func_151470_d()) {
                final EntityPlayerSP field_71439_g3 = ElytraFlight.mc.field_71439_g;
                /*SL:69*/field_71439_g3.field_70159_w += MathHelper.func_76126_a(v1) * this.boostSpeed.getValue();
                final EntityPlayerSP field_71439_g4 = ElytraFlight.mc.field_71439_g;
                /*SL:70*/field_71439_g4.field_70179_y -= MathHelper.func_76134_b(v1) * this.boostSpeed.getValue();
            }
        }
        /*SL:74*/if (this.mode.getValue() == FlightMode.FLIGHT) {
            ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b = /*EL:76*/true;
            ElytraFlight.mc.field_71439_g.field_70747_aH = /*EL:77*/(float)(Object)this.speed.getValue();
            /*SL:80*/if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                final EntityPlayerSP field_71439_g5 = ElytraFlight.mc.field_71439_g;
                /*SL:82*/field_71439_g5.field_70181_x += this.speed.getValue();
            }
            /*SL:84*/if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                final EntityPlayerSP field_71439_g6 = ElytraFlight.mc.field_71439_g;
                /*SL:86*/field_71439_g6.field_70181_x -= this.speed.getValue();
            }
        }
        /*SL:90*/if (this.mode.getValue() == FlightMode.CONTROL) {
            ElytraFlight.mc.field_71439_g.field_70181_x = /*EL:92*/0.0;
            ElytraFlight.mc.field_71439_g.field_70159_w = /*EL:93*/0.0;
            ElytraFlight.mc.field_71439_g.field_70179_y = /*EL:94*/0.0;
            final float v1 = /*EL:96*/(float)Math.toRadians(ElytraFlight.mc.field_71439_g.field_70177_z);
            final float v2 = /*EL:97*/(float)Math.toRadians(ElytraFlight.mc.field_71439_g.field_70125_A);
            /*SL:99*/if (ElytraFlight.mc.field_71474_y.field_74351_w.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70159_w = /*EL:101*/-(MathHelper.func_76126_a(v1) * MathHelper.func_76134_b(v2) * this.controlSpeed.getValue());
                ElytraFlight.mc.field_71439_g.field_70179_y = /*EL:102*/MathHelper.func_76134_b(v1) * MathHelper.func_76134_b(v2) * this.controlSpeed.getValue();
                ElytraFlight.mc.field_71439_g.field_70181_x = /*EL:103*/-(MathHelper.func_76126_a(v2) * this.controlSpeed.getValue());
            }
            else/*SL:104*/ if (ElytraFlight.mc.field_71474_y.field_74368_y.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70159_w = /*EL:106*/MathHelper.func_76126_a(v1) * MathHelper.func_76134_b(v2) * this.controlSpeed.getValue();
                ElytraFlight.mc.field_71439_g.field_70179_y = /*EL:107*/-(MathHelper.func_76134_b(v1) * MathHelper.func_76134_b(v2) * this.controlSpeed.getValue());
                ElytraFlight.mc.field_71439_g.field_70181_x = /*EL:108*/MathHelper.func_76126_a(v2) * this.controlSpeed.getValue();
            }
            /*SL:111*/if (ElytraFlight.mc.field_71474_y.field_74370_x.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70179_y = /*EL:113*/MathHelper.func_76126_a(v1) * MathHelper.func_76134_b(v2) * this.controlSpeed.getValue();
                ElytraFlight.mc.field_71439_g.field_70159_w = /*EL:114*/MathHelper.func_76134_b(v1) * MathHelper.func_76134_b(v2) * this.controlSpeed.getValue();
            }
            else/*SL:116*/ if (ElytraFlight.mc.field_71474_y.field_74366_z.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70179_y = /*EL:118*/-(MathHelper.func_76126_a(v1) * this.controlSpeed.getValue());
                ElytraFlight.mc.field_71439_g.field_70159_w = /*EL:119*/-(MathHelper.func_76134_b(v1) * this.controlSpeed.getValue());
            }
            /*SL:122*/if (ElytraFlight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70181_x = /*EL:124*/this.controlSpeed.getValue();
            }
            else/*SL:126*/ if (ElytraFlight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                ElytraFlight.mc.field_71439_g.field_70181_x = /*EL:128*/-this.controlSpeed.getValue();
            }
        }
    }
    
    public void onEnabled() {
        /*SL:136*/if (this.mode.getValue() != FlightMode.FLIGHT) {
            return;
        }
        ElytraFlight.mc.field_71439_g.field_71075_bZ.func_75092_a(/*EL:138*/(float)(Object)this.speed.getValue());
        ElytraFlight.mc.func_152344_a(/*EL:140*/() -> {
            if (ElytraFlight.mc.field_71439_g != null && ElytraFlight.mc.field_71439_g.func_184613_cA() && this.getEnabled() && this.mode.getValue() == FlightMode.FLIGHT) {
                ElytraFlight.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:145*/(Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        });
    }
    
    public void onDisabled() {
        /*SL:150*/if (this.mode.getValue() == FlightMode.FLIGHT) {
            ElytraFlight.mc.field_71439_g.field_71075_bZ.field_75100_b = /*EL:152*/false;
            ElytraFlight.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:153*/(Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }
    
    enum FlightMode
    {
        BOOST, 
        CONTROL, 
        FLIGHT;
    }
}
