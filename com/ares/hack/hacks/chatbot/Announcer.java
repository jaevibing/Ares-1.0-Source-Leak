package com.ares.hack.hacks.chatbot;

import com.ares.event.world.PlayerLeave;
import com.ares.event.world.PlayerJoin;
import com.ares.event.client.ares.HackDisabled;
import com.ares.event.client.ares.HackEnabled;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import com.ares.event.packet.PacketSent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.TimeUtils;
import com.ares.event.world.PlayerMove;
import com.ares.utils.chat.ChatUtils;
import com.ares.hack.settings.settings.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import java.time.Instant;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Announcer", description = "Don't use this unless your a penis", category = EnumCategory.CHATBOT)
public class Announcer extends BaseHack
{
    private Setting<Boolean> movementMsg;
    private Instant lastSentMovement;
    private Vec3d lastVec;
    private Setting<Boolean> blockMsg;
    private Instant lastSentBlocks;
    private Block lastBlock;
    private int numBlocks;
    private Setting<Boolean> breakMsg;
    private Instant lastSentBreak;
    private Block lastBreak;
    private int numBreaks;
    private Setting<Boolean> attackMsg;
    private Instant lastSentAttack;
    private Setting<Boolean> guiMsg;
    private Setting<Boolean> screenshotMsg;
    private AnnouncerScriptHandler announcerScriptHandler;
    
    public Announcer() {
        this.movementMsg = new BooleanSetting("Movement", this, true);
        this.lastSentMovement = Instant.now();
        this.lastVec = null;
        this.blockMsg = new BooleanSetting("Block Place", this, true);
        this.lastSentBlocks = Instant.now();
        this.lastBlock = null;
        this.numBlocks = 0;
        this.breakMsg = new BooleanSetting("Block Break", this, true);
        this.lastSentBreak = Instant.now();
        this.lastBreak = null;
        this.numBreaks = 0;
        this.attackMsg = new BooleanSetting("Attack Entities", this, true);
        this.lastSentAttack = Instant.now();
        this.guiMsg = new BooleanSetting("Gui", this, true);
        this.screenshotMsg = new BooleanSetting("Screenshot", this, true);
    }
    
