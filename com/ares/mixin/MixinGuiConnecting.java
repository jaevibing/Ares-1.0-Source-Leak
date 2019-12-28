package com.ares.mixin;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiScreen;

@Mixin({ GuiConnecting.class })
public class MixinGuiConnecting extends GuiScreen
{
    @Shadow
    private NetworkManager field_146371_g;
    
    @Overwrite
    public void func_73863_a(final int a1, final int a2, final float a3) {
        /*SL:24*/this.func_146276_q_();
        String v1 = /*EL:26*/"Unknown";
        final ServerData v2 = /*EL:27*/this.field_146297_k.func_147104_D();
        /*SL:28*/if (v2 != null && v2.field_78845_b != null) {
            /*SL:29*/v1 = v2.field_78845_b;
        }
        /*SL:31*/if (this.field_146371_g == null) {
            /*SL:33*/this.func_73732_a(this.field_146289_q, "Connecting to " + v1 + "...", this.field_146294_l / 2, this.field_146295_m / 2 - 50, 16777215);
        }
        else {
            /*SL:37*/this.func_73732_a(this.field_146289_q, "Logging into " + v1 + "...", this.field_146294_l / 2, this.field_146295_m / 2 - 50, 16777215);
        }
        /*SL:40*/super.func_73863_a(a1, a2, a3);
    }
}
