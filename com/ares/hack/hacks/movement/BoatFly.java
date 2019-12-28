package com.ares.hack.hacks.movement;

import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "BoatFly", description = "Experimental boatfly", category = EnumCategory.MOVEMENT)
public class BoatFly extends BaseHack
{
    public void onLogic() {
        /*SL:16*/if (!this.getEnabled()) {
            return;
        }
        /*SL:18*/if (BoatFly.mc.field_71439_g.func_184218_aH()) {
            boolean v2 = BoatFly.mc.field_71474_y.field_74351_w.func_151470_d();
            /*SL:41*/v2 = BoatFly.mc.field_71474_y.field_74370_x.func_151470_d();
            final boolean v3 = BoatFly.mc.field_71474_y.field_74366_z.func_151470_d();
            final boolean v4 = BoatFly.mc.field_71474_y.field_74368_y.func_151470_d();
            int v5;
            /*SL:45*/if (v2 && v3) {
                v5 = (v2 ? 0 : (v4 ? 180 : -1));
            }
            else/*SL:46*/ if (v2 && v4) {
                v5 = (v2 ? -90 : (v3 ? 90 : -1));
            }
            else {
                /*SL:48*/v5 = (v2 ? -90 : (v3 ? 90 : 0));
                /*SL:49*/if (v2) {
                    v5 /= 2;
                }
                else/*SL:50*/ if (v4) {
                    v5 = 180 - v5 / 2;
                }
            }
            /*SL:53*/if (v5 != -1 && (v2 || v2 || v3 || v4)) {
                final float v6 = BoatFly.mc.field_71439_g.field_70177_z + /*EL:54*/v5;
                BoatFly.mc.field_71439_g.func_184187_bx().field_70159_w = /*EL:55*/this.getRelativeX(v6) * 0.20000000298023224;
                BoatFly.mc.field_71439_g.func_184187_bx().field_70179_y = /*EL:56*/this.getRelativeZ(v6) * 0.20000000298023224;
            }
            BoatFly.mc.field_71439_g.field_70181_x = /*EL:59*/0.0;
            BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = /*EL:60*/0.0;
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:61*/(Packet)new CPacketPlayer.PositionRotation(BoatFly.mc.field_71439_g.func_184187_bx().field_70165_t + BoatFly.mc.field_71439_g.func_184187_bx().field_70159_w, BoatFly.mc.field_71439_g.func_184187_bx().field_70163_u, BoatFly.mc.field_71439_g.func_184187_bx().field_70161_v + BoatFly.mc.field_71439_g.func_184187_bx().field_70179_y, BoatFly.mc.field_71439_g.field_70177_z, BoatFly.mc.field_71439_g.field_70125_A, false));
            BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = /*EL:63*/0.0;
            /*SL:65*/if (BoatFly.mc.field_71474_y.field_74314_A.func_151470_d()) {
                BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = /*EL:67*/0.3;
            }
            /*SL:69*/if (BoatFly.mc.field_71474_y.field_151444_V.func_151470_d()) {
                BoatFly.mc.field_71439_g.func_184187_bx().field_70181_x = /*EL:71*/-0.3;
            }
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:74*/(Packet)new CPacketVehicleMove());
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:75*/(Packet)new CPacketSteerBoat(true, true));
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:77*/(Packet)new CPacketPlayer.PositionRotation(BoatFly.mc.field_71439_g.func_184187_bx().field_70165_t + BoatFly.mc.field_71439_g.func_184187_bx().field_70159_w, BoatFly.mc.field_71439_g.func_184187_bx().field_70163_u - 42069.0, BoatFly.mc.field_71439_g.func_184187_bx().field_70161_v + BoatFly.mc.field_71439_g.func_184187_bx().field_70179_y, BoatFly.mc.field_71439_g.field_70177_z, BoatFly.mc.field_71439_g.field_70125_A, true));
            BoatFly.mc.field_71439_g.func_184187_bx().field_70163_u -= /*EL:79*/42069.0;
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:81*/(Packet)new CPacketVehicleMove());
            BoatFly.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:82*/(Packet)new CPacketSteerBoat(true, true));
        }
    }
    
    private double[] moveLooking(final int a1) {
        /*SL:87*/return new double[] { BoatFly.mc.field_71439_g.field_70177_z, a1 };
    }
    
    public double getRelativeX(final float a1) {
        /*SL:91*/return MathHelper.func_76126_a(-a1 * 0.017453292f);
    }
    
    public double getRelativeZ(final float a1) {
        /*SL:95*/return MathHelper.func_76134_b(a1 * 0.017453292f);
    }
}
