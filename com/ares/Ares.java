package com.ares;

import com.ares.event.ServerTick;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import com.ares.event.packet.PacketRecieved;
import net.minecraft.client.gui.GuiScreen;
import com.ares.subguis.IngameMenuGui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import com.ares.hack.settings.BindHandler;
import com.ares.hack.optimizations.ClearGui;
import com.ares.hack.optimizations.Capes;
import com.ares.hack.optimizations.BetterScreenshot;
import com.ares.commands.Command;
import com.ares.commands.HudEditor;
import com.ares.commands.Builder;
import com.ares.commands.Drawn;
import com.ares.commands.Unbind;
import com.ares.commands.ViewInventory;
import com.ares.commands.Import;
import com.ares.commands.FakePlayer;
import com.ares.commands.Bind;
import com.ares.commands.Load;
import com.ares.commands.NomadBase;
import com.ares.commands.Save;
import com.ares.commands.Toggle;
import com.ares.commands.Spectate;
import com.ares.commands.Prefix;
import com.ares.commands.Help;
import com.ares.commands.Friend;
import com.ares.commands.FakeMsg;
import java.util.Set;
import com.ares.api.classloading.SimpleClassLoader;
import com.ares.hack.hacks.ui.Gui;
import com.ares.hack.hacks.hud.Hud;
import net.minecraft.world.border.WorldBorder;
import com.ares.hack.hacks.combat.WebAura;
import com.ares.hack.hacks.misc.VisualRange;
import com.ares.hack.hacks.exploit.VanishDetector;
import java.sql.Time;
import com.ares.hack.hacks.ui.swing.Swing;
import com.ares.hack.hacks.combat.Surround;
import com.ares.hack.hacks.combat.StrengthPotDetect;
import com.ares.hack.hacks.movement.Speed;
import com.ares.hack.hacks.player.Spammer;
import com.ares.hack.hacks.exploit.SoundCoordLogger;
import com.ares.hack.hacks.client.ShulkerPreview;
import com.ares.hack.hacks.ui.ServerNotResponding;
import com.ares.hack.hacks.exploit.ServerCrasher;
import com.ares.hack.hacks.exploit.SecretClose;
import com.ares.hack.hacks.player.Scaffold;
import com.ares.hack.hacks.movement.SafeWalk;
import com.ares.hack.hacks.player.RotationLock;
import com.ares.hack.hacks.misc.ReloadSoundSystem;
import com.ares.hack.hacks.render.RainbowEnchant;
import com.ares.hack.hacks.exploit.PingSpoof;
import com.ares.hack.hacks.movement.PullDown;
import com.ares.hack.hacks.ui.PlayersPotions;
import com.ares.hack.hacks.client.Notifications;
import com.ares.hack.hacks.exploit.NoSwing;
import com.ares.hack.hacks.render.NoRender;
import com.ares.hack.hacks.render.NoHands;
import com.ares.hack.hacks.player.NoBreakDelay;
import com.ares.hack.hacks.client.MsgOnToggle;
import com.ares.hack.hacks.render.MobOwner;
import com.ares.hack.hacks.exploit.MapSpam;
import com.ares.hack.hacks.client.MapTooltip;
import com.ares.hack.hacks.client.LogoutSpot;
import com.ares.hack.hacks.exploit.LagExploit;
import com.ares.hack.hacks.combat.KillAura;
import com.ares.hack.hacks.movement.Jesus;
import com.ares.hack.hacks.ui.ItemInfo;
import com.ares.hack.hacks.player.InstantPortalReuse;
import com.ares.hack.hacks.combat.InvisDetect;
import com.ares.hack.hacks.combat.HopperAura;
import com.ares.hack.hacks.render.HoleEsp;
import com.ares.hack.hacks.player.HighwayBot;
import com.ares.hack.hacks.render.HitboxEsp;
import com.ares.hack.hacks.exploit.GodMode;
import com.ares.hack.hacks.movement.GuiMove;
import com.ares.hack.hacks.misc.GrabCoords;
import com.ares.hack.hacks.player.FastPlace;
import com.ares.hack.hacks.player.FakeRotation;
import com.ares.hack.hacks.player.FakeItem;
import com.ares.hack.hacks.exploit.FakeCreative;
import com.ares.hack.hacks.client.ExtraTab;
import com.ares.hack.hacks.movement.ElytraFlight;
import com.ares.hack.hacks.client.DebugCrosshair;
import com.ares.hack.hacks.exploit.CoordTpExploit;
import com.ares.hack.hacks.ui.Coordinates;
import com.ares.hack.hacks.player.ConstantQMain;
import com.ares.hack.hacks.player.ChunkLogger;
import com.ares.hack.hacks.render.ChestESP;
import com.ares.hack.hacks.player.ChatModifier;
import com.ares.hack.hacks.client.ChatFilter;
import com.ares.hack.hacks.chatbot.ChatBot;
import com.ares.hack.hacks.misc.ChatAppend;
import com.ares.hack.hacks.combat.BoatAura;
import com.ares.hack.hacks.movement.BoatFly;
import com.ares.hack.hacks.client.BetterSign;
import com.ares.hack.hacks.client.BetterHighlightBox;
import com.ares.hack.hacks.client.BetterChat;
import com.ares.hack.hacks.player.AutoWither;
import com.ares.hack.hacks.combat.AutoTrap;
import com.ares.hack.hacks.combat.AutoTotem;
import com.ares.hack.hacks.misc.AutoReply;
import com.ares.hack.hacks.combat.AutoEz;
import com.ares.hack.hacks.combat.AutoCrystal;
import com.ares.hack.hacks.combat.AutoBedBomb;
import com.ares.hack.hacks.combat.Auto32k;
import com.ares.hack.hacks.exploit.AntiLag;
import com.ares.hack.hacks.client.AntiProfanity;
import com.ares.hack.hacks.render.AntiOverlay;
import com.ares.hack.hacks.client.AntiFOV;
import com.ares.hack.hacks.misc.AntiFall;
import com.ares.hack.hacks.misc.AntiChunkBan;
import com.ares.hack.hacks.combat.AntiDeathScreen;
import com.ares.hack.hacks.player.AntiBedTrap;
import com.ares.hack.hacks.player.AntiAFK;
import com.ares.hack.hacks.chatbot.Announcer;
import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.ares.HacksInitialisation;
import com.ares.gui.guis.clickgui.handlers.OptionsHandler;
import com.ares.gui.guis.clickgui.handlers.HacksHandler;
import com.ares.gui.guis.clickgui.handlers.CategoriesHandler;
import com.ares.gui.guis.clickgui.GuiUtil;
import org.lwjgl.opengl.Display;
import com.ares.utils.data.CapeUtils;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import com.ares.config.ConfigManager;
import com.ares.config.SetConfigs;
import net.minecraftforge.common.MinecraftForge;
import com.ares.utils.data.FriendUtils;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import java.io.File;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.ares.hack.hacks.hud.HudManager;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "ares", version = "1.0", clientSideOnly = true)
public class Ares
{
    public static final String MODID = "ares";
    public static final String VERSION = "1.0";
    public static String providedLicense;
    private static Boolean isDevMode;
    public static HudManager hudManager;
    static String lastChat;
    