    public void onEnabled() {
        try {
            /*SL:69*/this.announcerScriptHandler = new AnnouncerScriptHandler();
        }
        catch (Exception v1) {
            /*SL:73*/this.setEnabled(false);
            /*SL:74*/ChatUtils.printMessage("Failed to initialise Announcer script: " + v1.getMessage(), "red");
            /*SL:75*/v1.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onMovement(final PlayerMove.Pre v2) {
        /*SL:82*/if (this.getEnabled() && TimeUtils.haveSecondsPassed(this.lastSentMovement, Instant.now(), 60L) && this.movementMsg.getValue()) {
            /*SL:84*/if (this.lastVec == null) {
                /*SL:86*/this.lastVec = Announcer.mc.field_71439_g.func_174791_d();
                return;
            }
            final int a1 = /*EL:89*/(int)Math.round(this.lastVec.func_72438_d(Announcer.mc.field_71439_g.func_174791_d()));
            /*SL:91*/if (a1 > 0) {
                /*SL:93*/this.send(this.announcerScriptHandler.onMove(a1));
                /*SL:100*/this.lastSentMovement = Instant.now();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSent v0) {
        /*SL:108*/if (this.getEnabled() && v0.packet instanceof CPacketPlayerTryUseItemOnBlock && this.blockMsg.getValue()) {
            final CPacketPlayerTryUseItemOnBlock v = /*EL:110*/(CPacketPlayerTryUseItemOnBlock)v0.packet;
            final ItemStack v2 = Announcer.mc.field_71439_g.func_184586_b(/*EL:112*/v.func_187022_c());
            /*SL:114*/if (v2.func_77973_b() instanceof ItemBlock) {
                final Block a1 = /*EL:116*/((ItemBlock)v2.func_77973_b()).func_179223_d();
                /*SL:118*/if (this.lastBlock == null) {
                    /*SL:120*/this.lastBlock = a1;
                }
                /*SL:123*/if (this.lastBlock.equals(a1)) {
                    /*SL:125*/++this.numBlocks;
                }
            }
            /*SL:129*/if (TimeUtils.haveSecondsPassed(this.lastSentBlocks, Instant.now(), 60L) && this.numBlocks > 0) {
                /*SL:131*/this.send(this.announcerScriptHandler.onBlocksPlace(this.numBlocks, v2.func_82833_r()));
                /*SL:132*/this.lastSentBlocks = Instant.now();
                /*SL:133*/this.lastBlock = null;
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockBreak(final BlockEvent.BreakEvent v2) {
        /*SL:141*/if (this.getEnabled() && v2.getPlayer().equals((Object)Announcer.mc.field_71439_g)) {
            final Block a1 = /*EL:143*/v2.getState().func_177230_c();
            /*SL:145*/if (this.lastBreak == null) {
                /*SL:147*/this.lastBreak = a1;
            }
            /*SL:150*/if (this.lastBreak.equals(a1)) {
                /*SL:152*/++this.numBreaks;
            }
            /*SL:155*/if (TimeUtils.haveSecondsPassed(this.lastSentBreak, Instant.now(), 60L) && this.numBreaks > 0) {
                /*SL:157*/this.send(this.announcerScriptHandler.onBlocksBreak(this.numBreaks, a1.func_149732_F()));
                /*SL:158*/this.lastSentBreak = Instant.now();
                /*SL:159*/this.lastBreak = null;
            }
        }
    }
    
    @SubscribeEvent
    public void onAttack(final AttackEntityEvent a1) {
        /*SL:167*/if (this.getEnabled() && a1.getTarget() instanceof EntityLivingBase && /*EL:169*/TimeUtils.haveSecondsPassed(this.lastSentAttack, Instant.now(), 60L)) {
            /*SL:171*/this.send(this.announcerScriptHandler.onAttack(a1.getTarget().func_145748_c_().func_150260_c()));
            /*SL:172*/this.lastSentAttack = Instant.now();
        }
    }
    
    @SubscribeEvent
    public void onGui(final GuiOpenEvent a1) {
        /*SL:180*/if (this.getEnabled() && this.guiMsg.getValue() && a1.getGui() != null && a1.getGui() instanceof GuiInventory) {
            /*SL:182*/this.send(this.announcerScriptHandler.onOpenInventory((GuiInventory)a1.getGui()));
        }
    }
    
    @SubscribeEvent
    public void onScreenshot(final ScreenshotEvent a1) {
        /*SL:189*/if (this.getEnabled() && this.screenshotMsg.getValue()) {
            /*SL:190*/this.send(this.announcerScriptHandler.onScreenshot());
        }
    }
    
    @SubscribeEvent
    public void onHackEnabled(final HackEnabled a1) {
        /*SL:196*/if (this.getEnabled()) {
            /*SL:198*/this.send(this.announcerScriptHandler.onModuleEnabled());
        }
    }
    
    @SubscribeEvent
    public void onHackDisabled(final HackDisabled a1) {
        /*SL:205*/if (this.getEnabled()) {
            /*SL:207*/this.send(this.announcerScriptHandler.onModuleDisabled());
        }
    }
    
    @SubscribeEvent
    public void onPlayerJoin(final PlayerJoin a1) {
        /*SL:214*/if (this.getEnabled()) {
            /*SL:216*/this.send(this.announcerScriptHandler.onPlayerJoin());
        }
    }
    
    @SubscribeEvent
    public void onPlayerLeave(final PlayerLeave a1) {
        /*SL:223*/if (this.getEnabled()) {
            /*SL:225*/this.send(this.announcerScriptHandler.onPlayerLeave());
        }
    }
    
    private void send(String a1) {
        /*SL:232*/if (a1 == null) {
            return;
        }
        /*SL:234*/a1 = this.announcerScriptHandler.onSendMessage(a1);
        /*SL:236*/if (a1 == null) {
            return;
        }
        /*SL:238*/if (this.getEnabled()) {
            Announcer.mc.field_71439_g.func_71165_d(/*EL:239*/a1 + " thanks to Ares Client");
        }
    }
}
