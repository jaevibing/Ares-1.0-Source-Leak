package org.spongepowered.asm.obfuscation.mapping.common;

import com.google.common.base.Objects;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public class MappingMethod implements IMapping<MappingMethod>
{
    private final String owner;
    private final String name;
    private final String desc;
    
    public MappingMethod(final String a1, final String a2) {
        this(getOwnerFromName(a1), getBaseName(a1), a2);
    }
    
    public MappingMethod(final String a1, final String a2, final String a3) {
        this.owner = a1;
        this.name = a2;
        this.desc = a3;
    }
    
    @Override
    public Type getType() {
        /*SL:53*/return Type.METHOD;
    }
    
    @Override
    public String getName() {
        /*SL:58*/if (this.name == null) {
            /*SL:59*/return null;
        }
        /*SL:61*/return ((this.owner != null) ? (this.owner + "/") : "") + this.name;
    }
    
    @Override
    public String getSimpleName() {
        /*SL:66*/return this.name;
    }
    
    @Override
    public String getOwner() {
        /*SL:71*/return this.owner;
    }
    
    @Override
    public String getDesc() {
        /*SL:76*/return this.desc;
    }
    
    @Override
    public MappingMethod getSuper() {
        /*SL:81*/return null;
    }
    
    public boolean isConstructor() {
        /*SL:85*/return "<init>".equals(this.name);
    }
    
    @Override
    public MappingMethod move(final String a1) {
        /*SL:90*/return new MappingMethod(a1, this.getSimpleName(), this.getDesc());
    }
    
    @Override
    public MappingMethod remap(final String a1) {
        /*SL:95*/return new MappingMethod(this.getOwner(), a1, this.getDesc());
    }
    
    @Override
    public MappingMethod transform(final String a1) {
        /*SL:100*/return new MappingMethod(this.getOwner(), this.getSimpleName(), a1);
    }
    
    @Override
    public MappingMethod copy() {
        /*SL:105*/return new MappingMethod(this.getOwner(), this.getSimpleName(), this.getDesc());
    }
    
    public MappingMethod addPrefix(final String a1) {
        final String v1 = /*EL:116*/this.getSimpleName();
        /*SL:117*/if (v1 == null || v1.startsWith(a1)) {
            /*SL:118*/return this;
        }
        /*SL:120*/return new MappingMethod(this.getOwner(), a1 + v1, this.getDesc());
    }
    
    @Override
    public int hashCode() {
        /*SL:125*/return Objects.hashCode(this.getName(), this.getDesc());
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:130*/return this == a1 || /*EL:133*/(a1 instanceof MappingMethod && /*EL:134*/Objects.equal(this.name, ((MappingMethod)a1).name) && Objects.equal(this.desc, ((MappingMethod)a1).desc));
    }
    
    @Override
    public String serialise() {
        /*SL:141*/return this.toString();
    }
    
    @Override
    public String toString() {
        final String v1 = /*EL:146*/this.getDesc();
        /*SL:147*/return String.format("%s%s%s", this.getName(), (v1 != null) ? " " : "", (v1 != null) ? v1 : "");
    }
    
    private static String getBaseName(final String a1) {
        /*SL:151*/if (a1 == null) {
            /*SL:152*/return null;
        }
        final int v1 = /*EL:154*/a1.lastIndexOf(47);
        /*SL:155*/return (v1 > -1) ? a1.substring(v1 + 1) : a1;
    }
    
    private static String getOwnerFromName(final String a1) {
        /*SL:159*/if (a1 == null) {
            /*SL:160*/return null;
        }
        final int v1 = /*EL:162*/a1.lastIndexOf(47);
        /*SL:163*/return (v1 > -1) ? a1.substring(0, v1) : null;
    }
}
