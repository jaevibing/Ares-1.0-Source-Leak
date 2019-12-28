package com.ares.hack.hacks.chatbot;

import net.minecraft.client.gui.inventory.GuiInventory;
import java.util.Objects;

public class AnnouncerScriptHandler extends ChatBotScriptHandler
{
    private static final String FILENAME = "Ares/announcer.js";
    
    public AnnouncerScriptHandler() throws Exception {
        this.scriptHandler = new ScriptHandler().eval(ChatBotScriptHandler.getScriptContents("Ares/announcer.js")).addLogger(AnnouncerScriptHandler.logger);
    }
    
    String onSendMessage(final String v2) {
        try {
            /*SL:22*/return Objects.<String>requireNonNull(this.scriptHandler.invokeFunction("onSendMessage", v2));
        }
        catch (Exception a1) {
            /*SL:26*/a1.printStackTrace();
            /*SL:29*/return v2;
        }
    }
    
    String onMove(final int a1) {
        /*SL:32*/return this.invokeForString("onMove", a1);
    }
    
    String onAttack(final String a1) {
        /*SL:34*/return this.invokeForString("onAttack", a1);
    }
    
    String onBlocksBreak(final int a1, final String a2) {
        /*SL:36*/return this.invokeForString("onBlocksBreak", a1, a2);
    }
    
    String onBlocksPlace(final int a1, final String a2) {
        /*SL:38*/return this.invokeForString("onBlocksPlace", a1, a2);
    }
    
    String onOpenInventory(final GuiInventory a1) {
        /*SL:40*/return this.invokeForString("onOpenInventory", new Object[0]);
    }
    
    String onScreenshot() {
        /*SL:42*/return this.invokeForString("onScreenshot", new Object[0]);
    }
    
    String onModuleEnabled() {
        /*SL:44*/return this.invokeForString("onModuleEnabled", new Object[0]);
    }
    
    String onModuleDisabled() {
        /*SL:46*/return this.invokeForString("onModuleDisabled", new Object[0]);
    }
    
    String onPlayerJoin() {
        /*SL:48*/return this.invokeForString("onPlayerJoin", new Object[0]);
    }
    
    String onPlayerLeave() {
        /*SL:50*/return this.invokeForString("onPlayerLeave", new Object[0]);
    }
    
    private String invokeForString(final String v1, final Object... v2) {
        try {
            /*SL:56*/return (String)this.scriptHandler.invokeFunction(v1, v2);
        }
        catch (Exception a1) {
            /*SL:60*/a1.printStackTrace();
            /*SL:61*/return null;
        }
    }
}
