package com.ares;

import net.minecraft.client.multiplayer.ServerData;
import club.minnced.discord.rpc.DiscordEventHandlers;
import net.minecraftforge.fml.common.FMLLog;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

public class DiscordPresence
{
    private static final String APP_ID = "659123451973599253";
    private static final DiscordRPC rpc;
    private static DiscordRichPresence presence;
    private static boolean hasStarted;
    
    public static boolean start() {
        FMLLog.log.info(/*EL:35*/"Starting Discord RPC");
        /*SL:37*/if (DiscordPresence.hasStarted) {
            return false;
        }
        DiscordPresence.hasStarted = /*EL:39*/true;
        final DiscordEventHandlers v1 = /*EL:41*/new DiscordEventHandlers();
        /*SL:43*/v1.disconnected = ((a1, a2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(a1) + ", var2: " + a2));
        DiscordPresence.rpc.Discord_Initialize(/*EL:48*/"659123451973599253", v1, true, "");
        DiscordPresence.presence.startTimestamp = /*EL:50*/System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = /*EL:51*/"Main Menu";
        DiscordPresence.presence.state = /*EL:52*/"discord.gg/YPbqmFK";
        DiscordPresence.presence.largeImageKey = /*EL:54*/"areslogored";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
        String details;
        String state;
        int intValue;
        int intValue2;
        ServerData v2;
        String v3;
        String v4;
        String[] v5;
        /*SL:60*/new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DiscordPresence.rpc.Discord_RunCallbacks();
                    details = "";
                    state = "";
                    intValue = 0;
                    intValue2 = 0;
                    if (Globals.mc.func_71387_A()) {
                        details = "Singleplayer";
                    }
                    else if (Globals.mc.func_147104_D() != null) {
                        v2 = Globals.mc.func_147104_D();
                        if (!v2.field_78845_b.equals("")) {
                            details = "Multiplayer";
                            state = v2.field_78845_b;
                            if (v2.field_78846_c != null) {
                                try {
                                    v3 = v2.field_78846_c;
                                    v4 = v3.replaceAll("\\b(?![0-9]|\\/)\\b\\S+", "");
                                    v5 = v4.split("/");
                                    /*SL:154*/if (v5.length >= 2) {
                                        /*SL:155*/intValue = Integer.valueOf(v5[0]);
                                        intValue2 = Integer.valueOf(v5[1]);
                                    }
                                }
                                catch (Exception ex3) {}
                            }
                            if (state.contains("2b2t.org")) {
                                try {
                                    if (Ares.lastChat.startsWith("Position in queue: ")) {
                                        state = state + " " + Integer.parseInt(Ares.lastChat.substring(19)) + " in queue";
                                    }
                                }
                                catch (Throwable v6) {
                                    v6.printStackTrace();
                                }
                            }
                        }
                    }
                    else {
                        details = "Main Menu";
                        state = "discord.gg/ncQkFKU";
                    }
                    if (!details.equals(DiscordPresence.presence.details) || !state.equals(DiscordPresence.presence.state)) {
                        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    DiscordPresence.presence.details = details;
                    DiscordPresence.presence.state = state;
                    DiscordPresence.presence.partySize = intValue;
                    DiscordPresence.presence.partyMax = intValue2;
                    DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException ex2) {
                    ex2.printStackTrace();
                }
            }
            return;
        }, "Discord-RPC-Callback-Handler").start();
        FMLLog.log.info("Discord RPC initialised succesfully");
        return true;
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
        DiscordPresence.hasStarted = false;
    }
}
