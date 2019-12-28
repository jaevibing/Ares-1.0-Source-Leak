package com.ares.mixin;

import org.spongepowered.asm.mixin.Overwrite;
import java.util.Iterator;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.gui.chat.InfiniteChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Shadow
    @Final
    private Minecraft field_146247_f;
    @Shadow
    private int field_146250_j;
    @Shadow
    private boolean field_146251_k;
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Shadow
    @Final
    private List<ChatLine> field_146252_h;
    
    @Shadow
    public abstract void func_146242_c(final int p0);
    
    @Shadow
    public abstract int func_146228_f();
    
    @Shadow
    public abstract float func_146244_h();
    
    @Shadow
    public abstract boolean func_146241_e();
    
    @Shadow
    public abstract void func_146229_b(final int p0);
    
    @Overwrite
    private void func_146237_a(final ITextComponent a3, final int a4, final int v1, final boolean v2) {
        /*SL:41*/if (a4 != 0) {
            this.func_146242_c(a4);
        }
        /*SL:43*/for (final ITextComponent a5 : GuiUtilRenderComponents.func_178908_a(a3, MathHelper.func_76141_d(this.func_146228_f() / this.func_146244_h()), this.field_146247_f.field_71466_p, false, false)) {
            /*SL:45*/if (this.func_146241_e() && this.field_146250_j > 0) {
                /*SL:47*/this.field_146251_k = true;
                /*SL:48*/this.func_146229_b(1);
            }
            /*SL:51*/this.field_146253_i.add(0, new ChatLine(v1, a5, a4));
        }
        final InfiniteChat v3 = /*EL:54*/new InfiniteChat();
        MinecraftForge.EVENT_BUS.post(/*EL:55*/(Event)v3);
        /*SL:57*/if (v3.getResult() == Event.Result.ALLOW && !v2) {
            /*SL:59*/this.field_146252_h.add(0, new ChatLine(v1, a3, a4));
        }
        else {
            /*SL:63*/while (this.field_146253_i.size() > 100) {
                /*SL:65*/this.field_146253_i.remove(this.field_146253_i.size() - 1);
            }
            /*SL:68*/if (!v2) {
                /*SL:70*/this.field_146252_h.add(0, new ChatLine(v1, a3, a4));
                /*SL:72*/while (this.field_146252_h.size() > 100) {
                    /*SL:74*/this.field_146252_h.remove(this.field_146252_h.size() - 1);
                }
            }
        }
    }
}
