package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.TypeElement;
import javax.annotation.processing.ProcessingEnvironment;
import java.io.Serializable;

public class TypeReference implements Serializable, Comparable<TypeReference>
{
    private static final long serialVersionUID = 1L;
    private final String name;
    private transient TypeHandle handle;
    
    public TypeReference(final TypeHandle a1) {
        this.name = a1.getName();
        this.handle = a1;
    }
    
    public TypeReference(final String a1) {
        this.name = a1;
    }
    
    public String getName() {
        /*SL:72*/return this.name;
    }
    
    public String getClassName() {
        /*SL:79*/return this.name.replace('/', '.');
    }
    
    public TypeHandle getHandle(final ProcessingEnvironment v0) {
        /*SL:90*/if (this.handle == null) {
            final TypeElement v = /*EL:91*/v0.getElementUtils().getTypeElement(this.getClassName());
            try {
                /*SL:93*/this.handle = new TypeHandle(v);
            }
            catch (Exception a1) {
                /*SL:96*/a1.printStackTrace();
            }
        }
        /*SL:100*/return this.handle;
    }
    
    @Override
    public String toString() {
        /*SL:105*/return String.format("TypeReference[%s]", this.name);
    }
    
    @Override
    public int compareTo(final TypeReference a1) {
        /*SL:110*/return (a1 == null) ? -1 : this.name.compareTo(a1.name);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:115*/return a1 instanceof TypeReference && this.compareTo((TypeReference)a1) == 0;
    }
    
    @Override
    public int hashCode() {
        /*SL:120*/return this.name.hashCode();
    }
}
