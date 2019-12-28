package com.ares.mixin;

import net.minecraft.client.gui.Gui;
import com.ares.event.client.gui.chat.GetChatBackgroundColour;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.gui.chat.GetChatDefaultText;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiChat.class })
public class MixinGuiChat
{
    @Shadow
    protected GuiTextField field_146415_a;
    @Shadow
    private String field_146409_v;
    private static String persistenceText;
    
    @Inject(method = { "onGuiClosed" }, at = { @At("HEAD") })
    private void guiClosedWrapper(final CallbackInfo a1) {
        MixinGuiChat.persistenceText = /*EL:30*/this.field_146415_a.func_146179_b();
    }
    
    @Inject(method = { "<init>()V" }, at = { @At("RETURN") })
    private void createGuiWrapper(final CallbackInfo v2) {
        /*SL:36*/if (MixinGuiChat.persistenceText != null) {
            final GetChatDefaultText a1 = /*EL:38*/new GetChatDefaultText(this.field_146409_v, MixinGuiChat.persistenceText);
            MinecraftForge.EVENT_BUS.post(/*EL:39*/(Event)a1);
            /*SL:40*/this.field_146409_v = a1.defaultText;
        }
    }
    
    @Redirect(method = { "keyTyped(CI)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;sendChatMessage(Ljava/lang/String;)V"))
    private void onSendMessage(final GuiChat a1, final String a2) {
        MixinGuiChat.persistenceText = /*EL:47*/null;
        /*SL:48*/a1.func_175275_f(a2);
    }
    
    @Redirect(method = { "drawScreen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;drawRect(IIIII)V"))
    private void drawBackgroundWrapper(final int a1, final int a2, final int a3, final int a4, final int a5) {
        final GetChatBackgroundColour v1 = /*EL:54*/new GetChatBackgroundColour(a5);
        MinecraftForge.EVENT_BUS.post(/*EL:55*/(Event)v1);
        /*SL:56*/Gui.func_73734_a(a1, a2, a3, a4, v1.color.getRGB());
    }
}