    public static boolean getDevMode() {
        /*SL:88*/if (Ares.isDevMode != null) {
            /*SL:90*/return Ares.isDevMode;
        }
        try {
            final BufferedReader v1 = /*EL:94*/new BufferedReader(new FileReader("Ares/optimizations.txt"));
            String v2;
            /*SL:97*/while ((v2 = v1.readLine()) != null) {
                /*SL:99*/if (v2.equals("dev.enable.debugger")) {
                    return true;
                }
            }
            /*SL:102*/v1.close();
            /*SL:103*/return false;
        }
        catch (Exception v3) {
            /*SL:104*/return false;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent a1) {
        FMLLog.log.info(/*EL:110*/"\n  /$$$$$$\n /$$__  $$\n| $$  \\ $$  /$$$$$$   /$$$$$$   /$$$$$$$\n| $$$$$$$$ /$$__  $$ /$$__  $$ /$$_____/\n| $$__  $$| $$  \\__/| $$$$$$$$|  $$$$$$\n| $$  | $$| $$      | $$_____/ \\____  $$\n| $$  | $$| $$      |  $$$$$$$ /$$$$$$$/\n|__/  |__/|__/       \\_______/|_______/\n");
        FMLLog.log.info(/*EL:120*/"Loading ares...");
        /*SL:122*/DiscordPresence.start();
        final File v1 = /*EL:124*/new File("Ares");
        /*SL:125*/if (!v1.exists()) {
            /*SL:128*/v1.mkdir();
        }
    }
    
    @Mod.EventHandler
    public void postInit(final FMLInitializationEvent a1) {
        /*SL:135*/FriendUtils.read();
        MinecraftForge.EVENT_BUS.register(/*EL:137*/(Object)new SetConfigs());
        MinecraftForge.EVENT_BUS.register(/*EL:138*/(Object)this);
        /*SL:141*/this.initHacks();
        /*SL:142*/this.initSettings();
        /*SL:143*/this.initCommands();
        /*SL:144*/this.initGui();
        /*SL:145*/this.initOptimizations();
        Ares.hudManager = /*EL:147*/new HudManager();
        System.out.println(/*EL:150*/"Reading config");
        /*SL:151*/ConfigManager.read();
        /*SL:153*/Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("System shutdown, saving configs");
            HackManager.getAll().forEach(BaseHack::onDestroy);
            FriendUtils.save();
            ConfigManager.save();
            return;
        }));
        /*SL:164*/new CapeUtils();
        setTitle();
    }
    
    public static void setTitle() {
        /*SL:171*/Display.setTitle("Ares 1.0");
    }
    
    private void initGui() {
        /*SL:176*/GuiUtil.initializeGui();
        MinecraftForge.EVENT_BUS.register(/*EL:177*/(Object)new CategoriesHandler());
        MinecraftForge.EVENT_BUS.register(/*EL:178*/(Object)new HacksHandler());
        MinecraftForge.EVENT_BUS.register(/*EL:179*/(Object)new OptionsHandler());
    }
    
    private void initHacks() {
        MinecraftForge.EVENT_BUS.post(/*EL:184*/(Event)new HacksInitialisation.Pre());
        final Class[] v1 = /*EL:186*/{ Announcer.class, AntiAFK.class, AntiBedTrap.class, AntiDeathScreen.class, AntiChunkBan.class, AntiFall.class, AntiFOV.class, AntiOverlay.class, AntiProfanity.class, AntiLag.class, Auto32k.class, AutoBedBomb.class, AutoCrystal.class, AutoEz.class, AutoReply.class, AutoTotem.class, AutoTrap.class, AutoWither.class, BetterChat.class, BetterHighlightBox.class, BetterSign.class, BoatFly.class, BoatAura.class, ChatAppend.class, ChatBot.class, ChatFilter.class, ChatModifier.class, ChestESP.class, ChunkLogger.class, ConstantQMain.class, Coordinates.class, CoordTpExploit.class, DebugCrosshair.class, ElytraFlight.class, ExtraTab.class, FakeCreative.class, FakeItem.class, FakeRotation.class, FastPlace.class, GrabCoords.class, GuiMove.class, GodMode.class, HitboxEsp.class, HighwayBot.class, HoleEsp.class, HopperAura.class, InvisDetect.class, InstantPortalReuse.class, ItemInfo.class, Jesus.class, KillAura.class, LagExploit.class, LogoutSpot.class, MapTooltip.class, MapSpam.class, MobOwner.class, MsgOnToggle.class, NoBreakDelay.class, NoHands.class, NoRender.class, NoSwing.class, Notifications.class, PlayersPotions.class, PullDown.class, PingSpoof.class, RainbowEnchant.class, ReloadSoundSystem.class, RotationLock.class, SafeWalk.class, Scaffold.class, SecretClose.class, ServerCrasher.class, ServerNotResponding.class, ShulkerPreview.class, SoundCoordLogger.class, Spammer.class, Speed.class, StrengthPotDetect.class, Surround.class, Swing.class, Time.class, VanishDetector.class, VisualRange.class, WebAura.class, WorldBorder.class, Hud.class, Gui.class };
        final Set<Class> v2 = /*EL:287*/new SimpleClassLoader().build(v1).initialise().getErroredClasses();
        FMLLog.log.info(/*EL:289*/"Ares tried to load " + v1.length + " hacks, out of which " + v2.size() + " failed");
        FMLLog.log.info(/*EL:290*/"Failed hack: " + v2.toString());
        MinecraftForge.EVENT_BUS.post(/*EL:292*/(Event)new HacksInitialisation.Post(HackManager.getAll()));
        FMLLog.log.info(/*EL:294*/"Ares startup finished ");
    }
    
    private void initCommands() {
        final Class[] v1 = /*EL:299*/{ FakeMsg.class, Friend.class, Help.class, Prefix.class, Spectate.class, Toggle.class, Save.class, NomadBase.class, Load.class, Bind.class, FakePlayer.class, Import.class, ViewInventory.class, Unbind.class, Drawn.class, Builder.class, HudEditor.class };
        final Set<Class> v2 = /*EL:320*/new SimpleClassLoader().build(v1).initialise().getErroredClasses();
        FMLLog.log.info(/*EL:322*/"Ares tried to load " + v1.length + " commands, out of which " + v2.size() + " failed");
        FMLLog.log.info(/*EL:323*/"Failed commands: " + v2.toString());
        final Command v3 = /*EL:325*/new Command();
        MinecraftForge.EVENT_BUS.register(/*EL:326*/(Object)v3);
    }
    
    private void initOptimizations() {
        final Class[] v1 = /*EL:331*/{ BetterScreenshot.class, Capes.class, ClearGui.class };
        final Set<Class> v2 = /*EL:338*/new SimpleClassLoader().build(v1).initialise().getErroredClasses();
        FMLLog.log.info(/*EL:340*/"Ares tried to load " + v1.length + " optimizations, out of which " + v2.size() + " failed");
        FMLLog.log.info(/*EL:341*/"Failed optimizations: " + v2.toString());
    }
    
    private void initSettings() {
        MinecraftForge.EVENT_BUS.register(/*EL:346*/(Object)new BindHandler());
    }
    
    @SubscribeEvent
    public void onChatRecieved(final ClientChatReceivedEvent a1) {
        Ares.lastChat = /*EL:354*/a1.getMessage().func_150260_c();
    }
    
    @SubscribeEvent
    public void onGuiOpened(final GuiOpenEvent a1) {
        /*SL:365*/if (a1.getGui() instanceof GuiIngameMenu) {
            /*SL:367*/a1.setGui((GuiScreen)new IngameMenuGui());
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketRecieved a1) {
        /*SL:374*/if (a1.packet instanceof SPacketTimeUpdate) {
            MinecraftForge.EVENT_BUS.post(/*EL:376*/(Event)new ServerTick());
        }
    }
    
    static {
        Ares.providedLicense = "";
        Ares.isDevMode = null;
        Ares.lastChat = "";
    }
}
