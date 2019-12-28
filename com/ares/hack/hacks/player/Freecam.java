package com.ares.hack.hacks.player;

import com.ares.utils.render.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import com.ares.event.entity.IsCurrentRenderViewEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import com.ares.event.client.movement.TurnPlayerFromMouseInput;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.ares.hack.settings.settings.number.DoubleSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Freecam", description = "View the world from a free moving camera", category = EnumCategory.PLAYER)
public class Freecam extends BaseHack
{
    private Setting<Double> speed;
    EntityOtherPlayerMP camera;
    
    public Freecam() {
        this.speed = new DoubleSetting("Speed", this, 1.0, 0.1, 2.0);
    }
    
    public void onEnabled() {
        /*SL:31*/(this.camera = new EntityOtherPlayerMP((World)Freecam.mc.field_71441_e, Freecam.mc.field_71449_j.func_148256_e())).func_82149_j((Entity)Freecam.mc.field_71439_g);
        /*SL:33*/this.camera.field_70144_Y = 1.0f;
        /*SL:34*/this.camera.field_70145_X = true;
        boolean v0 = /*EL:36*/false;
        /*SL:37*/for (int v = -10; v > -150; ++v) {
            /*SL:39*/if (Freecam.mc.field_71441_e.func_73045_a(v) == null) {
                Freecam.mc.field_71441_e.func_73027_a(/*EL:41*/v, (Entity)this.camera);
                /*SL:43*/v0 = true;
                /*SL:44*/break;
            }
        }
        /*SL:49*/if (!v0) {
            /*SL:51*/ChatUtils.printMessage("No available entity ids!");
            /*SL:52*/this.setEnabled(false);
            /*SL:53*/return;
        }
        Freecam.mc.func_175607_a(/*EL:57*/(Entity)this.camera);
    }
    
    public void onDisabled() {
        Freecam.mc.func_175607_a((Entity)Freecam.mc.field_71439_g);
        Freecam.mc.field_71441_e.func_72900_e(/*EL:64*/(Entity)this.camera);
        /*SL:65*/this.camera = null;
    }
    
    @SubscribeEvent
    public void onTurnPlayer(final TurnPlayerFromMouseInput a1) {
        /*SL:71*/if (this.getEnabled() && /*EL:74*/!Keyboard.isKeyDown(56)) {
            /*SL:76*/a1.entity = (Entity)this.camera;
            /*SL:77*/this.camera.field_70177_z = a1.yaw;
            /*SL:78*/this.camera.field_70125_A = a1.pitch;
            /*SL:79*/this.camera.field_70759_as = a1.yaw;
            System.out.println(/*EL:80*/"Redirected turning");
        }
    }
    
    @SubscribeEvent
    public void isCurrentRenderViewEntity(final IsCurrentRenderViewEntity a1) {
        /*SL:88*/if (this.getEnabled()) {
            /*SL:90*/a1.result = true;
        }
    }
    
    public void onLogic() {
        /*SL:97*/if (this.getEnabled() && this.camera != null) {
            final float v1 = Freecam.mc.field_71439_g.field_71158_b.field_78902_a;
            final float v2 = Freecam.mc.field_71439_g.field_71158_b.field_192832_b;
            double v3 = /*EL:106*/this.camera.field_70165_t;
            double v4 = /*EL:107*/this.camera.field_70163_u;
            double v5 = /*EL:108*/this.camera.field_70161_v;
            final float v6 = /*EL:110*/MathHelper.func_76126_a(this.camera.field_70177_z * 0.017453292f);
            final float v7 = /*EL:111*/MathHelper.func_76134_b(this.camera.field_70177_z * 0.017453292f);
            /*SL:112*/v3 += (v1 * v7 - v2 * v6) * this.speed.getValue();
            /*SL:113*/v5 += (v2 * v7 + v1 * v6) * this.speed.getValue();
            /*SL:115*/if (Freecam.mc.field_71474_y.field_74314_A.func_151470_d()) {
                v4 += this.speed.getValue();
            }
            /*SL:116*/if (Freecam.mc.field_71474_y.field_74311_E.func_151470_d()) {
                v4 -= this.speed.getValue();
            }
            /*SL:118*/this.camera.func_70107_b(v3, v4, v5);
            /*SL:120*/this.camera.func_70071_h_();
        }
    }
    
    public void onRender3d() {
        /*SL:127*/if (this.getEnabled() && this.camera != null) {
            /*SL:129*/GL11.glEnable(3042);
            /*SL:130*/GL11.glBlendFunc(770, 771);
            /*SL:131*/GL11.glEnable(2848);
            /*SL:132*/GL11.glLineWidth(2.0f);
            /*SL:133*/GL11.glDisable(3553);
            /*SL:134*/GL11.glDisable(2929);
            /*SL:135*/GL11.glPushMatrix();
            /*SL:136*/GL11.glTranslated(-Freecam.mc.func_175598_ae().field_78730_l, -Freecam.mc.func_175598_ae().field_78731_m, -Freecam.mc.func_175598_ae().field_78728_n);
            /*SL:137*/GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            /*SL:138*/GL11.glPushMatrix();
            /*SL:139*/GL11.glTranslated(Freecam.mc.field_71439_g.field_70165_t, Freecam.mc.field_71439_g.field_70163_u, Freecam.mc.field_71439_g.field_70161_v);
            /*SL:140*/GL11.glScaled(Freecam.mc.field_71439_g.field_70130_N + 0.1, Freecam.mc.field_71439_g.field_70131_O + 0.1, Freecam.mc.field_71439_g.field_70130_N + 0.1);
            final AxisAlignedBB v1 = /*EL:141*/new AxisAlignedBB(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5);
            /*SL:142*/RenderUtils.drawOutlinedBox(v1);
            /*SL:143*/GL11.glPopMatrix();
            /*SL:144*/GL11.glEnable(2929);
            /*SL:145*/GL11.glEnable(3553);
            /*SL:146*/GL11.glDisable(3042);
            /*SL:147*/GL11.glDisable(2848);
        }
    }
}
