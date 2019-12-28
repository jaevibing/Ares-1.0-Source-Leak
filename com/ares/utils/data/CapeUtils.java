package com.ares.utils.data;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.cape.ShouldCapeEvent;
import com.ares.event.client.cape.FindCapeEvent;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.ResourceLocation;
import java.util.HashMap;

public class CapeUtils
{
    private static HashMap<String, capeStatus> capedUsers;
    private static ResourceLocation devCapeLoc;
    private static ResourceLocation donatorCapeLoc;
    
    public CapeUtils() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        CapeUtils.capedUsers = new HashMap<String, capeStatus>();
        this.getDevs();
        this.getOwners();
    }
    
    private boolean getDevs() {
        /*SL:54*/return this.get(CapeUtils.capedUsers, "https://pastebin.com/raw/g4wjzg5U", capeStatus.dev);
    }
    
    private boolean getOwners() {
        /*SL:59*/return this.get(CapeUtils.capedUsers, "https://pastebin.com/raw/vFGR223T", capeStatus.donator);
    }
    
    private boolean get(final HashMap<String, capeStatus> v-2, final String v-1, final capeStatus v0) {
        try {
            final URL a1 = /*EL:66*/new URL(v-1);
            final BufferedReader a2 = /*EL:67*/new BufferedReader(new InputStreamReader(a1.openStream()));
            String a3;
            /*SL:73*/while ((a3 = a2.readLine()) != null) {
                /*SL:75*/if (!a3.trim().isEmpty()) {
                    /*SL:76*/v-2.put(a3.trim().toLowerCase(), v0);
                }
            }
            /*SL:79*/a2.close();
            System.out.println(/*EL:80*/"Gave " + v0.name() + " capes to: " + v-2.toString());
            /*SL:81*/return true;
        }
        catch (Exception v) {
            /*SL:85*/v.printStackTrace();
            /*SL:87*/return false;
        }
    }
    
    public static capeStatus isCaped(final String a1) {
        /*SL:92*/if (CapeUtils.capedUsers == null) {
            /*SL:94*/new CapeUtils();
        }
        /*SL:97*/return CapeUtils.capedUsers.getOrDefault(a1.trim().toLowerCase(), capeStatus.none);
    }
    
    @SubscribeEvent
    public void onFindCape(final FindCapeEvent a1) {
        final ShouldCapeEvent v1 = /*EL:103*/new ShouldCapeEvent();
        MinecraftForge.EVENT_BUS.post(/*EL:104*/(Event)v1);
        /*SL:106*/if (v1.getResult() == Event.Result.ALLOW) {
            /*SL:108*/switch (isCaped(a1.networkPlayerInfo.func_178845_a().getName())) {
                case dev: {
                    /*SL:111*/a1.capeLoc = CapeUtils.devCapeLoc;
                    /*SL:112*/break;
                }
                case donator: {
                    /*SL:114*/a1.capeLoc = CapeUtils.donatorCapeLoc;
                    break;
                }
            }
        }
    }
    
    static {
        CapeUtils.capedUsers = null;
        CapeUtils.devCapeLoc = new ResourceLocation("ares", "cape_ares_dev.png");
        CapeUtils.donatorCapeLoc = new ResourceLocation("ares", "cape_ares.png");
    }
    
    private enum capeStatus
    {
        dev, 
        donator, 
        none;
    }
}
