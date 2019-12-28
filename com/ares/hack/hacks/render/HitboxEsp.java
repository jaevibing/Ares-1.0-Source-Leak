package com.ares.hack.hacks.render;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import com.ares.utils.data.FriendUtils;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import com.ares.utils.render.RenderUtils;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Hitbox ESP", description = "See outlines of players through walls", category = EnumCategory.RENDER, defaultOn = true, defaultIsVisible = false)
public class HitboxEsp extends BaseHack
{
    private Setting<Boolean> boxFriends;
    private Setting<Boolean> boxOthers;
    
    public HitboxEsp() {
        this.boxFriends = new BooleanSetting("Show friends hitbox", this, true);
        this.boxOthers = new BooleanSetting("Show others hitbox", this, false);
    }
    
    public void onRender3d() {
        /*SL:22*/if (!this.getEnabled() || HitboxEsp.mc.field_71441_e.field_73010_i.size() <= 0) {
            return;
        }
        /*SL:24*/RenderUtils.glStart(0.0f, 0.0f, 0.0f, 1.0f);
        /*SL:25*/this.renderOthers();
        /*SL:27*/GL11.glColor4f(0.0f, 255.0f, 0.0f, 1.0f);
        /*SL:28*/this.renderFriends();
        /*SL:30*/RenderUtils.glEnd();
    }
    
    private void renderFriends() {
        /*SL:35*/if (this.boxFriends.getValue()) {
            /*SL:37*/for (final EntityPlayer v0 : HitboxEsp.mc.field_71441_e.field_73010_i) {
                /*SL:39*/if (FriendUtils.isFriend(v0) && !v0.func_110124_au().equals(HitboxEsp.mc.field_71439_g.func_110124_au())) {
                    final AxisAlignedBB v = /*EL:41*/v0.func_174813_aQ();
                    /*SL:42*/RenderUtils.drawOutlinedBox(v);
                }
            }
        }
    }
    
    private void renderOthers() {
        /*SL:50*/if (this.boxOthers.getValue()) {
            /*SL:52*/for (final EntityPlayer v0 : HitboxEsp.mc.field_71441_e.field_73010_i) {
                /*SL:54*/if (!FriendUtils.isFriend(v0) && !v0.func_110124_au().equals(HitboxEsp.mc.field_71439_g.func_110124_au())) {
                    final AxisAlignedBB v = /*EL:56*/v0.func_174813_aQ();
                    /*SL:57*/RenderUtils.drawOutlinedBox(v);
                }
            }
        }
    }
}
