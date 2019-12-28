package com.ares.utils;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import net.minecraft.util.Session;

public class YggdrasilPayload
{
    public static Session loginPassword(final String a2, final String v1) {
        /*SL:14*/if (a2 == null || a2.length() <= 0 || v1 == null || v1.length() <= 0) {
            /*SL:15*/return null;
        }
        final YggdrasilAuthenticationService v2 = /*EL:17*/new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication v3 = /*EL:18*/(YggdrasilUserAuthentication)v2.createUserAuthentication(Agent.MINECRAFT);
        /*SL:19*/v3.setUsername(a2);
        /*SL:20*/v3.setPassword(v1);
        try {
            /*SL:22*/v3.logIn();
            /*SL:23*/return new Session(v3.getSelectedProfile().getName(), v3.getSelectedProfile().getId().toString(), v3.getAuthenticatedToken(), "LEGACY");
        }
        catch (AuthenticationException a3) {
            /*SL:25*/a3.printStackTrace();
            System.out.println(/*EL:26*/"Failed login: " + a2 + ":" + v1);
            /*SL:28*/return null;
        }
    }
    
    public static Session loginCrack(final String a1) {
        /*SL:32*/return new Session(a1, "", "", "LEGACY");
    }
}
