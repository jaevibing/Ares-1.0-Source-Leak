package com.ares.mixin.minecraftforge;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EventBus.class })
public class MixinEventBus
{
    @Redirect(method = { "post" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/eventhandler/IEventListener;invoke(Lnet/minecraftforge/fml/common/eventhandler/Event;)V", remap = false), remap = false)
    private void invoke(final IEventListener v-1, final Event v0) {
        try {
            /*SL:21*/v-1.invoke(v0);
        }
        catch (Throwable v) {
            final String a2 = /*EL:25*/"WARNING!!!! The event bus encountered an error while invoking event " + v0.getClass().getName() + /*EL:27*/"! Luckily your using Ares TM client (Trademarked and patented) so we've prevented your client from crashing! Isn't that lucky!";
            FMLLog.log.warn(/*EL:31*/a2);
            try {
                /*SL:35*/Minecraft.func_71410_x().field_71439_g.func_145747_a((ITextComponent)new TextComponentString(a2));
            }
            catch (Throwable a2) {
                /*SL:39*/a2.printStackTrace();
            }
            /*SL:42*/v.printStackTrace();
        }
    }
}
