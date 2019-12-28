package org.spongepowered.asm.obfuscation.mapping.mcp;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;

public class MappingFieldSrg extends MappingField
{
    private final String srg;
    
    public MappingFieldSrg(final String a1) {
        super(getOwnerFromSrg(a1), getNameFromSrg(a1), null);
        this.srg = a1;
    }
    
    public MappingFieldSrg(final MappingField a1) {
        super(a1.getOwner(), a1.getName(), null);
        this.srg = a1.getOwner() + "/" + a1.getName();
    }
    
    @Override
    public String serialise() {
        /*SL:48*/return this.srg;
    }
    
    private static String getNameFromSrg(final String a1) {
        /*SL:52*/if (a1 == null) {
            /*SL:53*/return null;
        }
        final int v1 = /*EL:55*/a1.lastIndexOf(47);
        /*SL:56*/return (v1 > -1) ? a1.substring(v1 + 1) : a1;
    }
    
    private static String getOwnerFromSrg(final String a1) {
        /*SL:60*/if (a1 == null) {
            /*SL:61*/return null;
        }
        final int v1 = /*EL:63*/a1.lastIndexOf(47);
        /*SL:64*/return (v1 > -1) ? a1.substring(0, v1) : null;
    }
}
