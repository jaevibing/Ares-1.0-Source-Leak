package org.spongepowered.asm.obfuscation.mapping.common;

import com.google.common.base.Strings;
import com.google.common.base.Objects;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public class MappingField implements IMapping<MappingField>
{
    private final String owner;
    private final String name;
    private final String desc;
    
    public MappingField(final String a1, final String a2) {
        this(a1, a2, null);
    }
    
    public MappingField(final String a1, final String a2, final String a3) {
        this.owner = a1;
        this.name = a2;
        this.desc = a3;
    }
    
    @Override
    public Type getType() {
        /*SL:55*/return Type.FIELD;
    }
    
    @Override
    public String getName() {
        /*SL:60*/return this.name;
    }
    
    @Override
    public final String getSimpleName() {
        /*SL:65*/return this.name;
    }
    
    @Override
    public final String getOwner() {
        /*SL:70*/return this.owner;
    }
    
    @Override
    public final String getDesc() {
        /*SL:75*/return this.desc;
    }
    
    @Override
    public MappingField getSuper() {
        /*SL:80*/return null;
    }
    
    @Override
    public MappingField move(final String a1) {
        /*SL:85*/return new MappingField(a1, this.getName(), this.getDesc());
    }
    
    @Override
    public MappingField remap(final String a1) {
        /*SL:90*/return new MappingField(this.getOwner(), a1, this.getDesc());
    }
    
    @Override
    public MappingField transform(final String a1) {
        /*SL:95*/return new MappingField(this.getOwner(), this.getName(), a1);
    }
    
    @Override
    public MappingField copy() {
        /*SL:100*/return new MappingField(this.getOwner(), this.getName(), this.getDesc());
    }
    
    @Override
    public int hashCode() {
        /*SL:105*/return Objects.hashCode(this.toString());
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:110*/return this == a1 || /*EL:113*/(a1 instanceof MappingField && /*EL:114*/Objects.equal(this.toString(), ((MappingField)a1).toString()));
    }
    
    @Override
    public String serialise() {
        /*SL:121*/return this.toString();
    }
    
    @Override
    public String toString() {
        /*SL:126*/return String.format("L%s;%s:%s", this.getOwner(), this.getName(), Strings.nullToEmpty(this.getDesc()));
    }
}
