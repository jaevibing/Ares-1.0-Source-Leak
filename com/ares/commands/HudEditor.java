package com.ares.commands;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.client.gui.GuiScreen;
import com.ares.hack.hacks.hud.impl.GuiEditHud;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;

public class HudEditor extends CommandBase
{
    private boolean toOpen;
    
    public HudEditor() {
        super(new String[] { "hudeditor", "HudEditor", "EditHud" });
        this.toOpen = false;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public boolean execute(final String[] a1) {
        /*SL:23*/return this.toOpen = true;
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent a1) {
        /*SL:29*/if (a1.phase == TickEvent.Phase.END && this.toOpen) {
            /*SL:31*/this.mc.func_152344_a(() -> {
                this.mc.func_147108_a((GuiScreen)new GuiEditHud());
                ChatUtils.printMessage("Opened Hud Editor");
                return;
            });
            /*SL:36*/this.toOpen = false;
        }
    }
    
    @Override
    public String getSyntax() {
        /*SL:43*/return "-hudeditor";
    }
}
