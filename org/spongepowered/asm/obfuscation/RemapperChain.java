package org.spongepowered.asm.obfuscation;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public class RemapperChain implements IRemapper
{
    private final List<IRemapper> remappers;
    
    public RemapperChain() {
        this.remappers = new ArrayList<IRemapper>();
    }
    
    @Override
    public String toString() {
        /*SL:42*/return String.format("RemapperChain[%d]", this.remappers.size());
    }
    
    public RemapperChain add(final IRemapper a1) {
        /*SL:52*/this.remappers.add(a1);
        /*SL:53*/return this;
    }
    
    @Override
    public String mapMethodName(final String v1, String v2, final String v3) {
        /*SL:58*/for (String a2 : this.remappers) {
            /*SL:59*/a2 = a2.mapMethodName(v1, v2, v3);
            /*SL:60*/if (a2 != null && !a2.equals(v2)) {
                /*SL:61*/v2 = a2;
            }
        }
        /*SL:64*/return v2;
    }
    
    @Override
    public String mapFieldName(final String v1, String v2, final String v3) {
        /*SL:69*/for (String a2 : this.remappers) {
            /*SL:70*/a2 = a2.mapFieldName(v1, v2, v3);
            /*SL:71*/if (a2 != null && !a2.equals(v2)) {
                /*SL:72*/v2 = a2;
            }
        }
        /*SL:75*/return v2;
    }
    
    @Override
    public String map(String v-1) {
        /*SL:80*/for (final IRemapper v1 : this.remappers) {
            final String a1 = /*EL:81*/v1.map(v-1);
            /*SL:82*/if (a1 != null && !a1.equals(v-1)) {
                /*SL:83*/v-1 = a1;
            }
        }
        /*SL:86*/return v-1;
    }
    
    @Override
    public String unmap(String v-1) {
        /*SL:91*/for (final IRemapper v1 : this.remappers) {
            final String a1 = /*EL:92*/v1.unmap(v-1);
            /*SL:93*/if (a1 != null && !a1.equals(v-1)) {
                /*SL:94*/v-1 = a1;
            }
        }
        /*SL:97*/return v-1;
    }
    
    @Override
    public String mapDesc(String v-1) {
        /*SL:102*/for (final IRemapper v1 : this.remappers) {
            final String a1 = /*EL:103*/v1.mapDesc(v-1);
            /*SL:104*/if (a1 != null && !a1.equals(v-1)) {
                /*SL:105*/v-1 = a1;
            }
        }
        /*SL:108*/return v-1;
    }
    
    @Override
    public String unmapDesc(String v-1) {
        /*SL:113*/for (final IRemapper v1 : this.remappers) {
            final String a1 = /*EL:114*/v1.unmapDesc(v-1);
            /*SL:115*/if (a1 != null && !a1.equals(v-1)) {
                /*SL:116*/v-1 = a1;
            }
        }
        /*SL:119*/return v-1;
    }
}
