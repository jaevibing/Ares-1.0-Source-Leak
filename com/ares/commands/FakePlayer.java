package com.ares.commands;

import com.ares.utils.chat.ChatUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import com.ares.api.MojangWebApi;

public class FakePlayer extends CommandBase
{
    public FakePlayer() {
        super("fakeplayer");
    }
    
    @Override
    public boolean execute(final String[] v-4) {
        try {
            /*SL:23*/if (v-4.length < 1) {
                /*SL:25*/return false;
            }
            final UUID fromString = /*EL:28*/UUID.fromString(MojangWebApi.grabRealUUID(v-4[0]));
            System.out.print(/*EL:30*/"UUID LOCATED: " + fromString.toString());
            final EntityOtherPlayerMP entityOtherPlayerMP = /*EL:32*/new EntityOtherPlayerMP((World)this.mc.field_71441_e, new GameProfile(fromString, v-4[0]));
            /*SL:34*/entityOtherPlayerMP.func_82149_j((Entity)this.mc.field_71439_g);
            final NBTTagCompound func_189511_e = /*EL:36*/this.mc.field_71439_g.func_189511_e(new NBTTagCompound());
            /*SL:37*/entityOtherPlayerMP.func_70020_e(func_189511_e);
            final int[] array;
            final int[] v0 = /*EL:41*/array = new int[] { -21, -69, -911, -420, -666, -2003 };
            for (final int a1 : array) {
                /*SL:43*/if (this.mc.field_71441_e.func_73045_a(a1) == null) {
                    /*SL:45*/this.mc.field_71441_e.func_73027_a(a1, (Entity)entityOtherPlayerMP);
                    /*SL:46*/return true;
                }
            }
            /*SL:50*/for (int v = -1; v > -400; --v) {
                /*SL:52*/if (this.mc.field_71441_e.func_73045_a(v) == null) {
                    /*SL:54*/this.mc.field_71441_e.func_73027_a(v, (Entity)entityOtherPlayerMP);
                    /*SL:55*/return true;
                }
            }
            /*SL:59*/ChatUtils.printMessage("No entity ids available", "gold");
            /*SL:60*/return false;
        }
        catch (Exception ex) {
            /*SL:64*/ChatUtils.printMessage(ex.getMessage(), "gold");
            /*SL:65*/ex.printStackTrace();
            /*SL:67*/return false;
        }
    }
    
    @Override
    public String getSyntax() {
        /*SL:73*/return "-fakeplayer DanTDM";
    }
}
