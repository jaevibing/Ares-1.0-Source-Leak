package com.ares.api;

import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import com.ares.utils.YggdrasilAuthenticator;
import com.ares.natives.EncryptedStringPool;
import com.ares.Globals;

public class AuthUtils implements Globals
{
    public static boolean mcLogin(final String a2, final String v1) throws AuthenticationException {
        System.out.println(/*EL:32*/EncryptedStringPool.poolGet(13) + a2);
        final YggdrasilAuthenticator v2 = /*EL:33*/new YggdrasilAuthenticator(a2, v1);
        /*SL:34*/if (v2.login()) {
            try {
                /*SL:38*/ObfuscationReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)Globals.mc, (Object)v2.getSession(), new String[] { EncryptedStringPool.poolGet(14), EncryptedStringPool.poolGet(15) });
            }
            catch (Exception a3) {
                /*SL:42*/a3.printStackTrace();
                /*SL:43*/return false;
            }
            System.out.println(/*EL:46*/EncryptedStringPool.poolGet(16));
            System.out.println(/*EL:47*/EncryptedStringPool.poolGet(17) + Globals.mc.func_110432_I().func_111286_b());
            System.out.println(/*EL:48*/EncryptedStringPool.poolGet(18) + getUsername());
            /*SL:49*/return true;
        }
        /*SL:51*/return false;
    }
    
    public static String getUsername() {
        /*SL:56*/return Globals.mc.func_110432_I().func_111285_a();
    }
    
    public static String getUUID() {
        /*SL:61*/return Globals.mc.func_110432_I().func_148256_e().getId().toString();
    }
}
