package com.ares.hack.hacks.hud.impl.elements;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.utils.ColourUtils;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Ping", description = "Shows the ping", category = EnumCategory.HUD, defaultIsVisible = false)
public class PingElement extends MoveableHudElement
{
    public PingElement() {
        super("Ping");
    }
    
    @Override
    public void render(final int v1, final int v2, final float v3) {
        /*SL:22*/super.render(v1, v2, v3);
        /*SL:23*/if (!this.shouldRender()) {
            return;
        }
        int v4 = /*EL:25*/0;
        /*SL:26*/if (PingElement.mc.field_71441_e != null && PingElement.mc.field_71441_e.field_72995_K && PingElement.mc.func_147114_u() != null) {
            EntityPlayer a2 = (EntityPlayer)PingElement.mc.field_71439_g;
            /*SL:30*/if (a2 != null) {
                /*SL:32*/a2 = PingElement.mc.func_147114_u().func_175102_a(a2.func_110124_au());
                /*SL:36*/if (a2 != null) {
                    /*SL:38*/v4 = a2.func_178853_c();
                }
            }
        }
        final String v5 = /*EL:43*/v4 + " ping";
        /*SL:45*/ColourUtils.drawRainbowString(v5, this.getPos().x, this.getPos().y);
        /*SL:47*/this.getSize().x = PingElement.mc.field_71466_p.func_78256_a(v5);
        /*SL:48*/this.getSize().y = PingElement.mc.field_71466_p.field_78288_b;
    }
}
