package com.ares.hack.hacks.client;

import net.minecraft.client.renderer.entity.RenderManager;
import com.ares.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.Vec3d;
import java.awt.Color;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.Utils;
import com.ares.event.world.PlayerLeave;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.HashMap;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Logout Spots", description = "Show the logout spots of other players", category = EnumCategory.CLIENT)
public class LogoutSpot extends BaseHack
{
    private HashMap<String, AxisAlignedBB> logoutSpots;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    
    public LogoutSpot() {
        this.logoutSpots = new HashMap<String, AxisAlignedBB>();
        this.red = new IntegerSetting("Red", this, 0, 0, 255);
        this.green = new IntegerSetting("Green", this, 0, 0, 255);
        this.blue = new IntegerSetting("Blue", this, 0, 0, 255);
        this.alpha = new IntegerSetting("Alpha", this, 255, 0, 255);
    }
    
    @SubscribeEvent
    public void onPlayerLeave(final PlayerLeave v-2) {
        final EntityPlayer func_152378_a = LogoutSpot.mc.field_71441_e.func_152378_a(/*EL:52*/v-2.gameProfile.getId());
        /*SL:54*/if (func_152378_a != null && LogoutSpot.mc.field_71439_g != null && !LogoutSpot.mc.field_71439_g.equals((Object)func_152378_a)) {
            final AxisAlignedBB a1 = /*EL:56*/func_152378_a.func_174813_aQ();
            final String v1 = /*EL:57*/func_152378_a.getDisplayNameString();
            /*SL:59*/if (this.logoutSpots.get(v1) != null) {
                /*SL:61*/this.logoutSpots.remove(v1);
            }
            /*SL:64*/this.logoutSpots.put(v1, a1);
            /*SL:66*/if (this.getEnabled()) {
                /*SL:68*/ChatUtils.printMessage(/*EL:69*/String.format("PlayerPreviewElement '%s' disconnected at %s", v1, Utils.vectorToString(func_152378_a.func_174791_d(), new boolean[0])), "red");
            }
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent a1) {
        /*SL:79*/if (LogoutSpot.mc.field_71441_e == null && this.logoutSpots.size() != 0) {
            this.logoutSpots.clear();
        }
    }
    
    public void onRender3d() {
        /*SL:85*/if (!this.getEnabled()) {
            return;
        }
        final Color v2 = /*EL:87*/new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
        Vec3d v3;
        final Vec3d a4;
        final double v4;
        final double v5;
        final RenderManager v6;
        final String v7;
        final int v8;
        final Color color;
        /*SL:88*/this.logoutSpots.forEach((a3, v1) -> {
            v3 = v1.func_189972_c();
            if (LogoutSpot.mc.field_71439_g.func_70092_e(v3.field_72450_a, v3.field_72448_b, v3.field_72449_c) > 2500.0) {
                a4 = v3.func_178788_d(new Vec3d(LogoutSpot.mc.func_175598_ae().field_78730_l, LogoutSpot.mc.func_175598_ae().field_78731_m, LogoutSpot.mc.func_175598_ae().field_78728_n)).func_72432_b();
                /*SL:138*/v3 = new Vec3d(LogoutSpot.mc.func_175598_ae().field_78730_l + a4.field_72450_a * 50.0, LogoutSpot.mc.func_175598_ae().field_78731_m + a4.field_72448_b * 50.0, LogoutSpot.mc.func_175598_ae().field_78728_n + a4.field_72449_c * 50.0);
            }
            v4 = LogoutSpot.mc.field_71439_g.func_70011_f(v3.field_72450_a, v3.field_72448_b, v3.field_72449_c) / 4.0;
            v5 = Math.max(1.6, v4);
            v6 = LogoutSpot.mc.func_175598_ae();
            GL11.glPushMatrix();
            GL11.glTranslated(-v6.field_78730_l, -v6.field_78731_m, -v6.field_78728_n);
            GL11.glTranslatef((float)v3.field_72450_a + 0.5f, (float)v3.field_72448_b + 0.5f, (float)v3.field_72449_c + 0.5f);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-v6.field_78735_i, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(v6.field_78732_j, 1.0f, 0.0f, 0.0f);
            GL11.glScaled(-v5, -v5, v5);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            v7 = a3 + " (" + LogoutSpot.mc.field_71439_g.func_70011_f(v1.func_189972_c().field_72450_a, v1.func_189972_c().field_72448_b, v1.func_189972_c().field_72449_c) + "m)";
            v8 = LogoutSpot.mc.field_71466_p.func_78256_a(v7) / 2;
            LogoutSpot.mc.field_71466_p.func_175063_a(v7, (float)(-v8), (float)(-(LogoutSpot.mc.field_71466_p.field_78288_b - 1)), color.getRGB());
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glPopMatrix();
            RenderUtils.glStart(color.getRed() / 255, color.getBlue() / 255, color.getRed() / 255, color.getAlpha());
            RenderUtils.drawOutlinedBox(v1);
            RenderUtils.glEnd();
        });
    }
}
