package com.ares.mixin;

import net.minecraft.client.gui.GuiChat;
import com.ares.commands.Command;
import org.lwjgl.input.Keyboard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.profiler.Profiler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
public class MixinMinecraft
{
    @Shadow
    @Final
    public Profiler field_71424_I;
    @Shadow
    public GameSettings field_71474_y;
    @Shadow
    public GuiScreen field_71462_r;
    
    @Redirect(method = { "runTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 0))
    private void endStartGUISection(final Profiler a1, final String a2) {
        /*SL:36*/a1.func_76318_c("clickgui");
    }
    
    @Redirect(method = { "runTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;tick()V", ordinal = 0))
    private void tickTextureManagerWithCorrectProfiler(final TextureManager a1) {
        /*SL:45*/this.field_71424_I.func_76318_c("textures");
        /*SL:46*/a1.func_110550_d();
        /*SL:47*/this.field_71424_I.func_76318_c("clickgui");
    }
    
    @Redirect(method = { "processKeyBinds" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActiveWrapper(final EntityPlayerSP a1) {
        try {
            /*SL:59*/if (this.field_71474_y.field_74343_n != EntityPlayer.EnumChatVisibility.HIDDEN && this.field_71462_r == null && Keyboard.getEventKeyState() && Keyboard.getEventCharacter() != '\0' && Command.commandPrefix != null && Command.commandPrefix.toCharArray().length >= 1 && /*EL:61*/Keyboard.getEventCharacter() == Command.commandPrefix.toCharArray()[0]) {
                /*SL:63*/((Minecraft)this).func_147108_a((GuiScreen)new GuiChat(Command.commandPrefix));
            }
        }
        catch (Exception ex) {}
        /*SL:71*/return a1.func_184587_cr();
    }
}
